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

import kotlin.experimental.ExperimentalNativeApi
import kotlin.test.Test

@OptIn(ExperimentalNativeApi::class)
class ResourceLoaderNoExecNativeUnitTest: ResourceLoaderNoExecBaseTest(
    // TODO: CKLib compile external/native.
    runTorMainCount = 0
//    runTorMainCount = when (Platform.osFamily) {
//        // TODO: Dynamic lib
//        OsFamily.IOS -> 0
//        OsFamily.WINDOWS -> RUN_TOR_MAIN_COUNT_WINDOWS
//        else -> RUN_TOR_MAIN_COUNT_UNIX
//    }
) {

    @Test
    fun stub() {}
}
