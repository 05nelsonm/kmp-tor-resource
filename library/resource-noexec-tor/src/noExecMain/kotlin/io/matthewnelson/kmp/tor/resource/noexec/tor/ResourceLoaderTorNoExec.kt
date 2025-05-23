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
import io.matthewnelson.kmp.file.InterruptedException
import io.matthewnelson.kmp.file.path
import io.matthewnelson.kmp.tor.common.api.GeoipFiles
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.common.core.synchronized
import io.matthewnelson.kmp.tor.common.core.synchronizedObject
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_GEOIPS
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP
import io.matthewnelson.kmp.tor.resource.geoip.ALIAS_GEOIP6
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.RESOURCE_CONFIG_LIB_TOR
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.threadSleep
import kotlin.concurrent.Volatile
import kotlin.jvm.JvmStatic
import kotlin.time.Duration.Companion.milliseconds

// noExecMain
public actual class ResourceLoaderTorNoExec: ResourceLoader.Tor.NoExec {

    public companion object {

        @JvmStatic
        public fun getOrCreate(
            resourceDir: File,
        ): ResourceLoader.Tor {
            @Suppress("DEPRECATION")
            return getOrCreate(
                resourceDir = resourceDir,
                registerShutdownHook = false,
            )
        }

        @JvmStatic
        @Deprecated("Use at your own peril. ShutdownHook registration causes abnormal exit behavior for Java/Android")
        public fun getOrCreate(
            resourceDir: File,
            registerShutdownHook: Boolean,
        ): ResourceLoader.Tor {
            return NoExec.getOrCreate(
                resourceDir = resourceDir,
                extract = ::extractGeoips,
                create = { dir -> KmpTorApi(dir, registerShutdownHook) },
                toString = ::toString,
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
    }

    @OptIn(InternalKmpTorApi::class)
    private class KmpTorApi(
        resourceDir: File,
        registerShutdownHook: Boolean,
    ): AbstractKmpTorApi(resourceDir, registerShutdownHook) {

        private val lock = synchronizedObject()

        @Throws(IllegalStateException::class, IOException::class)
        public override fun torRunMain(args: Array<String>) {
            val error = synchronized(lock) {
                val s = state()
                check(s == State.OFF) { "state[$s] != State.OFF" }

                val libTor = libTor()
                val job = runInThread(libTor.path, args)

                var e: String? = null

                while (e == null) {
                    try {
                        // Block the calling thread until we have a
                        // confirmed start in the allocated thread.
                        10.milliseconds.threadSleep()
                    } catch (_: InterruptedException) {}

                    when (state()) {
                        State.OFF,
                        State.STARTING -> e = job.checkError()

                        // No startup errors from kmp_tor.c will be
                        // returned beyond this point.
                        State.STARTED,
                        State.STOPPED -> break
                    }
                }

                if (e != null) {
                    // To clean up the Native Worker thread. kmp_tor.c
                    // will just return -1 because it will already be
                    // in State.OFF if it returned an error.
                    terminateAndAwaitResult()
                }

                e
            } ?: return

            throw IllegalStateException(error)
        }
    }

    @Throws(IllegalStateException::class)
    @Suppress("ConvertSecondaryConstructorToPrimary", "UnnecessaryOptInAnnotation", "unused")
    private actual constructor()
}
