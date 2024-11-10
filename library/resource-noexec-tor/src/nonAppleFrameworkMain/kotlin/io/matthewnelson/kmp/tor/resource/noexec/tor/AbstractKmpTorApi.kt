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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.*
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorApi2
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.deleteOnExit
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlinx.cinterop.toKString
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

// nonAppleFramework
@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(): TorApi2() {

    protected actual fun kmpTorRunMain(
        libTor: String,
        args: Array<String>,
    ): String? = memScoped {
        kmp_tor_run_main(
            lib_tor = libTor,
            argc = args.size,
            argv = args.toCStringArray(autofreeScope = this)
        )?.toKString()
    }

    protected actual fun kmpTorState(): Int = kmp_tor_state()

    protected actual fun kmpTorTerminateAndAwaitResult(): Int = kmp_tor_terminate_and_await_result()

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File {
        return RESOURCE_CONFIG_LIB_TOR
            .extractTo(TEMP_DIR, onlyIfDoesNotExist = true)
            .getValue(ALIAS_LIB_TOR)
    }

    init { libTor() }

    private companion object {

        private val TEMP_DIR: File by lazy {
            @OptIn(ExperimentalUuidApi::class)
            val tempDir = SysTempDir.resolve("kmp-tor_${Uuid.random()}")

            tempDir.deleteOnExit()
            RESOURCE_CONFIG_LIB_TOR.resources.forEach { resource ->
                if (resource.platform.isGzipped) {
                    tempDir.resolve(resource.platform.fsFileName + ".gz").deleteOnExit()
                }
                tempDir.resolve(resource.platform.fsFileName).deleteOnExit()
            }

            tempDir
        }
    }
}
