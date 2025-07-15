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
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi

// jvmAndroid
@OptIn(InternalKmpTorApi::class)
internal actual class KmpTorApi private constructor(
    private val resourceDir: File,
    registerShutdownHook: Boolean,
): TorApi() {

    @Throws(IllegalStateException::class, IOException::class)
    actual override fun torRunMain(args: Array<String>) {
        val cLibTor = synchronized(Companion) { extractLibTor(isInit = false) }.path.toCharArray()
        val cArgs = Array(args.size) { i -> args[i].toCharArray() }
        val errBuf = CharArray(ERR_BUF_LEN)

        val errLen = kmpTorRunMain(cLibTor, cArgs, errBuf)

        // Ensure Java won't GC them until after kmpTorRunMain returns from JNI layer
        cLibTor.size
        cArgs.size

        if (errLen <= 0) return

        val msg = errBuf.concatToString(endIndex = errLen)
        throw IllegalStateException(msg)
    }

    actual override fun state(): State = State.entries.elementAt(kmpTorState())
    actual override fun terminateAndAwaitResult(): Int = kmpTorTerminateAndAwaitResult()

    @Throws(IllegalStateException::class, IOException::class)
    private fun extractLibTor(isInit: Boolean): File = try {
        val libs = RESOURCE_CONFIG_LIB_TOR
            .extractTo(resourceDir, onlyIfDoesNotExist = !isInit)
            .findLibs()

        if (isInit) {
            // This is OK for Android because libtor.so is being found
            // via BaseDexClassLoader.findLibrary which returns absolute
            // paths either located in ApplicationInfo.nativeLibraryDir,
            // or uncompressed and aligned within base.apk (API 23+).
            @Suppress("UnsafeDynamicallyLoadedCode")
            System.load(libs.getValue(ALIAS_LIBTORJNI).path)
        }

        libs.getValue(ALIAS_LIBTOR)
    } catch (t: Throwable) {
        if (t is IOException) throw t
        if (t is IllegalStateException) throw t

        // UnsatisfiedLinkError
        throw IllegalStateException("Failed to load torjni", t)
    }

    init {
        extractLibTor(isInit = true)

        if (registerShutdownHook) {
            val t = Thread { terminateAndAwaitResult() }
            try {
                Runtime.getRuntime().addShutdownHook(/* hook = */ t)
            } catch (_: Throwable) {}
        }
    }

    internal actual companion object {

        // Defined in external/native/kmp_tor-jni.c
        private const val ERR_BUF_LEN = 512

        internal const val ALIAS_LIBTORJNI: String = "libtorjni"

        @JvmSynthetic
        @Throws(IllegalStateException::class, IOException::class)
        internal actual fun of(
            resourceDir: File,
            registerShutdownHook: Boolean,
        ): KmpTorApi = KmpTorApi(resourceDir, registerShutdownHook)

        @JvmStatic
        @Synchronized
        private external fun kmpTorRunMain(libTor: CharArray, args: Array<CharArray>, errBuf: CharArray): Int
        @JvmStatic
        @Synchronized
        private external fun kmpTorState(): Int
        @JvmStatic
        @Synchronized
        private external fun kmpTorTerminateAndAwaitResult(): Int
    }
}
