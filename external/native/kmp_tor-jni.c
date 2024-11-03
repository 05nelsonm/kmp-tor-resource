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

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorRunMainJNI
(JNIEnv *env, jobject thiz, jstring lib_tor, jint argc, jobjectArray args)
{
  assert(handle_t == NULL);

  int c_argc = 0;
  int result = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;

  if (c_argc == 0) {
    const char *c_arg = (*env)->GetStringUTFChars(env, lib_tor, NULL);
    if (c_arg == NULL) {
      return 1;
    }
    c_lib_tor = strdup(c_arg);
    (*env)->ReleaseStringUTFChars(env, lib_tor, c_arg);
  }

  if (c_lib_tor == NULL) {
    return 1;
  }

  c_argv = malloc(argc * sizeof(char *));
  if (c_argv == NULL) {
    free(c_lib_tor);
    return 1;
  }

  for (int i = 0; i < argc; i++) {
    c_argc++;

    if (result != 0) {
      c_argv[i] = NULL;
      continue;
    }

    jstring arg = (jstring) (*env) ->GetObjectArrayElement(env, args, i);
    const char *c_arg = (*env)->GetStringUTFChars(env, arg, NULL);
    if (c_arg != NULL) {
      c_argv[i] = strdup(c_arg);
      (*env)->ReleaseStringUTFChars(env, arg, c_arg);
    } else {
      c_argv[i] = NULL;
    }

    if (c_argv[i] == NULL) {
      result = -1;
    }
  }

  if (result == 0) {
    handle_t = kmp_tor_run_main(c_lib_tor, c_argc, c_argv);
  }

  for (int i = 0; i < c_argc; i++) {
    if (c_argv[i] != NULL) {
      free(c_argv[i]);
    }
  }
  free(c_argv);
  free(c_lib_tor);

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
  kmp_tor_handle_t *_handle_t = NULL;
  _handle_t = handle_t;
  handle_t = NULL;
  return kmp_tor_terminate_and_await_result(_handle_t);
}
