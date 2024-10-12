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

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import java.util.UUID

@JvmSynthetic
@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): TorApi = KmpTorApi()

@OptIn(InternalKmpTorApi::class)
private class KmpTorApi: TorApi() {

    private external fun kmpTorRunMain(args: Array<String>): Int

    override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val result = kmpTorRunMain(args)

        when (result) {
            -10 -> "JNI: Failed to acquire new tor_main_configuration_t"
            -11 -> "JNI: Failed to determine args array size"
            -12 -> "JNI: Failed to allocate memory for argv array"
            -13 -> "JNI: Failed to populate argv array with arguments"
            -14 -> "JNI: Failed to set tor_main_configuration_t arguments"
            else -> null
        }?.let { throw IllegalStateException(it) }

        return result
    }

    init {
        val tempDir = TEMP_DIR

        try {
            if (tempDir == null) {
                // Android Runtime. libtor.so
                System.loadLibrary("tor")
            } else {
                val libTor: File = RESOURCE_CONFIG_LIB_TOR
                    .extractTo(tempDir, onlyIfDoesNotExist = false)
                    .getValue(ALIAS_LIB_TOR)

                System.load(libTor.absolutePath)
            }
        } catch (t: Throwable) {
            if (t is IOException) throw t
            throw IllegalStateException("Failed to dynamically load tor library", t)
        }
    }

    private companion object {

        private val TEMP_DIR: File? by lazy {
            if (OSInfo.INSTANCE.isAndroidRuntime()) return@lazy null

            val tempDir = SysTempDir.resolve("kmp-tor_${UUID.randomUUID()}")

            val libTor = try {
                tempDir.resolve(RESOURCE_CONFIG_LIB_TOR[ALIAS_LIB_TOR].platform.fsFileName)
            } catch (_: NoSuchElementException) {
                // Is possible with android unit tests if the dependency is not present.
                // Will result in an error upon extraction attempt. Ignore it here.
                null
            }

            tempDir.deleteOnExit()
            libTor?.deleteOnExit()

            tempDir
        }
    }
}
