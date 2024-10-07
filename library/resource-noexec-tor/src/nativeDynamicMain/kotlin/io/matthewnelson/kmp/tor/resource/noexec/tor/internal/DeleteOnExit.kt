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
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import platform.posix.atexit

@OptIn(InternalKmpTorApi::class)
private val LOCK = SynchronizedObject()
private val FILES = ArrayList<File>(4)

// Deletes registered file on program exit in reverse order
internal fun File.deleteOnExit() {
    INIT

    @OptIn(InternalKmpTorApi::class)
    synchronized(LOCK) {
        if (FILES.contains(this)) {
            return@synchronized
        }

        FILES.add(this)
    }
}

@OptIn(ExperimentalForeignApi::class)
internal expect fun installAbnormalExitSignalHandler(execute: CPointer<CFunction<() -> Unit>>)

@OptIn(ExperimentalForeignApi::class)
private val INIT by lazy {
    val cFunction = staticCFunction(::execute)
    atexit(cFunction)
    installAbnormalExitSignalHandler(cFunction)
}

private fun execute() {
    @OptIn(InternalKmpTorApi::class)
    synchronized(LOCK) {
        while (FILES.isNotEmpty()) {
            FILES.removeLast().delete()
        }
    }
}
