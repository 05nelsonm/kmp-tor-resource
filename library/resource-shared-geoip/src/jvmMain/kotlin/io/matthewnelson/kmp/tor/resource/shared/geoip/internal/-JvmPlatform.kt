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

package io.matthewnelson.kmp.tor.resource.shared.geoip.internal

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.shared.geoip.ALIAS_GEOIP
import io.matthewnelson.kmp.tor.resource.shared.geoip.ALIAS_GEOIP6

internal class Loader private constructor() {
    init { throw IllegalStateException() }
}

@OptIn(InternalKmpTorApi::class)
@Suppress("NOTHING_TO_INLINE")
internal actual inline fun Resource.Config.Builder.platformConfigureGeoipResources() {
    val clazz = Loader::class.java

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
}
