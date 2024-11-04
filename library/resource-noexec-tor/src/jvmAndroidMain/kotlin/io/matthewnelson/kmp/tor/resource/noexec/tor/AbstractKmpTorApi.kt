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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "LocalVariableName")

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.HandleT
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.HandleT.Companion.toHandleTOrNull
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.Pointer
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorApi2
import java.util.UUID

// jvmAndroid
@OptIn(InternalKmpTorApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(): TorApi2() {

    private external fun kmpTorRunMainJNI(lib_tor: String, argc: Int, args: Array<String>): Pointer?
    private external fun kmpTorCheckErrorCodeJNI(pointer: Pointer): Int
    private external fun kmpTorTerminateAndAwaitResultJNI(pointer: Pointer): Int

    protected actual fun kmpTorRunMain(
        libTor: String,
        args: Array<String>,
    ): HandleT? = kmpTorRunMainJNI(libTor, args.size, args).toHandleTOrNull()

    protected actual fun kmpTorCheckErrorCode(
        handle: HandleT,
    ): Int = kmpTorCheckErrorCodeJNI(handle.ptr)

    protected actual fun kmpTorTerminateAndAwaitResult(
        handle: HandleT,
    ): Int = kmpTorTerminateAndAwaitResultJNI(handle.ptr)

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File = extractLibTor(loadTorJni = false)

    @Throws(IllegalStateException::class, IOException::class)
    private fun extractLibTor(loadTorJni: Boolean): File {
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

    internal companion object {

        internal const val ALIAS_LIB_TOR_JNI: String = "libtorjni"

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
