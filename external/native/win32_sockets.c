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
#include "win32_sockets.h"

#include <assert.h>
#include <stdlib.h>
#include <windows.h>

#ifdef HAVE_AF_UNIX_H
#include <afunix.h>
#else
#define UNIX_PATH_MAX 108

struct sockaddr_un {
  ADDRESS_FAMILY sun_family;
  char sun_path[UNIX_PATH_MAX];
} SOCKADDR_UN, *PSOCKADDR_UN;
#endif

int
win32_sockets_init()
{
  WSADATA wd;
  return WSAStartup(0x101, &wd);
}

int
win32_sockets_deinit()
{
  return WSACleanup();
}

int
win32_af_unix_socketpair(SOCKET fds[2])
{
  int result = -1;
  if (fds == NULL) {
    return result;
  }

  SOCKET sl = INVALID_SOCKET;
  SOCKET sc = INVALID_SOCKET;
  SOCKET sa = INVALID_SOCKET;
  WCHAR w_file[MAX_PATH] = { 0 };

    DWORD ret = 0;
    WCHAR w_path[MAX_PATH] = { 0 };
    WCHAR w_prefix[4] = { 0 };
    char c_file[MAX_PATH] = { 0 };
    socklen_t size;
    struct sockaddr_un sl_addr;
    struct sockaddr_un sc_addr;

    sl = socket(AF_UNIX, SOCK_STREAM, 0);
    if (sl == INVALID_SOCKET) {
      result = -2;
      goto do_failure;
    }

    memset(&sl_addr, 0, sizeof(sl_addr));

    ret = GetTempPathW(MAX_PATH, w_path);
    if (ret == 0) {
      result = -3;
      goto do_failure;
    }

    w_prefix[0] = '_';
    w_prefix[1] = 's';
    w_prefix[2] = '_';
    w_prefix[3] = '\0';

    ret = GetTempFileNameW(w_path, w_prefix, 0, w_file);
    if (ret == 0) {
      result = -4;
      goto do_failure;
    }
    DeleteFileW(w_file);

    ret = WideCharToMultiByte(
      CP_UTF8,
      0,
      w_file,
      MAX_PATH,
      c_file,
      MAX_PATH,
      NULL,
      NULL
    );
    if (ret == 0) {
      result = -5;
      goto do_failure;
    }

    int len = strlen(c_file);
    if (len <= 0 || len > UNIX_PATH_MAX) {
      result = -6;
      goto do_failure;
    }

    sl_addr.sun_family = AF_UNIX;
    strcpy(sl_addr.sun_path, c_file);

    if (bind(sl, (struct sockaddr *) &sl_addr, sizeof(sl_addr)) != 0) {
      result = -7;
      goto do_failure;
    }

    if (listen(sl, 1) != 0) {
      result = -8;
      goto do_failure;
    }

    sc = socket(AF_UNIX, SOCK_STREAM, 0);
    if (sc == INVALID_SOCKET) {
      result = -9;
      goto do_failure;
    }

    memset(&sc_addr, 0, sizeof(sc_addr));

    size = sizeof(sc_addr);
    if (getsockname(sl, (struct sockaddr *) &sc_addr, &size) != 0) {
      result = -10;
      goto do_failure;
    }

    if (connect(sc, (struct sockaddr *) &sc_addr, sizeof(sc_addr)) != 0) {
      result = -11;
      goto do_failure;
    }

    size = sizeof(sl_addr);
    sa = accept(sl, (struct sockaddr *) &sl_addr, &size);
    if (sa == INVALID_SOCKET) {
      result = -12;
      goto do_failure;
    }
    if (size != sizeof(sl_addr)) {
      result = -13;
      goto do_failure;
    }
    if (getsockname(sc, (struct sockaddr *) &sc_addr, &size) != 0) {
      result = -14;
      goto do_failure;
    }
    if (size != sizeof(sc_addr)) {
      result = -15;
      goto do_failure;
    }
    if (strcmp(sl_addr.sun_path, sc_addr.sun_path) != 0) {
      result = -16;
      goto do_failure;
    }

    closesocket(sl);
    DeleteFileW(w_file);
    fds[0] = sc;
    fds[1] = sa;
    return 0;

  do_failure:
    if (sl != INVALID_SOCKET) {
      closesocket(sl);
    }
    if (sc != INVALID_SOCKET) {
      closesocket(sc);
    }
    if (sa != INVALID_SOCKET) {
      closesocket(sa);
    }
    if (w_file[0] != 0) {
      DeleteFileW(w_file);
    }
    return result;
}
