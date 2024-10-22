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

// TODO: Return handle to thread

/*
 * Returns the following integer value depending on case:
 *  -10    : invalid arguments
 *  -11    : configuration failure
 *  -12    : dlopen/dlsym failure
 *  -13    : tor_main_configuration_new failure
 *  -14    : tor_main_configuration_set_command_line failure
 *  0      : tor_run_main returned success
 *  1 - 255: tor_run_main returned failure
 */
int kmp_tor_run_main(int shutdown_delay_millis, const char *libtor, int argc, char *argv[]);

#endif /* !defined(KMP_TOR_H) */
