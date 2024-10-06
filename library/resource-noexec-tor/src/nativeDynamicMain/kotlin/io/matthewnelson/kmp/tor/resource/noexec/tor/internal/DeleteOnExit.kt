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
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.SynchronizedObject
import io.matthewnelson.kmp.tor.common.core.synchronized
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.posix.atexit
import kotlin.concurrent.Volatile

@Volatile
private var HAS_EXECUTED: Boolean = false
@OptIn(InternalKmpTorApi::class)
private val LOCK = SynchronizedObject()
private val FILES = ArrayDeque<File>(2)

// Deletes registered file on program exit in reverse order
internal fun File.deleteOnExit() {
    INIT

    @OptIn(InternalKmpTorApi::class)
    synchronized(LOCK) {
        if (HAS_EXECUTED) {
            delete()
            return@synchronized
        }

        if (FILES.contains(this)) {
            return@synchronized
        }

        FILES.add(this)
    }
}

private val INIT by lazy {
    @OptIn(ExperimentalForeignApi::class)
    atexit(staticCFunction(::execute))

    // TODO: Install signal handlers
    //  https://github.com/JakeWharton/finalization-hook/blob/trunk/src/posixMain/kotlin/com/jakewharton/finalization/hook.kt
}

private fun execute() {
    @OptIn(InternalKmpTorApi::class)
    val files = synchronized(LOCK) {
        if (HAS_EXECUTED) return@synchronized null
        HAS_EXECUTED = true
        FILES
    } ?: return

    while (files.isNotEmpty()) {
        files.removeLast().delete()
    }
}
