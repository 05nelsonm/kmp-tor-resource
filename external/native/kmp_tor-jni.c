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
jstring_dup(JNIEnv *env, jstring arg)
{
  const char *_arg = (*env)->GetStringUTFChars(env, arg, NULL);
  char *dup = strdup(_arg);
  (*env)->ReleaseStringUTFChars(env, arg, _arg);
  return dup;
}

/*
 * Class:     io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi
 * Method:    kmpTorRunMain
 * Signature: (I;String;[Ljava/lang/String;)I
 *
 * Returns the following integer value depending on case:
 *  -9     : JNI to C conversion failure
 *  -10    : invalid arguments
 *  -11    : configuration failure
 *  -12    : dlopen/dlsym failure
 *  -13    : tor_main_configuration_new failure
 *  -14    : tor_main_configuration_set_command_line failure
 *  0      : tor_run_main returned success
 *  1 - 255: tor_run_main returned failure
 */
JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jint shutdown_delay_millis, jstring libtor, jobjectArray args)
{
  int result = -1;
  int argc = -1;
  char **argv = NULL;
  char *libtor_cstr = NULL;

  argc = (*env)->GetArrayLength(env, args);
  if (argc <= 0) {
    return -9;
  }

  libtor_cstr = jstring_dup(env, libtor);
  if (libtor_cstr == NULL) {
    return -9;
  }

  argv = malloc(argc * sizeof(char *));
  if (argv == NULL) {
    free(libtor_cstr);
    return -9;
  }

  for (int i = 0; i < argc; i++) {
    if (result != -1) {
      argv[i] = NULL;
      continue;
    }

    jstring arg = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    argv[i] = jstring_dup(env, arg);

    if (argv[i] == NULL) {
      result = -9;
    }
  }

  if (result == -1) {
    result = kmp_tor_run_main(shutdown_delay_millis, libtor_cstr, argc, argv);
  }

  for (int i = 0; i < argc; i++) {
    if (argv[i] != NULL) {
      free(argv[i]);
    }
  }
  free(argv);
  free(libtor_cstr);

  return result;
}
