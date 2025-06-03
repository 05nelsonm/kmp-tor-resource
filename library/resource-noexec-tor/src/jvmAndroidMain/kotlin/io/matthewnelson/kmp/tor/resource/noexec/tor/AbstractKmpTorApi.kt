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
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIBTOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorThread
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.findLibs
import kotlin.Throws
import kotlin.concurrent.Volatile

// jvmAndroid
@OptIn(InternalKmpTorApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(
    private val resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    private external fun kmpTorRunBlocking(libTor: CharArray, args: Array<CharArray>): String?
    protected actual external fun kmpTorState(): Int
    protected actual external fun kmpTorStopStage1InterruptAndAwaitResult(): Int
    protected actual external fun kmpTorStopStage2PostThreadExitCleanup(): Int

    protected actual fun startTorThread(
        libTor: String,
        args: Array<String>,
        threadName: String,
    ): Pair<TorThread, TorThread.Job> {
        val job = TorThreadJob(libTor, args)
        val t = Thread(job)
        t.name = threadName
        t.isDaemon = true
        t.priority = Thread.MAX_PRIORITY
        t.start()
        return TorThread { t.join() } to job
    }

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File = extractLibTor(isInit = false)

    @Throws(IllegalStateException::class, IOException::class)
    private fun extractLibTor(isInit: Boolean): File = try {
        val libs = RESOURCE_CONFIG_LIB_TOR
            .extractTo(resourceDir, onlyIfDoesNotExist = !isInit)
            .findLibs()

        if (isInit) {
            @Suppress("UnsafeDynamicallyLoadedCode")
            System.load(libs.getValue(ALIAS_LIBTORJNI).path)
        }

        libs.getValue(ALIAS_LIBTOR)
    } catch (t: Throwable) {
        if (t is IOException) throw t
        if (t is IllegalStateException) throw t

        // UnsatisfiedLinkError
        throw IllegalStateException("Failed to load torjni", t)
    }

    private inner class TorThreadJob(libTor: String, args: Array<String>): TorThread.Job, Runnable {

        @Volatile
        private var _args = Array(args.size) { i -> args[i].toCharArray() }
        @Volatile
        private var _libTor = libTor.toCharArray()
        @Volatile
        private var _error: String? = null

        override fun checkError(): String? = _error

        override fun run() {
            val e = kmpTorRunBlocking(_libTor, _args)
            _error = e
        }
    }

    init {
        extractLibTor(isInit = true)

        if (registerShutdownHook) {
            val t = Thread { terminateAndAwaitResult() }
            try {
                Runtime.getRuntime().addShutdownHook(/* hook = */ t)
            } catch (_: Throwable) {}
        }
    }

    internal companion object {
        internal const val ALIAS_LIBTORJNI: String = "libtorjni"
    }
}
