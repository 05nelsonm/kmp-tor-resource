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

static jobject null = NULL;

static jclass exception_clazz = NULL;
static jclass pointer_clazz = NULL;
static jfieldID pointer_field = NULL;
static jmethodID pointer_init = NULL;

void *
JLongToPointer(jlong l)
{
  if (l < 0) {
    return NULL;
  }
  void *result;
  memcpy(&result, &l, sizeof(void *));
  return result;
}

jlong
PointerToJLong(void *p)
{
  if (p == NULL) {
    return -1;
  }
  jlong result;
  memcpy(&result, &p, sizeof(void *));
  return result;
}

char *
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

void
ThrowIllegalState(JNIEnv *env, const char *fmt, ...)
{
  jclass illegal_state = exception_clazz;
  if (illegal_state == NULL) {
    illegal_state = (*env)->FindClass(env, "java/lang/IllegalStateException");
  }
  char msg[512];
  va_list args;
  va_start (args, fmt);
  vsnprintf(msg, sizeof(msg), fmt, args);
  va_end (args);
  (*env)->ThrowNew(env, illegal_state, msg);
}

JNIEXPORT jobject JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_torJNIRunMain
(JNIEnv *env, jobject thiz, jstring lib_tor, jstring win32_af_unix_path, jint argc, jobjectArray args)
{
  if (pointer_clazz == NULL) {
    ThrowIllegalState(env, "Pointer class not set");
    return null;
  }
  if (pointer_field == NULL) {
    ThrowIllegalState(env, "Pointer.value field not set");
    return null;
  }
  if (pointer_init == NULL) {
    ThrowIllegalState(env, "Pointer.<init> method not set");
    return null;
  }
  if (lib_tor == NULL) {
    ThrowIllegalState(env, "lib_tor cannot be NULL");
    return null;
  }
  if (args == NULL) {
    ThrowIllegalState(env, "args cannot be NULL");
    return null;
  }
  if (argc <= 0) {
    ThrowIllegalState(env, "argc must be greater than 0");
    return null;
  }

  int copy_args = 0;
  int c_argc = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;
  char *c_win32_af_unix_path = NULL;
  kmp_tor_handle_t *handle_t = NULL;

  c_lib_tor = JStringDup(env, lib_tor);
  if (c_lib_tor == NULL) {
    return null;
  }

  if (win32_af_unix_path != NULL) {
    c_win32_af_unix_path = JStringDup(env, win32_af_unix_path);
    if (c_win32_af_unix_path == NULL) {
      free(c_lib_tor);
      return null;
    }
  }

  c_argv = malloc(argc * sizeof(char *));
  if (c_argv == NULL) {
    free(c_lib_tor);
    if (c_win32_af_unix_path != NULL) {
      free(c_win32_af_unix_path);
    }
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
    handle_t = kmp_tor_run_main(c_lib_tor, c_win32_af_unix_path, c_argc, c_argv);
  }

  for (int i = 0; i < c_argc; i++) {
    if (c_argv[i] != NULL) {
      free(c_argv[i]);
    }
  }
  free(c_argv);
  free(c_lib_tor);
  if (c_win32_af_unix_path != NULL) {
    free(c_win32_af_unix_path);
  }

  if (handle_t == NULL) {
    return null;
  }

  jlong j_handle_t = PointerToJLong(handle_t);
  if (j_handle_t < 0) {
    kmp_tor_terminate_and_await_result(handle_t);
    ThrowIllegalState(env, "PointerToJLong failed");
    return null;
  }

  jobject pointer = (*env)->NewObject(env, pointer_clazz, pointer_init, j_handle_t);
  if (pointer == NULL) {
    kmp_tor_terminate_and_await_result(handle_t);
    ThrowIllegalState(env, "NewObject failed");
    return null;
  }

  return pointer;
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_torJNICheckErrorCode
(JNIEnv *env, jobject thiz, jobject pointer)
{
  if (pointer == NULL) {
    return -1;
  }
  kmp_tor_handle_t *handle_t = NULL;
  jlong j_handle_t = (*env)->GetLongField(env, pointer, pointer_field);
  handle_t = JLongToPointer(j_handle_t);
  if (handle_t == NULL) {
    return -1;
  }
  return kmp_tor_check_error_code(handle_t);
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_torJNITerminateAndAwaitResult
(JNIEnv *env, jobject thiz, jobject pointer)
{
  if (pointer == NULL) {
    ThrowIllegalState(env, "pointer cannot be NULL");
    return -1;
  }
  kmp_tor_handle_t *handle_t = NULL;
  jlong j_handle_t = (*env)->GetLongField(env, pointer, pointer_field);
  handle_t = JLongToPointer(j_handle_t);
  if (handle_t == NULL) {
    ThrowIllegalState(env, "JLongToPointer failed (has this handle already been terminated?)");
    return -1;
  }
  (*env)->SetLongField(env, pointer, pointer_field, -1);
  return kmp_tor_terminate_and_await_result(handle_t);
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *jvm, void *reserved)
{
  JNIEnv *env = NULL;

  if (JNI_OK != (*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_6)) {
    return JNI_ERR;
  }

  exception_clazz = (*env)->FindClass(env, "java/lang/IllegalStateException");
  if (exception_clazz == NULL) {
    return JNI_ERR;
  }
  exception_clazz = (*env)->NewWeakGlobalRef(env, exception_clazz);

  pointer_clazz = (*env)->FindClass(env, "io/matthewnelson/kmp/tor/resource/noexec/tor/internal/HandleT$Pointer");
  if (pointer_clazz == NULL) {
    (*env)->DeleteWeakGlobalRef(env, exception_clazz);
    exception_clazz = NULL;
    return JNI_ERR;
  }
  pointer_clazz = (*env)->NewWeakGlobalRef(env, pointer_clazz);
  pointer_field = (*env)->GetFieldID(env, pointer_clazz, "value", "J");
  pointer_init = (*env)->GetMethodID(env, pointer_clazz, "<init>", "(J)V");

  return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM *jvm, void *reserved)
{
  JNIEnv *env = NULL;

  if (JNI_OK != (*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_6)) {
    return;
  }

  if (exception_clazz != NULL) {
    (*env)->DeleteWeakGlobalRef(env, exception_clazz);
    exception_clazz = NULL;
  }

  if (pointer_clazz != NULL) {
    (*env)->DeleteWeakGlobalRef(env, pointer_clazz);
    pointer_clazz = NULL;
  }
  pointer_init = NULL;
  pointer_field = NULL;
}
