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
  int result;
} kmp_tor_run_thread_res_t;

typedef struct {
  kmp_tor_run_thread_res_t *res_t;
  void *cfg;
  int (*tor_api_run_main)(void *cfg);
} kmp_tor_run_thread_args_t;

struct kmp_tor_handle_t {
  int error_code;

  int argc;
  char **argv;

  void (*tor_api_cfg_free)(void *cfg);

  pthread_t thread_id;
  kmp_tor_run_thread_args_t *args_t;
  lib_handle_t *handle_t;
};

void *
kmp_tor_run_thread(void *arg)
{
  int rv = -1;
  kmp_tor_run_thread_args_t *args_t = arg;

  rv = args_t->tor_api_run_main(args_t->cfg);
  if (rv < 0 || rv > 255) {
    rv = 1;
  }

  args_t->res_t->result = rv;
  return args_t->res_t;
}

void
kmp_tor_handle_free(kmp_tor_handle_t *handle_t)
{
  assert(handle_t != NULL);
}

kmp_tor_handle_t *
kmp_tor_run_main(const char *lib_tor, int argc, char *argv[])
{
  // TODO
  if (lib_tor == NULL) {
    return NULL;
  }
  if (argc <= 0) {
    return NULL;
  }
  if (argv == NULL) {
    return NULL;
  }
  return NULL;
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
  // TODO
  return 1;
}
