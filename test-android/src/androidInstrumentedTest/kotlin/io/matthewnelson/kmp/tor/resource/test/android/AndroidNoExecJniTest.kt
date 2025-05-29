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
package io.matthewnelson.kmp.tor.resource.test.android

import android.os.Build
import android.system.Os
import dalvik.annotation.optimization.CriticalNative
import dalvik.system.BaseDexClassLoader
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.resource.noexec.tor.ResourceLoaderTorNoExec
import io.matthewnelson.kmp.tor.resource.test.android.internal.IS_USING_MOCK_RESOURCES
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AndroidNoExecJniTest {

    private companion object BINDER: ResourceLoader.RuntimeBinder {

        @JvmStatic
        @CriticalNative
        private external fun executeNative(): Int

        @JvmStatic
        @CriticalNative
        private external fun checkLoaded(): Int

        private val IS_LOADED: Boolean by lazy {
            try {
                System.loadLibrary("testjni")
                check(checkLoaded() == 0) { "checkLoaded was not 0" }
                true
            } catch (t: Throwable) {
                t.printStackTrace()
                false
            }
        }

        private val LOADER: ResourceLoader.Tor.NoExec by lazy {
            val dir = SysTempDir.resolve("android_emulator")
            ResourceLoaderTorNoExec.getOrCreate(dir) as ResourceLoader.Tor.NoExec
        }
    }

    @Test
    fun givenLibTor_whenLegacyPackaging_thenIsUncompressedAndAlignedIfApi23OrAbove() {
        printEnvironment()
        var cl: ClassLoader? = this::class.java.classLoader
        var libTor: File? = null
        while (cl != null) {
            if (cl is BaseDexClassLoader && libTor == null) {
                libTor = cl.findLibrary("tor")?.let { File(it) }
            }
            cl = cl.parent
        }

        assertNotNull(libTor)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // jni legacy packaging not available and should have installed
            // libs to the ApplicationInfo.nativeLibraryDir
            assertTrue(libTor.exists())
            return
        }

        // Check we're not using legacy packaging (i.e. libtor.so is uncompressed in the base.apk zip)
        // This, coupled with other tests ensures that, given all other tests passing, ensures that
        // kmp_tor.c call to dlopen using the uncompressed and aligned libtor.so works properly.
        assertFalse(libTor.exists())
        assertTrue(libTor.path.contains("base.apk!/lib/"))
    }

    @Test
    fun givenNoExecTor_whenUsingAndroidNativeJni_thenLibTorLoadsSuccessfullyEvenWhenLegacyPackagingFalse() {
        assertTrue(IS_LOADED, "[libtestjni.so].IS_LOADED != true")

        if (IS_USING_MOCK_RESOURCES) {
            println("Skipping...")
            return
        }

        printEnvironment()
        val result = executeNative()
        assertEquals(0, result)
    }

    @Test
    fun givenNoExecTor_whenUsingAndroid_thenLibTorLoadsSuccessfullyWhenLegacyPackagingFalse() {
        if (IS_USING_MOCK_RESOURCES) {
            println("Skipping...")
            return
        }

        printEnvironment()
        val result = LOADER.withApi(BINDER) {
            torRunMain(listOf("--version"))
            terminateAndAwaitResult()
        }

        assertEquals(0, result)
    }

    private fun printEnvironment() {
        buildString {
            appendLine("--- Os.environ ---")
            Os.environ()?.forEach { line -> append("    ").appendLine(line) }
            appendLine("---    END     ---")
        }.let { println(it) }
    }
}
