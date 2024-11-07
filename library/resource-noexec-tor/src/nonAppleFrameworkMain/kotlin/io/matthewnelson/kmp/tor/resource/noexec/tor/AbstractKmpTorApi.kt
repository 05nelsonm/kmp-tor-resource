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

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.*
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.HandleT
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.HandleT.Companion.toHandleTOrNull
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.MINGW_AF_UNIX_TMP_FILE_NAME
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorApi2
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.deleteOnExit
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlin.random.Random
import kotlin.system.getTimeNanos

// nonAppleFramework
@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(): TorApi2() {

    protected actual fun kmpTorRunMain(
        libTor: String,
        args: Array<String>,
    ): HandleT? = memScoped {
        val ptr = kmp_tor_run_main(
            lib_tor = libTor,
            win32_af_unix_path = MINGW_AF_UNIX_PATH?.path,
            argc = args.size,
            argv = args.toCStringArray(autofreeScope = this)
        )

        ptr.toHandleTOrNull()
    }

    protected actual fun kmpTorCheckErrorCode(
        handle: HandleT,
    ): Int = kmp_tor_check_error_code(handle.ptr)

    protected actual fun kmpTorTerminateAndAwaitResult(
        handle: HandleT,
    ): Int = kmp_tor_terminate_and_await_result(handle.ptr)

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File {
        return RESOURCE_CONFIG_LIB_TOR
            .extractTo(TEMP_DIR, onlyIfDoesNotExist = true)
            .getValue(ALIAS_LIB_TOR)
    }

    init { libTor() }

    private companion object {

        private val TEMP_DIR: File by lazy {
            // TODO: Replace with Uuid (Kotlin 2.0.20+)
            @Suppress("DEPRECATION")
            val tempDir = Random(getTimeNanos()).nextBytes(16).encodeToString(Base16).let { suffix ->
                SysTempDir.resolve("kmp-tor_$suffix")
            }

            tempDir.deleteOnExit()
            RESOURCE_CONFIG_LIB_TOR.resources.forEach { resource ->
                if (resource.platform.isGzipped) {
                    tempDir.resolve(resource.platform.fsFileName + ".gz").deleteOnExit()
                }
                tempDir.resolve(resource.platform.fsFileName).deleteOnExit()
            }

            tempDir
        }

        private val MINGW_AF_UNIX_PATH: File? by lazy {
            if (!RESOURCE_CONFIG_LIB_TOR[ALIAS_LIB_TOR].platform.fsFileName.endsWith(".dll")) {
                return@lazy null
            }

            TEMP_DIR.resolve(MINGW_AF_UNIX_TMP_FILE_NAME).also { it.deleteOnExit() }
        }
    }
}
