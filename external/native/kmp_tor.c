/*
 * Copyright (c) 2024 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
#include "kmp_tor.h"
#include "lib_load.h"

#include <assert.h>
#include <inttypes.h>
#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#ifdef _WIN32
#include "win32_sockets.h"

typedef SOCKET kmp_tor_socket_t;
#define KMP_TOR_SOCKET_INVALID INVALID_SOCKET
#define __closesocket closesocket
#else
#include <sys/socket.h>

typedef int kmp_tor_socket_t;
#define KMP_TOR_SOCKET_INVALID (-1)
#define __closesocket close
#endif // _WIN32

#define KMP_TOR_AWAITING_RESULT -1

typedef struct {
  int argc;
  char **argv;

  void *cfg;

  void (*OPENSSL_cleanup)(void);
  void (*tor_api_cfg_free)(void *cfg);
  void* (*tor_api_cfg_new)(void);
  int (*tor_api_cfg_set_command_line)(void *cfg, int argc, char **argv);
  int (*tor_api_run_main)(void *cfg);
#ifdef _WIN32
  kmp_tor_socket_t (*tor_api_cfg_set_ctrl_socket)(void *cfg);

  int was_win32_sockets_initialized;
#endif // _WIN32
  kmp_tor_socket_t ctrl_socket;
  kmp_tor_socket_t ctrl_socket_owned;

  lib_handle_t *lib_t;

  int tor_run_main_result;
} kmp_tor_handle_t;

static kmp_tor_handle_t    *s_handle_t      = NULL;
static int                  s_kmp_tor_state = KMP_TOR_STATE_OFF;
static pthread_mutex_t      kmp_tor_lock    = PTHREAD_MUTEX_INITIALIZER;

static void
kmp_tor_sleep(int millis)
{
  assert(millis > 0);
  usleep((useconds_t) millis * 1000);
}

static void
kmp_tor_closesocket(kmp_tor_socket_t s)
{
  if (s == KMP_TOR_SOCKET_INVALID) {
    return;
  }
  __closesocket(s);
}

static void
kmp_tor_free(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);

  kmp_tor_closesocket(handle_t->ctrl_socket);
  kmp_tor_closesocket(handle_t->ctrl_socket_owned);
  handle_t->ctrl_socket = KMP_TOR_SOCKET_INVALID;
  handle_t->ctrl_socket_owned = KMP_TOR_SOCKET_INVALID;

  handle_t->tor_api_cfg_new = NULL;
  handle_t->tor_api_cfg_set_command_line = NULL;
#ifdef _WIN32
  handle_t->tor_api_cfg_set_ctrl_socket = NULL;
#endif // _WIN32

  if (handle_t->cfg != NULL) {
    handle_t->tor_api_cfg_free(handle_t->cfg);
    handle_t->cfg = NULL;
  }
  handle_t->tor_api_run_main = NULL;
  handle_t->tor_api_cfg_free = NULL;

  if (handle_t->argv != NULL) {
    for (int i = 0; i < handle_t->argc; i++) {
      if (handle_t->argv[i] != NULL) {
        free(handle_t->argv[i]);
        handle_t->argv[i] = NULL;
      }
    }
    free(handle_t->argv);
    handle_t->argv = NULL;
  }

  if (handle_t->OPENSSL_cleanup != NULL) {
    handle_t->OPENSSL_cleanup();
    handle_t->OPENSSL_cleanup = NULL;
  }

  if (handle_t->lib_t != NULL) {
    lib_load_close(handle_t->lib_t);
    handle_t->lib_t = NULL;
  }

#ifdef _WIN32
  if (handle_t->was_win32_sockets_initialized == 0) {
    win32_sockets_deinit();
    handle_t->was_win32_sockets_initialized = -1;
  }
#endif

  free(handle_t);

  pthread_mutex_lock(&kmp_tor_lock);
    s_kmp_tor_state = KMP_TOR_STATE_OFF;
  pthread_mutex_unlock(&kmp_tor_lock);
}

static const char *
kmp_tor_configure_lib_t(const char * lib_tor, kmp_tor_handle_t *handle_t)
{
  assert(lib_tor != NULL);
  assert(handle_t != NULL);

  handle_t->lib_t = lib_load_open(lib_tor);
  if (handle_t->lib_t == NULL) {
    return "Failed to load tor";
  }

  *(void **) (&handle_t->OPENSSL_cleanup) = lib_load_resolve(handle_t->lib_t, "OPENSSL_cleanup");
  if (handle_t->OPENSSL_cleanup == NULL) {
    return "Failed to resolve symbol OPENSSL_cleanup";
  }

  *(void **) (&handle_t->tor_api_cfg_free) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_free");
  if (handle_t->tor_api_cfg_free == NULL) {
    return "Failed to resolve symbol tor_main_configuration_free";
  }

  *(void **) (&handle_t->tor_api_run_main) = lib_load_resolve(handle_t->lib_t, "tor_run_main");
  if (handle_t->tor_api_run_main == NULL) {
    return "Failed to resolve symbol tor_run_main";
  }

  *(void **) (&handle_t->tor_api_cfg_new) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_new");
  if (handle_t->tor_api_cfg_new == NULL) {
    return "Failed to resolve symbol tor_main_configuration_new";
  }

  *(void **) (&handle_t->tor_api_cfg_set_command_line) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_set_command_line");
  if (handle_t->tor_api_cfg_set_command_line == NULL) {
    return "Failed to resolve symbol tor_main_configuration_set_command_line";
  }

#ifdef _WIN32
  *(void **) (&handle_t->tor_api_cfg_set_ctrl_socket) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_setup_control_socket");
  if (handle_t->tor_api_cfg_set_ctrl_socket == NULL) {
    return "Failed to resolve symbol tor_main_configuration_setup_control_socket";
  }
#endif // _WIN32

  return NULL;
}

static const char *
kmp_tor_configure_tor(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);

  handle_t->cfg = handle_t->tor_api_cfg_new();
  if (handle_t->cfg == NULL) {
    return "Failed to acquire a new tor_main_configuration_t";
  }

  int result = 0;
  kmp_tor_socket_t fds[2] = { KMP_TOR_SOCKET_INVALID };
  char *s1 = NULL;
  char *s2 = NULL;

#ifdef _WIN32
  result = win32_af_unix_socketpair(fds);
#else
  result = socketpair(AF_UNIX, SOCK_STREAM, 0, fds);
#endif // _WIN32

  if (result == 0) {
    char buf[32];
    if (snprintf(buf, sizeof(buf), "%"PRIu64, (uint64_t) fds[1]) < 0) {
      result = -1;
    } else {
      s1 = strdup("--__OwningControllerFD");
      s2 = strdup(buf);

      if (s1 == NULL) {
        result = -1;
      }
      if (s2 == NULL) {
        result = -1;
      }
    }
  }

  if (result == 0) {
    handle_t->ctrl_socket = fds[0];
    handle_t->ctrl_socket_owned = fds[1];
    handle_t->argv[handle_t->argc - 2] = s1;
    handle_t->argv[handle_t->argc - 1] = s2;
  } else {
    if (s1 != NULL) {
      free(s1);
    }
    if (s2 != NULL) {
      free(s2);
    }
    kmp_tor_closesocket(fds[0]);
    kmp_tor_closesocket(fds[1]);
#ifdef _WIN32
    // Windows will try tor's implementation to obtain an owned
    // controller socket. Disregard the last 2 slots that were
    // added (which are NULL) so tor does not include them in the
    // event it is successful.
    handle_t->argc = handle_t->argc - 2;

    // Try tor's implementation
    handle_t->ctrl_socket = handle_t->tor_api_cfg_set_ctrl_socket(handle_t->cfg);
#endif // _WIN32
  }

  if (handle_t->ctrl_socket == KMP_TOR_SOCKET_INVALID) {
    return "Failed to setup controller socket";
  }

  if (handle_t->tor_api_cfg_set_command_line(handle_t->cfg, handle_t->argc, handle_t->argv) != 0) {
    return "Failed to set tor_main_configuration_t arguments";
  }

  return NULL;
}

const char *
kmp_tor_run_blocking(const char *lib_tor, int argc, char *argv[])
{
  if (lib_tor == NULL) {
    return "lib_tor cannot be NULL";
  }
  if (argc <= 0) {
    return "argc must be greater than 0";
  }
  if (argv == NULL) {
    return "argv cannot be NULL";
  }

  int i_result = 0;
  const char *c_result = NULL;
  kmp_tor_handle_t *handle_t = NULL;

  pthread_mutex_lock(&kmp_tor_lock);
    i_result = s_kmp_tor_state;
    if (s_kmp_tor_state == KMP_TOR_STATE_OFF) {
      s_kmp_tor_state = KMP_TOR_STATE_STARTING;
    }
  pthread_mutex_unlock(&kmp_tor_lock);

  if (i_result != KMP_TOR_STATE_OFF) {
    if (i_result == KMP_TOR_STATE_STARTING) {
      return "tor is already starting up";
    }
    if (i_result == KMP_TOR_STATE_STARTED) {
      return "tor is already started";
    }
    if (i_result == KMP_TOR_STATE_STOPPED) {
      return "tor is stopped and awaiting termination";
    }
    // catch-all
    return "tor state is not KMP_TOR_STATE_OFF";
  }

  handle_t = malloc(sizeof(kmp_tor_handle_t));
  if (handle_t == NULL) {
    pthread_mutex_lock(&kmp_tor_lock);
      s_kmp_tor_state = KMP_TOR_STATE_OFF;
    pthread_mutex_unlock(&kmp_tor_lock);
    return "Failed to create kmp_tor_handle_t";
  } else {
    handle_t->argc = 0;
    handle_t->argv = NULL;

    handle_t->cfg = NULL;

    handle_t->OPENSSL_cleanup = NULL;
    handle_t->tor_api_cfg_free = NULL;
    handle_t->tor_api_cfg_new = NULL;
    handle_t->tor_api_cfg_set_command_line = NULL;
    handle_t->tor_api_run_main = NULL;
#ifdef _WIN32
    handle_t->tor_api_cfg_set_ctrl_socket = NULL;
    handle_t->was_win32_sockets_initialized = -1;
#endif // _WIN32
    handle_t->ctrl_socket = KMP_TOR_SOCKET_INVALID;
    handle_t->ctrl_socket_owned = KMP_TOR_SOCKET_INVALID;
    handle_t->lib_t = NULL;
    handle_t->tor_run_main_result = KMP_TOR_AWAITING_RESULT;
  }

#ifdef _WIN32
  handle_t->was_win32_sockets_initialized = win32_sockets_init();
  if (handle_t->was_win32_sockets_initialized != 0) {
    kmp_tor_free(handle_t);
    return "Failed to initialize windows sockets";
  }
#endif

  handle_t->argc = argc + 2;
  handle_t->argv = malloc(handle_t->argc * sizeof(char *));
  if (handle_t->argv == NULL) {
    kmp_tor_free(handle_t);
    return "Failed to create argv";
  }

  handle_t->argv[argc] = NULL;
  handle_t->argv[argc + 1] = NULL;

  for (int i = 0; i < argc; i++) {
    if (i_result != 0) {
      handle_t->argv[i] = NULL;
      continue;
    }

    if (argv[i] != NULL) {
      handle_t->argv[i] = strdup(argv[i]);
    } else {
      handle_t->argv[i] = NULL;
    }

    if (handle_t->argv[i] == NULL) {
      i_result = -1;
    }
  }

  if (i_result != 0) {
    kmp_tor_free(handle_t);
    return "Failed to copy arguments to argv";
  }

  c_result = kmp_tor_configure_lib_t(lib_tor, handle_t);
  if (c_result != NULL) {
    kmp_tor_free(handle_t);
    return c_result;
  }

  c_result = kmp_tor_configure_tor(handle_t);
  if (c_result != NULL) {
    kmp_tor_free(handle_t);
    return c_result;
  }

  pthread_mutex_lock(&kmp_tor_lock);
    s_kmp_tor_state = KMP_TOR_STATE_STARTED;
    s_handle_t = handle_t;
  pthread_mutex_unlock(&kmp_tor_lock);

  i_result = handle_t->tor_api_run_main(handle_t->cfg);
  if (i_result < 0 || i_result > 255) {
    i_result = 1;
  }

  kmp_tor_sleep(100);

  pthread_mutex_lock(&kmp_tor_lock);
    s_kmp_tor_state = KMP_TOR_STATE_STOPPED;
    handle_t->tor_run_main_result = i_result;
  pthread_mutex_unlock(&kmp_tor_lock);

  return NULL;
}

int
kmp_tor_state()
{
  int state;
  pthread_mutex_lock(&kmp_tor_lock);
    state = s_kmp_tor_state;
  pthread_mutex_unlock(&kmp_tor_lock);
  return state;
}

int
kmp_tor_terminate_and_await_result()
{
  int result = 1;
  kmp_tor_handle_t *handle_t = NULL;

  while (result == 1 && handle_t == NULL) {
    pthread_mutex_lock(&kmp_tor_lock);
      if (s_kmp_tor_state == KMP_TOR_STATE_OFF) {
        result = -1;
      } else {
        handle_t = s_handle_t;
        s_handle_t = NULL;
      }
    pthread_mutex_unlock(&kmp_tor_lock);

    if (result == 1 && handle_t == NULL) {
      kmp_tor_sleep(1);
    }
  }

  if (handle_t == NULL) {
    return -1;
  }

  kmp_tor_closesocket(handle_t->ctrl_socket);
  handle_t->ctrl_socket = KMP_TOR_SOCKET_INVALID;

  int attempts = 250;
  result = KMP_TOR_AWAITING_RESULT;

  while (result == KMP_TOR_AWAITING_RESULT) {
    pthread_mutex_lock(&kmp_tor_lock);
      result = handle_t->tor_run_main_result;
    pthread_mutex_unlock(&kmp_tor_lock);

    if (result == KMP_TOR_AWAITING_RESULT) {
      // If after 2.5 seconds tor has not shutdown after the
      // owning controller FD was closed, there is a problem
      // and we should terminate instead of hanging...
      if (attempts-- < 0) {
        break;
      }

      kmp_tor_sleep(10);
    }
  }

  kmp_tor_free(handle_t);
  return result;
}
