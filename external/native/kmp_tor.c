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
  const char *libtor;
  int argc;
  char **argv;
} kmp_tor_run_thread_t;

typedef struct {
  int result;
  lib_handle_t *handle_t;
} kmp_tor_run_thread_res_t;

void *
kmp_tor_run_thread(void *arg)
{
  kmp_tor_run_thread_t *args_t = arg;
  kmp_tor_run_thread_res_t *res_t = malloc(sizeof(kmp_tor_run_thread_res_t));
  if (res_t == NULL) {
    return NULL;
  }

  res_t->handle_t = lib_load_open(args_t->libtor);
  if (res_t->handle_t == NULL) {
    res_t->result = -12;
    return res_t;
  }

  void* (*tor_api_cfg_new)(void) = NULL;
  int (*tor_api_cfg_set_command_line)(void *cfg, int argc, char **argv) = NULL;
  void (*tor_api_cfg_free)(void *cfg) = NULL;
  int (*tor_api_run_main)(void *cfg) = NULL;

  *(void **) (&tor_api_cfg_new) = lib_load_resolve(res_t->handle_t, "tor_main_configuration_new");
  if (tor_api_cfg_new == NULL) {
    res_t->result = -12;
    return res_t;
  }

  *(void **) (&tor_api_cfg_set_command_line) = lib_load_resolve(res_t->handle_t, "tor_main_configuration_set_command_line");
  if (tor_api_cfg_set_command_line == NULL) {
    res_t->result = -12;
    return res_t;
  }

  *(void **) (&tor_api_cfg_free) = lib_load_resolve(res_t->handle_t, "tor_main_configuration_free");
  if (tor_api_cfg_free == NULL) {
    res_t->result = -12;
    return res_t;
  }

  *(void **) (&tor_api_run_main) = lib_load_resolve(res_t->handle_t, "tor_run_main");
  if (tor_api_run_main == NULL) {
    res_t->result = -12;
    return res_t;
  }

  void *cfg = NULL;
  int rv = -1;

  cfg = tor_api_cfg_new();
  if (cfg == NULL) {
    res_t->result = -13;
    return res_t;
  }

  if (tor_api_cfg_set_command_line(cfg, args_t->argc, args_t->argv) < 0) {
    rv = -14;
  }

  if (rv == -1) {
    rv = tor_api_run_main(cfg);
    if (rv < 0 || rv > 255) {
      rv = 1;
    }
    usleep((useconds_t) 2000);
  }

  tor_api_cfg_free(cfg);
  res_t->result = rv;
  return res_t;
}

/*
 * Returns the following integer value depending on case:
 *  -10    : invalid arguments
 *  -11    : configuration failure
 *  -12    : dlopen/dlsym failure
 *  -13    : tor_main_configuration_new failure
 *  -14    : tor_main_configuration_set_command_line failure
 *  0      : tor_run_main returned success
 *  1 - 255: tor_run_main returned failure
 */
int
kmp_tor_run_main(int shutdown_delay_millis, const char *libtor, int argc, char *argv[])
{
  if (shutdown_delay_millis <= 0) {
    return -10;
  }
  if (libtor == NULL) {
    return -10;
  }
  if (argc <= 0) {
    return -10;
  }
  if (argv == NULL) {
    return -10;
  }

  kmp_tor_run_thread_t *args_t = malloc(sizeof(kmp_tor_run_thread_t));
  if (args_t == NULL) {
    return -11;
  }

  args_t->libtor = libtor;
  args_t->argc = argc;
  args_t->argv = argv;

  int result = 0;
  pthread_attr_t attrs_t;
  result = pthread_attr_init(&attrs_t);
  if (result != 0) {
    free(args_t);
    return -11;
  }

  // TODO: Configure pthread_attr_t

  pthread_t thread_id;
  result = pthread_create(&thread_id, &attrs_t, kmp_tor_run_thread, (void *) args_t);
  pthread_attr_destroy(&attrs_t);
  if (result != 0) {
    free(args_t);
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

    if (result >= 0) {
      // tor_run_main was called
      usleep((useconds_t) shutdown_delay_millis * 1000);
    }

    if (res_t->handle_t != NULL) {
      lib_load_close(res_t->handle_t);
    }

    free(res);
  } else {
    result = -11;
  }

  free(args_t);
  return result;
}
