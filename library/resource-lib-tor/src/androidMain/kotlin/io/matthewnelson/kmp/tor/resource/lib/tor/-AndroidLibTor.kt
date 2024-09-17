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
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.lib.tor.internal.configureExecutableResource
import io.matthewnelson.kmp.tor.resource.lib.tor.internal.toTorResourcePath

@InternalKmpTorApi
public fun Resource.Config.Builder.tryConfigureTestTorResources(
    aliasLibTor: String,
    aliasTor: String?,
) {
    if (OSInfo.INSTANCE.isAndroidRuntime()) {
        error("Android runtime detected. Use lib locator to find 'libtor.so' & 'libtorexec.so' paths.")
        return
    }

    val classpathLoader = "io.matthewnelson.kmp.tor.resource.android.unit.test.tor.Loader"

    val loader = try {
        Class.forName(classpathLoader)
    } catch (_: Throwable) {
        null
    }

    if (loader == null) {
        error("""
            Failed to find class '${classpathLoader}'
            Missing dependency for Android Unit Tests?

            Try adding the 'resource-android-unit-test-tor' or 'resource-android-unit-test-tor-gpl'
            dependency as 'testImplementation'.
        """.trimIndent())
        return
    }

    if (aliasTor != null) {
        configureExecutableResource(aliasTor) { host, arch ->
            resourcePath = host.toTorResourcePath(arch, isLib = false)
            resourceClass = loader
        }
    }

    configureExecutableResource(aliasLibTor) { host, arch ->
        resourcePath = host.toTorResourcePath(arch, isLib = true)
        resourceClass = loader
    }
}
