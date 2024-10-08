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
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import kotlin.random.Random
import kotlin.test.*

open class ResourceLoaderNoExecUnitTest {

    private object TestBinder: ResourceLoader.RuntimeBinder

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

        val result = try {
            loader.withApi(TestBinder) {
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
        val loader = loader
        assertIs<ResourceLoader.Tor.NoExec>(loader)

        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        val result = try {
            loader.withApi(TestBinder) {
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
