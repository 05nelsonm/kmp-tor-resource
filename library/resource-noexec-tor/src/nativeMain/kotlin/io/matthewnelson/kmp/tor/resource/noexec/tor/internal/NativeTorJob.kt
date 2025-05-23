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
package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlinx.cinterop.toKString
import kotlin.concurrent.Volatile
import kotlin.native.concurrent.ObsoleteWorkersApi
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker

@OptIn(ObsoleteWorkersApi::class)
internal fun Worker.executeTorJob(libTor: String, args: Array<String>): TorJob {
    val job = NativeTorJob(libTor, args)
    execute(TransferMode.SAFE, { job }) { it.execute() }
    return job
}

private class NativeTorJob(private val libTor: String, private val args: Array<String>): TorJob {

    @Volatile
    private var error: String? = null

    override fun checkError(): String? = error

    fun execute() {
        @OptIn(ExperimentalForeignApi::class)
        error = memScoped {
            kmp_tor_run_blocking(
                lib_tor = libTor,
                argc = args.size,
                argv = args.toCStringArray(autofreeScope = this)
            )?.toKString()
        }
    }
}
