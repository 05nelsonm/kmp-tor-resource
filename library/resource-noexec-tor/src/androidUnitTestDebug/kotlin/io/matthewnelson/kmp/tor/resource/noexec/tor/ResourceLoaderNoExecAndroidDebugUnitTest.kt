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

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import kotlin.test.Test

@OptIn(InternalKmpTorApi::class)
class ResourceLoaderNoExecAndroidDebugUnitTest: ResourceLoaderNoExecBaseTest(
    runTorMainCount = when (OSInfo.INSTANCE.osHost) {
        is OSHost.Windows -> RUN_TOR_MAIN_COUNT_WINDOWS
        else -> RUN_TOR_MAIN_COUNT_UNIX
    }
) {

    @Test
    fun stub() {}
}
