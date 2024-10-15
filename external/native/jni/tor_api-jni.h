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
#include <jni.h>

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
(JNIEnv *, jobject, jobjectArray);
