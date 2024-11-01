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
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#ifdef _WIN32
#include <winsock.h>
typedef SOCKET tor_control_socket_t;
#define INVALID_TOR_CONTROL_SOCKET INVALID_SOCKET
#define raw_closesocket closesocket
#else
typedef int tor_control_socket_t;
#define INVALID_TOR_CONTROL_SOCKET (-1)
#define raw_closesocket close
#endif /* defined(_WIN32) */

#define ERR_CODE_NONE          -100
#define ERR_CODE_ARGS          -10
#define ERR_CODE_CFG           -11
#define ERR_CODE_LIB_LOAD      -12
#define ERR_CODE_THREAD        -13
#define ERR_CODE_TOR_CFG_NEW   -14
#define ERR_CODE_TOR_CFG_SET   -15

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
  tor_control_socket_t ctrl_socket;

  pthread_t thread_id;
  kmp_tor_run_thread_args_t *args_t;
  lib_handle_t *lib_t;
};

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

  return res_t;
}

void
kmp_tor_free(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);

  if (handle_t->args_t != NULL) {
    if (handle_t->args_t->cfg != NULL) {
      handle_t->tor_api_cfg_free(handle_t->args_t->cfg);
    }
    free(handle_t->args_t);
  }

  if (handle_t->argv != NULL) {
    for (int i = 0; i < handle_t->argc; i++) {
      if (handle_t->argv[i] != NULL) {
        free(handle_t->argv[i]);
      }
    }
    free(handle_t->argv);
  }

  if (handle_t->OPENSSL_cleanup != NULL) {
    handle_t->OPENSSL_cleanup();
  }

  if (handle_t->lib_t != NULL) {
    lib_load_close(handle_t->lib_t);
  }

  free(handle_t);
}

kmp_tor_handle_t *
kmp_tor_run_main(const char *lib_tor, int argc, char *argv[])
{
  kmp_tor_handle_t *handle_t = NULL;
  pthread_attr_t attrs_t;
  void* (*tor_api_cfg_new)(void) = NULL;
  int (*tor_api_cfg_set_command_line)(void *cfg, int argc, char **argv) = NULL;
  tor_control_socket_t (*tor_api_cfg_setup_ctrl_socket)(void *cfg) = NULL;

  handle_t = malloc(sizeof(kmp_tor_handle_t));
  if (handle_t == NULL) {
    return NULL;
  } else {
    handle_t->error_code = ERR_CODE_NONE;

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

  handle_t->argc = argc;
  handle_t->argv = malloc(argc * sizeof(char *));
  if (handle_t->argv == NULL) {
    kmp_tor_free(handle_t);
    return NULL;
  }

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

  // TODO: Configure pthread_attr_t

  handle_t->lib_t = lib_load_open(lib_tor);
  if (handle_t->lib_t == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  *(void **) (&handle_t->OPENSSL_cleanup) = lib_load_resolve(handle_t->lib_t, "OPENSSL_cleanup");
  if (handle_t->OPENSSL_cleanup == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  *(void **) (&handle_t->tor_api_cfg_free) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_free");
  if (handle_t->tor_api_cfg_free == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  *(void **) (&handle_t->args_t->tor_api_run_main) = lib_load_resolve(handle_t->lib_t, "tor_run_main");
  if (handle_t->args_t->tor_api_run_main == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  *(void **) (&tor_api_cfg_new) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_new");
  if (tor_api_cfg_new == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  *(void **) (&tor_api_cfg_set_command_line) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_set_command_line");
  if (tor_api_cfg_set_command_line == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  *(void **) (&tor_api_cfg_setup_ctrl_socket) = lib_load_resolve(handle_t->lib_t, "tor_main_configuration_setup_control_socket");
  if (tor_api_cfg_setup_ctrl_socket == NULL) {
    handle_t->error_code = ERR_CODE_LIB_LOAD;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  handle_t->args_t->cfg = tor_api_cfg_new();
  if (handle_t->args_t->cfg == NULL) {
    handle_t->error_code = ERR_CODE_TOR_CFG_NEW;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  if (tor_api_cfg_set_command_line(handle_t->args_t->cfg, handle_t->argc, handle_t->argv) != 0) {
    handle_t->error_code = ERR_CODE_TOR_CFG_SET;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  handle_t->ctrl_socket = tor_api_cfg_setup_ctrl_socket(handle_t->args_t->cfg);
  if (handle_t->ctrl_socket == INVALID_TOR_CONTROL_SOCKET) {
    handle_t->error_code = ERR_CODE_TOR_CFG_SET;
    pthread_attr_destroy(&attrs_t);
    return handle_t;
  }

  if (pthread_create(&handle_t->thread_id, &attrs_t, kmp_tor_run_thread, (void *) handle_t->args_t) != 0) {
    handle_t->error_code = ERR_CODE_THREAD;
  }
  pthread_attr_destroy(&attrs_t);

  if (handle_t->error_code == ERR_CODE_NONE) {
    usleep((useconds_t) 50 * 1000);
  } else {
    raw_closesocket(handle_t->ctrl_socket);
    handle_t->ctrl_socket = INVALID_TOR_CONTROL_SOCKET;
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

    raw_closesocket(handle_t->ctrl_socket);
    pthread_join(handle_t->thread_id, &res);

    if (res != NULL) {
      kmp_tor_run_thread_res_t *res_t = res;
      result = res_t->result;
      free(res_t);
    }
  }

  kmp_tor_free(handle_t);
  return result;
}
