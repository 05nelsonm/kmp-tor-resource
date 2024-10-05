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
@file:Suppress("KotlinRedundantDiagnosticSuppress")

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import kotlinx.cinterop.ExperimentalForeignApi

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureLibTorResources() { /* no-op */ }

@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): TorApi = KmpTorApi

@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class)
private object KmpTorApi: TorApi() {

    @Throws(IllegalArgumentException::class, IllegalStateException::class, IOException::class)
    override fun torMainProtected(args: Array<String>) {
        throw IllegalStateException("Not yet implemented")

        // TODO: Implement
        @Suppress("UNREACHABLE_CODE")
        tor_api_get_provider_version()
        @Suppress("UNREACHABLE_CODE")
        tor_main_configuration_new()
        @Suppress("UNREACHABLE_CODE")
        tor_main_configuration_set_command_line(null, 0, null)
        @Suppress("UNREACHABLE_CODE")
        tor_run_main(null)
        @Suppress("UNREACHABLE_CODE")
        tor_main_configuration_free(null)
    }
}
