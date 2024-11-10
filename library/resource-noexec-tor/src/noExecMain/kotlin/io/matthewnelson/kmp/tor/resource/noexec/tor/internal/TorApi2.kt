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
import io.matthewnelson.kmp.tor.common.api.TorApi

internal abstract class TorApi2: TorApi() {

    public enum class State {
        OFF,
        STARTING,
        STARTED,
        STOPPED,
    }

    public abstract fun state(): State

    public abstract fun terminateAndAwaitResult(): Int

    @Throws(IllegalStateException::class, IOException::class)
    public fun torRunMain2(configuration: List<String>) {
        val args = Array(configuration.size + 1) { i ->
            if (i == 0) "tor" else configuration[i - 1]
        }

        torRunMain(args)
    }

    @Throws(IllegalStateException::class, IOException::class)
    protected abstract fun torRunMain(args: Array<String>)

    public final override fun toString(): String = "TorApi[state=${state()}]"

    final override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        throw IllegalStateException("TODO")
    }
}
