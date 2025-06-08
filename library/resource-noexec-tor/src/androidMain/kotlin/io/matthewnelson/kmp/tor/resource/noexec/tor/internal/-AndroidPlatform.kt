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

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import android.annotation.SuppressLint
import android.os.Build
import android.system.Os
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.lib.tor.tryConfigureTestTorResources
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.KmpTorApi.Companion.ALIAS_LIBTORJNI
import kotlin.Throws

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureLibTorResources() {
    if (OSInfo.INSTANCE.isAndroidRuntime()) {
        @SuppressLint("ObsoleteSdkInt")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            error("Android API 21+ is required")
            return
        }

        var isMissing = false

        arrayOf(ENV_KEY_LIBTOR, ENV_KEY_LIBTORJNI).forEach { key ->
            if (Os.getenv(key).isNullOrBlank()) {
                error("LIB[${key.envKeyLibName()}] not found")
                isMissing = true
            }
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

        return
    }

    tryConfigureTestTorResources(
        aliasLibTor = ALIAS_LIBTOR,
        aliasLibTorJni = ALIAS_LIBTORJNI,
        aliasTor = null,
    )
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibs(): Map<String, File> {
    if (contains(ALIAS_LIBTOR) && contains(ALIAS_LIBTORJNI)) return this

    @OptIn(InternalKmpTorApi::class)
    if (!OSInfo.INSTANCE.isAndroidRuntime()) return this

    // Error from ResourceConfig would hit before this function has
    // a chance to be called, but...
    val result = toMutableMap()

    arrayOf(
        arrayOf(ALIAS_LIBTOR, ENV_KEY_LIBTOR),
        arrayOf(ALIAS_LIBTORJNI, ENV_KEY_LIBTORJNI),
    ).forEach { array ->
        val alias = array[0]
        if (result.contains(alias)) return@forEach

        val key = array[1]

        val lib = try {
            Os.getenv(key)
                ?.toFile()
                ?: throw IllegalStateException("LIB[${key.envKeyLibName()}] not found")
        } catch (t: Throwable) {
            if (t is IllegalStateException) throw t
            throw IllegalStateException("LIB[${key.envKeyLibName()}] not found")
        }
        result[alias] = lib
    }

    return result
}
