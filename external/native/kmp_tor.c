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
#include <fcntl.h>

#ifdef SOCK_CLOEXEC
#include <errno.h>
#endif // SOCK_CLOEXEC

typedef int kmp_tor_socket_t;
#define KMP_TOR_SOCKET_INVALID (-1)
#define __closesocket close
#endif // _WIN32

#define KMP_TOR_RESULT_AWAITING -1

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
  kmp_tor_socket_t ctrl_socket_0;
  kmp_tor_socket_t ctrl_socket_1;

  pthread_t thread_id;
  lib_handle_t *lib_t;

  int tor_run_main_result;
} kmp_tor_handle_t;

static pthread_mutex_t      s_kmp_tor_lock          = PTHREAD_MUTEX_INITIALIZER;
static int                  s_kmp_tor_state         = KMP_TOR_STATE_OFF;
static kmp_tor_handle_t    *s_handle_t              = NULL;

static void *
kmp_tor_execute(void *arg)
{
  int rv = -1;
  void *cfg = NULL;
  int (*tor_api_run_main)(void *cfg) = NULL;
  void (*tor_api_cfg_free)(void *cfg) = NULL;
  void (*OPENSSL_cleanup)(void) = NULL;
  assert(!arg);

  pthread_mutex_lock(&s_kmp_tor_lock);
    if (s_handle_t) {
      cfg = s_handle_t->cfg;
      tor_api_run_main = s_handle_t->tor_api_run_main;
      tor_api_cfg_free = s_handle_t->tor_api_cfg_free;
      OPENSSL_cleanup = s_handle_t->OPENSSL_cleanup;

      s_handle_t->cfg = NULL;
      s_handle_t->tor_api_run_main = NULL;
      s_handle_t->tor_api_cfg_free = NULL;
      s_handle_t->OPENSSL_cleanup = NULL;

      s_kmp_tor_state = KMP_TOR_STATE_STARTED;
    }
  pthread_mutex_unlock(&s_kmp_tor_lock);

  assert(cfg);
  assert(tor_api_run_main);
  assert(tor_api_cfg_free);
  assert(OPENSSL_cleanup);

  rv = tor_api_run_main(cfg);
  if (rv < 0 || rv > 255) {
    rv = 1;
  }

  usleep((useconds_t) (100 * 1000));

  tor_api_cfg_free(cfg);
  OPENSSL_cleanup();

  cfg = NULL;
  tor_api_run_main = NULL;
  tor_api_cfg_free = NULL;
  OPENSSL_cleanup = NULL;

  pthread_mutex_lock(&s_kmp_tor_lock);
    s_kmp_tor_state = KMP_TOR_STATE_STOPPED;
    if (s_handle_t) {
      s_handle_t->tor_run_main_result = rv;
      rv = -1;
    }
  pthread_mutex_unlock(&s_kmp_tor_lock);

  assert(rv == -1);

  return NULL;
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
  assert(handle_t);

  kmp_tor_closesocket(handle_t->ctrl_socket_0);
  kmp_tor_closesocket(handle_t->ctrl_socket_1);
  handle_t->ctrl_socket_0 = KMP_TOR_SOCKET_INVALID;
  handle_t->ctrl_socket_1 = KMP_TOR_SOCKET_INVALID;

  handle_t->tor_api_cfg_new = NULL;
  handle_t->tor_api_cfg_set_command_line = NULL;
#ifdef _WIN32
  handle_t->tor_api_cfg_set_ctrl_socket = NULL;
#endif // _WIN32

  if (handle_t->cfg) {
    handle_t->tor_api_cfg_free(handle_t->cfg);
    handle_t->cfg = NULL;
  }
  handle_t->tor_api_run_main = NULL;
  handle_t->tor_api_cfg_free = NULL;

  if (handle_t->argv) {
    for (int i = 0; i < handle_t->argc; i++) {
      if (handle_t->argv[i] != NULL) {
        free(handle_t->argv[i]);
        handle_t->argv[i] = NULL;
      }
    }
    free(handle_t->argv);
    handle_t->argv = NULL;
  }

  if (handle_t->OPENSSL_cleanup) {
    handle_t->OPENSSL_cleanup();
    handle_t->OPENSSL_cleanup = NULL;
  }

  if (handle_t->lib_t) {
    lib_load_close(handle_t->lib_t);
    handle_t->lib_t = NULL;
  }

#ifdef _WIN32
  if (handle_t->was_win32_sockets_initialized == 0) {
    win32_sockets_deinit();
    handle_t->was_win32_sockets_initialized = -1;
  }
#endif // _WIN32

  free(handle_t);

  pthread_mutex_lock(&s_kmp_tor_lock);
    s_kmp_tor_state = KMP_TOR_STATE_OFF;
  pthread_mutex_unlock(&s_kmp_tor_lock);
}

