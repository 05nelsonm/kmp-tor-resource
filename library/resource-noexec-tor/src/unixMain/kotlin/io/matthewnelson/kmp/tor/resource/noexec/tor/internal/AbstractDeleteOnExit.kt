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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import kotlinx.cinterop.*
import platform.posix.atexit
import kotlin.concurrent.Volatile

internal actual sealed class AbstractDeleteOnExit protected actual constructor() {

    protected actual abstract fun execute()

    protected actual val initialize: Unit by lazy(LazyThreadSafetyMode.NONE) {
        require(EXECUTE == null) {
            "AbstractDeleteOnExit cannot be inherited from more than once, and can only be initialized once."
        }

        EXECUTE = ::execute

        @OptIn(ExperimentalForeignApi::class)
        if (atexit(staticCFunction(::staticExecute)) != 0) {
            // TODO: handle error
            println("Failed to register hook with atexit")
        }

        // TODO: Signal handlers...

        Unit
    }
}

@Volatile
private var EXECUTE: (() -> Unit)? = null

private fun staticExecute() { EXECUTE?.invoke() }
