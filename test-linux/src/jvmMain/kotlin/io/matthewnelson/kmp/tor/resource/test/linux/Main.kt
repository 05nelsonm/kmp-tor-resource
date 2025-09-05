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
package io.matthewnelson.kmp.tor.resource.test.linux

import io.matthewnelson.kmp.file.absoluteFile2
import io.matthewnelson.kmp.file.exists2
import io.matthewnelson.kmp.file.mkdirs2
import io.matthewnelson.kmp.file.toFile
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.common.core.OSArch
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo
import io.matthewnelson.kmp.tor.resource.exec.tor.ResourceLoaderTorExec
import java.util.concurrent.TimeUnit

internal fun main(vararg array: String) {
    check(array.size == 4) { "Invalid arguments. 4 expected (e.g. \"10\" \"linux-libc\" \"aarch64\" \"/path/to/runtime/dir\"" }

    val timeoutSeconds = array[0].toInt().coerceIn(2, 5 * 60)
    var dirRuntime = array[3].toFile().absoluteFile2()

    // Prevent accidental deletion of a project directory
    if (dirRuntime.exists2() && dirRuntime.resolve("build.gradle.kts").exists2()) {
        dirRuntime = dirRuntime.resolve("build")
    }

    assertOsInfo(expectedHost = array[1], expectedArch = array[2])

    val dirRoot = dirRuntime.resolve("test-linux-root")
    Runtime.getRuntime().addShutdownHook(Thread { dirRoot.deleteRecursively() })

    val dirResources = dirRoot.resolve("resources")
    val dirData = dirRoot.resolve("data").mkdirs2(mode = "700", mustCreate = true)
    val dirCache = dirRoot.resolve("cache").mkdirs2(mode = "700", mustCreate = true)
    val dirHs = dirRoot.resolve("hs").mkdirs2(mode = "700", mustCreate = true)

    println("Starting tor using directory[$dirRoot]")

    val loader = ResourceLoaderTorExec.getOrCreate(resourceDir = dirResources) as ResourceLoader.Tor.Exec

    val geoip = loader.extract()
    val b = loader.process(Binder) { tor, configureEnv ->
        val args = buildList<String> {
            add(tor.path)
            add("-f"); add("-")

            add("--CacheDirectory"); add(dirCache.path)
            add("--DataDirectory"); add(dirData.path)

            add("--GeoIPFile"); add(geoip.geoip.path)
            add("--GeoIPv6File"); add(geoip.geoip6.path)

            add("--HiddenServiceDir"); add(dirHs.path)
            add("--HiddenServiceVersion"); add("3")
            add("--HiddenServicePort"); add("80")

            add("--DisableNetwork"); add("0")
            add("--SocksPort"); add("auto")

            add("--DormantCanceledByStartup"); add("1")
            add("--RunAsDaemon"); add("0")
        }

        ProcessBuilder(args).apply {
            redirectErrorStream(true)

            environment().apply {
                configureEnv()
                put("HOME", dirRoot.path)
            }
        }
    }

    var p: Process? = null
    val out = StringBuilder()
    try {
        p = b.start()
        p.outputStream.close()

        Thread {
            try {
                val reader = p.inputStream.reader().buffered()

                while (true) {
                    val line = reader.readLine() ?: break
                    println(line)
                    out.appendLine(line)
                }
            } catch (_: Throwable) {}
        }.apply {
            isDaemon = true
            priority = Thread.MAX_PRIORITY
        }.start()

        p.waitFor(timeoutSeconds.toLong(), TimeUnit.SECONDS)
    } finally {
        p?.destroy()
    }

    val outString = out.toString()
    if (outString.contains("[notice] Parsing GEOIP IPv4 file")) {
        println("SUCCESS")
        return
    }

    throw RuntimeException(outString)
}

@OptIn(InternalKmpTorApi::class)
@Throws(IllegalArgumentException::class)
private fun assertOsInfo(expectedHost: String, expectedArch: String) {
    val host = OSInfo.INSTANCE.osHost
    val arch = OSInfo.INSTANCE.osArch

    check(host !is OSHost.Unknown) { "Unknown OSHost[$host]" }
    check(arch !is OSArch.Unsupported) { "Unsupported OSArch[$arch]" }

    require(expectedHost == host.toString()) { "expectedHost[$expectedHost] != actual[$host]" }
    require(expectedArch == arch.toString()) { "expectedArch[$expectedArch] != actual[$arch]" }

    println("PASS - OSHost[$host] - OSArch[$arch]")
}

private object Binder: ResourceLoader.RuntimeBinder
