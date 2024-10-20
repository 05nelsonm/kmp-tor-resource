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

#ifndef KMP_TOR_JNI_H
#define KMP_TOR_JNI_H

#include <jni.h>

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
(JNIEnv *, jobject, jint, jstring, jobjectArray);

#endif /* !defined(KMP_TOR_JNI_H) */
