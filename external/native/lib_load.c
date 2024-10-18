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
  __android_log_print(ANDROID_LOG_ERROR, "torjni", ##__VA_ARGS__)
#endif // __ANDROID__

#ifdef _WIN32
#include <windows.h>

struct lib_handle_t {
  char *lib;
  HMODULE ptr;
  char *err_buf;
};
#else
#include <dlfcn.h>

struct lib_handle_t {
  char *lib;
  void *ptr;
};
#endif

char *
lib_load_error(lib_handle_t *handle)
{
  assert(handle != NULL);
#ifdef _WIN32
  assert(handle->err_buf != NULL);

  DWORD result;
  result = FormatMessageA(
    /* dwFlags */       FORMAT_MESSAGE_FROM_SYSTEM | FORMAT_MESSAGE_IGNORE_INSERTS,
    /* lpSource */      NULL,
    /* dwMessageId */   GetLastError(),
    /* dwLanguageId */  SUBLANG_DEFAULT * 1024 + LANG_NEUTRAL, // MAKELANGID macro
    /* lpBuffer */      handle->err_buf,
    /* nSize */         sizeof(handle->err_buf),
    /* va_list */       NULL
  );
  if (result == 0) {
    return "Error Not Known";
  }

  return handle->err_buf;
#else

  return dlerror();
#endif
}

void
lib_handle_assert(lib_handle_t *handle)
{
  assert(handle != NULL);
  assert(handle->lib != NULL);
#ifdef _WIN32
  assert(handle->err_buf != NULL);
#endif
  assert(handle->ptr != NULL);
  return;
}

void
lib_handle_free(lib_handle_t *handle)
{
  assert(handle != NULL);
  assert(handle->ptr == NULL);

  free(handle->lib);
  handle->lib = NULL;
#ifdef _WIN32
  free(handle->err_buf);
  handle->err_buf = NULL;
#endif
  free(handle);
  return;
}

int
lib_handle_close(lib_handle_t *handle)
{
  lib_handle_assert(handle);

  int result = -1;
  char *err = NULL;

  for (int i = 0; i < 5; i++) {
    if (i > 0) {
      // Exponential back off before trying again
      usleep((useconds_t) i * 1000);
    }

#ifdef _WIN32
    // FreeLibrary returns 0 on failure
    if (FreeLibrary(handle->ptr) == 0) {
      result = -1;
    } else {
      result = 0;
    }
#else
    result = dlclose(handle->ptr);
#endif

    if (result == 0) {
      break;
    } else {
      err = lib_load_error(handle);
    }
  }

  if (result != 0) {
    fprintf(stderr, "KmpTor: Failed to close handle[%s] - error[%s]\n", handle->lib, err);
  }

  handle->ptr = NULL;

  return result;
}

lib_handle_t *
lib_load_open(const char *lib)
{
  if (lib == NULL) {
    return NULL;
  }

  lib_handle_t *handle = malloc(sizeof(lib_handle_t));
  if (handle == NULL) {
    fprintf(stderr, "KmpTor: Failed to allocate memory to lib_handle_t for lib[%s]\n", lib);
    return NULL;
  }
#ifdef _WIN32

  handle->err_buf = malloc(2048 * sizeof(char *));
  if (handle->err_buf == NULL) {
    fprintf(stderr, "KmpTor: Failed to allocate memory to lib_handle_t.err_buf for lib[%s]\n", lib);
    lib_handle_free(handle);
    return NULL;
  }
  handle->err_buf[0] = 0;
#endif

  handle->lib = strdup(lib);
  if (handle->lib == NULL) {
    fprintf(stderr, "KmpTor: Failed to allocate memory to lib_handle_t.lib for lib[%s]\n", lib);
    lib_handle_free(handle);
    return NULL;
  }
#ifdef _WIN32

  int len = 0;
  wchar_t *w_lib = NULL;

  len = MultiByteToWideChar(CP_THREAD_ACP, 0, handle->lib, -1, NULL, 0);
  if (len == 0) {
    lib_handle_free(handle);
    return NULL;
  }
  w_lib = malloc(len * sizeof(*w_lib));
  MultiByteToWideChar(CP_THREAD_ACP, 0, handle->lib, -1, w_lib, len);

  handle->ptr = LoadLibraryExW(w_lib, NULL, 0);
  if (handle->ptr == NULL) {
    handle->ptr = LoadLibraryW(w_lib);
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
  handle->ptr = dlopen(handle->lib, RTLD_NOW | RTLD_LOCAL);
#endif

  if (handle->ptr == NULL) {
    char *err = lib_load_error(handle);
    fprintf(stderr, "KmpTor: Failed to open lib[%s] - error[%s]\n", handle->lib, err);
    lib_handle_free(handle);
    return NULL;
  }

  return handle;
}

void *
lib_load_resolve(lib_handle_t *handle, const char *symbol)
{
  lib_handle_assert(handle);
  void *ptr = NULL;

#ifdef _WIN32
  ptr = GetProcAddress(handle->ptr, symbol);
#else
  ptr = dlsym(handle->ptr, symbol);
#endif

  if (ptr == NULL) {
    char *err = lib_load_error(handle);
    fprintf(stderr, "KmpTor: Failed to resolve symbol[%s] - error[%s]\n", symbol, err);
  }

  return ptr;
}

int
lib_load_close(lib_handle_t *handle)
{
  int result;
  result = lib_handle_close(handle);
  usleep((useconds_t) 1000);

  // Verify it has been unloaded.
#ifdef _WIN32
  // TODO: If Failure - GetModuleHandle && UnmapViewOfFile
#else
  int i = 1;
  int flags = (RTLD_NOLOAD | RTLD_LOCAL);

  handle->ptr = dlopen(handle->lib, flags);

  while (handle->ptr != NULL) {
    result = lib_handle_close(handle);
    usleep((useconds_t) i * 1000);
    handle->ptr = dlopen(handle->lib, flags);
    if (i++ > 10) {
      break;
    }
  }

  if (handle->ptr != NULL) {
    result = lib_handle_close(handle);
    fprintf(stderr, "KmpTor: dlclose failed to unload lib[%s] after %i attempts\n", handle->lib, i - 2);
  }
#endif

  lib_handle_free(handle);
  return result;
}
