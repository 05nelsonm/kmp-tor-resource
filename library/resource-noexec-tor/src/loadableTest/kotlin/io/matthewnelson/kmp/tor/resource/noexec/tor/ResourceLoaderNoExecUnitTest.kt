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

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.readBytes
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import kotlin.random.Random
import kotlin.test.*

@OptIn(InternalKmpTorApi::class)
open class ResourceLoaderNoExecUnitTest {

    private companion object {
        val random = Random.Default.nextBytes(8).encodeToString(Base16)
        val testDir = SysTempDir.resolve("kmp_tor_resource_noexec_test")
        val workDir = testDir.resolve(random)

        private val loader by lazy { ResourceLoaderTorNoExec.getOrCreate(resourceDir = workDir) }
    }

    @AfterTest
    fun cleanUp() {
        workDir.delete()
        testDir.delete()
    }

    @Test
    fun givenResourceLoaderNoExec_whenExtractGeoipFiles_thenIsSuccessful() {
        println(loader)

        val geoips = loader.extract()
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
        val loader = loader
        assertIs<ResourceLoader.Tor.NoExec>(loader)

        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        try {
            loader.withApi {
                torMain(listOf("--version"))
            }
        } catch (e: NotImplementedError) {
            println("Skipping...")
            return
        }
    }

    @Test
    fun givenResourceLoaderNoExec_whenRunInvalidConfig_thenThrowsIllegalArgumentException() {
        val loader = loader
        assertIs<ResourceLoader.Tor.NoExec>(loader)

        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        try {
            loader.withApi {
                torMain(listOf(
                    "--SocksPort", "-1",
                    "--verify-config",
                    "--quiet",
                ))
            }

            fail("Tor completed without expected exception...")
        } catch (e: NotImplementedError) {
            // TODO: Remove. Issue #58
            println("Skipping...")
            return
        } catch (e: IllegalArgumentException) {
            // pass
        }
    }
}
