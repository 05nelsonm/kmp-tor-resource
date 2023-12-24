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

import io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi
import io.matthewnelson.kmp.tor.resource.tor.internal.RESOURCE_CONFIG
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(InternalKmpTorApi::class)
class TorResourceAndroidTest: TorResourceBaseTest() {

    @Test
    fun givenResourceConfig_whenAndroidEmulator_thenConfigLoadsOnlyGeoips() {
        assertEquals(0, RESOURCE_CONFIG.errors.size)
        assertEquals(2, RESOURCE_CONFIG.resources.size)
    }
}
