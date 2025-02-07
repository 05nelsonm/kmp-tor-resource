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

import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.kmp_tor_run_main
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.kmp_tor_state
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.kmp_tor_terminate_and_await_result
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCStringArray
import kotlinx.cinterop.toKString
import platform.Foundation.NSBundle

// appleFramework
@OptIn(ExperimentalForeignApi::class)
internal actual sealed class AbstractKmpTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(
    resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    private val bundle: NSBundle

    protected actual fun kmpTorRunMain(
        libTor: String,
        args: Array<String>,
    ): String? = memScoped {
        kmp_tor_run_main(
            lib_tor = libTor,
            argc = args.size,
            argv = args.toCStringArray(autofreeScope = this),
        )?.toKString()
    }

    protected actual fun kmpTorState(): Int = kmp_tor_state()

    protected actual fun kmpTorTerminateAndAwaitResult(): Int = kmp_tor_terminate_and_await_result()

    @Throws(IllegalStateException::class, IOException::class)
    protected actual fun libTor(): File {
        if (bundle.isLoaded()) {
            check(bundle.unload()) { "Failed to unload $bundle" }
        }

        return "$FRAMEWORK/$LIB_NAME".toFile()
    }

    init {
        val path = NSBundle.mainBundle.bundlePath.toFile()
            .resolve("Frameworks")
            .resolve(FRAMEWORK)
            .path

        val bundle = NSBundle.bundleWithPath(path)
        check(bundle != null) {
            "Framework not found: $path"
        }
        check(bundle.bundleIdentifier == FRAMEWORK_ID) {
            "$FRAMEWORK identifier does not match expected[$FRAMEWORK_ID]"
        }
        this.bundle = bundle
    }

    private companion object {
        private const val LIB_NAME: String = "LibTor"
        private const val FRAMEWORK: String = "$LIB_NAME.framework"
        private const val FRAMEWORK_ID: String = "io.matthewnelson.kmp.tor.resource.lib.tor"
    }
}
