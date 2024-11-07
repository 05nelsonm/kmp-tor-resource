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

#ifndef KMP_TOR_H
#define KMP_TOR_H

#define ERR_CODE_NONE           -100
#define ERR_CODE_ARGS           -10
#define ERR_CODE_CFG            -11
#define ERR_CODE_LIB_LOAD       -12
#define ERR_CODE_THREAD         -13
#define ERR_CODE_TOR_CFG_NEW    -14
#define ERR_CODE_TOR_CFG_SET    -15
#define ERR_CODE_TOR_CFG_SOCKET -16

typedef struct kmp_tor_handle_t kmp_tor_handle_t;

kmp_tor_handle_t *kmp_tor_run_main(const char *lib_tor, int argc, char *argv[]);

int kmp_tor_check_error_code(kmp_tor_handle_t *handle_t);

int kmp_tor_terminate_and_await_result(kmp_tor_handle_t *handle_t);

#endif /* !defined(KMP_TOR_H) */
