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

#ifndef LIB_LOAD_H
#define LIB_LOAD_H

typedef struct lib_handle_t lib_handle_t;

lib_handle_t *lib_load_open(const char *lib);

void *lib_load_resolve(lib_handle_t *handle_t, const char *symbol);

int lib_load_close(lib_handle_t *handle_t);

#endif /* !defined(LIB_LOAD_H) */
