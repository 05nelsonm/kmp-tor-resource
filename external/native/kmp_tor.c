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
  lib_handle_t *handle;
} kmp_tor_run_thread_res_t;

void *
kmp_tor_run_thread(void *arg)
{
  kmp_tor_run_thread_t *arg_t = arg;
  kmp_tor_run_thread_res_t *res_t = malloc(sizeof(kmp_tor_run_thread_res_t));
  if (res_t == NULL) {
    return NULL;
  }

  res_t->handle = lib_load_open(arg_t->libtor);
  if (res_t->handle == NULL) {
    res_t->result = -10;
    return res_t;
  }

  void* (*fn_cfg_new)(void) = NULL;
  int (*fn_cfg_set_command_line)(void *cfg, int argc, char **argv) = NULL;
  void (*fn_cfg_free)(void *cfg) = NULL;
  int (*fn_run_main)(void *cfg) = NULL;

  *(void **) (&fn_cfg_new) = lib_load_resolve(res_t->handle, "tor_main_configuration_new");
  if (fn_cfg_new == NULL) {
    res_t->result = -11;
    return res_t;
  }

  *(void **) (&fn_cfg_set_command_line) = lib_load_resolve(res_t->handle, "tor_main_configuration_set_command_line");
  if (fn_cfg_set_command_line == NULL) {
    res_t->result = -11;
    return res_t;
  }

  *(void **) (&fn_cfg_free) = lib_load_resolve(res_t->handle, "tor_main_configuration_free");
  if (fn_cfg_free == NULL) {
    res_t->result = -11;
    return res_t;
  }

  *(void **) (&fn_run_main) = lib_load_resolve(res_t->handle, "tor_run_main");
  if (fn_run_main == NULL) {
    res_t->result = -11;
    return res_t;
  }

  void *cfg = NULL;
  int rv = -1;

  cfg = fn_cfg_new();
  if (cfg == NULL) {
    res_t->result = -12;
    return res_t;
  }

  if (fn_cfg_set_command_line(cfg, arg_t->argc, arg_t->argv) < 0) {
    rv = -13;
  }

  if (rv == -1) {
    rv = fn_run_main(cfg);
    if (rv < 0 || rv > 255) {
      rv = 1;
    }
    usleep((useconds_t) 2000);
  }

  fn_cfg_free(cfg);
  res_t->result = rv;
  return res_t;
}

/*
 * Returns the following integer value depending on case:
 *  -8     : malloc failed for kmp_tor_run_thread_t
 *  -9     : pthread failure
 *  -10    : dlopen failed
 *  -11    : dlsym failed
 *  -12    : tor_main_configuration_new failed
 *  -13    : tor_main_configuration_set_command_line failed
 *  0      : tor_run_main success
 *  1 - 255: tor_run_main failed
 */
int
kmp_tor_run_main(int usleep_millis, const char *libtor, int argc, char *argv[])
{
  kmp_tor_run_thread_t *args_t = malloc(sizeof(kmp_tor_run_thread_t));
  if (args_t == NULL) {
    return -8;
  }

  args_t->libtor = libtor;
  args_t->argc = argc;
  args_t->argv = argv;

  int p;
  void *res;
  pthread_t thread_id;
  pthread_attr_t attr;

  p = pthread_attr_init(&attr);
  if (p != 0) {
    free(args_t);
    return -9;
  }

  // TODO: Configure attrs

  p = pthread_create(&thread_id, &attr, kmp_tor_run_thread, (void *) args_t);
  pthread_attr_destroy(&attr);

  if (p != 0) {
    free(args_t);
    return -9;
  }

  p = pthread_join(thread_id, &res);
  if (p != 0) {
    // TODO: pthread_exit
  }
  if (res == NULL) {
    // TODO
  }
  if (usleep_millis > 0) {
    usleep((useconds_t) usleep_millis * 1000);
  }

  kmp_tor_run_thread_res_t *res_t = res;
  if (res_t->handle != NULL) {
    lib_load_close(res_t->handle);
  }

  p = res_t->result;
  free(args_t);
  free(res_t);
  return p;
}
