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

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline fun OSHost.toTorResourcePath(
    arch: OSArch,
    isLib: Boolean,
): String {
    val path = if (isLib) "lib" else "exec"
    val name = if (isLib) resourceNameLibTor else resourceNameTor
    return "/io/matthewnelson/kmp/tor/resource/$path/tor/native/$this/$arch/$name"
}

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal inline fun Resource.Config.Builder.configureWindowsDLLRedirect(loader: Class<*>) {
    if (OSInfo.INSTANCE.osHost !is OSHost.Windows) return

    resource("DLL redirect") {
        isExecutable = false
        platform {
            resourcePath = "/io/matthewnelson/kmp/tor/resource/exec/tor/native/${OSHost.Windows}/tor.exe.local"
            resourceClass = loader
        }
    }
}
