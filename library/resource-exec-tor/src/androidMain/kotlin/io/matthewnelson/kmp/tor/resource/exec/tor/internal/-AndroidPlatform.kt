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
@file:Suppress("KotlinRedundantDiagnosticSuppress")

package io.matthewnelson.kmp.tor.resource.exec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.common.lib.locator.KmpTorLibLocator
import io.matthewnelson.kmp.tor.resource.lib.tor.configureJavaTorResource

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureTorResource() {
    if (OSInfo.INSTANCE.isAndroidRuntime()) {
        // Is Android Runtime.
        //
        // Binaries are extracted on application install to the
        // nativeLibraryDir. This is required as Android does not
        // allow execution from data directory on API 28+ (cannot
        // download executables and run them).
        if (KmpTorLibLocator.find("libtor.so") != null) {
            // Available. Will be configured when extractTo is called.
            return
        }

        if (KmpTorLibLocator.isInitialized()) {
            error("""
                Failed to find 'libtor.so' within nativeLibraryDir.
    
                Ensure the following are set correctly:
                build.gradle(.kts):  'android.packaging.jniLibs.useLegacyPackaging' is set to 'true'
                AndroidManifest.xml: 'android:extractNativeLibs' is set to 'true'
                gradle.properties:   'android.bundle.enableUncompressedNativeLibs' is set to 'false'
            """.trimIndent())
        } else {
            // Startup initializer did not initialize...
            error(KmpTorLibLocator.errorMsg())
        }

        return
    }

    // Not Android runtime. Try using unit test dependency.
    configureJavaTorResource()
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibTor(): Map<String, File> {
    if (contains(ALIAS_TOR)) return this

    val lib = KmpTorLibLocator.require("libtor.so")
    return toMutableMap().apply { put(ALIAS_TOR, lib) }
}
