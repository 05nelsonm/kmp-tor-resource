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

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.SynchronizedObject
import io.matthewnelson.kmp.tor.common.core.synchronized
import kotlin.concurrent.Volatile

internal expect sealed class AbstractDeleteOnExit protected constructor() {

    protected abstract fun execute()

    protected val initialize: Unit
}

@OptIn(InternalKmpTorApi::class)
internal data object DeleteOnExit: AbstractDeleteOnExit() {

    @Volatile
    private var _hasExecuted: Boolean = false
    private val lock = SynchronizedObject()
    private val files = ArrayDeque<File>(2)

    fun add(file: File) {
        synchronized(lock) {
            initialize

            if (_hasExecuted) {
                file.delete()
                return@synchronized
            }

            if (files.contains(file)) {
                return@synchronized
            }

            files.add(file)
        }
    }

    override fun execute() {
        val files = synchronized(lock) {
            if (_hasExecuted) return@synchronized null
            _hasExecuted = true
            files
        } ?: return

        while (files.isNotEmpty()) {
            files.removeLast().delete()
        }
    }
}
