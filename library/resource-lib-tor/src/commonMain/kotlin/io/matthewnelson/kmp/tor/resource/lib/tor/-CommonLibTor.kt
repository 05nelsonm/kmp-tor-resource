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
package io.matthewnelson.kmp.tor.resource.lib.tor

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSArch
import io.matthewnelson.kmp.tor.common.core.OSHost
import kotlin.jvm.JvmName

@InternalKmpTorApi
public const val ALIAS_TOR: String = "tor"

@InternalKmpTorApi
@get:JvmName("torFileNameFor")
public val OSHost.torFileName: String get() = when (this) {
    is OSHost.Windows -> "tor.exe.gz"
    else -> "tor.gz"
}

@InternalKmpTorApi
public fun OSArch.isSupportedBy(osHost: OSHost): Boolean = when (osHost) {
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
