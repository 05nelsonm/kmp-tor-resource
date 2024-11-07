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

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.MINGW_AF_UNIX_TMP_FILE_NAME
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.deleteOnExit
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.win32_socketpair
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.posix.INVALID_SOCKET
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalForeignApi::class)
class Win32SocketsUnitTest {

    private companion object {
        @OptIn(ExperimentalUuidApi::class)
        val MINGW_AF_UNIX_PATH: File by lazy {
            val d = SysTempDir.resolve("kmp-tor_${Uuid.random()}")
            d.deleteOnExit()
            d.resolve(MINGW_AF_UNIX_TMP_FILE_NAME).also { it.deleteOnExit() }
        }
    }

    @Test
    fun givenFDS_whenSocketPair_thenReturnsExpected() {
        val fds = ULongArray(2) { INVALID_SOCKET }

        val result = fds.usePinned { pinned ->
            win32_socketpair(
                af_unix_path = MINGW_AF_UNIX_PATH.path,
                fds = pinned.addressOf(0),
            )
        }

        // TODO: Implement win32_sockets.c
        assertEquals(-1, result)
        assertEquals(INVALID_SOCKET, fds[0])
        assertEquals(INVALID_SOCKET, fds[1])


        // TODO: close sockets
    }
}
