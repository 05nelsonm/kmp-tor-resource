/*
 * Copyright (c) 2025 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.test.linux

import io.matthewnelson.kmp.file.absolutePath2
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.resource.test.linux.internal.IS_USING_MOCK_RESOURCES
import kotlin.test.Test

@OptIn(InternalKmpTorApi::class)
class TestLinuxUnitTest {

    @Test
    fun givenMain_whenCalled_thenRuns() {
        if (IS_USING_MOCK_RESOURCES) {
            println("Skipping...")
            return
        }

        // Will throw exception if it fails
        main(
            /* timeout = */ 3.toString(),
            /* expectedHost = */ OSInfo.INSTANCE.osHost.toString(),
            /* expectedArch = */ OSInfo.INSTANCE.osArch.toString(),
            /* runtimeDir = */ "".toFile().absolutePath2(),
        )
    }
}
