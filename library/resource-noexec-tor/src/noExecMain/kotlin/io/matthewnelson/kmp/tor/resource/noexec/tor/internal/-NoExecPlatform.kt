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

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.InterruptedException
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.geoip.configureGeoipResources
import kotlin.jvm.JvmSynthetic
import kotlin.time.Duration

internal const val ALIAS_LIB_TOR: String = "libtor"

@get:JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal val RESOURCE_CONFIG_GEOIPS: Resource.Config by lazy {
    Resource.Config.create { configureGeoipResources() }
}

@get:JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal val RESOURCE_CONFIG_LIB_TOR: Resource.Config by lazy {
    Resource.Config.create { configureLibTorResources() }
}

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal expect inline fun Resource.Config.Builder.configureLibTorResources()

@Suppress("NOTHING_TO_INLINE")
@Throws(InterruptedException::class)
internal expect inline fun Duration.threadSleep()
