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

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(InternalKmpTorApi::class)
open class ResourceLoaderExecUnitTest {

    @Test
    fun givenResourceLoaderExec_whenExtracted_thenIsSuccessful() {
        val random = Random.Default.nextBytes(8).encodeToString(Base16)

        val testDir = SysTempDir.resolve("kmp_tor_resource_exec_test")
        val workDir = testDir.resolve(random)

        val loader = ResourceLoaderTorExec.getOrCreate(resourceDir = workDir)
        assertIs<ResourceLoader.Tor.Exec>(loader)
        println(loader)

        val geoips = loader.extract()
        println(geoips)

        val tor = loader.execute { tor, _ -> tor }

        // Ensures that it was extracted alongside the tor executable
        val sharedLib = tor.parentFile!!.resolve(SHARED_LIB_NAME)

        // Ensure that only windows extracts a .local file for DLL redirect.
        val local = tor.parentFile!!.resolve("tor.exe.local")

        try {
            assertTrue(tor.readBytes().isNotEmpty())
            assertTrue(sharedLib.readBytes().isNotEmpty())
            assertTrue(geoips.geoip.readBytes().isNotEmpty())
            assertTrue(geoips.geoip6.readBytes().isNotEmpty())

            assertFalse(tor.name.endsWith(".gz"))
            assertFalse(sharedLib.name.endsWith(".gz"))
            assertFalse(geoips.geoip.name.endsWith(".gz"))
            assertFalse(geoips.geoip6.name.endsWith(".gz"))

            // Native will first write gzipped file to system, then decompress
            // via zlib to separate file. Check to make sure that was cleaned up.
            assertFalse("${tor.path}.gz".toFile().exists())
            assertFalse("${sharedLib.path}.gz".toFile().exists())
            assertFalse("${geoips.geoip.path}.gz".toFile().exists())
            assertFalse("${geoips.geoip6.path}.gz".toFile().exists())

            if (IS_WINDOWS) {
                assertTrue(local.exists())
            } else {
                assertFalse(local.exists())
                assertTrue(tor.isExecutable())
                assertFalse(geoips.geoip.isExecutable())
                assertFalse(geoips.geoip6.isExecutable())
            }
        } finally {
            tor.delete()
            sharedLib.delete()
            local.delete()
            geoips.geoip.delete()
            geoips.geoip6.delete()
            workDir.delete()
            testDir.delete()
        }
    }
}
