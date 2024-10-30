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

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.name
import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.tor.common.api.GeoipFiles
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_GEOIPS
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP6
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic

// noExecMain
public actual class ResourceLoaderTorNoExec: ResourceLoader.Tor.NoExec {

    public companion object {

        @JvmStatic
        public fun getOrCreate(
            resourceDir: File
        ): ResourceLoader.Tor {
            return NoExec.getOrCreate(
                resourceDir = resourceDir,
                extract = ::extractGeoips,
                load = ::KmpTorApi,
                toString = ::toString
            )
        }

        @Volatile
        private var isFirstExtraction: Boolean = true

        @OptIn(InternalKmpTorApi::class)
        private fun extractGeoips(resourceDir: File): GeoipFiles {
            val map = RESOURCE_CONFIG_GEOIPS
                .extractTo(resourceDir, onlyIfDoesNotExist = !isFirstExtraction)

            isFirstExtraction = false

            return GeoipFiles(
                geoip = map.getValue(ALIAS_GEOIP),
                geoip6 = map.getValue(ALIAS_GEOIP6),
            )
        }

        @OptIn(InternalKmpTorApi::class)
        private fun toString(resourceDir: File): String = buildString {
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
                // test resources. iOS is always empty. Do not include.
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
    }

    private class KmpTorApi: AbstractKmpTorApi() {

        @Throws(IllegalStateException::class, IOException::class)
        override fun torRunMainProtected(args: Array<String>): Handle {
            val libTor = libTor()
            val handleT = kmpTorRunMain(libTor.path, args)

            check(handleT != null) { "Memory allocation failure" }

            when (val r = kmpTorCheckResult(handleT)) {
                -100 -> null // All good, running
                -10 -> "kmp_tor_run_main invalid arguments"
                -11 -> "kmp_tor_run_main configuration failure"
                -12 -> "Failed to load ${libTor.name}"
                -13 -> "tor_main_configuration_new failure"
                -14 -> "tor_main_configuration_set_command_line failure"
                else -> "kmp_tor_run_main experienced an unknown error code[$r]"
            }?.let { error ->
                kmpTorTerminateAndAwaitResult(handleT)
                throw IllegalStateException(error)
            }

            return Handle { kmpTorTerminateAndAwaitResult(handleT) }
        }
    }

    @Throws(IllegalStateException::class)
    @Suppress("ConvertSecondaryConstructorToPrimary", "UnnecessaryOptInAnnotation", "unused")
    private actual constructor()
}
