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
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ALIAS_LIBTOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorThread
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.executeTorThreadJob
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.findLibs
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.kmp_tor_state
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.kmp_tor_stop_stage1_interrupt_and_await_result
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.kmp_tor_stop_stage2_post_thread_exit_cleanup
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.concurrent.Volatile
import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.Worker

// nonAppleFramework
@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class, ObsoleteWorkersApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(
    private val resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    @Volatile
    private var threadNo = 0L

    protected actual fun startTorThread(libTor: String, args: Array<String>): Pair<TorThread, TorThread.Job> {
        val w = Worker.start(name = "tor_run_main-${++threadNo}")
        val job = w.executeTorThreadJob(libTor, args)
        return TorThread { w.requestTermination(processScheduledJobs = false).result } to job
    }

    protected actual fun kmpTorState(): Int = kmp_tor_state()
    protected actual fun kmpTorStopStage1InterruptAndAwaitResult(): Int = kmp_tor_stop_stage1_interrupt_and_await_result()
    protected actual fun kmpTorStopStage2PostThreadExitCleanup(): Int = kmp_tor_stop_stage2_post_thread_exit_cleanup()

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File = extractLibTor(isInit = false)

    private fun extractLibTor(isInit: Boolean): File = RESOURCE_CONFIG_LIB_TOR
        .extractTo(resourceDir, onlyIfDoesNotExist = !isInit)
        .findLibs()
        .getValue(ALIAS_LIBTOR)

    init { extractLibTor(isInit = true) }
}
