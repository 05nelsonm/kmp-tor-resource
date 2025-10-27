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

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.http
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.delete2
import io.matthewnelson.kmp.file.mkdirs2
import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.file.readBytes
import io.matthewnelson.kmp.file.readUtf8
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.LOADER
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.WORK_DIR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_GEOIPS
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

abstract class ResourceLoaderNoExecBaseTest protected constructor(
    private val runTorMainCount: Int = RUN_TOR_MAIN_COUNT_UNIX,
) {

    protected companion object {
        const val RUN_TOR_MAIN_COUNT_UNIX: Int = 400
        const val RUN_TOR_MAIN_COUNT_WINDOWS: Int = 150
    }

    private val skipTorRunMain: Boolean get() {
        val skip = runTorMainCount <= 0 || !CAN_RUN_FULL_TESTS
        if (skip) println("Skipping...")
        return skip
    }

    protected abstract val factory: HttpClientEngineFactory<*>?

    @AfterTest
    fun cleanUp() {
        @OptIn(InternalKmpTorApi::class)
        listOf(RESOURCE_CONFIG_GEOIPS, RESOURCE_CONFIG_LIB_TOR).forEach { config ->
            config.resources.forEach { resource ->
                LOADER.resourceDir.resolve(resource.platform.fsFileName).delete2(ignoreReadOnly = true)
            }
        }
        try {
            WORK_DIR.delete2()
        } catch (_: IOException) {}
    }

//    @Test
//    open fun givenResourceLoaderNoExec_whenExtractGeoipFiles_thenIsSuccessful() {
//        println(LOADER)
//
//        val geoips = LOADER.extract()
//        println(geoips)
//
//        assertTrue(geoips.geoip.readBytes().isNotEmpty())
//        assertTrue(geoips.geoip6.readBytes().isNotEmpty())
//    }
//
//    @Test
//    open fun givenResourceLoaderNoExec_whenWithApi_thenLoadsSuccessfully() {
//        if (skipTorRunMain) return
//
//        val result = LOADER.withApi(TestRuntimeBinder) {
//            torRunMain(listOf("--version"))
//
//            assertFailsWith<IllegalStateException> { torRunMain(listOf("--version")) }
//
//            terminateAndAwaitResult()
//        }
//
//        assertEquals(0, result)
//    }
//
//    @Test
//    open fun givenResourceLoaderNoExec_whenMultipleRuns_thenLibTorIsUnloaded() {
//        if (skipTorRunMain) return
//
//        repeat(runTorMainCount) { index ->
//            if (index < 5 || (index + 1) % 10 == 0) {
//                println("\nRUN[${index + 1}]")
//            }
//
//            val result = LOADER.withApi(TestRuntimeBinder) {
//                assertEquals(TorApi.State.OFF, state())
//                assertEquals(-1, terminateAndAwaitResult())
//
//                torRunMain(
//                    listOf(
//                        "--SocksPort", "-1",
//                        "--verify-config",
//                        "--quiet",
//                    )
//                )
//
//                assertNotEquals(TorApi.State.OFF, state())
//
//                val rv = terminateAndAwaitResult()
//
//                assertEquals(TorApi.State.OFF, state())
//
//                rv
//            }
//
//            assertEquals(1, result)
//        }
//    }
//
    @Test
    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    open fun givenHandle_whenTerminateAndAwait_thenTorExits(): TestResult {
//        val count = runTorMainCount / 25
        val count = 1_000

        return runTest(timeout = (count * 4).seconds) {
            if (skipTorRunMain) return@runTest

            val helper = TorApiHelper(scope = this)
            if (helper == null) {
                println("Skipping...")
                return@runTest
            }

//            val hsDir = LOADER.resourceDir.resolve("hs")
//
//            fun deleteHsDir() {
//                helper.deleteTestFiles(
//                    hsDir.resolve("authorized_clients"),
//                    hsDir.resolve("hostname"),
//                    hsDir.resolve("hs_ed25519_public_key"),
//                    hsDir.resolve("hs_ed25519_secret_key"),
//                    hsDir,
//                )
//            }
//
//            helper.job.invokeOnCompletion { deleteHsDir() }
//
//            helper.args.apply {
//                add("--HiddenServiceDir"); add(hsDir.path)
//                add("--HiddenServiceVersion"); add("3")
//                add("--HiddenServicePort"); add("80")
//            }

            println("LOG_FILE[${helper.logFile}]")
            repeat(count) { index ->
                if (index < 4 || (index + 1) % 5 == 0) {
                    println("RUN_TOR[${index + 1}]")
                }

                val api = LOADER.withApi(TestRuntimeBinder) { this }
                api.torRunMain(helper.args)
                val completion = helper.job.invokeOnCompletion { api.terminateAndAwaitResult() }

//                withContext(helper.bgDispatcher) {
//                    delay(1.seconds)
                    assertEquals(0, api.terminateAndAwaitResult())
//                }

                completion.dispose()

//                val logText = helper.logFile.readUtf8()
//
//                helper.logFile.delete2()
//                helper.deleteCacheDir()
//                helper.deleteDataDir()
////                deleteHsDir()
//
//                listOf(
//                    "Tor can't help you if you use it wrong!",
//                    "Delaying directory fetches: DisableNetwork is set.",
//                    "Owning controller connection has closed -- exiting now.",
//                    "Catching signal TERM, exiting cleanly.",
//                ).mapNotNull { expected ->
//                    if (logText.contains(expected)) return@mapNotNull null
//
//                    "Logs did not contain EXPECTED[$expected]. RUN_TOR[${index + 1}]"
//                }.takeIf { it.isNotEmpty() }?.joinToString(separator = "\n")?.let { errors ->
//                    throw AssertionError(errors + "\n\n" + logText)
//                }
            }
        }
    }
//
//    @Test
//    open fun givenTor_whenQueryCheckTorProject_thenConnectionIsUsingTor() = runTest(timeout = 7.minutes) {
//        val factory = factory
//        if (factory == null) {
//            println("Skipping...")
//            return@runTest
//        }
//        val helper = TorApiHelper(scope = this, disableNetwork = "0")
//        if (helper == null) {
//            println("Skipping...")
//            return@runTest
//        }
//
//        val api = LOADER.withApi(TestRuntimeBinder) { this }
//        api.torRunMain(helper.args)
//        helper.job.invokeOnCompletion { api.terminateAndAwaitResult() }
//
//        val (proxyHttp, proxySocks) = withContext(helper.bgDispatcher) {
//            var http = ""
//            var socks = ""
//            while (true) {
//                if (api.state() != TorApi.State.STARTED) {
//                    throw AssertionError("Tor stopped unexpectedly\n\n")
//                }
//
//                delay(1.seconds)
//
//                val text = try {
//                    helper.logFile.readUtf8()
//                } catch (_: IOException) {
//                    continue
//                }
//
//                if (!text.contains("Bootstrapped 100%")) continue
//
//                text.lines().forEach { line ->
//                    if (!line.contains("Opened ")) return@forEach
//                    val i = line.indexOfLast { it.isWhitespace() }
//                    if (i == -1) return@forEach
//                    val address = line.substring(i + 1, line.length)
//
//                    if (line.contains("HTTP tunnel listener connection")) {
//                        http = address
//                    }
//                    if (line.contains("Socks listener connection")) {
//                        socks = address
//                    }
//                }
//                break
//            }
//            check(http.isNotBlank()) { "http port was blank" }
//            check(socks.isNotBlank()) { "socks port was blank" }
//            http to socks
//        }
//
//        val clientHttp = HttpClient(factory) {
//            engine {
//                proxy = ProxyBuilder.http("http://$proxyHttp")
//            }
//        }
//
//        // Unsupported by Darwin/WinHttp clients.
//        val clientSocks = try {
//            HttpClient(factory) {
//                engine {
//                    proxy = ProxyBuilder.socks(
//                        host = proxySocks.substringBefore(':'),
//                        port = proxySocks.substringAfter(':').toInt(),
//                    )
//                }
//            }
//        } catch (_: Throwable) {
//            null
//        }
//
//        helper.job.invokeOnCompletion {
//            try {
//                clientHttp.close()
//            } catch (_: Throwable) {}
//        }
//        helper.job.invokeOnCompletion {
//            try {
//                clientSocks?.close()
//            } catch (_: Throwable) {}
//        }
//
//        val congratulations = Array(10) { i ->
//            val client = if (i % 2 == 0) clientHttp else clientSocks ?: clientHttp
//
//            async {
//                val response = try {
//                    client.get("https://check.torproject.org/")
//                } catch (_: Throwable) {
//                    println("FAILED_CALL[$i]")
//                    return@async null
//                }
//
//                if (response.status != HttpStatusCode.OK) {
//                    println("FAILED_STATUS[$i]")
//                    response.cancel()
//                    null
//                } else {
//                    response.bodyAsText()
//                }
//            }
//        }.toList().awaitAll().mapNotNull { response ->
//            response?.contains("Congratulations.")
//        }
//
//        assertTrue(congratulations.isNotEmpty())
//        congratulations.forEach { congratulation ->
//            assertTrue(congratulation, "check.torproject.org failure. We are NOT using tor...")
//        }
//        println("SUCCESS!")
//    }

    private class TorApiHelper private constructor(val job: Job) {

        val logFile = LOADER.resourceDir.resolve("test.log")
        val cacheDir = LOADER.resourceDir.resolve("cache")
        val dataDir = LOADER.resourceDir.resolve("data")

        @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
        val bgDispatcher: CoroutineDispatcher = newSingleThreadContext("bg-tor-dispatcher")

        val args = ArrayList<String>(25).apply {
            add("--ignore-missing-torrc")
            add("--quiet")

            add("--CacheDirectory"); add(cacheDir.path)
            add("--DataDirectory"); add(dataDir.path)

            add("--SocksPort"); add("auto")
            add("--HTTPTunnelPort"); add("auto")

            add("--Log"); add("debug file $logFile")
            add("--TruncateLogFile"); add("1")

            add("--DormantCanceledByStartup"); add("1")
            add("--RunAsDaemon"); add("0")
        }

        fun deleteCacheDir() {
            deleteTestFiles(
                cacheDir.resolve("cached-certs"),
                cacheDir.resolve("cached-microdesc-consensus"),
                cacheDir.resolve("cached-microdescs"),
                cacheDir.resolve("cached-microdescs.new"),
                cacheDir,
            )
        }

        fun deleteDataDir() {
            deleteTestFiles(
                dataDir.resolve("lock"),
                dataDir.resolve("state"),
                dataDir.resolve("keys"),
                dataDir,
            )
        }

        fun deleteTestFiles(vararg files: File) {
            files.forEach { file ->
                try {
                    file.delete2(ignoreReadOnly = true)
                } catch (_: IOException) {}
            }
        }

        init {
            cacheDir.mkdirs2(mode = null)
            dataDir.mkdirs2(mode = null)

            job.invokeOnCompletion { deleteTestFiles(logFile) }
            job.invokeOnCompletion { deleteCacheDir() }
            job.invokeOnCompletion { deleteDataDir() }
            @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
            job.invokeOnCompletion {
                try {
                    (bgDispatcher as CloseableCoroutineDispatcher).close()
                } catch (_: Throwable) {}
            }
        }

        companion object {

            operator fun invoke(scope: TestScope, disableNetwork: String = "1"): TorApiHelper? {
                if (!CAN_RUN_FULL_TESTS) return null

                val geoipFiles = LOADER.extract()
                // Mock resources
                if (geoipFiles.geoip.readBytes().size < 50_000) return null

                val helper = TorApiHelper(scope.coroutineContext.job)
                helper.args.apply {
                    add("--DisableNetwork"); add(disableNetwork)
                    add("--GeoIPFile"); add(geoipFiles.geoip.path)
                    add("--GeoIPv6File"); add(geoipFiles.geoip6.path)
                }
                return helper
            }
        }
    }
}
