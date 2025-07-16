/*
 * Copyright (c) 2025 Matthew Nelson
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
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.SynchronizedObject
import io.matthewnelson.kmp.tor.common.core.synchronized
import io.matthewnelson.kmp.tor.common.core.synchronizedObject
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlinx.cinterop.toKString

// nonAppleFramework
@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class)
internal actual class KmpTorApi private constructor(private val resourceDir: File): TorApi() {

    private val ctx: CPointer<__kmp_tor_context_t>
    private val lock: SynchronizedObject

    @Throws(IllegalStateException::class, IOException::class)
    actual override fun torRunMain(args: Array<String>) {
        val libTor = synchronized(lock) { extractLibTor(isInit = false) }.path
        val error: String = memScoped {
            val result = __kmp_tor_run_main(
                __ctx = ctx,
                lib_tor = libTor,
                argc = args.size,
                argv = args.toCStringArray(autofreeScope = this),
            )

            result?.toKString()
        } ?: return
        throw IllegalStateException(error)
    }

    actual override fun state(): State = State.entries.elementAt(__kmp_tor_state(ctx))
    actual override fun terminateAndAwaitResult(): Int = __kmp_tor_terminate_and_await_result(ctx)

    @Throws(IllegalStateException::class, IOException::class)
    private fun extractLibTor(isInit: Boolean): File = RESOURCE_CONFIG_LIB_TOR
        .extractTo(resourceDir, onlyIfDoesNotExist = !isInit)
        .findLibs()
        .getValue(ALIAS_LIBTOR)

    init {
        extractLibTor(isInit = true)
        ctx = __kmp_tor_init() ?: throw IllegalStateException("Failed to initialized kmp_tor_context_t")
        lock = synchronizedObject()
    }

    internal actual companion object {

        @Throws(IllegalStateException::class, IOException::class)
        internal actual fun of(
            resourceDir: File,
            registerShutdownHook: Boolean,
        ): KmpTorApi = KmpTorApi(resourceDir)
    }
}
