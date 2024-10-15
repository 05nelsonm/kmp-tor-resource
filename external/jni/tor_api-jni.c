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
#include "tor_api-jni.h"

#include <jni.h>
#include <stdlib.h>
#include <string.h>
#ifdef _WIN32
#include <winsock2.h>
#endif
#include <tor_api.h>

/*
 * Class:     io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi
 * Method:    kmpTorRunMain
 * Signature: ([Ljava/lang/String;)I
 *
 * Returns the following integer value depending on case:
 *  -10    : tor_main_configuration_new failed
 *  -11    : JNI GetArrayLength for argc failed
 *  -12    : malloc for argv failed
 *  -13    : strdup returned NULL when copying args to argv
 *  -14    : tor_main_configuration_set_command_line failed
 *  0      : tor_run_main success
 *  1 - 255: tor_run_main failed
 */
JNIEXPORT jint JNICALL
Java_io_matthewnelson_kmp_tor_resource_noexec_tor_internal_KmpTorApi_kmpTorRunMain
(JNIEnv *env, jobject thiz, jobjectArray args)
{
  char **argv = NULL;
  int argc = -1;
  int rv = -1;
  tor_main_configuration_t *cfg = NULL;

  cfg = tor_main_configuration_new();
  if (cfg == NULL) {
    return -10;
  }

  argc = (*env)->GetArrayLength(env, args);
  if (argc == -1) {
    tor_main_configuration_free(cfg);
    return -11;
  }

  argv = (char **) malloc(argc * sizeof(char *));
  if (argv == NULL) {
    tor_main_configuration_free(cfg);
    return -12;
  }

  for (jsize i = 0; i < argc; i++) {
    if (rv != -1) {
      argv[i] = NULL;
      continue;
    }

    jstring argStr = (jstring) (*env)->GetObjectArrayElement(env, args, i);
    const char *arg = (*env)->GetStringUTFChars(env, argStr, NULL);
    argv[i] = strdup(arg);
    (*env)->ReleaseStringUTFChars(env, argStr, arg);

    if (argv[i] == NULL) {
      rv = -13;
    }
  }

  if (rv == -1) {
    if (tor_main_configuration_set_command_line(cfg, argc, argv) < 0) {
      rv = -14;
    }
  }

  if (rv == -1) {
    rv = tor_run_main(cfg);
  }

  tor_main_configuration_free(cfg);
  for (int i = 0; i < argc; i++) {
    char *arg = NULL;
    arg = argv[i];
    if (arg != NULL) {
      free(arg);
    }
  }
  free(argv);

  if (rv == -13 || rv == -14) {
    return rv;
  }

  if (rv < 0 || rv > 255) {
    return 1;
  } else {
    return rv;
  }
}
