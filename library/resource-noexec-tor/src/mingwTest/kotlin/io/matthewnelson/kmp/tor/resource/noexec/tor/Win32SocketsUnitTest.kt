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

import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.win32_af_unix_socketpair
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.win32_sockets_deinit
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.win32_sockets_init
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import platform.posix.*
import kotlin.test.*

@OptIn(DelicateCoroutinesApi::class, ExperimentalForeignApi::class)
class Win32SocketsUnitTest {

    private companion object {

        private val AF_UNIX_SUPPORTED by lazy {
            val s = socket(AF_UNIX, SOCK_STREAM, 0)
            if (s == INVALID_SOCKET) {
                false
            } else {
                closesocket(s)
                true
            }
        }
    }

    @BeforeTest
    fun setup() {
        assertEquals(0, win32_sockets_init())
    }

    @AfterTest
    fun tearDown() {
        win32_sockets_deinit()
    }

    @Test
    fun givenWin32Sockets_whenAfUnixSocketPair_thenReturnsConnectedSockets() = runTest {
        if (!AF_UNIX_SUPPORTED) {
            println("Skipping...")
            return@runTest
        }

        assertEquals(-1, win32_af_unix_socketpair(null))

        val fds = ULongArray(2) { INVALID_SOCKET }

        val result = fds.usePinned { pinned ->
            win32_af_unix_socketpair(pinned.addressOf(0))
        }

        try {
            assertEquals(0, result)
            assertNotEquals(INVALID_SOCKET, fds[0])
            assertNotEquals(INVALID_SOCKET, fds[1])

            val expected: Byte = 5
            val job = Job()
            currentCoroutineContext().job.invokeOnCompletion { job.cancel() }

            GlobalScope.launch {
                val buf = ByteArray(1)
                buf.usePinned { pinned ->
                    recv(fds[1], pinned.addressOf(0), buf.size, 0)
                }

                if (buf[0] == expected) {
                    job.complete()
                } else {
                    job.cancel()
                }
            }

            ByteArray(1) { expected }.usePinned { pinned ->
                send(fds[0], pinned.addressOf(0), 1, 0)
            }

            job.join()
            assertFalse(job.isCancelled)
        } finally {
            closesocket(fds[0])
            closesocket(fds[1])
        }
    }
}
