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

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.*
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlinx.cinterop.toKString
import kotlin.concurrent.AtomicReference
import kotlin.concurrent.Volatile
import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker

// nonAppleFramework
@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class, ObsoleteWorkersApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(
    private val resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    private val worker = AtomicReference<Worker?>(null)

    public actual final override fun state(): State = State.entries.elementAt(kmp_tor_state())

    public actual final override fun terminateAndAwaitResult(): Int {
        val result = kmp_tor_terminate_and_await_result()
        worker.getAndSet(null)?.requestTermination(false)?.result
        return result
    }

    @Throws(IllegalStateException::class)
    protected actual fun runInThread(libTor: String, args: Array<String>): TorJob {
        check(worker.value == null) { "Worker != null. terminateAndAwaitResult is required" }

        val w = Worker.start(name = "tor_run_main")
        val job = w.executeTorJob(libTor, args)
        worker.value = w
        return job
    }

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File = extractLibTor(isInit = false)

    private fun extractLibTor(isInit: Boolean): File {
        if (IS_ANDROID_NATIVE) return "libtor.so".toFile()

        return RESOURCE_CONFIG_LIB_TOR
            .extractTo(resourceDir, onlyIfDoesNotExist = !isInit)
            .getValue(ALIAS_LIB_TOR)
    }

    init { extractLibTor(isInit = true) }
}
