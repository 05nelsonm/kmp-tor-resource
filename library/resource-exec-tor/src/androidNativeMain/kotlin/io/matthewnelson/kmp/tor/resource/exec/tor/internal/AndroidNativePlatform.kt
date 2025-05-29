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
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.__ANDROID_API_M__
import platform.posix.android_get_device_api_level
import platform.posix.getenv

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureTorResources() {
    var isNotExtracted = false
    var isMissing = false

    // Is Android Runtime.
    //
    // Binaries are extracted on application install to the
    // nativeLibraryDir. This is required as Android does not
    // allow execution from data directory on API 28+ (cannot
    // download executables and run them).
    arrayOf(ENV_KEY_LIBTOR, ENV_KEY_LIBTOREXEC).forEach { key ->
        @OptIn(ExperimentalForeignApi::class)
        val lib = getenv(key)?.toKString()?.toFile()
        if (lib == null) {
            error("LIB[${key.envKeyLibName()}] not found")
            isMissing = true
            return@forEach
        }
        // Check both exist, indicative that they have been
        // extracted to the nativeLibraryDir
        if (lib.exists()) return@forEach
        error("LIB[${key.envKeyLibName()}].exists() != true")
        isNotExtracted = true
    }

    if (isMissing) {
        error("""
            A library was missing. Please ensure you have the
            resource-compilation-exec-tor{-gpl} Android dependency and:
            <meta-data
                android:name='io.matthewnelson.kmp.tor.resource.compilation.lib.tor.KmpTorResourceInitializer'
                android:value='androidx.startup' />
            under InitializationProvider in your AndroidManifest.xml
        """.trimIndent())
    }
    if (isNotExtracted) {
        error("""
            A library was present, but was not extracted to the
            ApplicationInfo.nativeLibraryDir upon application install.

            Ensure the following are set correctly:
            build.gradle(.kts):  'android.packaging.jniLibs.useLegacyPackaging' is set to 'true'
            AndroidManifest.xml: 'android:extractNativeLibs' is set to 'true'
            gradle.properties:   'android.bundle.enableUncompressedNativeLibs' is set to 'false'
        """.trimIndent())
    }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun MutableMap<String, String>.configureProcessEnvironment(resourceDir: File) {
    if (android_get_device_api_level() > __ANDROID_API_M__) return
    // API 23-

    // Should never be null here b/c extraction would have failed
    // if the configureEnv callback is being invoked
    @OptIn(ExperimentalForeignApi::class)
    val dir = getenv(ENV_KEY_LIBTOREXEC)
            ?.toKString()
            ?.toFile()
            ?.parentFile
            ?: return

    setLD_LIBRARY_PATH(dir)
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibTorExec(): Map<String, File> {
    if (contains(ALIAS_TOREXEC)) return this

    val lib = try {
        @OptIn(ExperimentalForeignApi::class)
        getenv(ENV_KEY_LIBTOREXEC)
            ?.toKString()
            ?.toFile()
            ?: throw IllegalStateException("${ENV_KEY_LIBTOREXEC.envKeyLibName()} not found")
    } catch (t: Throwable) {
        if (t is IllegalStateException) throw t
        throw IllegalStateException("${ENV_KEY_LIBTOREXEC.envKeyLibName()} not found")
    }

    return toMutableMap().apply { put(ALIAS_TOREXEC, lib) }
}
