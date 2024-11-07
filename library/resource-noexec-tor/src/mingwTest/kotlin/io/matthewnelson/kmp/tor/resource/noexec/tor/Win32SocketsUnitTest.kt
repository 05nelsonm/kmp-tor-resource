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
package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.win32_socketpair
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.INVALID_SOCKET
import platform.posix.closesocket
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalForeignApi::class)
class Win32SocketsUnitTest {

    @Test
    fun givenFDS_whenSocketPair_thenReturnsExpected() {
        val fds = ULongArray(2) { INVALID_SOCKET }

        val result = fds.usePinned { pinned ->
            win32_socketpair(pinned.addressOf(0))
        }

        // TODO: Implement win32_sockets.c
        try {
            assertEquals(-1, result)
            assertEquals(INVALID_SOCKET, fds[0])
            assertEquals(INVALID_SOCKET, fds[1])
        } finally {
            if (fds[0] == INVALID_SOCKET) {
                closesocket(fds[0])
            }
            if (fds[1] == INVALID_SOCKET) {
                closesocket(fds[1])
            }
        }
    }
}
