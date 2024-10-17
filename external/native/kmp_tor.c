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

void* (*fn_cfg_new)(void) = NULL;
int (*fn_cfg_set_command_line)(void *cfg, int argc, char **argv) = NULL;
void (*fn_cfg_free)(void *cfg) = NULL;
int (*fn_run_main)(void *cfg) = NULL;

int
libtor_free(const char *libtor, void *handle)
{
  int result;

  fn_cfg_new = NULL;
  fn_cfg_set_command_line = NULL;
  fn_cfg_free = NULL;
  fn_run_main = NULL;

  result = lib_load_close(libtor, handle);

  return result;
}

/*
 * Returns the following integer value depending on case:
 *  -10    : dlopen failed
 *  -11    : dlsym failed
 *  -12    : tor_main_configuration_new failed
 *  -13    : tor_main_configuration_set_command_line failed
 *  0      : tor_run_main success
 *  1 - 255: tor_run_main failed
 */
int kmp_tor_run_main(int usleep_millis, const char *libtor, int argc, char *argv[])
{
  void *handle = NULL;

  handle = lib_load_open(libtor);
  if (handle == NULL) {
    return -10;
  }

  fn_cfg_new = lib_load_symbol(handle, "tor_main_configuration_new");
  if (fn_cfg_new == NULL) {
    libtor_free(libtor, handle);
    return -11;
  }

  fn_cfg_set_command_line = lib_load_symbol(handle, "tor_main_configuration_set_command_line");
  if (fn_cfg_set_command_line == NULL) {
    libtor_free(libtor, handle);
    return -11;
  }

  *(void **) (&fn_cfg_free) = lib_load_symbol(handle, "tor_main_configuration_free");
  if (fn_cfg_free == NULL) {
    libtor_free(libtor, handle);
    return -11;
  }

  *(void **) (&fn_run_main) = lib_load_symbol(handle, "tor_run_main");
  if (fn_run_main == NULL) {
    libtor_free(libtor, handle);
    return -11;
  }

  void *cfg = NULL;
  int rv = -1;

  cfg = fn_cfg_new();
  if (cfg == NULL) {
    libtor_free(libtor, handle);
    return -12;
  }

  if (fn_cfg_set_command_line(cfg, argc, argv) < 0) {
    rv = -13;
  }

  if (rv == -1) {
    rv = fn_run_main(cfg);
    if (usleep_millis > 0) {
      useconds_t microsec = usleep_millis * 1000;
      usleep(microsec);
    }
  }

  fn_cfg_free(cfg);
  cfg = NULL;
  libtor_free(libtor, handle);

  if (rv == -13) {
    return rv;
  }

  if (rv < 0 || rv > 255) {
    return 1;
  } else {
    return rv;
  }
}
