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

import android.annotation.SuppressLint
import android.os.Build
import android.system.Os
import io.matthewnelson.kmp.file.ANDROID
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.lib.tor.tryConfigureTestTorResources

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureTorResources() {
    if (ANDROID.SDK_INT != null) {
        @SuppressLint("ObsoleteSdkInt")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            error("Android API 21+ is required")
            return
        }

        var isNotExtracted = false
        var isMissing = false

        // Is Android Runtime.
        //
        // Binaries are extracted on application install to the
        // nativeLibraryDir. This is required as Android does not
        // allow execution from data directory on API 28+ (cannot
        // download executables and run them).
        arrayOf(ENV_KEY_LIBTOR, ENV_KEY_LIBTOREXEC).forEach { key ->
            val lib = Os.getenv(key)
            if (lib.isNullOrBlank()) {
                error("LIB[${key.envKeyLibName()}] not found")
                isMissing = true
                return@forEach
            }

            // Check file exist, indicative that they have been
            // extracted to the nativeLibraryDir
            if (lib.toFile().exists()) return@forEach
            error("LIB[${key.envKeyLibName()}].exists() != true")
            isNotExtracted = true
        }

        if (isMissing) {
            error("""
                A library was missing. Please ensure you have:
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

        return
    }

    // Not Android runtime. Try using unit test dependencies.
    tryConfigureTestTorResources(
        aliasLibTor = ALIAS_LIBTOR,
        aliasLibTorJni = null,
        aliasTor = ALIAS_TOREXEC,
    )
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun MutableMap<String, String>.configureProcessEnvironment(resourceDir: File) {
    if (ANDROID.SDK_INT != null) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) return
        // API 23-

        // Should never be null here b/c extraction would have failed
        // if the configureEnv callback is being invoked.
        val dir = Os.getenv(ENV_KEY_LIBTOREXEC)?.toFile()?.parentFile ?: return
        setLD_LIBRARY_PATH(dir)
        return
    }

    // If we're configuring the process environment, extraction was
    // successful (so utilizing android unit test dependency).
    if (OSInfo.INSTANCE.osHost !is OSHost.Linux.Android) return

    // This would only be the case if someone is compiling an Android
    // app ON an Android device, but hey...
    setLD_LIBRARY_PATH(resourceDir)
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibTorExec(): Map<String, File> {
    if (contains(ALIAS_TOREXEC)) return this

    if (ANDROID.SDK_INT == null) return this

    // Error from ResourceConfig would hit before this function has
    // a chance to be called, but...
    val lib = try {
        Os.getenv(ENV_KEY_LIBTOREXEC)
            ?.toFile()
            ?: throw IllegalStateException("${ENV_KEY_LIBTOREXEC.envKeyLibName()} not found")
    } catch (t: Throwable) {
        if (t is IllegalStateException) throw t
        throw IllegalStateException("${ENV_KEY_LIBTOREXEC.envKeyLibName()} not found")
    }

    return toMutableMap().apply { put(ALIAS_TOREXEC, lib) }
}
