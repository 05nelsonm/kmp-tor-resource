/*
 * Copyright (c) 2023 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.tor

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.file.*
import kotlin.random.Random
import kotlin.test.*

abstract class TorResourceBaseTest {

    open val isWindows: Boolean = false
    private val testDir = SysTempDir.resolve("kmp_tor_test")

    @Test
    open fun givenKmpTorBinaryResources_whenInstalled_thenIsSuccessful() {
        val random = Random.Default.nextBytes(8).encodeToString(Base16)

        // Will check extraction uses mkdirs instead of mkdir (which would fail)
        val workDir = testDir.resolve(random)

        val paths = TorResource(workDir.toString().toFile()).install()
        println(paths)

        val geoip = paths.geoip
        val geoip6 = paths.geoip6
        val tor = paths.tor

        try {

            assertTrue(geoip.readBytes().isNotEmpty())
            assertTrue(geoip6.readBytes().isNotEmpty())
            assertTrue(tor.readBytes().isNotEmpty())

            // Resource files were gzipped. Check to see if the .gz
            // extension was removed.
            assertFalse(geoip.name.endsWith(".gz"))
            assertFalse(geoip6.name.endsWith(".gz"))
            assertFalse(tor.name.endsWith(".gz"))

            // Native will first write gzipped file to system,
            // then decompress them via zlib to their final destination.
            // Check to make sure the .gz file was cleaned up
            assertFalse("${geoip.path}.gz".toFile().exists())
            assertFalse("${geoip6.path}.gz".toFile().exists())
            assertFalse("${tor.path}.gz".toFile().exists())

            if (!isWindows) {
                assertFalse(geoip.isExecutable())
                assertFalse(geoip6.isExecutable())
                assertTrue(tor.isExecutable())
            }
        } finally {
            geoip.delete()
            geoip6.delete()
            tor.delete()
            workDir.delete()
            testDir.delete()
        }
    }
}
