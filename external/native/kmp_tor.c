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
#define KMP_TOR_SOCKET_SHUT_RDWR SD_BOTH
#define __closesocket closesocket
#else
#include <sys/socket.h>

typedef int kmp_tor_socket_t;
#define KMP_TOR_SOCKET_INVALID (-1)
#define KMP_TOR_SOCKET_SHUT_RDWR SHUT_RDWR
#define __closesocket close
#endif // _WIN32

typedef struct {
  int result;
} kmp_tor_run_thread_res_t;

typedef struct {
  void *cfg;
  int (*tor_api_run_main)(void *cfg);
} kmp_tor_run_thread_args_t;

struct kmp_tor_handle_t {
  int error_code;

  int argc;
  char **argv;

  void (*OPENSSL_cleanup)(void);
  void (*tor_api_cfg_free)(void *cfg);

  void* (*tor_api_cfg_new)(void);
  int (*tor_api_cfg_set_command_line)(void *cfg, int argc, char **argv);
#ifdef _WIN32
  kmp_tor_socket_t (*tor_api_cfg_set_ctrl_socket)(void *cfg);
#endif // _WIN32

  kmp_tor_socket_t ctrl_socket;
  kmp_tor_socket_t ctrl_socket_owned;

  pthread_t thread_id;
  kmp_tor_run_thread_args_t *args_t;
  lib_handle_t *lib_t;
};

void
kmp_tor_sleep(int millis)
{
  assert(millis > 0);

  int limit = 5;
  while (limit > 0) {
    if (usleep((useconds_t) millis * 1000) == 0) {
      break;
    } else {
      limit--;
    }
  }
}

void
kmp_tor_closesocket(kmp_tor_socket_t s)
{
  int retries = 100;
  while (retries-- > 0) {
    if (__closesocket(s) == 0) {
      break;
    } else {
      kmp_tor_sleep(1);
    }
  }
}

void *
kmp_tor_run_thread(void *arg)
{
  int rv = -1;
  kmp_tor_run_thread_args_t *args_t = arg;
  kmp_tor_run_thread_res_t *res_t = NULL;
  res_t = malloc(sizeof(kmp_tor_run_thread_res_t));

  rv = args_t->tor_api_run_main(args_t->cfg);
  if (rv < 0 || rv > 255) {
    rv = 1;
  }

  if (res_t != NULL) {
    res_t->result = rv;
  }

  kmp_tor_sleep(100);

  return res_t;
}

void
kmp_tor_free(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);

  if (handle_t->ctrl_socket != KMP_TOR_SOCKET_INVALID) {
    kmp_tor_closesocket(handle_t->ctrl_socket);
    handle_t->ctrl_socket = KMP_TOR_SOCKET_INVALID;
  }

  if (handle_t->ctrl_socket_owned != KMP_TOR_SOCKET_INVALID) {
    kmp_tor_closesocket(handle_t->ctrl_socket_owned);
    handle_t->ctrl_socket_owned = KMP_TOR_SOCKET_INVALID;
  }

  handle_t->tor_api_cfg_new = NULL;
  handle_t->tor_api_cfg_set_command_line = NULL;
#ifdef _WIN32
  handle_t->tor_api_cfg_set_ctrl_socket = NULL;
#endif // _WIN32

  if (handle_t->args_t != NULL) {
    if (handle_t->args_t->cfg != NULL) {
      handle_t->tor_api_cfg_free(handle_t->args_t->cfg);
    }
    handle_t->args_t->cfg = NULL;
    handle_t->args_t->tor_api_run_main = NULL;
    free(handle_t->args_t);
    handle_t->args_t = NULL;
  }

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
    kmp_tor_sleep(50);
    lib_load_close(handle_t->lib_t);
    handle_t->lib_t = NULL;
  }

  free(handle_t);
}

int
kmp_tor_configure_pthread_attr_t(pthread_attr_t *attrs_t)
{
  assert(attrs_t != NULL); // TODO
  return 0;
}

