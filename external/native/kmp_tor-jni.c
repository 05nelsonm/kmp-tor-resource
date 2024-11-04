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

static jclass pointer_clazz = NULL;
static jfieldID pointer_field = NULL;
static jmethodID pointer_init = NULL;

static void *
JLongToPointer(jlong l)
{
  void *result;
  memcpy(&result, &l, sizeof(void *));
  return result;
}

static jlong
PointerToJLong(void *p)
{
  jlong result;
  memcpy(&result, &p, sizeof(void *));
  return result;
}

JNIEXPORT jobject JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_torJNIRunMain
(JNIEnv *env, jobject thiz, jstring lib_tor, jint argc, jobjectArray args)
{
  assert(pointer_clazz != NULL);
  assert(pointer_field != NULL);
  assert(pointer_init != NULL);
  assert(lib_tor != NULL);
  assert(args != NULL);
  assert(argc > 0);

  int c_argc = 0;
  int copy_args = 0;
  char **c_argv = NULL;
  char *c_lib_tor = NULL;
  kmp_tor_handle_t *handle_t = NULL;

  if (c_argc == 0) {
    const char *c_arg = (*env)->GetStringUTFChars(env, lib_tor, NULL);
    if (c_arg != NULL) {
      c_lib_tor = strdup(c_arg);
      (*env)->ReleaseStringUTFChars(env, lib_tor, c_arg);
    }
  }

  if (c_lib_tor == NULL) {
    return (jobject) NULL;
  }

  c_argv = malloc(argc * sizeof(char *));
  if (c_argv == NULL) {
    free(c_lib_tor);
    return (jobject) NULL;
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
      const char *c_arg = (*env)->GetStringUTFChars(env, j_arg, NULL);
      if (c_arg != NULL) {
        c_argv[c_argc] = strdup(c_arg);
        (*env)->ReleaseStringUTFChars(env, j_arg, c_arg);
      } else {
        c_argv[c_argc] = NULL;
      }
      (*env)->DeleteLocalRef(env, j_arg);
    }

    if (c_argv[c_argc] == NULL) {
      copy_args = -1;
    }

    c_argc++;
  }

  if (copy_args == 0) {
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
    return (jobject) NULL;
  }

  jlong j_handle_t = PointerToJLong(handle_t);
  return (*env)->NewObject(env, pointer_clazz, pointer_init, j_handle_t);
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_torJNICheckErrorCode
(JNIEnv *env, jobject thiz, jobject pointer)
{
  kmp_tor_handle_t *handle_t = NULL;
  jlong j_handle_t = (*env)->GetLongField(env, pointer, pointer_field);
  handle_t = JLongToPointer(j_handle_t);
  return kmp_tor_check_error_code(handle_t);
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_torJNITerminateAndAwaitResult
(JNIEnv *env, jobject thiz, jobject pointer)
{
  kmp_tor_handle_t *handle_t = NULL;
  jlong j_handle_t = (*env)->GetLongField(env, pointer, pointer_field);
  handle_t = JLongToPointer(j_handle_t);
  return kmp_tor_terminate_and_await_result(handle_t);
}

JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *jvm, void *reserved)
{
  JNIEnv *env = NULL;

  if (JNI_OK != (*jvm)->GetEnv(jvm, (void **) &env, JNI_VERSION_1_6)) {
    return JNI_ERR;
  }

  pointer_clazz = (*env)->FindClass(env, "io/matthewnelson/kmp/tor/resource/noexec/tor/internal/HandleT$Pointer");
  if (pointer_clazz == NULL) {
    return JNI_ERR;
  }
  pointer_clazz = (*env)->NewGlobalRef(env, pointer_clazz);
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

  if (pointer_clazz != NULL) {
    (*env)->DeleteGlobalRef(env, pointer_clazz);
    pointer_clazz = NULL;
  }
  pointer_init = NULL;
  pointer_field = NULL;
}
