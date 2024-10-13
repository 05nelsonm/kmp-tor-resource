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
package io.matthewnelson.kmp.tor.resource.lib.tor.internal

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSArch
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal inline fun OSHost.toTorResourcePath(
    arch: OSArch,
    resourceLib: String,
): String {
    val resourceName = when (resourceLib) {
        "lib" -> resourceNameLibTor
        "exec" -> resourceNameTor
        "noexec" -> resourceNameLibTorJni
        else -> throw IllegalArgumentException("Unknown resourceLib[$resourceLib]")
    }

    return "/io/matthewnelson/kmp/tor/resource/$resourceLib/tor/native/$this/$arch/$resourceName"
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal inline fun Resource.Config.Builder.configureTorResource(
    alias: String,
    loader: Class<*>,
) {
    configureWindowsDLLRedirect(loader)
    configureExecutableResource(alias) { host, arch ->
        resourcePath = host.toTorResourcePath(arch, resourceLib = "exec")
        resourceClass = loader
    }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal inline fun Resource.Config.Builder.configureLibTorResource(
    alias: String,
    loader: Class<*>,
) {
    configureExecutableResource(alias) { host, arch ->
        resourcePath = host.toTorResourcePath(arch, resourceLib = "lib")
        resourceClass = loader
    }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal inline fun Resource.Config.Builder.configureLibTorJniResource(
    alias: String,
    loader: Class<*>,
) {
    when (OSInfo.INSTANCE.osHost) {
        is OSHost.Windows -> {}
        else -> return
    }

    configureExecutableResource(alias) { host, arch ->
        resourcePath = host.toTorResourcePath(arch, resourceLib = "noexec")
        resourceClass = loader
    }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
private inline fun Resource.Config.Builder.configureWindowsDLLRedirect(loader: Class<*>) {
    if (OSInfo.INSTANCE.osHost !is OSHost.Windows) return

    resource("DLL redirect") {
        isExecutable = false
        platform {
            resourcePath = "/io/matthewnelson/kmp/tor/resource/exec/tor/native/${OSHost.Windows}/tor.exe.local"
            resourceClass = loader
        }
    }
}
