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
import io.ktor.client.engine.curl.Curl
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.LOADER
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.__kmp_tor_init
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.test.Test
import kotlin.test.assertNull

class ResourceLoaderNoExecLinuxUnitTest: ResourceLoaderNoExecBaseTest() {
    override val factory: HttpClientEngineFactory<*>? = Curl

    @Test
    fun givenKmpTorContext_whenAlreadyLoaded_thenReturnsNull() {
        LOADER.withApi(TestRuntimeBinder) {}
        @OptIn(ExperimentalForeignApi::class)
        assertNull(__kmp_tor_init())
    }
}
