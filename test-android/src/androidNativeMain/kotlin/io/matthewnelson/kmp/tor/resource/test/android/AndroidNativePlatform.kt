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
@file:Suppress("FunctionName", "SpellCheckingInspection", "UNUSED")

package io.matthewnelson.kmp.tor.resource.test.android

import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.resource.noexec.tor.ResourceLoaderTorNoExec
import kotlinx.cinterop.ExperimentalForeignApi
import platform.android.jint
import kotlin.experimental.ExperimentalNativeApi

private object BINDER: ResourceLoader.RuntimeBinder

private val LOADER: ResourceLoader.Tor.NoExec by lazy {
    ResourceLoaderTorNoExec.getOrCreate(SysTempDir.resolve("test_jni")) as ResourceLoader.Tor.NoExec
}

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
@CName("Java_io_matthewnelson_kmp_tor_resource_test_android_AndroidNoExecJniTest_executeNative")
public fun Java_io_matthewnelson_kmp_tor_resource_test_android_AndroidNoExecJniTest_executeNative(): jint = try {
    LOADER.withApi(BINDER) {

        try {
            torRunMain(listOf("--version"))
        } catch (t: Throwable) {
            t.printStackTrace()
            return@withApi -10
        }

        println("TorApi.State." + state().name)

        terminateAndAwaitResult()
    }
} catch (t: Throwable) {
    t.printStackTrace()
    -20
}

@OptIn(ExperimentalForeignApi::class, ExperimentalNativeApi::class)
@CName("Java_io_matthewnelson_kmp_tor_resource_test_android_AndroidNoExecJniTest_checkLoaded")
public fun Java_io_matthewnelson_kmp_tor_resource_test_android_AndroidNoExecJniTest_checkLoaded(): jint {
    return 0
}
