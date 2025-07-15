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
@file:Suppress("RedundantUnitReturnType")

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.ktor.client.engine.HttpClientEngineFactory
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import kotlinx.coroutines.test.TestResult
import org.junit.AfterClass
import org.junit.BeforeClass
import kotlin.system.exitProcess
import kotlin.test.Test

@OptIn(InternalKmpTorApi::class)
class ResourceLoaderNoExecJvmUnitTest: ResourceLoaderNoExecBaseTest(
    runTorMainCount = when (OSInfo.INSTANCE.osHost) {
        is OSHost.Windows -> RUN_TOR_MAIN_COUNT_WINDOWS
        else -> RUN_TOR_MAIN_COUNT_UNIX
    },
) {

    companion object {

        // Disgusting, but Java TLS is absolute trash
        private var SUCCESSES = 0

        @JvmStatic
        @BeforeClass
        fun beforeClass() { SUCCESSES = 0 }

        @JvmStatic
        @AfterClass
        fun afterClass() {
            if (SUCCESSES == 5) exitProcess(0)
        }
    }

    override val factory: HttpClientEngineFactory<*>? = null

    @Test
    override fun givenResourceLoaderNoExec_whenExtractGeoipFiles_thenIsSuccessful() {
        super.givenResourceLoaderNoExec_whenExtractGeoipFiles_thenIsSuccessful()
        SUCCESSES++
    }

    @Test
    override fun givenResourceLoaderNoExec_whenWithApi_thenLoadsSuccessfully() {
        super.givenResourceLoaderNoExec_whenWithApi_thenLoadsSuccessfully()
        SUCCESSES++
    }

    @Test
    override fun givenResourceLoaderNoExec_whenMultipleRuns_thenLibTorIsUnloaded() {
        super.givenResourceLoaderNoExec_whenMultipleRuns_thenLibTorIsUnloaded()
        SUCCESSES++
    }

    @Test
    override fun givenHandle_whenTerminateAndAwait_thenTorExits(): TestResult {
        val ret = super.givenHandle_whenTerminateAndAwait_thenTorExits()
        SUCCESSES++
        return ret
    }

    @Test
    override fun givenTor_whenQueryCheckTorProject_thenConnectionIsUsingTor(): TestResult {
        val ret = super.givenTor_whenQueryCheckTorProject_thenConnectionIsUsingTor()
        SUCCESSES++
        return ret
    }
}
