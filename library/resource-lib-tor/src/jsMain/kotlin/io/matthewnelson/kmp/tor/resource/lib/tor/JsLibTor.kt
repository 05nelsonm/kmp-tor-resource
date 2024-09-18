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
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.lib.tor.internal.IS_GPL
import io.matthewnelson.kmp.tor.resource.lib.tor.internal.configureExecutableResource
import io.matthewnelson.kmp.tor.resource.lib.tor.internal.resourceNameLibTor
import io.matthewnelson.kmp.tor.resource.lib.tor.internal.resourceNameTor

@InternalKmpTorApi
public fun Resource.Config.Builder.configureTorResources(
    aliasLibTor: String,
    aliasTor: String,
) {
    val moduleNamePrefix = "kmp-tor.resource-exec-tor" + if (IS_GPL) "-gpl" else ""

    configureExecutableResource(aliasTor) { host, arch ->
        moduleName = "$moduleNamePrefix.$host"
        resourcePath = "/$arch/${host.resourceNameTor}"
    }

    if (OSInfo.INSTANCE.osHost is OSHost.Windows) {
        resource("DLL redirect") {
            isExecutable = false
            platform {
                moduleName = "$moduleNamePrefix.${OSHost.Windows}"
                resourcePath = "tor.exe.local"
            }
        }
    }

    configureExecutableResource(aliasLibTor) { host, arch ->
        moduleName = "$moduleNamePrefix.$host"
        resourcePath = "/$arch/${host.resourceNameLibTor}"
    }
}
