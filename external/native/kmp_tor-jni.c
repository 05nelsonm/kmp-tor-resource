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

#include <stdlib.h>

static char *
JCharArrayToCString(JNIEnv *env, jcharArray a)
{
  if (!a) {
    return NULL;
  }

  char *c_arg = NULL;
  int len = -1;

  len = (*env)->GetArrayLength(env, a);
  if (len < 0) {
    return NULL;
  }

  c_arg = malloc((len + 1) * sizeof(char *));
  if (!c_arg) {
    return NULL;
  } else {
    c_arg[len] = '\0';
  }

  if (len == 0) {
    return c_arg;
  }

  jchar buf[len];
  (*env)->GetCharArrayRegion(env, a, 0, len, buf);

  for (jsize i = 0; i < len; i++) {
    c_arg[i] = buf[i];
  }

  return c_arg;
}

JNIEXPORT jstring JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jcharArray lib_tor, jobjectArray args)
{
  if (!lib_tor) {
    return (*env)->NewStringUTF(env, "lib_tor cannot be NULL");
  }
  if (!args) {
    return (*env)->NewStringUTF(env, "args cannot be NULL");
  }

  jsize j_argc = (*env)->GetArrayLength(env, args);
  if (j_argc <= 0) {
    return (*env)->NewStringUTF(env, "args cannot be empty");
  }

  int copy_args = 0;
  int c_argc = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;
  const char *error = NULL;

  c_lib_tor = JCharArrayToCString(env, lib_tor);
  if (!c_lib_tor) {
    return (*env)->NewStringUTF(env, "JCharArrayToCString failed to copy lib_tor");
  }

  c_argv = malloc(j_argc * sizeof(char *));
  if (!c_argv) {
    free(c_lib_tor);
    c_lib_tor = NULL;
    return (*env)->NewStringUTF(env, "Failed to create c_argv");
  }

  for (jsize i = 0; i < j_argc; i++) {
    if (copy_args != 0) {
      c_argv[c_argc] = NULL;
      c_argc++;
      continue;
    }

    jcharArray j_arg = (jcharArray) (*env)->GetObjectArrayElement(env, args, i);
    c_argv[c_argc] = JCharArrayToCString(env, j_arg);

    if (!c_argv[c_argc]) {
      copy_args = -1;
    }

    c_argc++;
  }

  if (copy_args == 0) {
    error = kmp_tor_run_main(c_lib_tor, c_argc, c_argv);
  } else {
    error = "Failed to copy arguments to C";
  }

  for (int i = 0; i < c_argc; i++) {
    if (c_argv[i]) {
      free(c_argv[i]);
      c_argv[i] = NULL;
    }
  }
  free(c_argv);
  c_argv = NULL;
  free(c_lib_tor);
  c_lib_tor = NULL;

  if (error) {
    return (*env)->NewStringUTF(env, error);
  }

  return NULL;
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorState
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_state();
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorTerminateAndAwaitResult
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_terminate_and_await_result();
}
