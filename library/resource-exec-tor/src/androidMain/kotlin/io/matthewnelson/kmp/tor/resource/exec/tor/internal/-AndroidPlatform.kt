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
import io.matthewnelson.kmp.file.parentPath
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.common.lib.locator.KmpTorLibLocator
import io.matthewnelson.kmp.tor.resource.lib.tor.tryConfigureTestTorResources

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureTorResources() {
    if (OSInfo.INSTANCE.isAndroidRuntime()) {
        val missing = ArrayList<String>(2)

        // Is Android Runtime.
        //
        // Binaries are extracted on application install to the
        // nativeLibraryDir. This is required as Android does not
        // allow execution from data directory on API 28+ (cannot
        // download executables and run them).
        if (KmpTorLibLocator.find("libtor.so") == null) {
            missing.add("libtor.so")
        }
        if (KmpTorLibLocator.find("libtorexec.so") == null) {
            missing.add("libtorexec.so")
        }

        if (missing.isEmpty()) return

        if (KmpTorLibLocator.isInitialized()) {
            error("""
                Failed to find $missing within nativeLibraryDir.
    
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

    // Not Android runtime. Try using unit test dependencies.
    tryConfigureTestTorResources(aliasLibTor = ALIAS_LIB_TOR, aliasTor = ALIAS_TOR)
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibTorExec(): Map<String, File> {
    if (contains(ALIAS_TOR)) return this

    val lib = KmpTorLibLocator.require("libtorexec.so")
    return toMutableMap().apply { put(ALIAS_TOR, lib) }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun MutableMap<String, String>.configureProcessEnvironment(resourceDir: File) {
    if (OSInfo.INSTANCE.isAndroidRuntime()) {
        // Should never be null here b/c extraction would have failed
        // if the configureEnv callback is being invoked.
        val dir = KmpTorLibLocator.find("libtor.so")?.parentPath ?: return
        this["LD_LIBRARY_PATH"] = dir
        return
    }

    when (OSInfo.INSTANCE.osHost) {
        OSHost.MacOS -> this["LD_LIBRARY_PATH"] = resourceDir.path
        else -> Unit
    }
}
