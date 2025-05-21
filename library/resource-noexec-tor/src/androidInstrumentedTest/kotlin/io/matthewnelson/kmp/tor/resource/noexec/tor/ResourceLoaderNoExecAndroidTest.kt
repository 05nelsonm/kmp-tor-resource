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

import io.matthewnelson.kmp.file.parentPath
import io.matthewnelson.kmp.tor.common.lib.locator.KmpTorLibLocator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes

class ResourceLoaderNoExecAndroidTest: ResourceLoaderNoExecBaseTest() {

    private companion object {
        private val TIMEOUT: Duration = 20.minutes
    }

    @Test
    fun givenAndroidNative_whenExecuteTestBinary_thenIsSuccessful() {
        val executable = KmpTorLibLocator.require("libTestExec.so")

        var p: Process? = null
        try {
            p = ProcessBuilder(listOf(executable.path)).apply {
                redirectErrorStream(true)
                environment()["LD_LIBRARY_PATH"] = executable.parentPath
            }.start()

            p.outputStream.close()

            var isComplete = false
            Thread {
                try {
                    p.inputStream.use { s ->
                        val buf = ByteArray(DEFAULT_BUFFER_SIZE * 2)
                        while (true) {
                            val read = s.read(buf)
                            if (read == -1) break
                            System.out.write(buf, 0, read)
                        }
                    }
                } finally {
                    isComplete = true
                }
            }.apply {
                isDaemon = true
                priority = Thread.MAX_PRIORITY
            }.start()

            var timeout = TIMEOUT
            while (true) {
                if (isComplete) break
                check (timeout > Duration.ZERO) { "Timed out" }

                Thread.sleep(100)
                timeout -= 100.milliseconds
            }
        } finally {
            p?.destroy()
        }

        assertNotNull(p)

        var exitCode: Int? = null
        while (exitCode == null) {
            try {
                exitCode = p.exitValue()
            } catch (_: IllegalThreadStateException) {
                Thread.sleep(50)
            }
        }

        assertEquals(0, exitCode)
    }
}
