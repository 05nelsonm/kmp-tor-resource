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
#include "lib_load.h"

#include <stdlib.h>
#include <unistd.h>

#ifdef _WIN32
#include <windows.h>
#else
#include <dlfcn.h>
#endif

void *
lib_load_open(const char *lib)
{
#ifdef _WIN32
  wchar_t *w_lib = NULL;

  if (lib != NULL) {
    int len;
    len = MultiByteToWideChar(CP_THREAD_ACP, 0, lib, -1, NULL, 0);
    if (len == 0) {
      return NULL;
    }
    w_lib = malloc(len * sizeof(*w_lib));
    MultiByteToWideChar(CP_THREAD_ACP, 0, lib, -1, w_lib, len);
  }

  HMODULE m = NULL;
  m = LoadLibraryExW(w_lib, NULL, 0);
  if (m == NULL) {
    m = LoadLibraryW(w_lib);
  }

  if (w_lib != NULL) {
    free(w_lib);
  }

  return m;
#else
  return dlopen(lib, RTLD_LAZY);
#endif
}

void *
lib_load_symbol(void *handle, const char *symbol)
{
#ifdef _WIN32
  return GetProcAddress((HMODULE) handle, symbol);
#else
  return dlsym(handle, symbol);
#endif
}

int
lib_load_close(void *handle)
{
  int result;
#ifdef _WIN32
  result = FreeLibrary((HMODULE) handle);
#else
  result = dlclose(handle);
#endif
  usleep((useconds_t) 5000);
  return result;
}

#ifdef _WIN32
char *
lib_load_last_error(void)
{
  // TODO
  return NULL;
}
#endif

char *
lib_load_error(void)
{
#ifdef _WIN32
  return lib_load_last_error();
#else
  return dlerror();
#endif
}
