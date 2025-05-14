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
package io.matthewnelson.kmp.tor.resource.filterjar

import io.matthewnelson.filterjar.*
import io.matthewnelson.immutable.collections.immutableSetOf
import io.matthewnelson.immutable.collections.toImmutableSet
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSArch
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.resource.filterjar.internal.resolveOSInfo
import org.gradle.api.provider.Property

/**
 * An extension that utilizes [FilterJarExtension] under the hood to configure `kmp-tor-resource`
 * filters for dependency artifact minification by filtering out tor compilations that will not be
 * utilized.
 *
 * @see [KmpTorResourceFilterJarPlugin]
 * */
@FilterJarDsl
public abstract class KmpTorResourceFilterJarExtension internal constructor(
    delegate: FilterJarExtension,
): FilterJarApi by delegate {

    public companion object {
        public const val NAME: String = "kmpTorResourceFilterJar"
    }

    private val configurations = LinkedHashMap<String, Set<String>>(1, 1.0f)

    /**
     * See [FilterJarExtension.logging]
     * */
    @JvmField
    public val logging: Property<Boolean> = delegate.logging

    /**
     * Creates [FilterJarConfig] keep filters for provided os hosts and architectures
     *
     * Supported [host] values:
     *  - `current`
     *      - i.e. keep compilations of tor for the current host
     *  - `linux-android`
     *  - `linux-libc`
     *  - `macos`
     *  - `mingw`
     *  - `windows`
     *      - An accepted alias of `mingw`
     *
     * Supported [arch] values:
     *  - `all`
     *      - i.e. keep compilations of tor for all architectures of the specified host
     *  - `current`
     *      - i.e. keep compilations of tor for the current host's architecture of the specified host
     *  - `aarch64`
     *  - `armv7`
     *  - `ppc64`
     *  - `x86`
     *  - `x86_64`
     *
     * e.g.
     *
     *     // Keep only the tor compilation for the current host and architecture
     *     keepTorCompilation(host = "current", arch = "current")
     *
     *     // Keep tor compilations of all architectures for the current host
     *     keepTorCompilation(host = "current", arch = "all")
     *
     * @throws [IllegalArgumentException] if [host] or [arch] are unsupported values
     * */
    @FilterJarDsl
    public fun keepTorCompilation(host: String, arch: String) {
        keepTorCompilation(host, immutableSetOf(arch))
    }

    /**
     * Creates [FilterJarConfig] keep filters for provided os hosts and architectures
     *
     * Supported [host] values:
     *  - `current`
     *      - i.e. keep compilations of tor for the current host
     *  - `linux-android`
     *  - `linux-libc`
     *  - `macos`
     *  - `mingw`
     *  - `windows`
     *      - An accepted alias of `mingw`
     *
     * Supported [arches] values:
     *  - `all`
     *      - i.e. keep compilations of tor for all architectures of the specified host
     *  - `current`
     *      - i.e. keep compilations of tor for the current host's architecture of the specified host
     *  - `aarch64`
     *  - `armv7`
     *  - `ppc64`
     *  - `x86`
     *  - `x86_64`
     *
     * e.g.
     *
     *     // Keep only the aarch64 and x86_64 compilations of tor for the current host
     *     keepTorCompilation(host = "current", "aarch64", "x86_64")
     *
     * @throws [IllegalArgumentException] if [host] or [arches] contain unsupported values
     * */
    @FilterJarDsl
    public fun keepTorCompilation(host: String, vararg arches: String) {
        keepTorCompilation(host, immutableSetOf(*arches))
    }

    /**
     * Creates [FilterJarConfig] keep filters for provided os hosts and architectures
     *
     * Supported [host] values:
     *  - `current`
     *      - i.e. keep compilations of tor for the current host
     *  - `linux-android`
     *  - `linux-libc`
     *  - `macos`
     *  - `mingw`
     *  - `windows`
     *      - An accepted alias of `mingw`
     *
     * Supported [arches] values:
     *  - `all`
     *      - i.e. keep compilations of tor for all architectures of the specified host
     *  - `current`
     *      - i.e. keep compilations of tor for the current host's architecture of the specified host
     *  - `aarch64`
     *  - `armv7`
     *  - `ppc64`
     *  - `x86`
     *  - `x86_64`
     *
     * e.g.
     *
     *     // Keep only the aarch64 and x86_64 compilations of tor for the current host
     *     keepTorCompilation(host = "current", arches = setOf("aarch64", "x86_64"))
     *
     * @throws [IllegalArgumentException] if [host] or [arches] contain unsupported values
     * */
    @FilterJarDsl
    public fun keepTorCompilation(host: String, arches: Collection<String>) {
        configurations[host] = arches.toImmutableSet()
        try {
            @OptIn(InternalKmpTorApi::class)
            configurations.resolveOSInfo().configureFilters()
        } catch (t: Throwable) {
            configurations.remove(host)
            throw t
        }
    }

    @InternalKmpTorApi
    private fun Map<OSHost, Set<OSArch>>.configureFilters() {
        filterGroup(group = "io.matthewnelson.kmp-tor") { group ->
            // Will also work for -gpl variant
            group.filter(artifact = "resource-lib-tor") { config ->
                config.exclude(path = "io/matthewnelson/kmp/tor/resource/lib/tor/native") { keep ->
                    keep.apply(targets = this)
                }
            }
            // Will also work for -gpl variant
            group.filter(artifact = "resource-exec-tor") { config ->
                config.exclude(path = "io/matthewnelson/kmp/tor/resource/exec/tor/native") { keep ->
                    keep.apply(targets = this)
                    if (!keys.contains(OSHost.Windows)) return@exclude
                    keep.keep("/${OSHost.Windows}/tor.exe.local")
                }
            }
            // Will also work for -gpl variant
            group.filter(artifact = "resource-noexec-tor") { config ->
                config.exclude(path = "io/matthewnelson/kmp/tor/resource/noexec/tor/native") { keep ->
                    keep.apply(targets = this)
                }
            }
        }
    }

    @InternalKmpTorApi
    private fun FilterJarConfig.DSL.Keep.apply(targets: Map<OSHost, Set<OSArch>>) {
        targets.forEach { (osHost, osArches) ->
            osArches.forEach { osArch ->
                keep("/$osHost/$osArch/")
            }
        }
    }
}