int
kmp_tor_configure_lib_t(const char * lib_tor, kmp_tor_handle_t *handle_t)
{
  handle_t->lib_t = lib_load_open(lib_tor);
  if (handle_t->lib_t == NULL) {
    return -1;
  }

  *(void **) (&handle_t->OPENSSL_cleanup) = lib_load_resolve(handle_t->lib_t, "OPENSSL_cleanup");
  if (handle_t->OPENSSL_cleanup == NULL) {
    return -1;
  }

  *(void **) (&handle_t->tor_api_cfg_free) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_free");
  if (handle_t->tor_api_cfg_free == NULL) {
    return -1;
  }

  *(void **) (&handle_t->args_t->tor_api_run_main) = lib_load_resolve(handle_t->lib_t, "tor_run_main");
  if (handle_t->args_t->tor_api_run_main == NULL) {
    return -1;
  }

  *(void **) (&handle_t->tor_api_cfg_new) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_new");
  if (handle_t->tor_api_cfg_new == NULL) {
    return -1;
  }

  *(void **) (&handle_t->tor_api_cfg_set_command_line) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_set_command_line");
  if (handle_t->tor_api_cfg_set_command_line == NULL) {
    return -1;
  }

#ifdef _WIN32
  *(void **) (&handle_t->tor_api_cfg_set_ctrl_socket) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_setup_control_socket");
  if (handle_t->tor_api_cfg_set_ctrl_socket == NULL) {
    return -1;
  }
#endif // _WIN32

  return 0;
}

int
kmp_tor_configure_tor(kmp_tor_handle_t *handle_t, const char *win32_af_unix_path)
{
  handle_t->args_t->cfg = handle_t->tor_api_cfg_new();
  if (handle_t->args_t->cfg == NULL) {
    handle_t->error_code = ERR_CODE_TOR_CFG_NEW;
    return -1;
  }

  int result = 0;
  kmp_tor_socket_t fds[2];
  fds[0] = KMP_TOR_SOCKET_INVALID;
  fds[1] = KMP_TOR_SOCKET_INVALID;
  char *s1 = NULL;
  char *s2 = NULL;

#ifdef _WIN32
  result = win32_socketpair(win32_af_unix_path, fds);
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
    if (fds[0] != KMP_TOR_SOCKET_INVALID) {
      kmp_tor_closesocket(fds[0]);
    }
    if (fds[1] != KMP_TOR_SOCKET_INVALID) {
      kmp_tor_closesocket(fds[1]);
    }
#ifdef _WIN32
    // Windows will try tor's implementation to obtain an owned
    // controller socket. Disregard the last 2 slots that were
    // added (which are NULL) so tor does not include them in the
    // event it is successful.
    handle_t->argc = handle_t->argc - 2;

    // Try tor's implementation
    handle_t->ctrl_socket = handle_t->tor_api_cfg_set_ctrl_socket(handle_t->args_t->cfg);
#endif // _WIN32
  }

  if (handle_t->ctrl_socket == KMP_TOR_SOCKET_INVALID) {
    handle_t->error_code = ERR_CODE_TOR_CFG_SOCKET;
    return -1;
  }

  // Shutdown ability to send/receive. This owned controller socket is
  // STRICTLY to asynchronously interrupt tor's main loop w/o having
  // to deal with multi-threaded signal handing which is sketch AF.
  if (shutdown(handle_t->ctrl_socket, KMP_TOR_SOCKET_SHUT_RDWR) != 0) {
    handle_t->error_code = ERR_CODE_TOR_CFG_SOCKET;
    return -1;
  }

  if (handle_t->tor_api_cfg_set_command_line(handle_t->args_t->cfg, handle_t->argc, handle_t->argv) != 0) {
    handle_t->error_code = ERR_CODE_TOR_CFG_SET;
    return -1;
  }

#ifndef _WIN32
  if (win32_af_unix_path != NULL) { /* ignore */ }
#endif // _WIN32

  return 0;
}

