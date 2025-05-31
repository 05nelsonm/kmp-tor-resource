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
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.TorThread
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

        @Volatile
        private var torThread: TorThread? = null
        private val lock = synchronizedObject()

        @Throws(IllegalStateException::class, IOException::class)
        protected override fun torRunMain(args: Array<String>) {
            val error = synchronized(lock) {
                state().let { state ->
                    check(state == State.OFF) { "current[State.$state] != expected[State.OFF]" }
                }

                val (thread, job) = run {
                    val libTor = libTor()
                    startTorThread(libTor.path, args)
                }

                var e: String? = null

                while (e == null) {
                    try {
                        // Block the calling thread until we have a
                        // confirmed start in the allocated thread.
                        10.milliseconds.threadSleep()
                    } catch (_: InterruptedException) {}

                    when (nativeState()) {
                        State.OFF,
                        State.STARTING -> e = job.checkError()

                        // No startup errors from kmp_tor.c will be
                        // returned beyond this point.
                        State.STARTED,
                        State.STOPPED -> break
                    }
                }

                if (e != null) {
                    while (true) {
                        try {
                            // Jvm can throw InterruptedException if this
                            // thread has been interrupted. Ignore it and
                            // go again.
                            thread.awaitCompletion()
                            break
                        } catch (_: InterruptedException) {}
                    }
                } else {
                    torThread = thread
                }

                e
            } ?: return

            throw IllegalStateException(error)
        }

        public override fun state(): State = when (val s = nativeState()) {
            State.STARTING,
            State.STARTED,
            State.STOPPED -> s

            // Native state could be shutdown, but the platform thread
            // could still be running awaiting completion. Because there
            // is no "STOPPING" state, return STOPPED until the reference
            // is cleared via terminateAndAwaitResult.
            State.OFF -> if (torThread == null) s else State.STOPPED
        }

        public override fun terminateAndAwaitResult(): Int {
            val thread = synchronized(lock) {
                val t = torThread ?: return@synchronized null

                // Another caller from a different thread is requesting
                // termination
                if (t is TorThreadRefWaiter) return@synchronized t

                // First invocation of terminateAndAwaitResult for this
                // torRunMain job. Set up a waiter so subsequent calls
                // from different threads will also block until the
                // reference is cleared.
                torThread = TorThreadRefWaiter {
                    while (true) {
                        if (torThread == null) break

                        try {
                            10.milliseconds.threadSleep()
                        } catch (_: InterruptedException) {}
                    }
                }

                TorThread {
                    try {
                        while (true) {
                            try {
                                // Jvm can throw InterruptedException if this
                                // thread has been interrupted. We ignore it
                                // and go again.
                                t.awaitCompletion()
                                break
                            } catch (_: InterruptedException) {}
                        }
                    } finally {
                        torThread = null
                    }
                }
            }

            val result = kmpTorTerminateAndAwaitResult()
            thread?.awaitCompletion()
            return result
        }

        // Native Worker can only call terminate once, so this is utilized
        // as a stopgap if there are multiple calls to terminateAndAwaitResult.
        // They will simply block the thread reference to be dropped before
        // returning.
        private fun interface TorThreadRefWaiter: TorThread

        @Suppress("NOTHING_TO_INLINE")
        private inline fun nativeState(): State = State.entries.elementAt(kmpTorState())
    }

    @Throws(IllegalStateException::class)
    @Suppress("ConvertSecondaryConstructorToPrimary", "UnnecessaryOptInAnnotation", "unused")
    private actual constructor()
}
