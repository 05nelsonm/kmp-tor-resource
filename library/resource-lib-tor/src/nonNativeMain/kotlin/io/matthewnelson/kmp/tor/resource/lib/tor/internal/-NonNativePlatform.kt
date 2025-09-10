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

package io.matthewnelson.kmp.tor.resource.lib.tor.internal

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.*

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline val OSHost.resourceNameTor: String get() = when (this) {
    is OSHost.Windows -> "tor.exe.gz"
    else -> "tor.gz"
}

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline val OSHost.resourceNameLibTor: String get() = when (this) {
    is OSHost.Windows -> "tor.dll.gz"
    is OSHost.MacOS -> "libtor.dylib.gz"
    else -> "libtor.so.gz"
}

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline val OSHost.resourceNameLibTorJni: String get() = when (this) {
    is OSHost.Windows -> "torjni.dll.gz"
    is OSHost.MacOS -> "libtorjni.dylib.gz"
    else -> "libtorjni.so.gz"
}

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline fun Resource.Config.Builder.configureExecutableResource(
    alias: String,
    crossinline block: PlatformResource.Builder.(host: OSHost, arch: OSArch) -> Unit,
) {
    val host = OSInfo.INSTANCE.osHost
    if (host is OSHost.Unknown) {
        error("Unknown host[$host]")
        return
    }

    val arch = OSInfo.INSTANCE.osArch
    if (!arch.isSupportedBy(host)) {
        error("Unsupported architecture[$arch] for host[$host]")
        return
    }

    resource(alias) {
        mode("554")
        platform { block(host, arch) }
    }
}

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE", "REDUNDANT_ELSE_IN_WHEN")
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
        OSArch.Riscv64,
        OSArch.X86,
        OSArch.X86_64,
    )
    is OSHost.Linux.Musl -> setOf(
        OSArch.Aarch64,
        OSArch.X86,
        OSArch.X86_64,
    )
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
