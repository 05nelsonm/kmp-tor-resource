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
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIBTOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorThread
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.findLibs
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlin.Throws
import kotlin.concurrent.Volatile
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

// jvmAndroid
@OptIn(InternalKmpTorApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(
    private val resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    // Using a fixed thread pool will keep the thread around for the lifetime
    // of the application. This is because if a new Thread that cleans itself
    // up once done is used on each invocation of kmpTorRunBlocking, Java may
    // call pthread_key_delete too soon before native resources are released
    // which could cause tor to abort.
    private val executor = run {
        val threadNo = AtomicLong(0)
        Executors.newFixedThreadPool(/* nThreads = */ 1) { runnable ->
            Thread(runnable).apply {
                name = "tor_run_main-${threadNo.incrementAndGet()}"
                isDaemon = true
                priority = Thread.MAX_PRIORITY
            }
        }
    }

    private external fun kmpTorRunBlocking(libTor: CharArray, args: Array<CharArray>): String?
    protected actual external fun kmpTorState(): Int
    protected actual external fun kmpTorTerminateAndAwaitResult(): Int

    protected actual fun startTorThread(libTor: String, args: Array<String>): Pair<TorThread, TorThread.Job> {
        val job = TorThreadJob(libTor, args)
        val future = executor.submit(job)
        return TorThread { future.get() } to job
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
            ifNeedsSleep {
                try {
                    Thread.sleep(100)
                } catch (t: InterruptedException) {
                    _error = "Thread[${Thread.currentThread().name}] was interrupted"
                    throw t
                }
            }

            val e = kmpTorRunBlocking(_libTor, _args)
            _error = e

            ifNeedsSleep { Thread.sleep(150) }
        }

        // For some reason on Linux the libjvm thread implementation has
        // issues with calling into the JNI layer for a blocking call.
        // A little Thread.sleep before/after seems to help?
        @Suppress("NOTHING_TO_INLINE")
        @OptIn(ExperimentalContracts::class)
        private inline fun ifNeedsSleep(block: () -> Unit) {
            contract {
                callsInPlace(block, InvocationKind.AT_MOST_ONCE)
            }

            when (OSInfo.INSTANCE.osHost) {
                is OSHost.Linux.Libc,
                is OSHost.Linux.Musl -> block()
                else -> return
            }
        }
    }

    init {
        try {
            extractLibTor(isInit = true)
        } catch (t: Throwable) {
            executor.shutdown()
            throw t
        }

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
