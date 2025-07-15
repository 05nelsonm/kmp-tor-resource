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

#include <jni.h>
#include <assert.h>
#include <stdlib.h>
#include <string.h>

#ifndef JNI_VERSION_1_6
#define __JNI_VERSION 0x00010006
#else
#define __JNI_VERSION JNI_VERSION_1_6
#endif // JNI_VERSION_1_6

#define ERR_BUF_LEN 512

static kmp_tor_context_t *ctx = NULL;
static jclass clazz_kmp_tor_api = NULL;

static int
CStringToErrBuf(JNIEnv *env, jcharArray err_buf, const char *error)
{
  // err_buf is checked for non-NULL & capacity ERR_BUF_LEN in KMP_TOR_JNI_kmpTorRunMain

  if (!error) {
    return 0;
  }

  int len = strlen(error);
  if (len <= 0) {
    return 0;
  }
  if (len > ERR_BUF_LEN) {
    len = ERR_BUF_LEN;
  }

  jchar j_error[len];
  for (jsize i = 0; i < len; i++) {
    j_error[i] = error[i];
  }
  (*env)->SetCharArrayRegion(env, err_buf, 0, len, j_error);

  return len;
}

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

  jchar j_buf[len];
  (*env)->GetCharArrayRegion(env, a, 0, len, j_buf);

  for (jsize i = 0; i < len; i++) {
    c_arg[i] = j_buf[i];
  }

  return c_arg;
}

static jint JNICALL
KMP_TOR_JNI_kmpTorRunMain
(JNIEnv *env, jobject thiz, jcharArray lib_tor, jobjectArray args, jcharArray err_buf)
{
  assert(lib_tor);
  assert(args);
  assert(err_buf);
  assert((*env)->GetArrayLength(env, err_buf) == ERR_BUF_LEN);

  jsize j_argc = (*env)->GetArrayLength(env, args);
  if (j_argc <= 0) {
    return CStringToErrBuf(env, err_buf, "args cannot be empty");
  }

  int copy_args = 0;
  int c_argc = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;
  const char *error = NULL;

  c_lib_tor = JCharArrayToCString(env, lib_tor);
  if (!c_lib_tor) {
    return CStringToErrBuf(env, err_buf, "JCharArrayToCString failed to copy lib_tor");
  }

  c_argv = malloc(j_argc * sizeof(char *));
  if (!c_argv) {
    free(c_lib_tor);
    c_lib_tor = NULL;
    return CStringToErrBuf(env, err_buf, "Failed to create c_argv");
  }

  for (jsize i = 0; i < j_argc; i++) {
    if (copy_args != 0) {
      c_argv[c_argc] = NULL;
      c_argc++;
      continue;
    }

    jcharArray j_arg = (jcharArray) (*env)->GetObjectArrayElement(env, args, i);
    c_argv[c_argc] = JCharArrayToCString(env, j_arg);
    if (j_arg) {
      (*env)->DeleteLocalRef(env, j_arg);
    }

    if (!c_argv[c_argc]) {
      copy_args = -1;
    }

    c_argc++;
  }

  if (copy_args == 0) {
    error = kmp_tor_run_main(ctx, c_lib_tor, c_argc, c_argv);
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

  return CStringToErrBuf(env, err_buf, error);
}

static jint JNICALL
KMP_TOR_JNI_kmpTorState
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_state(ctx);
}

static jint JNICALL
KMP_TOR_JNI_kmpTorTerminateAndAwaitResult
(JNIEnv *env, jobject thiz)
{
  return kmp_tor_terminate_and_await_result(ctx);
}

static JNINativeMethod kmp_tor_jni_methods[] = {
  {"kmpTorRunMain",                 "([C[[C[C)I", (void *) &KMP_TOR_JNI_kmpTorRunMain},
  {"kmpTorState",                   "()I",        (void *) &KMP_TOR_JNI_kmpTorState},
  {"kmpTorTerminateAndAwaitResult", "()I",        (void *) &KMP_TOR_JNI_kmpTorTerminateAndAwaitResult},
};

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved)
{
  if (ctx) {
    return JNI_ERR;
  }

  JNIEnv *env;
  if ((*vm)->GetEnv(vm, (void **)&env, __JNI_VERSION) != JNI_OK) {
    return JNI_ERR;
  }

  jclass c = (*env)->FindClass(env, "io/matthewnelson/kmp/tor/resource/noexec/tor/internal/KmpTorApi");
  if (!c) {
    return JNI_ERR;
  }
  clazz_kmp_tor_api = (*env)->NewGlobalRef(env, c);
  (*env)->DeleteLocalRef(env, c);
  if (!clazz_kmp_tor_api) {
    return JNI_ERR;
  }

  ctx = kmp_tor_init();
  if (!ctx) {
    (*env)->DeleteGlobalRef(env, clazz_kmp_tor_api);
    clazz_kmp_tor_api = NULL;
    return JNI_ERR;
  }

  int r = (*env)->RegisterNatives(env, clazz_kmp_tor_api, kmp_tor_jni_methods, sizeof(kmp_tor_jni_methods)/sizeof(JNINativeMethod));
  if (r != JNI_OK) {
    kmp_tor_deinit(ctx);
    ctx = NULL;
    (*env)->DeleteGlobalRef(env, clazz_kmp_tor_api);
    clazz_kmp_tor_api = NULL;
    return JNI_ERR;
  }

  return __JNI_VERSION;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM *vm, void *reserved)
{
  kmp_tor_deinit(ctx);
  ctx = NULL;

  JNIEnv *env;
  if ((*vm)->GetEnv(vm, (void **)&env, __JNI_VERSION) != JNI_OK) {
    return;
  }
  if (!clazz_kmp_tor_api) {
    return;
  }
  (*env)->UnregisterNatives(env, clazz_kmp_tor_api);
  (*env)->DeleteGlobalRef(env, clazz_kmp_tor_api);
  clazz_kmp_tor_api = NULL;
}
