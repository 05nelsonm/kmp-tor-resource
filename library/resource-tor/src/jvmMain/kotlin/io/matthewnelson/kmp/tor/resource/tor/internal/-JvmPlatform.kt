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
package io.matthewnelson.kmp.tor.resource.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.resource.tor.TorResources
import io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi
import io.matthewnelson.kmp.tor.core.resource.ImmutableMap
import io.matthewnelson.kmp.tor.core.resource.OSHost
import io.matthewnelson.kmp.tor.core.resource.OSInfo
import io.matthewnelson.kmp.tor.core.resource.Resource

// Jvm
@get:JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal actual val RESOURCE_CONFIG: Resource.Config by lazy {
    Resource.Config.create {
        val clazz = TorResources::class.java

        resource(ALIAS_GEOIP) {
            isExecutable = false

            platform {
                resourceClass = clazz
                resourcePath = PATH_RESOURCE_GEOIP
            }
        }

        resource(ALIAS_GEOIP6) {
            isExecutable = false

            platform {
                resourceClass = clazz
                resourcePath = PATH_RESOURCE_GEOIP6
            }
        }

        val host = OSInfo.INSTANCE.osHost

        if (host is OSHost.Unknown) {
            error("Unknown host[$host]")
            return@create
        }

        val arch = OSInfo.INSTANCE.osArch

        val torResourcePath = host.toTorResourcePathOrNull(arch)

        if (torResourcePath == null) {
            error("Unsupported architecutre[$arch] for host[$host]")
            return@create
        }

        resource(ALIAS_TOR) {
            isExecutable = true

            platform {
                resourceClass = clazz
                resourcePath = torResourcePath
            }
        }
    }
}

// no-op
@JvmSynthetic
@OptIn(InternalKmpTorApi::class)
@Suppress("ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT")
internal actual fun ImmutableMap<String, File>.findLibTor(): Map<String, File> = this
