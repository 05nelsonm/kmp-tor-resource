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

internal expect fun TorApi2.registerOnShutdownHook(handle: () -> TorApi2.Handle?)

internal abstract class TorApi2: TorApi() {

    @Volatile
    private var _handle: SynchronizedHandle? = null
    @Volatile
    private var _isStarting: Boolean = false
    @OptIn(InternalKmpTorApi::class)
    private val lock = SynchronizedObject()

    @get:JvmName("state")
    public val state: State get() {
        if (_isStarting) {
            return State.STARTING
        }

        _handle?.let { return it.handleState() }

        return State.OFF
    }

    @get:JvmName("isOff")
    public val isOff: Boolean get() = state == State.OFF

    @get:JvmName("isActive")
    public val isActive: Boolean get() = when (state) {
        State.STARTING, State.STARTED -> true
        else -> false
    }

    @get:JvmName("isStopped")
    public val isStopped: Boolean get() = state == State.STOPPED

    public enum class State {
        OFF,
        STARTING,
        STARTED,
        STOPPED,
    }

    public interface Handle {

        public fun handleState(): State

        @Throws(IllegalStateException::class)
        public fun terminateAndAwaitResult(): Int
    }

    @Throws(IllegalStateException::class, IOException::class)
    public fun torRunMain2(configuration: List<String>): Handle {
        check(state == State.OFF) { "tor is running" }

        @OptIn(InternalKmpTorApi::class)
        val args = synchronized(lock) {
            if (state != State.OFF) return@synchronized null
            Array(configuration.size + 1) { i ->
                if (i == 0) "tor" else configuration[i - 1]
            }.also{ _isStarting = true }
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

    private inner class SynchronizedHandle(delegate: Handle): Handle {

        @Volatile
        private var _delegate: Handle? = delegate
        @Volatile
        private var _result: Int? = null

        public override fun handleState(): State {
            if (_result != null) {
                return State.OFF
            }

            _delegate?.let { return it.handleState() }

            return State.STOPPED
        }

        @Throws(IllegalStateException::class)
        public override fun terminateAndAwaitResult(): Int {
            _result?.let { return it }

            @OptIn(InternalKmpTorApi::class)
            val delegate = synchronized(lock) {
                val d = _delegate
                _delegate = null
                d
            }

            if (delegate == null) {
                _result?.let { return it }
                throw IllegalStateException("terminateAndAwaitResult has already been invoked. Waiting for result")
            }

            val result = try {
                delegate.terminateAndAwaitResult().also { _result = it }
            } finally {
                _handle = null
            }

            return result
        }
    }

    public final override fun toString(): String = "TorApi[state=$state, handle=$_handle]"

    final override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val temp = args.toMutableList()
        temp.removeFirstOrNull()
        return torRunMain2(temp).terminateAndAwaitResult()
    }

    init { registerOnShutdownHook { _handle } }
}
