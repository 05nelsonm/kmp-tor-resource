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

import io.matthewnelson.kmp.file.readBytes
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.LOADER
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.TEST_DIR
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.WORK_DIR
import kotlin.test.*

/**
 * Implemented in given source sets such that tests do not run
 * for AndroidDebugUnitTest, but do so for AndroidUnitTest. Even
 * with `forkEvery = 1` for Android/Jvm `Test` tasks, it still seems
 * to load libtor.so and pollute the memory space (resulting in a
 * crash). So this is just to disable that source set...
 * */
abstract class ResourceLoaderNoExecBaseTest(private val runTorMain: Boolean = true) {

    @AfterTest
    fun cleanUp() {
        WORK_DIR.delete()
        TEST_DIR.delete()
    }

    @Test
    fun givenResourceLoaderNoExec_whenExtractGeoipFiles_thenIsSuccessful() {
        println(LOADER)

        val geoips = LOADER.extract()
        println(geoips)

        try {
            assertTrue(geoips.geoip.readBytes().isNotEmpty())
            assertTrue(geoips.geoip6.readBytes().isNotEmpty())
        } finally {
            geoips.geoip.delete()
            geoips.geoip6.delete()
        }
    }

    @Test
    fun givenResourceLoaderNoExec_whenWithApi_thenLoadsSuccessfully() {
        if (!runTorMain || !CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        val result = LOADER.withApi(TestRuntimeBinder) {
            torRunMain(listOf("--version"))
        }

        assertEquals(0, result)
    }

    @Test
    fun givenResourceLoaderNoExec_whenRunInvalidConfig_thenReturnsNon0() {
        if (!runTorMain || !CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        repeat(1000) { index ->
            if ((index + 1) % 5 == 0) {
                println("RUN[${index + 1}]")
            }

            val result = LOADER.withApi(TestRuntimeBinder) {
                assertFalse(isRunning)

                val rv = torRunMain(
                    listOf(
                        "--SocksPort", "-1",
                        "--verify-config",
                        "--quiet"
                    )
                )

                assertFalse(isRunning)

                rv
            }

            assertEquals(1, result)
        }
    }
}
