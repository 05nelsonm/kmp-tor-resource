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

import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.file.readBytes
import io.matthewnelson.kmp.file.readUtf8
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.LOADER
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.TEST_DIR
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.WORK_DIR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_GEOIPS
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Implemented in given source sets such that tests do not run
 * for AndroidDebugUnitTest, but do so for AndroidUnitTest. Even
 * with `forkEvery = 1` for Android/Jvm `Test` tasks, it still seems
 * to load libtor.so and pollute the memory space (resulting in a
 * crash). So this is just to disable that source set...
 * */
abstract class ResourceLoaderNoExecBaseTest protected constructor(
    private val runTorMainCount: Int = RUN_TOR_MAIN_COUNT_UNIX
) {

    protected companion object {
        const val RUN_TOR_MAIN_COUNT_UNIX: Int = 500
        const val RUN_TOR_MAIN_COUNT_WINDOWS: Int = 200
    }

    private val skipTorRunMain: Boolean get() {
        val skip = runTorMainCount <= 0 || !CAN_RUN_FULL_TESTS
        if (skip) {
            println("Skipping...")
        }
        return skip
    }

    @AfterTest
    fun cleanUp() {
        @OptIn(InternalKmpTorApi::class)
        listOf(RESOURCE_CONFIG_GEOIPS, RESOURCE_CONFIG_LIB_TOR).forEach { config ->
            config.resources.forEach { resource ->
                LOADER.resourceDir.resolve(resource.platform.fsFileName).delete()
            }
        }
        WORK_DIR.delete()
        TEST_DIR.delete()
    }

    @Test
    fun givenResourceLoaderNoExec_whenExtractGeoipFiles_thenIsSuccessful() {
        println(LOADER)

        val geoips = LOADER.extract()
        println(geoips)

        assertTrue(geoips.geoip.readBytes().isNotEmpty())
        assertTrue(geoips.geoip6.readBytes().isNotEmpty())
    }

    @Test
    fun givenResourceLoaderNoExec_whenWithApi_thenLoadsSuccessfully() {
        if (skipTorRunMain) return

        val result = LOADER.withApi(TestRuntimeBinder) {
            torRunMain(listOf("--version"))

            assertFailsWith<IllegalStateException> { torRunMain(listOf("--version")) }

            terminateAndAwaitResult()
        }

        assertEquals(0, result)
    }

    @Test
    fun givenResourceLoaderNoExec_whenMultipleRuns_thenLibTorIsUnloaded() {
        if (skipTorRunMain) return

        repeat(runTorMainCount) { index ->
            if ((index + 1) % 10 == 0) {
                println("RUN[${index + 1}]")
            }

            val result = LOADER.withApi(TestRuntimeBinder) {
                assertEquals(TorApi.State.OFF, state())

                torRunMain(
                    listOf(
                        "--SocksPort", "-1",
                        "--verify-config",
                        "--quiet",
                    )
                )

                assertNotEquals(TorApi.State.OFF, state())

                val rv = terminateAndAwaitResult()

                assertEquals(TorApi.State.OFF, state())

                rv
            }

            assertEquals(1, result)
        }
    }

    @Test
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    fun givenHandle_whenTerminateAndAwait_thenTorExits() = runTest(timeout = 5.minutes) {
        if (skipTorRunMain) return@runTest

        val job = currentCoroutineContext().job

        val geoipFiles = LOADER.extract().let { files ->
            if (files.geoip.readBytes().size < 50_000) {
                // Mock resources...
                println("Skipping...")
                return@runTest
            }

            files
        }

        val logFile = LOADER.resourceDir.resolve("test.log")
        job.invokeOnCompletion { logFile.delete() }

        val args = ArrayList<String>(10).apply {
            add("--ignore-missing-torrc")
            add("--quiet")

            val dataDir = LOADER.resourceDir.resolve("data")

            job.invokeOnCompletion { dataDir.resolve("lock").delete() }
            job.invokeOnCompletion { dataDir.resolve("state").delete() }
            job.invokeOnCompletion { dataDir.resolve("keys").delete() }

            listOf(
                "--DataDirectory" to dataDir,
                "--CacheDirectory" to LOADER.resourceDir.resolve("cache"),
            ).forEach { (option, argument) ->
                argument.mkdirs()
                job.invokeOnCompletion { argument.delete() }
                add(option); add(argument.path)
            }

            add("--GeoIPFile"); add(geoipFiles.geoip.toString())
            add("--GeoIPv6File"); add(geoipFiles.geoip6.toString())

            add("--Log"); add("notice file $logFile")
            add("--TruncateLogFile"); add("1")

            add("--DormantCanceledByStartup"); add("1")
            add("--SocksPort"); add("0")

            add("--DisableNetwork"); add("1")
            add("--RunAsDaemon"); add("0")
        }

        val bgDispatcher = newSingleThreadContext("bg-tor-terminate")
        job.invokeOnCompletion { bgDispatcher.close() }

        repeat(runTorMainCount / 10) { index ->
            if ((index + 1) % 5 == 0) {
                println("RUN_TOR[${index + 1}]")
            }

            val api = LOADER.withApi(TestRuntimeBinder) {
                torRunMain(args)
                this
            }

            val completion = job.invokeOnCompletion { api.terminateAndAwaitResult() }

            withContext(bgDispatcher) {
                delay(1.seconds)
                assertEquals(0, api.terminateAndAwaitResult())
            }

            completion.dispose()

            val logText = logFile.readUtf8()
            logFile.delete()

            listOf(
                "Tor can't help you if you use it wrong!",
                "Delaying directory fetches: DisableNetwork is set.",
                "Owning controller connection has closed -- exiting now.",
                "Catching signal TERM, exiting cleanly.",
            ).mapNotNull { expected ->
                if (logText.contains(expected)) return@mapNotNull null

                "Logs did not contain EXPECTED[$expected]. RUN_TOR[${index + 1}]"
            }.takeIf { it.isNotEmpty() }?.joinToString(separator = "\n")?.let { errors ->
                throw AssertionError(errors + "\n\n" + logText)
            }
        }
    }
}
