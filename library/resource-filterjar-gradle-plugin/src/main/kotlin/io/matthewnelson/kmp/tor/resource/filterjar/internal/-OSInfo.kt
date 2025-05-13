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
package io.matthewnelson.kmp.tor.resource.filterjar.internal

import io.matthewnelson.immutable.collections.toImmutableMap
import io.matthewnelson.immutable.collections.toImmutableSet
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSArch
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo

@InternalKmpTorApi
@Throws(IllegalArgumentException::class)
internal fun Map<String, Set<String>>.resolveOSInfo(): Map<OSHost, Set<OSArch>> {
    require(isNotEmpty()) { "targets cannot be empty" }
    val resolved = LinkedHashMap<OSHost, Set<OSArch>>(size, 1.0f)
    forEach { (host, arches) ->
        val osArches = arches.toOSArches()
        require(osArches.isNotEmpty()) { "No architectures were specified for host[$host]" }
        resolved[host.toOSHost()] = osArches
    }
    return resolved.toImmutableMap()
}

@InternalKmpTorApi
@Throws(IllegalArgumentException::class)
internal fun String.toOSHost(): OSHost = when (lowercase()) {
    "current" -> {
        val current = OSInfo.INSTANCE.osHost
        when (current) {
            is OSHost.FreeBSD,
            is OSHost.Linux.Musl,
            is OSHost.Unknown -> throw IllegalArgumentException("host[current] resolution to OSHost[$current] is unsupported")
            else -> {}
        }
        current
    }
//    OSHost.FreeBSD.path -> OSHost.FreeBSD
    OSHost.Linux.Android.path -> OSHost.Linux.Android
    OSHost.Linux.Libc.path -> OSHost.Linux.Libc
//    OSHost.Linux.Musl.path -> OSHost.Linux.Musl
    OSHost.MacOS.path -> OSHost.MacOS
    "windows", OSHost.Windows.path -> OSHost.Windows
    else -> throw IllegalArgumentException(
        """
            Unknown host[$this]. Recognized values are:
                current
                ${OSHost.Linux.Android}
                ${OSHost.Linux.Libc}
                ${OSHost.MacOS}
                ${OSHost.Windows}
                windows
        """.trimIndent()
    )
}

@InternalKmpTorApi
@Throws(IllegalArgumentException::class)
internal fun Set<String>.toOSArches(): Set<OSArch> = flatMapTo(LinkedHashSet(size, 1.0f)) { arch ->
    when (arch.lowercase()) {
        "all" -> listOf(
            OSArch.Aarch64,
            OSArch.Armv7,
            OSArch.Ppc64,
            OSArch.X86,
            OSArch.X86_64,
        )
        "current" -> {
            val current = OSInfo.INSTANCE.osArch
            require(current !is OSArch.Unsupported) { "arch[current] resolution to OSArch[$current] is unsupported" }
            listOf(current)
        }
        OSArch.Aarch64.path -> listOf(OSArch.Aarch64)
        OSArch.Armv7.path -> listOf(OSArch.Armv7)
        OSArch.Ppc64.path -> listOf(OSArch.Ppc64)
        OSArch.X86.path -> listOf(OSArch.X86)
        OSArch.X86_64.path -> listOf(OSArch.X86_64)
        else -> throw IllegalArgumentException(
            """
                Unknown arch[$arch]. Recognized values are:
                    all
                    current
                    ${OSArch.Aarch64}
                    ${OSArch.Armv7}
                    ${OSArch.Ppc64}
                    ${OSArch.X86}
                    ${OSArch.X86_64}
            """.trimIndent()
        )
    }
}.toImmutableSet()
