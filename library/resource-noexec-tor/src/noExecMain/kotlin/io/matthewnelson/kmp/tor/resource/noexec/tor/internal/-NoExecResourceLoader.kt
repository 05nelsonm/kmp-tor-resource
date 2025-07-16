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
package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.GeoipFiles
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP6
import kotlin.concurrent.Volatile

@Volatile
private var IS_FIRST_EXTRACTION: Boolean = true

@OptIn(InternalKmpTorApi::class)
internal fun noExecExtractGeoips(resourceDir: File): GeoipFiles {
    val map = RESOURCE_CONFIG_GEOIPS
        .extractTo(resourceDir, onlyIfDoesNotExist = !IS_FIRST_EXTRACTION)

    IS_FIRST_EXTRACTION = false

    return GeoipFiles(
        geoip = map.getValue(ALIAS_GEOIP),
        geoip6 = map.getValue(ALIAS_GEOIP6),
    )
}

@OptIn(InternalKmpTorApi::class)
internal fun noExecToString(resourceDir: File): String = buildString {
    appendLine("ResourceLoader.Tor.NoExec: [")
    append("    resourceDir: ")
    appendLine(resourceDir)

    RESOURCE_CONFIG_GEOIPS.toString().lines().let { lines ->
        appendLine("    configGeoips: [")
        for (i in 1 until lines.size) {
            append("    ")
            appendLine(lines[i])
        }
    }

    RESOURCE_CONFIG_LIB_TOR.let { config ->
        // Android may have an empty configuration if not using
        // test resources. iOS and androidNative are always empty;
        // do not include if that is the case.
        if (config.errors.isEmpty() && config.resources.isEmpty()) return@let

        val lines = config.toString().lines()
        appendLine("    configLibTor: [")
        for (i in 1 until lines.size) {
            append("    ")
            appendLine(lines[i])
        }
    }

    append(']')
}
