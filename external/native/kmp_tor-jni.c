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
#include "kmp_tor-jni.h"
#include "kmp_tor.h"

#include <assert.h>
#include <jni.h>
#include <stdlib.h>
#include <string.h>

static kmp_tor_handle_t *handle_t = NULL;

char *
JStringDup(JNIEnv *env, jstring arg)
{
  const char *_arg = (*env)->GetStringUTFChars(env, arg, NULL);
  char *dup = strdup(_arg);
  (*env)->ReleaseStringUTFChars(env, arg, _arg);
  return dup;
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorRunMainJNI
(JNIEnv *env, jobject thiz, jstring lib_tor, jobjectArray args)
{
  assert(handle_t == NULL);

  int argc = -1;
  int result = 0;
  char **argv = NULL;
  char *lib_tor_cstr = NULL;

  argc = (*env)->GetArrayLength(env, args);
  if (argc <= 0) {
    return 1;
  }

  lib_tor_cstr = JStringDup(env, lib_tor);
  if (lib_tor_cstr == NULL) {
    return 1;
  }

  argv = malloc(argc * sizeof(char *));
  if (argv == NULL) {
    free(lib_tor_cstr);
    return 1;
  }

  for (int i = 0; i < argc; i++) {
    if (result != 0) {
      argv[i] = NULL;
      continue;
    }

    jstring arg = (jstring) (*env) ->GetObjectArrayElement(env, args, i);
    argv[i] = JStringDup(env, arg);

    if (argv[i] == NULL) {
      result = -1;
    }
  }

  if (result == 0) {
    handle_t = kmp_tor_run_main(lib_tor_cstr, argc, argv);
  }

  for (int i = 0; i < argc; i++) {
    if (argv[i] != NULL) {
      free(argv[i]);
    }
  }
  free(argv);
  free(lib_tor_cstr);

  if (handle_t == NULL) {
    return 1;
  } else {
    return 0;
  }
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorCheckErrorCodeJNI
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_check_error_code(handle_t);
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorTerminateAndAwaitResultJNI
(JNIEnv *env, jobject thiz)
{
  int result = 0;
  result = kmp_tor_terminate_and_await_result(handle_t);
  handle_t = NULL;
  return result;
}