kmp_tor_handle_t *
kmp_tor_run_main(const char *lib_tor, const char *win32_af_unix_path, int argc, char *argv[])
{
  kmp_tor_handle_t *handle_t = NULL;
  pthread_attr_t attrs_t;

  handle_t = malloc(sizeof(kmp_tor_handle_t));
  if (handle_t == NULL) {
    return NULL;
  } else {
    handle_t->error_code = ERR_CODE_NONE;
    handle_t->ctrl_socket = KMP_TOR_SOCKET_INVALID;
    handle_t->ctrl_socket_owned = KMP_TOR_SOCKET_INVALID;

#ifdef _WIN32
    if (win32_af_unix_path == NULL) {
      handle_t->error_code = ERR_CODE_ARGS;
    }
#endif // _WIN32
    if (lib_tor == NULL) {
      handle_t->error_code = ERR_CODE_ARGS;
    }
    if (argc <= 0) {
      handle_t->error_code = ERR_CODE_ARGS;
    }
    if (argv == NULL) {
      handle_t->error_code = ERR_CODE_ARGS;
    }
    if (handle_t->error_code != ERR_CODE_NONE) {
      return handle_t;
    }
  }

  handle_t->args_t = malloc(sizeof(kmp_tor_run_thread_args_t));
  if (handle_t->args_t == NULL) {
    kmp_tor_free(handle_t);
    return NULL;
  }

  handle_t->argc = argc + 2;
  handle_t->argv = malloc(handle_t->argc * sizeof(char *));
  if (handle_t->argv == NULL) {
    kmp_tor_free(handle_t);
    return NULL;
  }

  handle_t->argv[argc] = NULL;
  handle_t->argv[argc + 1] = NULL;

  for (int i = 0; i < argc; i++) {
    if (handle_t->error_code != ERR_CODE_NONE) {
      handle_t->argv[i] = NULL;
      continue;
    }

    if (argv[i] != NULL) {
      handle_t->argv[i] = strdup(argv[i]);
    } else {
      handle_t->argv[i] = NULL;
    }

    if (handle_t->argv[i] == NULL) {
      handle_t->error_code = ERR_CODE_CFG;
    }
  }

  if (handle_t->error_code != ERR_CODE_NONE) {
    return handle_t;
  }

  if (pthread_attr_init(&attrs_t) != 0) {
    handle_t->error_code = ERR_CODE_THREAD;
    return handle_t;
  }

  if (kmp_tor_configure_pthread_attr_t(&attrs_t) != 0) {
    handle_t->error_code = ERR_CODE_THREAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  if (kmp_tor_configure_lib_t(lib_tor, handle_t) != 0) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  if (kmp_tor_configure_tor(handle_t, win32_af_unix_path) != 0) {
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  if (pthread_create(&handle_t->thread_id, &attrs_t, kmp_tor_run_thread, (void *) handle_t->args_t) != 0) {
    handle_t->error_code = ERR_CODE_THREAD;
  }
  pthread_attr_destroy(&attrs_t);

  if (handle_t->error_code == ERR_CODE_NONE) {
    kmp_tor_sleep(50);
  }

  return handle_t;
}

int
kmp_tor_check_error_code(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);
  return handle_t->error_code;
}

int
kmp_tor_terminate_and_await_result(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);
  int result = 1;

  if (handle_t->error_code == ERR_CODE_NONE) {
    void *res = NULL;
    int retries = 5;

    kmp_tor_closesocket(handle_t->ctrl_socket);
    handle_t->ctrl_socket = KMP_TOR_SOCKET_INVALID;

    while (retries-- > 0) {
      if (pthread_join(handle_t->thread_id, &res) == 0) {
        retries = -1;
        break;
      }
    }

    if (retries == 0) {
      kmp_tor_sleep(250);
    }

    if (res != NULL) {
      kmp_tor_run_thread_res_t *res_t = res;
      result = res_t->result;
      free(res_t);
    }
  }

  kmp_tor_free(handle_t);
  return result;
}
