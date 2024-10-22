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
package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): TorApi = KmpTorApi()

@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class)
private class KmpTorApi: TorApi() {

    override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val libtor = extractLibTor()
        val result = memScoped {
            kmp_tor_run_main(
                shutdown_delay_millis = 100,
                libtor = libtor.path,
                argc = args.size,
                argv = args.toCStringArray(autofreeScope = this),
            )
        }

        when (result) {
            -10 -> "kmp_tor_run_main invalid arguments"
            -11 -> "kmp_tor_run_main configuration failure"
            -12 -> "Failed to load ${libtor.name}"
            -13 -> "tor_main_configuration_new failure"
            -14 -> "tor_main_configuration_set_command_line failure"
            else -> null
        }?.let { throw IllegalStateException(it) }

        return result
    }

    private fun extractLibTor(): File {
        return RESOURCE_CONFIG_LIB_TOR
            .extractTo(TEMP_DIR, onlyIfDoesNotExist = true)
            .getValue(ALIAS_LIB_TOR)
    }

    init { extractLibTor() }

    private companion object {

        private val TEMP_DIR: File by lazy {
            @OptIn(ExperimentalUuidApi::class)
            val tempDir = SysTempDir.resolve("kmp-tor_${Uuid.random()}")

            tempDir.deleteOnExit()
            RESOURCE_CONFIG_LIB_TOR.resources.forEach { resource ->
                tempDir.resolve(resource.platform.fsFileName).deleteOnExit()
            }

            tempDir
        }
    }
}
