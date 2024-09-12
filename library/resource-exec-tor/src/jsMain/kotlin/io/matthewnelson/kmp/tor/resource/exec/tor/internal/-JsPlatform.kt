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

package io.matthewnelson.kmp.tor.resource.exec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.shared.tor.isSupportedBy
import io.matthewnelson.kmp.tor.resource.shared.tor.torFileName

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureTorResource() {
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

    val suffix = if (IS_GPL) "-gpl" else ""

    resource(ALIAS_TOR) {
        isExecutable = true
        platform {
            moduleName = "kmp-tor.resource-exec-tor${suffix}.${host}"
            resourcePath = "/${arch}/${host.torFileName}"
        }
    }
}

//@Throws(IllegalStateException::class)
@Suppress("NOTHING_TO_INLINE", "ACTUAL_ANNOTATIONS_NOT_MATCH_EXPECT")
internal actual inline fun Map<String, File>.findLibTor(): Map<String, File> = this