static const char *
kmp_tor_configure_lib_t(const char * lib_tor, kmp_tor_handle_t *handle_t)
{
  assert(lib_tor);
  assert(handle_t);

  handle_t->lib_t = lib_load_open(lib_tor);
  if (!handle_t->lib_t) {
    return "Failed to load tor";
  }

  *(void **) (&handle_t->OPENSSL_cleanup) = lib_load_resolve(handle_t->lib_t, "OPENSSL_cleanup");
  if (!handle_t->OPENSSL_cleanup) {
    return "Failed to resolve symbol OPENSSL_cleanup";
  }

  *(void **) (&handle_t->tor_api_cfg_free) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_free");
  if (!handle_t->tor_api_cfg_free) {
    return "Failed to resolve symbol tor_main_configuration_free";
  }

  *(void **) (&handle_t->tor_api_run_main) = lib_load_resolve(handle_t->lib_t, "tor_run_main");
  if (!handle_t->tor_api_run_main) {
    return "Failed to resolve symbol tor_run_main";
  }

  *(void **) (&handle_t->tor_api_cfg_new) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_new");
  if (!handle_t->tor_api_cfg_new) {
    return "Failed to resolve symbol tor_main_configuration_new";
  }

  *(void **) (&handle_t->tor_api_cfg_set_command_line) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_set_command_line");
  if (!handle_t->tor_api_cfg_set_command_line) {
    return "Failed to resolve symbol tor_main_configuration_set_command_line";
  }

#ifdef _WIN32
  *(void **) (&handle_t->tor_api_cfg_set_ctrl_socket) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_setup_control_socket");
  if (!handle_t->tor_api_cfg_set_ctrl_socket) {
    return "Failed to resolve symbol tor_main_configuration_setup_control_socket";
  }
#endif // _WIN32

  return NULL;
}

static const char *
kmp_tor_configure_tor(kmp_tor_handle_t *handle_t)
{
  assert(handle_t);

  handle_t->cfg = handle_t->tor_api_cfg_new();
  if (!handle_t->cfg) {
    return "Failed to acquire a new tor_main_configuration_t";
  }

  int result = -1;
  kmp_tor_socket_t fds[2] = { KMP_TOR_SOCKET_INVALID };
  char *s1 = NULL;
  char *s2 = NULL;

#ifdef _WIN32
  result = win32_af_unix_socketpair(fds);
#else

  int set_fd_cloexec = 0;
#ifdef SOCK_CLOEXEC
  result = socketpair(AF_UNIX, SOCK_STREAM | SOCK_CLOEXEC, 0, fds);
  if (result != 0 && errno == EINVAL) {
    set_fd_cloexec = 1;
    result = socketpair(AF_UNIX, SOCK_STREAM, 0, fds);
  }
#else
  set_fd_cloexec = 1;
  result = socketpair(AF_UNIX, SOCK_STREAM, 0, fds);
#endif // SOCK_CLOEXEC

#ifdef FD_CLOEXEC
  if (result == 0 && set_fd_cloexec) {
    result = fcntl(fds[0], F_SETFD, FD_CLOEXEC);
    if (result == 0) {
      result = fcntl(fds[1], F_SETFD, FD_CLOEXEC);
    }
  }
#endif // FD_CLOEXEC

#endif // _WIN32

  if (result == 0) {
    char buf[32];
    if (snprintf(buf, sizeof(buf), "%"PRIu64, (uint64_t) fds[1]) < 0) {
      result = -1;
    } else {
      s1 = strdup("--__OwningControllerFD");
      s2 = strdup(buf);

      if (!s1) {
        result = -1;
      }
      if (!s2) {
        result = -1;
      }
    }
  }

  if (result == 0) {
    handle_t->ctrl_socket_0 = fds[0];
    handle_t->ctrl_socket_1 = fds[1];
    handle_t->argv[handle_t->argc - 2] = s1;
    handle_t->argv[handle_t->argc - 1] = s2;
  } else {
    if (s1) {
      free(s1);
    }
    if (s2) {
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
    handle_t->ctrl_socket_0 = handle_t->tor_api_cfg_set_ctrl_socket(handle_t->cfg);
#endif // _WIN32
  }

  if (handle_t->ctrl_socket_0 == KMP_TOR_SOCKET_INVALID) {
    return "Failed to setup controller socket";
  }

  if (handle_t->tor_api_cfg_set_command_line(handle_t->cfg, handle_t->argc, handle_t->argv) != 0) {
    return "Failed to set tor_main_configuration_t arguments";
  }

  return NULL;
}

const char *
kmp_tor_run_main(const char *lib_tor, int argc, char *argv[])
{
  if (!lib_tor) {
    return "lib_tor cannot be NULL";
  }
  if (argc <= 0) {
    return "argc must be greater than 0";
  }
  if (!argv) {
    return "argv cannot be NULL";
  }

  int i_result = 0;
  const char *c_result = NULL;
  kmp_tor_handle_t *handle_t = NULL;
  pthread_attr_t attr_t;

  pthread_mutex_lock(&s_kmp_tor_lock);
    i_result = s_kmp_tor_state;
    if (s_kmp_tor_state == KMP_TOR_STATE_OFF) {
      s_kmp_tor_state = KMP_TOR_STATE_STARTING;
    }
  pthread_mutex_unlock(&s_kmp_tor_lock);

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
  if (!handle_t) {
    pthread_mutex_lock(&s_kmp_tor_lock);
      s_kmp_tor_state = KMP_TOR_STATE_OFF;
    pthread_mutex_unlock(&s_kmp_tor_lock);
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
    handle_t->ctrl_socket_0 = KMP_TOR_SOCKET_INVALID;
    handle_t->ctrl_socket_1 = KMP_TOR_SOCKET_INVALID;
    handle_t->lib_t = NULL;
    handle_t->tor_run_main_result = KMP_TOR_RESULT_AWAITING;
  }

#ifdef _WIN32
  handle_t->was_win32_sockets_initialized = win32_sockets_init();
  if (handle_t->was_win32_sockets_initialized != 0) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return "Failed to initialize windows sockets";
  }
#endif // _WIN32

  // Adding 2 to the end in order to setup __OwningControllerFD to
  // interrupt tor's main loop by closing it down whenever needed,
  // instead of dealing with signals (which can be a nightmare).
  handle_t->argc = argc + 2;
  handle_t->argv = malloc(handle_t->argc * sizeof(char *));
  if (!handle_t->argv) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return "Failed to create argv";
  }

  handle_t->argv[argc] = NULL;
  handle_t->argv[argc + 1] = NULL;

  for (int i = 0; i < argc; i++) {
    if (i_result != 0) {
      handle_t->argv[i] = NULL;
      continue;
    }

    if (argv[i]) {
      handle_t->argv[i] = strdup(argv[i]);
    } else {
      handle_t->argv[i] = NULL;
    }

    if (!handle_t->argv[i]) {
      i_result = -1;
    }
  }

  if (i_result != 0) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return "Failed to copy arguments to argv";
  }

  c_result = kmp_tor_configure_lib_t(lib_tor, handle_t);
  if (c_result) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return c_result;
  }

  c_result = kmp_tor_configure_tor(handle_t);
  if (c_result) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return c_result;
  }

  if (pthread_attr_init(&attr_t) != 0) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return "Failed to initialize pthread_attr_t";
  }

  if (pthread_attr_setdetachstate(&attr_t, PTHREAD_CREATE_JOINABLE) != 0) {
    pthread_attr_destroy(&attr_t);
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return "Failed to set pthread_attr_t detachstate to PTHREAD_CREATE_JOINABLE";
  }

  pthread_mutex_lock(&s_kmp_tor_lock);
    if (pthread_create(&handle_t->thread_id, &attr_t, kmp_tor_execute, NULL) == 0) {
      s_handle_t = handle_t;
      handle_t = NULL;
    }
  pthread_mutex_unlock(&s_kmp_tor_lock);

  pthread_attr_destroy(&attr_t);

  if (handle_t) {
    kmp_tor_free(handle_t);
    handle_t = NULL;
    return "Failed to start tor thread";
  }

  while (i_result == 0) {
    pthread_mutex_lock(&s_kmp_tor_lock);
      if (s_kmp_tor_state != KMP_TOR_STATE_STARTING) {
        i_result = 1;
      }
    pthread_mutex_unlock(&s_kmp_tor_lock);

    if (i_result == 0) {
      usleep((useconds_t) (5 * 1000));
    }
  }

  return NULL;
}

