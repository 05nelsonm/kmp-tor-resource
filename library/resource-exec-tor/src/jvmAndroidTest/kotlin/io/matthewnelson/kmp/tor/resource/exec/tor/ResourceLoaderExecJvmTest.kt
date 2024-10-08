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
package io.matthewnelson.kmp.tor.resource.exec.tor

import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import kotlin.io.readBytes
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

open class ResourceLoaderExecJvmTest: ResourceLoaderExecBaseTest() {

    protected open val ctrlPortArgument = "auto"

    @Test
    fun givenTor_whenProcessExecution_thenRuns() {
        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        val loader = ResourceLoaderTorExec.getOrCreate(testDir)
        assertIs<ResourceLoader.Tor.Exec>(loader)

        val geoipFiles = loader.extract().let { files ->
            if (files.geoip.readBytes().size < 50_000) {
                // mock resources being utilized. do not use
                files.geoip.delete()
                files.geoip6.delete()
                return@let null
            }

            files
        }

        val b = loader.process(TestRuntimeBinder) { tor, configureEnv ->
            ProcessBuilder().apply {
                val env = environment()
                env["HOME"] = testDir.path
                env.configureEnv()
                redirectErrorStream(true)

                val cmds = mutableListOf<String>().apply {
                    add(tor.path)
                    add("-f"); add("-")

                    listOf(
                        "--DataDirectory" to testDir.resolve("data"),
                        "--CacheDirectory" to testDir.resolve("cache"),
                    ).forEach { (option, argument) ->
                        argument.deleteRecursively()
                        argument.mkdirs()
                        add(option); add(argument.path)
                    }

                    if (geoipFiles != null) {
                        add("--GeoIPFile"); add(geoipFiles.geoip.toString())
                        add("--GeoIPv6File"); add(geoipFiles.geoip6.toString())
                    }

                    add("--DormantCanceledByStartup"); add("1")
                    add("--ControlPort"); add(ctrlPortArgument)
                    add("--SocksPort"); add("auto")

                    add("--DisableNetwork"); add("1")
                    add("--RunAsDaemon"); add("0")
                }

                command(cmds)
            }
        }

        var p: Process? = null
        val out = StringBuilder()

        try {
            p = b.start()

            // Using arguments "-f" "-" to skip a torrc file. Need to close
            // this end so tor stops waiting for configuration input.
            p.outputStream.close()

            Thread {
                try {
                    val reader = p.inputStream.reader().buffered()

                    while (true) {
                        val line = reader.readLine() ?: break
                        out.appendLine(line)
                    }
                } catch (_: Throwable) {}
            }.start()

            // Can't use Process.waitFor(3, TimeUnit.SECONDS) b/c
            // it requires Android API 24+ so emulator will fail.
            Thread.sleep(3_000)
        } finally {
            p?.destroy()
        }

        Thread.sleep(500)

        val outString = out.toString()
        assertTrue(outString.contains("Delaying directory fetches: DisableNetwork is set."), outString)
    }
}