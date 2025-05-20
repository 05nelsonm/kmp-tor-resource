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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.matthewnelson.kmp.tor.resource.exec.tor

import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.ALIAS_LIB_TOR
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.ALIAS_TOR
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.RESOURCE_CONFIG_TOR
import kotlin.jvm.JvmStatic
import kotlin.test.*

@OptIn(InternalKmpTorApi::class)
actual abstract class ResourceLoaderExecBaseTest {

    protected companion object {

        @JvmStatic
        protected val TEST_RESOURCE_DIR: File by lazy {
            if (TEST_DIR.isBlank()) {
                // Android
                SysTempDir.resolve("kmp-tor-exec" + if (IS_GPL) "-gpl" else "")
            } else {
                // build/test-resources/{sourceSet.name}
                TEST_DIR.toFile()
            }
        }
    }

    @Test
    fun givenResourceLoaderExec_whenAllResourcesExtracted_thenAreConfiguredAsExpected() {
        val loader = ResourceLoaderTorExec.getOrCreate(resourceDir = TEST_RESOURCE_DIR)
        assertIs<ResourceLoader.Tor.Exec>(loader)
        println(loader)

        val (torName, libTorName) = try {
            Pair(
                RESOURCE_CONFIG_TOR[ALIAS_TOR].platform.fsFileName,
                RESOURCE_CONFIG_TOR[ALIAS_LIB_TOR].platform.fsFileName,
            )
        } catch (_: NoSuchElementException) {
            // Android
            "libtorexec.so" to "libtor.so"
        }

        // Ensure resources are not present
        with(loader.resourceDir) {
            listOf(
                resolve("geoip"),
                resolve("geoip6"),
                resolve(torName),
                resolve(libTorName),
                resolve("tor.exe.local"),
            ).forEach { it.delete(); assertFalse(it.exists()) }
        }

        val geoips = loader.extract()
        println(geoips)

        val tor = loader.process(TestRuntimeBinder) { torExecutable, _ -> torExecutable }

        // Ensures that it was extracted alongside the tor executable.
        // Have to use tor's parent file b/c android runtime it will
        // not be in the declared resource directory, it is in the
        // nativeLibraryDir.
        val sharedLib = tor.parentFile!!.resolve(libTorName)

        // Ensure that only windows extracts a .local file for DLL redirect
        // alongside tor.exe.
        val local = tor.parentFile!!.resolve("tor.exe.local")

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
            assertTrue(sharedLib.isExecutable())
            assertFalse(geoips.geoip.isExecutable())
            assertFalse(geoips.geoip6.isExecutable())
        }
    }
}
