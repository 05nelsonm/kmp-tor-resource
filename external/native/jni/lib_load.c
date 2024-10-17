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

#include <assert.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

#ifdef __ANDROID__
#include <android/log.h>
#define fprintf(ignored, ...) \
  __android_log_print(ANDROID_LOG_ERROR, "torjni", ##__VA_ARGS__)
#endif // __ANDROID__

#ifdef _WIN32
#include <windows.h>
#else
#include <dlfcn.h>
#endif

void *
lib_load_open(const char *lib)
{
  if (lib == NULL) {
    return NULL;
  }

  void *ptr = NULL;
#ifdef _WIN32
  int len;
  wchar_t *w_lib = NULL;

  len = MultiByteToWideChar(CP_THREAD_ACP, 0, lib, -1, NULL, 0);
  if (len == 0) {
    return NULL;
  }
  w_lib = malloc(len * sizeof(*w_lib));
  MultiByteToWideChar(CP_THREAD_ACP, 0, lib, -1, w_lib, len);

  HMODULE m = NULL;
  m = LoadLibraryExW(w_lib, NULL, 0);
  if (m == NULL) {
    m = LoadLibraryW(w_lib);
  }

  if (w_lib != NULL) {
    free(w_lib);
  }

  ptr = m;
#else
  // RTLD_NOW:   kmp-tor compiles tor as shared lib and uses
  //             expression maps to only expose what is in
  //             tor_api.h (so only a few functions). No use
  //             in specifying RTLD_LAZY because right after
  //             opening we'll be getting pointers to all
  //             those functions
  // RTLD_LOCAL: Even though this is documented as the default
  //             for Linux/Android when not expressed, it is
  //             NOT for macOS (RTLD_GLOBAL is).
  ptr = dlopen(lib, RTLD_NOW | RTLD_LOCAL);
#endif
  if (ptr == NULL) {
    fprintf(stderr, "KmpTor: Failed to open lib[%s]: error[%s]\n", lib, lib_load_error());
  }
  return ptr;
}

void *
lib_load_symbol(void *handle, const char *symbol)
{
  assert(handle != NULL);

  void *ptr = NULL;
#ifdef _WIN32
  ptr = GetProcAddress((HMODULE) handle, symbol);
#else
  ptr = dlsym(handle, symbol);
#endif
  if (ptr == NULL) {
    fprintf(stderr, "KmpTor: Failed to resolve symbol[%s]: error[%s]\n", symbol, lib_load_error());
  }
  return ptr;
}

int
lib_load_close(void *handle)
{
  assert(handle != NULL);

  int result = -1;
  char *err = NULL;

  for (int i = 0; i < 5; i++) {
    if (i > 0) {
      // Exponential back off before trying again
      usleep((useconds_t) i * 1000);
    }
#ifdef _WIN32
    result = FreeLibrary((HMODULE) handle);
#else
    result = dlclose(handle);
#endif
    if (result == 0) {
      break;
    } else {
      err = lib_load_error();
    }
  }

  if (result != 0) {
    fprintf(stderr, "KmpTor: Failed to close lib: error[%s]\n", err);
  }

  return result;
}

#ifdef _WIN32
char *
lib_load_last_error(void)
{
  return "TODO";
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
