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

open class ResourceLoaderNoExecUnitTest {

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
        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        val result = try {
            LOADER.withApi(TestRuntimeBinder) {
                torRunMain(listOf("--version"))
            }
        } catch (e: NotImplementedError) {
            // TODO: Remove. Issue #58
            println("Skipping...")
            return
        }

        assertEquals(0, result)
    }

    @Test
    fun givenResourceLoaderNoExec_whenRunInvalidConfig_thenReturnsNon0() {
        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        val result = try {
            LOADER.withApi(TestRuntimeBinder) {
                torRunMain(listOf(
                    "--SocksPort", "-1",
                    "--verify-config",
                    "--quiet",
                ))
            }
        } catch (e: NotImplementedError) {
            // TODO: Remove. Issue #58
            println("Skipping...")
            return
        }

        assertEquals(-1, result)
    }
}
