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

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import io.matthewnelson.kmp.file.toFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class ResourceLoaderNoExecAndroidTest: ResourceLoaderNoExecBaseTest() {

    private companion object {
        private val TIMEOUT: Duration = 15.minutes
    }

    private val ctx = ApplicationProvider.getApplicationContext<Context>()
    private val nativeLibraryDir = ctx.applicationInfo.nativeLibraryDir

    @Test
    fun givenAndroidNative_whenExecuteTestBinary_thenIsSuccessful() {
        val executable = nativeLibraryDir.toFile().resolve("libTestExec.so")

        var p: Process? = null
        val mark = TimeSource.Monotonic.markNow()
        val output = StringBuilder()
        try {
            p = ProcessBuilder(listOf(executable.path))
                .redirectErrorStream(true)
                .start()

            p.outputStream.close()

            var isComplete = false
            Thread {
                try {
                    p.inputStream.buffered().reader().use { s ->
                        val buf = CharArray(DEFAULT_BUFFER_SIZE * 2)
                        while (true) {
                            val read = s.read(buf)
                            if (read == -1) break
                            output.append(buf, 0, read)
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
                check (timeout > Duration.ZERO) { "Timed out\n${output}" }

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

        output.appendLine().append("RUN LENGTH: ${mark.elapsedNow().inWholeSeconds}s")
        val out = output.toString()
        assertEquals(0, exitCode, out)
        println(out)
    }
}
