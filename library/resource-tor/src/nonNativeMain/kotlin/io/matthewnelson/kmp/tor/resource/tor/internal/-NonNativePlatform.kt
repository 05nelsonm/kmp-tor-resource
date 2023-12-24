/*
 * Copyright (c) 2023 Matthew Nelson
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

package io.matthewnelson.kmp.tor.resource.tor.internal

import io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi
import io.matthewnelson.kmp.tor.core.resource.OSArch
import io.matthewnelson.kmp.tor.core.resource.OSHost
import kotlin.jvm.JvmSynthetic

@JvmSynthetic
internal const val PATH_RESOURCE_GEOIP = "/io/matthewnelson/kmp/tor/resource/tor/geoip.gz"
@JvmSynthetic
internal const val PATH_RESOURCE_GEOIP6 = "/io/matthewnelson/kmp/tor/resource/tor/geoip6.gz"

@JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal fun OSHost.toTorResourcePathOrNull(osArch: OSArch): String? {
    if (!osArch.isSupportedBy(this)) return null

    val name = when (this) {
        is OSHost.FreeBSD,
        is OSHost.Linux,
        is OSHost.MacOS -> "tor.gz"
        is OSHost.Windows -> "tor.exe.gz"
        else -> return null
    }

    return "/io/matthewnelson/kmp/tor/resource/tor/native/$this/$osArch/$name"
}

@JvmSynthetic
@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline fun OSArch.isSupportedBy(osHost: OSHost): Boolean = when (osHost) {
    is OSHost.FreeBSD -> emptySet()
    is OSHost.Linux.Android -> setOf(
        OSArch.Aarch64,
        OSArch.Armv7,
        OSArch.X86,
        OSArch.X86_64,
    )
    is OSHost.Linux.Libc -> setOf(
        OSArch.Aarch64,
        OSArch.Armv7,
        OSArch.Ppc64,
        OSArch.X86,
        OSArch.X86_64,
    )
    is OSHost.Linux.Musl -> emptySet()
    is OSHost.MacOS -> setOf(
        OSArch.Aarch64,
        OSArch.X86_64,
    )
    is OSHost.Unknown -> emptySet()
    is OSHost.Windows -> setOf(
        OSArch.X86,
        OSArch.X86_64,
    )
    else -> emptySet()
}.contains(this)
