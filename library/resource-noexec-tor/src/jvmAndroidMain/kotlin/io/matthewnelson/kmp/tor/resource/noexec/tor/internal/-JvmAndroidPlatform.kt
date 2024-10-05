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

    // TODO: tor_main JNI implementation. Issue #58.

    @Throws(IllegalArgumentException::class, IllegalStateException::class, IOException::class)
    override fun torMainProtected(args: Array<String>) {
        TODO("Not yet implemented")
    }

    init {
        try {
            if (OSInfo.INSTANCE.isAndroidRuntime()) {
                // libtor.so
                System.loadLibrary("tor")
            } else {
                val tempDir = SysTempDir.resolve("kmp-tor_${UUID.randomUUID()}")

                try {
                    val libTor: File = RESOURCE_CONFIG_LIB_TOR
                        .extractTo(tempDir, onlyIfDoesNotExist = false)
                        .getValue(ALIAS_LIB_TOR)

                    System.load(libTor.absolutePath)

                    tempDir.deleteOnExit()
                    libTor.deleteOnExit()
                } catch (t: Throwable) {
                    tempDir.deleteRecursively()
                    throw t
                }
            }
        } catch (t: Throwable) {
            throw IllegalStateException("Failed to dynamically load tor library", t)
        }
    }
}
