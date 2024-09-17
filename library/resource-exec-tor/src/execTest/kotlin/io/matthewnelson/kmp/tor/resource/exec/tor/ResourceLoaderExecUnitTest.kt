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

        val paths = loader.extract()
        println(paths)

        try {
            assertTrue(paths.executable.readBytes().isNotEmpty())
            assertTrue(paths.geoips.geoip.readBytes().isNotEmpty())
            assertTrue(paths.geoips.geoip6.readBytes().isNotEmpty())

            assertFalse(paths.executable.name.endsWith(".gz"))
            assertFalse(paths.geoips.geoip.name.endsWith(".gz"))
            assertFalse(paths.geoips.geoip6.name.endsWith(".gz"))

            // Native will first write gzipped file to system, then decompress
            // via zlib to separate file. Check to make sure that was cleaned up.
            assertFalse("${paths.executable.path}.gz".toFile().exists())
            assertFalse("${paths.geoips.geoip.path}.gz".toFile().exists())
            assertFalse("${paths.geoips.geoip6.path}.gz".toFile().exists())

            if (!IS_WINDOWS) {
                assertTrue(paths.executable.isExecutable())
                assertFalse(paths.geoips.geoip.isExecutable())
                assertFalse(paths.geoips.geoip.isExecutable())
            }
        } finally {
            paths.executable.delete()
            paths.executable.parentFile.let { parent ->
                listOf(
                    "libtor.so",
                    "libtor.dylib",
                    "tor.dll"
                ).forEach { libname ->
                    parent?.resolve(libname)?.delete()
                }
            }
            paths.geoips.geoip.delete()
            paths.geoips.geoip6.delete()
            workDir.delete()
            testDir.delete()
        }
    }
}