int
kmp_tor_state()
{
  int state;
  pthread_mutex_lock(&s_kmp_tor_lock);
    state = s_kmp_tor_state;
  pthread_mutex_unlock(&s_kmp_tor_lock);
  return state;
}

int
kmp_tor_terminate_and_await_result()
{
  int result = KMP_TOR_RESULT_AWAITING;
  kmp_tor_handle_t *handle_t = NULL;
  void *ret = NULL;

  while (result == KMP_TOR_RESULT_AWAITING && handle_t == NULL) {
    pthread_mutex_lock(&s_kmp_tor_lock);
      if (s_kmp_tor_state == KMP_TOR_STATE_OFF) {
        // Pop out
        result = KMP_TOR_RESULT_AWAITING - 1;
      } else {
        if (s_handle_t) {
          kmp_tor_closesocket(s_handle_t->ctrl_socket_0);
          s_handle_t->ctrl_socket_0 = KMP_TOR_SOCKET_INVALID;

          result = s_handle_t->tor_run_main_result;
          if (result != KMP_TOR_RESULT_AWAITING) {
            handle_t = s_handle_t;
            s_handle_t = NULL;
          }
        }
      }
    pthread_mutex_unlock(&s_kmp_tor_lock);

    if (result == KMP_TOR_RESULT_AWAITING && handle_t == NULL) {
      usleep((useconds_t) (10 * 1000));
    }
  }

  if (!handle_t) {
    return -1;
  }

  pthread_join(handle_t->thread_id, &ret);
  assert(!ret);

  usleep((useconds_t) (50 * 1000));

  kmp_tor_free(handle_t);
  handle_t = NULL;
  return result;
}
