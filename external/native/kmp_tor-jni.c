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

/*
 * Class:     io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi
 * Method:    kmpTorRunMain
 * Signature: (I;String;[Ljava/lang/String;)I
 *
 * Returns the following integer value depending on case:
 *  -6     : JNI to C conversion failure
 *  -7     : invalid arguments
 *  -8     : kmp_tor_run_thread_t configuration failure
 *  -9     : pthread_attr_t configuration failure
 *  -10    : dlopen/dlsym failure
 *  -11    : pthread failure
 *  -12    : tor_main_configuration_new failure
 *  -13    : tor_main_configuration_set_command_line failure
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
  const char *libtor_cstr = NULL;

  argc = (*env)->GetArrayLength(env, args);
  if (argc <= 0) {
    return -6;
  }

  argv = malloc(argc * sizeof(char *));
  if (argv == NULL) {
    return -6;
  }

  libtor_cstr = (*env)->GetStringUTFChars(env, libtor, NULL);

  for (int i = 0; i < argc; i++) {
    jstring arg = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    argv[i] = (char *) (*env)->GetStringUTFChars(env, arg, NULL);
  }

  result = kmp_tor_run_main(shutdown_delay_millis, libtor_cstr, argc, argv);

  (*env)->ReleaseStringUTFChars(env, libtor, libtor_cstr);
  for (int i = 0; i < argc; i++) {
    jstring arg = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    (*env)->ReleaseStringUTFChars(env, arg, argv[i]);
  }
  free(argv);

  return result;
}
