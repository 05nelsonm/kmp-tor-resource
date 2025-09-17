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
package io.matthewnelson.kmp.tor.resource.geoip

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.delete2
import io.matthewnelson.kmp.file.exists2
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

@OptIn(InternalKmpTorApi::class)
class ResourceConfigUnitTest {

    @Test
    fun givenGeoipFiles_whenAddedToConfig_thenAreExtracted() {
        val config = Resource.Config.create { configureGeoipResources() }

        val tmpDir = SysTempDir.resolve("geoip_" + Random.nextBytes(8).encodeToString(Base16))

        val paths = config.extractTo(tmpDir, onlyIfDoesNotExist = false)
        val geoip = paths[ALIAS_GEOIP]!!
        val geoip6 = paths[ALIAS_GEOIP6]!!

        try {
            assertTrue(geoip.exists2())
            assertTrue(geoip6.exists2())
        } finally {
            geoip.delete2(ignoreReadOnly = true)
            geoip6.delete2(ignoreReadOnly = true)
            tmpDir.delete2(ignoreReadOnly = true)
        }
    }
}
