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
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.absoluteFile2
import io.matthewnelson.kmp.tor.common.api.GeoipFiles
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.*
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.ALIAS_TOREXEC
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.RESOURCE_CONFIG_GEOIPS
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.RESOURCE_CONFIG_TOR
import io.matthewnelson.kmp.tor.resource.exec.tor.internal.findLibTorExec
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP6
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

// execMain
public actual class ResourceLoaderTorExec: ResourceLoader.Tor.Exec {

    public companion object {

        /**
         * Creates a new instance of [ResourceLoaderTorExec] with provided [resourceDir]. If
         * an instance of [ResourceLoader.Tor] already exists, that will be returned instead.
         *
         * @param [resourceDir] The directory to extract resources to.
         *
         * @throws [IOException] If [absoluteFile2] has to reference the filesystem to construct
         *   an absolute path and fails due to a filesystem security exception.
         * @throws [UnsupportedOperationException] On Kotlin/JS-Browser if [absoluteFile2]
         *   references the filesystem to construct an absolute path.
         * */
        @JvmStatic
        public fun getOrCreate(
            resourceDir: File,
        ): ResourceLoader.Tor = Exec.getOrCreate(
            resourceDir = resourceDir,
            extract = ::extractGeoips,
            extractTor = ::extractTor,
            configureEnv = { configureProcessEnvironment(it) },
            toString = ::toString,
        )

        @Volatile
        private var isFirstExtractionGeoip: Boolean = true
        @Volatile
        private var isFirstExtractionTor: Boolean = true

        @OptIn(InternalKmpTorApi::class)
        private fun extractGeoips(resourceDir: File): GeoipFiles {
            val map = RESOURCE_CONFIG_GEOIPS
                .extractTo(resourceDir, onlyIfDoesNotExist = !isFirstExtractionGeoip)

            isFirstExtractionGeoip = false

            // If an exception has not been encountered at this point,
            // the map will contain paths for all 3 aliased resources.
            return GeoipFiles(geoip = map.getValue(ALIAS_GEOIP), geoip6 = map.getValue(ALIAS_GEOIP6))
        }

        @OptIn(InternalKmpTorApi::class)
        private fun extractTor(resourceDir: File): File {
            val map = RESOURCE_CONFIG_TOR
                .extractTo(resourceDir, onlyIfDoesNotExist = !isFirstExtractionTor)
                .findLibTorExec()

            isFirstExtractionTor = false

            return map.getValue(ALIAS_TOREXEC)
        }

        @OptIn(InternalKmpTorApi::class)
        private fun toString(resourceDir: File): String = buildString {
            appendLine("ResourceLoader.Tor.Exec: [")
            append("    resourceDir: ")
            appendLine(resourceDir)

            RESOURCE_CONFIG_GEOIPS.toString().lines().let { lines ->
                appendLine("    configGeoips: [")
                for (i in 1 until lines.size) {
                    append("    ")
                    appendLine(lines[i])
                }
            }

            RESOURCE_CONFIG_TOR.let { config ->
                // Android may have an empty configuration if not using
                // test resources. Do not include
                if (config.errors.isEmpty() && config.resources.isEmpty()) return@let

                val lines = config.toString().lines()
                appendLine("    configTor: [")
                for (i in 1 until lines.size) {
                    append("    ")
                    appendLine(lines[i])
                }
            }

            append(']')
        }
    }

    @Throws(IllegalStateException::class)
    @Suppress("ConvertSecondaryConstructorToPrimary", "UnnecessaryOptInAnnotation", "unused")
    private actual constructor()
}
