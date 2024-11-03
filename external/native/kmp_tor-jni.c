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

#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

static kmp_tor_handle_t *handle_t = NULL;

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorRunMainJNI
(JNIEnv *env, jobject thiz, jstring lib_tor, jint argc, jobjectArray args)
{
  int c_argc = 0;
  int result = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;
  kmp_tor_handle_t *_handle_t = NULL;

  if (lib_tor == NULL) {
    return 1;
  }
  if (argc <= 0) {
    return 1;
  }
  if (args == NULL) {
    return 1;
  }

  if (c_argc == 0) {
    jboolean is_copy;
    const char *c_arg = (*env)->GetStringUTFChars(env, lib_tor, &is_copy);
    if (c_arg != NULL) {
      c_lib_tor = strdup(c_arg);
    }
    if (is_copy) {
      (*env)->ReleaseStringUTFChars(env, lib_tor, c_arg);
    }
  }

  if (c_lib_tor == NULL) {
    return 1;
  }

  c_argv = malloc(argc * sizeof(char *));
  if (c_argv == NULL) {
    free(c_lib_tor);
    return 1;
  }

  for (jsize i = 0; i < argc; i++) {
    if (result != 0) {
      c_argv[c_argc++] = NULL;
      continue;
    }

    jstring j_arg = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    if (j_arg == NULL) {
      c_argv[c_argc] = NULL;
    } else {
      jboolean is_copy;
      const char *c_arg = (*env)->GetStringUTFChars(env, j_arg, &is_copy);
      if (c_arg != NULL) {
        c_argv[c_argc] = strdup(c_arg);
      } else {
        c_argv[c_argc] = NULL;
      }
      if (is_copy) {
        (*env)->ReleaseStringUTFChars(env, j_arg, c_arg);
      }
      (*env)->DeleteLocalRef(env, j_arg);
    }

    if (c_argv[c_argc] == NULL) {
      result = -1;
    }

    c_argc++;
  }

  if (result == 0) {
    _handle_t = kmp_tor_run_main(c_lib_tor, c_argc, c_argv);
  }

  for (int i = 0; i < c_argc; i++) {
    if (c_argv[i] != NULL) {
      free(c_argv[i]);
    }
  }
  free(c_argv);
  free(c_lib_tor);

  if (_handle_t == NULL) {
    return 1;
  }

  handle_t = _handle_t;
  usleep((useconds_t) 5 * 1000);
  return 0;
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
  int result;
  kmp_tor_handle_t *_handle_t = NULL;

  _handle_t = handle_t;
  handle_t = NULL;
  result = kmp_tor_terminate_and_await_result(_handle_t);
  usleep((useconds_t) 5 * 1000);
  return result;
}
