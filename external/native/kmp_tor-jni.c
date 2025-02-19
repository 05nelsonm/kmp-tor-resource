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
#include <string.h>

static jstring null = NULL;

static char *
JStringDup(JNIEnv *env, jstring s)
{
  if (s == NULL) {
    return NULL;
  }

  char *dup = NULL;
  const char *c_arg = NULL;

  c_arg = (*env)->GetStringUTFChars(env, s, NULL);
  if (c_arg != NULL) {
    dup = strdup(c_arg);
    (*env)->ReleaseStringUTFChars(env, s, c_arg);
  }

  return dup;
}

static void
ThrowIllegalState(JNIEnv *env, const char *fmt, ...)
{
  jclass illegal_state = (*env)->FindClass(env, "java/lang/IllegalStateException");
  char msg[512];
  va_list args;
  va_start (args, fmt);
  vsnprintf(msg, sizeof(msg), fmt, args);
  va_end (args);
  (*env)->ThrowNew(env, illegal_state, msg);
}

JNIEXPORT jstring JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jstring lib_tor, jobjectArray args)
{
  if (lib_tor == NULL) {
    ThrowIllegalState(env, "lib_tor cannot be NULL");
    return null;
  }
  if (args == NULL) {
    ThrowIllegalState(env, "args cannot be NULL");
    return null;
  }

  jsize argc = (*env)->GetArrayLength(env, args);
  if (argc <= 0) {
    ThrowIllegalState(env, "args cannot be empty");
    return null;
  }

  int copy_args = 0;
  int c_argc = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;
  const char *error = NULL;

  c_lib_tor = JStringDup(env, lib_tor);
  if (c_lib_tor == NULL) {
    ThrowIllegalState(env, "JStringDup failed to copy lib_tor");
    return null;
  }

  c_argv = malloc(argc * sizeof(char *));
  if (c_argv == NULL) {
    free(c_lib_tor);
    ThrowIllegalState(env, "Failed to create c_argv");
    return null;
  }

  for (jsize i = 0; i < argc; i++) {
    if (copy_args != 0) {
      c_argv[c_argc++] = NULL;
      continue;
    }

    jstring j_arg = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    if (j_arg == NULL) {
      c_argv[c_argc] = NULL;
    } else {
      c_argv[c_argc] = JStringDup(env, j_arg);
      (*env)->DeleteLocalRef(env, j_arg);
    }

    if (c_argv[c_argc] == NULL) {
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
    if (c_argv[i] != NULL) {
      free(c_argv[i]);
    }
  }
  free(c_argv);
  free(c_lib_tor);

  if (error != NULL) {
    ThrowIllegalState(env, error);
  }
  return null;
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorState
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_state();
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorTerminateAndAwaitResult
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_terminate_and_await_result();
}
