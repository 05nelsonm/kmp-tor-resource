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
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>

typedef struct {
  int argc;
  char **argv;

  void* (*tor_api_cfg_new)(void);
  int (*tor_api_cfg_set_command_line)(void *cfg, int argc, char **argv);
  void (*tor_api_cfg_free)(void *cfg);
  int (*tor_api_run_main)(void *cfg);
} kmp_tor_run_thread_t;

typedef struct {
  int result;
} kmp_tor_run_thread_res_t;

void *
kmp_tor_run_thread(void *arg)
{
  kmp_tor_run_thread_t *args_t = arg;
  kmp_tor_run_thread_res_t *res_t = malloc(sizeof(kmp_tor_run_thread_res_t));
  if (res_t == NULL) {
    return NULL;
  }

  void *cfg = NULL;
  int rv = -1;

  cfg = args_t->tor_api_cfg_new();
  if (cfg == NULL) {
    res_t->result = -12;
    return res_t;
  }

  if (args_t->tor_api_cfg_set_command_line(cfg, args_t->argc, args_t->argv) < 0) {
    rv = -13;
  }

  if (rv == -1) {
    rv = args_t->tor_api_run_main(cfg);
    if (rv < 0 || rv > 255) {
      rv = 1;
    }
    usleep((useconds_t) 2000);
  }

  args_t->tor_api_cfg_free(cfg);
  res_t->result = rv;
  return res_t;
}

void
kmp_tor_free(kmp_tor_run_thread_t *args_t)
{
  assert(args_t != NULL);

  if (args_t->argv != NULL) {
    assert(args_t->argc > 0);

    for (int i = 0; i < args_t->argc; i++) {
      if (args_t->argv[i] != NULL) {
        free(args_t->argv[i]);
      }
    }
    free(args_t->argv);
  }

  args_t->tor_api_cfg_new = NULL;
  args_t->tor_api_cfg_set_command_line = NULL;
  args_t->tor_api_cfg_free = NULL;
  args_t->tor_api_run_main = NULL;

  free(args_t);
}

/*
 * Returns the following integer value depending on case:
 *  -7     : invalid arguments
 *  -8     : kmp_tor_run_thread_t configuration failure
 *  -9     : pthread_attr_t configuration failure
 *  -10    : dlopen/dlsym failure
 *  -11    : pthread failure
 *  -12    : tor_main_configuration_new failure
 *  -13    : tor_main_configuration_set_command_line failure
 *  0      : tor_run_main returned success
 *  1 - 255: tor_run_main returned failure
 */
int
kmp_tor_run_main(int shutdown_delay_millis, const char *libtor, int argc, char *argv[])
{
  if (shutdown_delay_millis <= 0) {
    return -7;
  }
  if (libtor == NULL) {
    return -7;
  }
  if (argc <= 0) {
    return -7;
  }
  if (argv == NULL) {
    return -7;
  }

  kmp_tor_run_thread_t *args_t = malloc(sizeof(kmp_tor_run_thread_t));
  if (args_t == NULL) {
    return -8;
  }

  args_t->argc = argc;
  args_t->argv = malloc(argc * sizeof(char *));
  if (args_t->argv == NULL) {
    kmp_tor_free(args_t);
    return -8;
  }

  int result = 0;
  for (int i = 0; i < argc; i++) {
    if (result != 0) {
      args_t->argv[i] = NULL;
      continue;
    }

    args_t->argv[i] = strdup(argv[i]);
    if (args_t->argv[i] == NULL) {
      result = -1;
    }
  }

  if (result != 0) {
    kmp_tor_free(args_t);
    return -8;
  }

  pthread_attr_t attrs_t;
  result = pthread_attr_init(&attrs_t);
  if (result != 0) {
    kmp_tor_free(args_t);
    return -9;
  }

  // TODO: Configure pthread_attr_t

  lib_handle_t *handle_t = lib_load_open(libtor);
  if (handle_t == NULL) {
    kmp_tor_free(args_t);
    pthread_attr_destroy(&attrs_t);
    return -10;
  }

  *(void **) (&args_t->tor_api_cfg_new) = lib_load_resolve(handle_t, "tor_main_configuration_new");
  if (args_t->tor_api_cfg_new == NULL) {
    kmp_tor_free(args_t);
    pthread_attr_destroy(&attrs_t);
    lib_load_close(handle_t);
    return -10;
  }

  *(void **) (&args_t->tor_api_cfg_set_command_line) = lib_load_resolve(handle_t, "tor_main_configuration_set_command_line");
  if (args_t->tor_api_cfg_set_command_line == NULL) {
    kmp_tor_free(args_t);
    pthread_attr_destroy(&attrs_t);
    lib_load_close(handle_t);
    return -10;
  }

  *(void **) (&args_t->tor_api_cfg_free) = lib_load_resolve(handle_t, "tor_main_configuration_free");
  if (args_t->tor_api_cfg_free == NULL) {
    kmp_tor_free(args_t);
    pthread_attr_destroy(&attrs_t);
    lib_load_close(handle_t);
    return -10;
  }

  *(void **) (&args_t->tor_api_run_main) = lib_load_resolve(handle_t, "tor_run_main");
  if (args_t->tor_api_run_main == NULL) {
    kmp_tor_free(args_t);
    pthread_attr_destroy(&attrs_t);
    lib_load_close(handle_t);
    return -10;
  }

  pthread_t thread_id;

  result = pthread_create(&thread_id, &attrs_t, kmp_tor_run_thread, (void *) args_t);
  pthread_attr_destroy(&attrs_t);
  if (result != 0) {
    kmp_tor_free(args_t);
    lib_load_close(handle_t);
    return -11;
  }

  void *res = NULL;
  result = pthread_join(thread_id, &res);
  if (result != 0) {
    // TODO: pthread_kill
  }
  if (res != NULL) {
    kmp_tor_run_thread_res_t *res_t = res;
    result = res_t->result;
    free(res_t);
  } else {
    result = -11;
  }

  if (result >= 0) {
    // tor_run_main was called
    usleep((useconds_t) shutdown_delay_millis * 1000);
  }

  kmp_tor_free(args_t);
  lib_load_close(handle_t);
  return result;
}
