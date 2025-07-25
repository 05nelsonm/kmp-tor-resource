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
package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.winhttp.WinHttp
import kotlin.test.Test

class ResourceLoaderNoExecMingwUnitTest: ResourceLoaderNoExecBaseTest(RUN_TOR_MAIN_COUNT_WINDOWS) {
    override val factory: HttpClientEngineFactory<*>? = WinHttp

    @Test
    fun stub() {}
}
