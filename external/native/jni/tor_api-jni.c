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
#include "tor_api-jni.h"
#include "lib_load.h"

#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/*
 * Class:     io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi
 * Method:    kmpTorRunMain
 * Signature: (I;String;[Ljava/lang/String;)I
 *
 * Returns the following integer value depending on case:
 *  -10    : dlopen failed
 *  -11    : dlsym failed
 *  -12    : tor_main_configuration_new failed
 *  -13    : JNI GetArrayLength for argc failed
 *  -14    : malloc for argv failed
 *  -15    : strdup returned NULL when copying args to argv
 *  -16    : tor_main_configuration_set_command_line failed
 *  0      : tor_run_main success
 *  1 - 255: tor_run_main failed
 */
JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jint usleep_millis, jstring libtor, jobjectArray args)
{
  void *handle = NULL;
  void* (*fn_cfg_new)(void) = NULL;
  int (*fn_cfg_set_command_line)(void *cfg, int argc, char **argv) = NULL;
  void (*fn_cfg_free)(void *cfg) = NULL;
  int (*fn_run_main)(void *cfg) = NULL;

  const char *cstr_libtor = (*env)->GetStringUTFChars(env, libtor, NULL);
  handle = lib_load_open(cstr_libtor);
  (*env)->ReleaseStringUTFChars(env, libtor, cstr_libtor);
  if (handle == NULL) {
    return -10;
  }

  fn_cfg_new = lib_load_symbol(handle, "tor_main_configuration_new");
  if (fn_cfg_new == NULL) {
    lib_load_close(handle);
    return -11;
  }

  fn_cfg_set_command_line = lib_load_symbol(handle, "tor_main_configuration_set_command_line");
  if (fn_cfg_set_command_line == NULL) {
    fn_cfg_new = NULL;
    lib_load_close(handle);
    return -11;
  }

  *(void **) (&fn_cfg_free) = lib_load_symbol(handle, "tor_main_configuration_free");
  if (fn_cfg_free == NULL) {
    fn_cfg_new = NULL;
    fn_cfg_set_command_line = NULL;
    lib_load_close(handle);
    return -11;
  }

  *(void **) (&fn_run_main) = lib_load_symbol(handle, "tor_run_main");
  if (fn_run_main == NULL) {
    fn_cfg_new = NULL;
    fn_cfg_set_command_line = NULL;
    fn_cfg_free = NULL;
    lib_load_close(handle);
    return -11;
  }

  void *cfg = NULL;
  char **argv = NULL;
  int argc = -1;
  int rv = -1;

  cfg = fn_cfg_new();
  if (cfg == NULL) {
    fn_cfg_new = NULL;
    fn_cfg_set_command_line = NULL;
    fn_cfg_free = NULL;
    fn_run_main = NULL;
    lib_load_close(handle);
    return -12;
  }

  argc = (*env)->GetArrayLength(env, args);
  if (argc == -1) {
    fn_cfg_free(cfg);

    fn_cfg_new = NULL;
    fn_cfg_set_command_line = NULL;
    fn_cfg_free = NULL;
    fn_run_main = NULL;
    lib_load_close(handle);
    return -13;
  }

  argv = (char **) malloc(argc * sizeof(char *));
  if (argv == NULL) {
    fn_cfg_free(cfg);

    fn_cfg_new = NULL;
    fn_cfg_set_command_line = NULL;
    fn_cfg_free = NULL;
    fn_run_main = NULL;
    lib_load_close(handle);
    return -14;
  }

  for (jsize i = 0; i < argc; i++) {
    if (rv != -1) {
      argv[i] = NULL;
      continue;
    }

    jstring argStr = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    const char *arg = (*env)->GetStringUTFChars(env, argStr, NULL);
    argv[i] = strdup(arg);
    (*env)->ReleaseStringUTFChars(env, argStr, arg);

    if (argv[i] == NULL) {
      rv = -15;
    }
  }

  if (rv == -1) {
    if (fn_cfg_set_command_line(cfg, argc, argv) < 0) {
      rv = -16;
    }
  }

  if (rv == -1) {
    rv = fn_run_main(cfg);
    if (usleep_millis > 0) {
      useconds_t microsec = usleep_millis * 1000;
      usleep(microsec);
    }
  }

  fn_cfg_free(cfg);

  for (int i = 0; i < argc; i++) {
    char *arg = NULL;
    arg = argv[i];
    if (arg != NULL) {
      free(arg);
    }
  }
  free(argv);

  fn_cfg_new = NULL;
  fn_cfg_set_command_line = NULL;
  fn_cfg_free = NULL;
  fn_run_main = NULL;
  lib_load_close(handle);

  if (rv == -15 || rv == -16) {
    return rv;
  }

  if (rv < 0 || rv > 255) {
    return 1;
  } else {
    return rv;
  }
}
