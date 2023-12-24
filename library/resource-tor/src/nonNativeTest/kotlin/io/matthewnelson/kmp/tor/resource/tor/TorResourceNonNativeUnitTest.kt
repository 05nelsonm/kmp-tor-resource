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
import io.matthewnelson.kmp.tor.core.resource.OSHost
import io.matthewnelson.kmp.tor.core.resource.OSInfo
import io.matthewnelson.kmp.tor.resource.tor.internal.RESOURCE_CONFIG
import io.matthewnelson.kmp.tor.resource.tor.internal.isSupportedBy
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(InternalKmpTorApi::class)
class TorResourceNonNativeUnitTest: TorResourceBaseTest() {

    override val isWindows: Boolean = OSInfo.INSTANCE.osHost is OSHost.Windows

    @Test
    fun givenTorBinaryResources_whenConfigured_thenIsExpectedForHostMachine() {
        println(RESOURCE_CONFIG)

        if (!OSInfo.INSTANCE.osArch.isSupportedBy(OSInfo.INSTANCE.osHost)) {
            // If host machine running this test is not supported, the
            // tor binaries will not be configured and an error will
            // be added instead.
            assertEquals(1, RESOURCE_CONFIG.errors.size)
            assertEquals(2, RESOURCE_CONFIG.resources.size)
        } else {
            // Specifically for Android, this should obtain the Loader
            // class from module :binary-android-unit-test and have
            // the appropriate resources to load for the host machine.
            assertEquals(0, RESOURCE_CONFIG.errors.size)

            // If it's Android runtime, tor should be obtained via `core-resource-initializer`
            // and not have a "tor" alias within its config
            val expected = if (OSInfo.INSTANCE.osHost is OSHost.Linux.Android) 2 else 3
            assertEquals(expected, RESOURCE_CONFIG.resources.size)
        }
    }
}
