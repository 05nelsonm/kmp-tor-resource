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

import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.SynchronizedObject
import io.matthewnelson.kmp.tor.common.core.synchronized
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmName

internal abstract class TorApi2: TorApi() {

    @Volatile
    private var _handle: SynchronizedHandle? = null
    @Volatile
    private var _isStarting: Boolean = false
    @OptIn(InternalKmpTorApi::class)
    private val lock = SynchronizedObject()

    @get:JvmName("isRunning2")
    public val isRunning2: Boolean get() = _isStarting || _handle != null

    public fun interface Handle {

        @Throws(IllegalStateException::class)
        public fun terminateAndAwaitResult(): Int
    }

    @Throws(IllegalStateException::class, IOException::class)
    public fun torRunMain2(configuration: List<String>): Handle {
        check(!isRunning2) { "tor is running" }

        @OptIn(InternalKmpTorApi::class)
        val args = synchronized(lock) {
            if (isRunning2) return@synchronized null

            var assertNext: Pair<String, String>? = null
            val array = Array(configuration.size + 1) { i ->
                val arg = if (i == 0) "tor" else configuration[i - 1]

                assertNext?.let { (option, expected) ->
                    check(arg == expected) {
                        "Option[$option] invalid argument[$arg], expected[$expected]"
                    }
                    assertNext = null
                }

                if (arg.contains("DisableSignalHandlers")) {
                    assertNext = arg to "0"
                }

                arg
            }
            _isStarting = true
            array
        }

        check(args != null) { "tor is running" }

        val handle = try {
            torRunMainProtected(args).let { handle ->
                SynchronizedHandle(handle).also { _handle = it }
            }
        } finally {
            _isStarting = false
        }

        return handle
    }

    @Throws(IllegalStateException::class, IOException::class)
    protected abstract fun torRunMainProtected(args: Array<String>): Handle

    private inner class SynchronizedHandle(private val delegate: Handle): Handle {

        @Volatile
        private var _result: Int? = null
        @Volatile
        private var _isTerminating: Boolean = false

        @Throws(IllegalStateException::class)
        public override fun terminateAndAwaitResult(): Int {
            _result?.let { return it }

            @OptIn(InternalKmpTorApi::class)
            val terminate = synchronized(lock) {
                _result?.let { return it }
                if (_isTerminating) return@synchronized false
                _isTerminating = true
                true
            }

            if (!terminate) {
                throw IllegalStateException("terminateAndAwaitResult has already been invoked. Waiting for result")
            }

            val result = try {
                delegate.terminateAndAwaitResult().also { _result = it }
            } finally {
                _handle = null
                _isTerminating = false
            }

            return result
        }
    }

    public final override fun toString(): String = "TorApi[isRunning=$isRunning2, handle=$_handle]"

    final override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val temp = args.toMutableList()
        temp.removeFirstOrNull()
        return torRunMain2(temp).terminateAndAwaitResult()
    }
}
