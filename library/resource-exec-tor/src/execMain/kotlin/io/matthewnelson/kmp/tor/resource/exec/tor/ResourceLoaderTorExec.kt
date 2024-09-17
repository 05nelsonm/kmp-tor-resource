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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "RemoveRedundantQualifierName")

package io.matthewnelson.kmp.tor.resource.exec.tor

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.Paths
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.ALIAS_TOR
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.RESOURCE_CONFIG
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.findLibTor
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP6
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

// execMain
public actual class ResourceLoaderTorExec: ResourceLoader.Tor.Exec {

    public companion object {

        @JvmStatic
        public fun getOrCreate(
            resourceDir: File,
        ): ResourceLoader.Tor {
            @OptIn(InternalKmpTorApi::class)
            return Exec.getOrCreate(
                resourceDir = resourceDir,
                extractTo = ::extractTo,
                toString = ::toString,
            )
        }

        @Volatile
        private var isFirstExtraction: Boolean = true

        @OptIn(InternalKmpTorApi::class)
        private fun extractTo(resourceDir: File): Paths.Tor {
            val map = RESOURCE_CONFIG
                .extractTo(resourceDir, onlyIfDoesNotExist = !isFirstExtraction)
                .findLibTor()

            isFirstExtraction = false

            // If an exception has not been encountered at this point,
            // the map will contain paths for all 3 aliased resources.
            return Paths.Tor(
                executable = map.getValue(ALIAS_TOR),
                geoips = Paths.Geoips(
                    geoip = map.getValue(ALIAS_GEOIP),
                    geoip6 = map.getValue(ALIAS_GEOIP6),
                )
            )
        }

        @OptIn(InternalKmpTorApi::class)
        private fun toString(resourceDir: File): String = buildString {
            appendLine("ResourceLoader.Tor.Exec: [")
            append("    resourceDir: ")
            appendLine(resourceDir)

            appendLine("    resourceConfig: [")
            val lines = RESOURCE_CONFIG.toString().lines()
            for (i in 1 until lines.size) {
                append("    ")
                appendLine(lines[i])
            }

            append(']')
        }
    }

    @OptIn(InternalKmpTorApi::class)
    @Throws(IllegalStateException::class)
    @Suppress("ConvertSecondaryConstructorToPrimary", "UnnecessaryOptInAnnotation", "unused")
    private actual constructor()
}
