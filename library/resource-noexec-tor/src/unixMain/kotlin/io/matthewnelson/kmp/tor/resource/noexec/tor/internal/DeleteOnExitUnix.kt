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
@file:Suppress("KotlinRedundantDiagnosticSuppress")

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.reinterpret
import platform.posix.NULL
import platform.posix.SIGABRT
import platform.posix.SIGALRM
import platform.posix.SIGBUS
import platform.posix.SIGFPE
import platform.posix.SIGHUP
import platform.posix.SIGINT
import platform.posix.SIGTERM
import platform.posix.SIGQUIT
import platform.posix.sigaction

@OptIn(ExperimentalForeignApi::class)
internal actual fun installUnixSignalHandlerOrNull(
    handler: CPointer<CFunction<(sig: Int) -> Unit>>,
): Unit? {
    val sa = cValue<sigaction> {
        sa_flags = 0
        configure(handler)
    }

    arrayOf(SIGABRT, SIGALRM, SIGBUS, SIGFPE, SIGHUP, SIGINT, SIGTERM, SIGQUIT).forEach { signal ->
        sigaction(signal, sa, NULL?.reinterpret())
    }

    return Unit
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(ExperimentalForeignApi::class)
internal expect inline fun sigaction.configure(handler: CPointer<CFunction<(sig: Int) -> Unit>>)
