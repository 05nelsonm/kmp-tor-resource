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
package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureLibTorResources() {
    @OptIn(ExperimentalForeignApi::class)
    if (getenv(ENV_KEY_LIBTOR) != null) return

    error("""
        LIB[${ENV_KEY_LIBTOR.envKeyLibName()}] not found. Please ensure you have the
        resource-compilation-lib-tor{-gpl} Android dependency and:
        <meta-data
            android:name='io.matthewnelson.kmp.tor.resource.compilation.lib.tor.LibTorInitializer'
            android:value='androidx.startup' />
        under InitializationProvider in your AndroidManifest.xml
    """.trimIndent())
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibs(): Map<String, File> {
    if (contains(ALIAS_LIBTOR)) return this

    val lib = try {
        @OptIn(ExperimentalForeignApi::class)
        getenv(ENV_KEY_LIBTOR)
            ?.toKString()
            ?.toFile()
            ?: throw IllegalStateException("${ENV_KEY_LIBTOR.envKeyLibName()} not found")
    } catch (t: Throwable) {
        if (t is IllegalStateException) throw t
        throw IllegalStateException("${ENV_KEY_LIBTOR.envKeyLibName()} not found")
    }

    return toMutableMap().apply { put(ALIAS_LIBTOR, lib) }
}
