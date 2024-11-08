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
win32_socketpair(SOCKET fds[2])
{
  assert(fds != NULL);

  // TODO

  return -1;
}
