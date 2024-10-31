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

char *
JStringDup(JNIEnv *env, jstring arg)
{
  const char *_arg = (*env)->GetStringUTFChars(env, arg, NULL);
  char *dup = strdup(_arg);
  (*env)->ReleaseStringUTFChars(env, arg, _arg);
  return dup;
}

jfieldID
GetPointerFieldID(JNIEnv *env, jobject pointer)
{
  jclass pointerClazz = (*env)->GetObjectClass(env, pointer);
  if (pointerClazz == NULL) {
    return NULL;
  }

  return (*env)->GetFieldID(env, pointerClazz, "pointer", "J");
}

JNIEXPORT void JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jobject pointer, jstring lib_tor, jobjectArray args)
{
  int argc = -1;
  int result = 0;
  char **argv = NULL;
  char *lib_tor_cstr = NULL;
  kmp_tor_handle_t *handle_t = NULL;
  jfieldID pointerField = NULL;

  pointerField = GetPointerFieldID(env, pointer);
  if (pointerField == NULL) {
    return;
  }

  argc = (*env)->GetArrayLength(env, args);
  if (argc <= 0) {
    return;
  }

  lib_tor_cstr = JStringDup(env, lib_tor);
  if (lib_tor_cstr == NULL) {
    return;
  }

  argv = malloc(argc * sizeof(char *));
  if (argv == NULL) {
    free(lib_tor_cstr);
    return;
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

  if (handle_t != NULL) {
    (*env)->SetLongField(env, pointer, pointerField, handle_t);
  }

  return;
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorCheckErrorCode
(JNIEnv *env, jobject thiz, jobject pointer)
{
  kmp_tor_handle_t *handle_t = NULL;

  jfieldID pointerField = GetPointerFieldID(env, pointer);
  handle_t = (*env)->GetLongField(env, pointer, pointerField);

  return kmp_tor_check_error_code(handle_t);
}

JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_AbstractKmpTorApi_kmpTorTerminateAndAwaitResult
(JNIEnv *env, jobject thiz, jobject pointer)
{
  kmp_tor_handle_t *handle_t = NULL;

  jfieldID pointerField = GetPointerFieldID(env, pointer);
  handle_t = (*env)->GetLongField(env, pointer, pointerField);

  return kmp_tor_terminate_and_await_result(handle_t);
}
