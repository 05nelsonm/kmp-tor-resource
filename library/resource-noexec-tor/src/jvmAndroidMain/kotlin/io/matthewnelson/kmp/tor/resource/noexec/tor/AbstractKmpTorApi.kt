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
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorJob
import kotlin.Throws

// jvmAndroid
@OptIn(InternalKmpTorApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(
    private val resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    private external fun kmpTorRunBlocking(libTor: String, args: Array<String>): String?

    @Throws(IllegalStateException::class)
    protected actual fun kmpTorRunInThread(libTor: String, args: Array<String>): TorJob {
        var error: String? = null
        var l: String? = libTor
        var a: Array<String>? = args
        val t = Thread {
            val e = kmpTorRunBlocking(l!!, a!!)
            error = e
            l = null
            a = null
        }
        t.name = "tor_run_main"
        t.isDaemon = true
        t.priority = Thread.MAX_PRIORITY
        t.start()
        return object : TorJob { override fun checkError(): String? = error }
    }

    protected actual external fun kmpTorState(): Int
    protected actual external fun kmpTorTerminateAndAwaitResult(): Int

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File = extractLibTor(isInit = false)

    @Throws(IllegalStateException::class, IOException::class)
    private fun extractLibTor(isInit: Boolean): File = try {
        if (OSInfo.INSTANCE.isAndroidRuntime()) {
            // libtorjni.so & libtor.so
            if (isInit) {
                System.loadLibrary("torjni")
            }
            "libtor.so".toFile()
        } else {
            val map: Map<String, File> = RESOURCE_CONFIG_LIB_TOR
                .extractTo(resourceDir, onlyIfDoesNotExist = !isInit)

            if (isInit) {
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

    init {
        extractLibTor(isInit = true)

        if (registerShutdownHook) {
            val t = Thread { terminateAndAwaitResult() }
            try {
                Runtime.getRuntime().addShutdownHook(t)
            } catch (_: Throwable) {}
        }
    }

    internal companion object {
        internal const val ALIAS_LIB_TOR_JNI: String = "libtorjni"
    }
}
