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
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import java.util.UUID

internal const val ALIAS_LIB_TOR_JNI: String = "libtorjni"

@JvmSynthetic
@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): TorApi = KmpTorApi()

@OptIn(InternalKmpTorApi::class)
private class KmpTorApi: TorApi() {

    private external fun kmpTorRunMain(shutdownDelayMillis: Int, libtor: String, args: Array<String>): Int

    override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val libtor = extractLibTor()
        val result = kmpTorRunMain(
            shutdownDelayMillis = 100,
            libtor = libtor.path,
            args = args,
        )

        when (result) {
            -9  -> "JNI: Failed to convert args to C"
            -10 -> "JNI: kmp_tor_run_main invalid arguments"
            -11 -> "JNI: kmp_tor_run_main configuration failure"
            -12 -> "JNI: Failed to load ${libtor.name}"
            -13 -> "JNI: tor_main_configuration_new failure"
            -14 -> "JNI: tor_main_configuration_set_command_line failure"
            else -> null
        }?.let { throw IllegalStateException(it) }

        return result
    }

    private fun extractLibTor(loadTorJni: Boolean = false): File {
        val tempDir = TEMP_DIR

        return try {
            if (tempDir == null) {
                // Android Runtime >> libtorjni.so & libtor.so
                if (loadTorJni) {
                    System.loadLibrary("torjni")
                }
                "libtor.so".toFile()
            } else {
                val map: Map<String, File> = RESOURCE_CONFIG_LIB_TOR
                    .extractTo(tempDir, onlyIfDoesNotExist = true)

                if (loadTorJni) {
                    System.load(map.getValue(ALIAS_LIB_TOR_JNI).path)
                }
                map.getValue(ALIAS_LIB_TOR)
            }
        } catch (t: Throwable) {
            if (t is IOException) throw t
            if (t is IllegalStateException) throw t

            // UnsatisfiedLinkError
            throw IllegalStateException("Failed to load torjni", t)
        }
    }

    init { extractLibTor(loadTorJni = true) }

    private companion object {

        private val TEMP_DIR: File? by lazy {
            if (OSInfo.INSTANCE.isAndroidRuntime()) return@lazy null

            val tempDir = SysTempDir.resolve("kmp-tor_${UUID.randomUUID()}")

            tempDir.deleteOnExit()
            RESOURCE_CONFIG_LIB_TOR.resources.forEach { resource ->
                tempDir.resolve(resource.platform.fsFileName).deleteOnExit()
            }

            tempDir
        }
    }
}
