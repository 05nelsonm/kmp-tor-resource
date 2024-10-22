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
#include <string.h>
#include <unistd.h>

#ifdef __ANDROID__
#include <android/log.h>
#define fprintf(ignored, ...) \
  __android_log_print(ANDROID_LOG_ERROR, "lib_load", ##__VA_ARGS__)
#endif // __ANDROID__

#ifdef _WIN32
#include <windows.h>

struct lib_handle_t {
  char *lib;
  HMODULE handle;
  char *err_buf;
};
#else
#include <dlfcn.h>

struct lib_handle_t {
  char *lib;
  void *handle;
};
#endif

void
lib_load_assert(lib_handle_t *handle_t)
{
  assert(handle_t != NULL);
  assert(handle_t->lib != NULL);
#ifdef _WIN32
  assert(handle_t->err_buf != NULL);
#endif
  assert(handle_t->handle != NULL);
  return;
}

char *
lib_load_error(lib_handle_t *handle_t)
{
  assert(handle_t != NULL);
#ifdef _WIN32
  assert(handle_t->err_buf != NULL);
  handle_t->err_buf[0] = 0;

  DWORD result;
  result = FormatMessageA(
    /* dwFlags */       FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
    /* lpSource */      NULL,
    /* dwMessageId */   GetLastError(),
    /* dwLanguageId */  SUBLANG_DEFAULT * 1024 + LANG_NEUTRAL, // MAKELANGID macro
    /* lpBuffer */      handle_t->err_buf,
    /* nSize */         sizeof(handle_t->err_buf),
    /* va_list */       NULL
  );
  if (result == 0) {
    return "Error Not Known";
  }

  return handle_t->err_buf;
#else

  return dlerror();
#endif
}

void
lib_load_free(lib_handle_t *handle_t)
{
  assert(handle_t != NULL);
  assert(handle_t->handle == NULL);

  if (handle_t->lib != NULL) {
    free(handle_t->lib);
  }
#ifdef _WIN32
  if (handle_t->err_buf != NULL) {
    free(handle_t->err_buf);
  }
#endif
  free(handle_t);
  return;
}

lib_handle_t *
lib_load_open(const char *lib)
{
  if (lib == NULL) {
    return NULL;
  }

  lib_handle_t *handle_t = malloc(sizeof(lib_handle_t));
  if (handle_t == NULL) {
    fprintf(stderr, "KmpTor: Failed to allocate memory to lib_handle_t for lib[%s]\n", lib);
    return NULL;
  }

  handle_t->lib = strdup(lib);
  if (handle_t->lib == NULL) {
    fprintf(stderr, "KmpTor: Failed to allocate memory to lib_handle_t.lib for lib[%s]\n", lib);
    lib_load_free(handle_t);
    return NULL;
  }
#ifdef _WIN32

  handle_t->err_buf = malloc(2048 * sizeof(char *));
  if (handle_t->err_buf == NULL) {
    fprintf(stderr, "KmpTor: Failed to allocate memory to lib_handle_t.err_buf for lib[%s]\n", lib);
    lib_load_free(handle_t);
    return NULL;
  }
  handle_t->err_buf[0] = 0;

  int len = 0;
  wchar_t *w_lib = NULL;

  len = MultiByteToWideChar(CP_THREAD_ACP, 0, handle_t->lib, -1, NULL, 0);
  if (len == 0) {
    lib_load_free(handle_t);
    return NULL;
  }

  w_lib = malloc(len * sizeof(*w_lib));
  if (w_lib == NULL) {
    lib_load_free(handle_t);
    return NULL;
  }

  MultiByteToWideChar(CP_THREAD_ACP, 0, handle_t->lib, -1, w_lib, len);

  handle_t->handle = LoadLibraryExW(w_lib, NULL, 0);
  if (handle_t->handle == NULL) {
    handle_t->handle = LoadLibraryW(w_lib);
  }

  free(w_lib);
#else

  // RTLD_NOW:   kmp-tor compiles tor as shared lib and uses
  //             export maps to only expose what is available
  //             in tor_api.h (so only a few functions). No use
  //             in specifying RTLD_LAZY because right after
  //             opening we'll be getting pointers to all
  //             those functions.
  // RTLD_LOCAL: Even though this is documented as the default
  //             for Linux/Android when not present, it is NOT
  //             the default for macOS (RTLD_GLOBAL is).
  handle_t->handle = dlopen(handle_t->lib, RTLD_NOW | RTLD_LOCAL);
#endif

  if (handle_t->handle == NULL) {
    char *err = lib_load_error(handle_t);
    fprintf(stderr, "KmpTor: Failed to open lib[%s] - error[%s]\n", handle_t->lib, err);
    lib_load_free(handle_t);
    return NULL;
  }

  return handle_t;
}

void *
lib_load_resolve(lib_handle_t *handle_t, const char *symbol)
{
  lib_load_assert(handle_t);
  void *ptr = NULL;

#ifdef _WIN32
  ptr = GetProcAddress(handle_t->handle, symbol);
#else
  ptr = dlsym(handle_t->handle, symbol);
#endif

  if (ptr == NULL) {
    char *err = lib_load_error(handle_t);
    fprintf(stderr, "KmpTor: Failed to resolve symbol[%s] - error[%s]\n", symbol, err);
  }

  return ptr;
}

int
lib_load_close(lib_handle_t *handle_t)
{
  lib_load_assert(handle_t);

  int result = -1;
  char *err = NULL;

  for (int i = 0; i < 5; i++) {
    if (i > 0) {
      // Exponential back off before trying again
      usleep((useconds_t) i * 1000);
    }

#ifdef _WIN32
    // FreeLibrary returns 0 on failure
    if (FreeLibrary(handle_t->handle) == 0) {
      result = -1;
    } else {
      result = 0;
    }
#else
    result = dlclose(handle_t->handle);
#endif

    if (result == 0) {
      break;
    } else {
      err = lib_load_error(handle_t);
    }
  }

  if (result != 0) {
    fprintf(stderr, "KmpTor: Failed to close handle[%s] - error[%s]\n", handle_t->lib, err);
  }

  handle_t->handle = NULL;
  lib_load_free(handle_t);
  return result;
}
