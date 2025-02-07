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

#ifndef WIN32_SOCKETS_H
#define WIN32_SOCKETS_H

#ifndef _WIN32
error Only Win32 targets are supported!
#endif // _WIN32

#include <ws2tcpip.h>

int win32_sockets_init();

int win32_sockets_deinit();

int win32_af_unix_socketpair(SOCKET fds[2]);

#endif /* !defined(WIN32_SOCKETS_H) */
