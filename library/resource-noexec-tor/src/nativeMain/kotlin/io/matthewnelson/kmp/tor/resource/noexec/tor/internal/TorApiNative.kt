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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "UnnecessaryOptInAnnotation")

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.tor.common.api.TorApi
import kotlinx.cinterop.*

@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): TorApi = KmpTorApi()

@OptIn(ExperimentalForeignApi::class)
internal expect sealed class NativeTorApi
@Throws(IllegalStateException::class, IOException::class)
protected constructor(): TorApi {

    protected fun getProviderVersion(): CPointer<ByteVar>?
    protected fun configurationNew(): CPointer<*>?
    protected fun configurationFree(cfg: CPointer<*>)
    protected fun configurationSetCmdLine(
        cfg: CPointer<*>,
        argc: Int,
        argv: CArrayPointer<CPointerVar<ByteVar>>,
    ): Int
    protected fun runMain(cfg: CPointer<*>): Int
}

@OptIn(ExperimentalForeignApi::class)
private class KmpTorApi
@Throws(IllegalStateException::class, IOException::class)
constructor(): NativeTorApi() {

    override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val cfg = configurationNew()
        if (cfg == null) {
            log.notifyErr("failed to acquire a new configuration")
            return -1
        }

        @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
        var logger: Logger? = log
        val result = memScoped {
            try {
                configurationSetCmdLine(cfg, args.size, args.toCStringArray(autofreeScope = this)).let { result ->
                    if (result == 0) return@let

                    log.notifyErr("failed to set configuration cmd line options")
                    return@memScoped result
                }

                // TODO: Add logging

                runMain(cfg)
            } finally {
                configurationFree(cfg)
                logger = null
            }
        }

        return result
    }
}
