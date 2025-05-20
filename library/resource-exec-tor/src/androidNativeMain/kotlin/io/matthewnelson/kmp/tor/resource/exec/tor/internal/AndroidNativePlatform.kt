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
package io.matthewnelson.kmp.tor.resource.exec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.parentFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.common.lib.locator.KmpTorLibLocator
import platform.posix.__ANDROID_API_M__
import platform.posix.android_get_device_api_level

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureTorResources() {
    val missing = ArrayList<String>(2)

    if (KmpTorLibLocator.find("libtor.so") == null) {
        missing.add("libtor.so")
    }
    if (KmpTorLibLocator.find("libtorexec.so") == null) {
        missing.add("libtorexec.so")
    }

    if (missing.isEmpty()) return

    if (KmpTorLibLocator.isInitialized()) {
        """
            Failed to find $missing within nativeLibraryDir.

            Ensure the following android dependency is present:
            io.matthewnelson.kmp-tor:resource-compilation-exec-tor{-gpl}:{version}

            Ensure the following are set correctly:
            build.gradle(.kts):  'android.packaging.jniLibs.useLegacyPackaging' is set to 'true'
            AndroidManifest.xml: 'android:extractNativeLibs' is set to 'true'
            gradle.properties:   'android.bundle.enableUncompressedNativeLibs' is set to 'false'
        """.trimIndent()
    } else {
        """
            TODO: 
        """.trimIndent()
    }.let { message -> error(message) }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun MutableMap<String, String>.configureProcessEnvironment(resourceDir: File) {
    if (android_get_device_api_level() > __ANDROID_API_M__) return
    // API 23-

    // Should never be null here b/c extraction would have failed
    // if the configureEnv callback is being invoked
    val dir = KmpTorLibLocator.find("libtor.so")?.parentFile ?: return
    setLD_LIBRARY_PATH(dir)
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibTorExec(): Map<String, File> {
    if (contains(ALIAS_TOR)) return this

    val lib = KmpTorLibLocator.require("libtorexec.so")
    return toMutableMap().apply { put(ALIAS_TOR, lib) }
}
