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
 *  -6     : strdup returned NULL when copying libtor
 *  -7     : JNI GetArrayLength for argc failed
 *  -8     : malloc for argv failed
 *  -9     : strdup returned NULL when copying args to argv
 *  -10    : dlopen failed
 *  -11    : dlsym failed
 *  -12    : tor_main_configuration_new failed
 *  -13    : tor_main_configuration_set_command_line failed
 *  0      : tor_run_main success
 *  1 - 255: tor_run_main failed
 */
JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jint usleep_millis, jstring libtor, jobjectArray args)
{
  char *cstr_libtor = NULL;
  char **argv = NULL;
  int argc = -1;
  int rv = -1;

  cstr_libtor = jstring_dup(env, libtor);
  if (cstr_libtor == NULL) {
    return -6;
  }

  argc = (*env)->GetArrayLength(env, args);
  if (argc == -1) {
    free(cstr_libtor);
    return -7;
  }

  argv = (char **) malloc(argc * sizeof(char *));
  if (argv == NULL) {
    free(cstr_libtor);
    return -8;
  }

  for (jsize i = 0; i < argc; i++) {
    if (rv != -1) {
      argv[i] = NULL;
      continue;
    }

    jstring arg = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    argv[i] = jstring_dup(env, arg);

    if (argv[i] == NULL) {
      rv = -9;
    }
  }

  if (rv == -1) {
    rv = kmp_tor_run_main(usleep_millis, cstr_libtor, argc, argv);
  }

  for (int i = 0; i < argc; i++) {
    char *arg = NULL;
    arg = argv[i];
    if (arg != NULL) {
      free(arg);
    }
  }
  free(argv);
  free(cstr_libtor);

  return rv;
}
