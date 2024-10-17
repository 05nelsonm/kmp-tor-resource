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
package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.*
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.common.core.OSInfo
import java.util.UUID

internal const val ALIAS_LIB_TOR_JNI: String = "libtorjni"

@JvmSynthetic
@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): TorApi = KmpTorApi()

@OptIn(InternalKmpTorApi::class)
private class KmpTorApi: TorApi() {

    @Volatile
    private var count: UInt = 0u

    private external fun kmpTorRunMain(usleepMillis: Int, libtor: String, args: Array<String>): Int

    override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val (libtor, deleteOnExitDisposable) = extract(loadTorJni = false).let { root ->
            val parentDir = root.parentFile
                // Android Runtime (just the .so name with no directory)
                ?: return@let root to null

            val copy = parentDir.resolve(root.name + ".${count++}")
            val disposable = Hook.deleteOnExit(copy)

            try {
                root.inputStream().buffered().use { iStream ->
                    copy.outputStream().buffered().use { oStream ->
                        val buf = ByteArray(4096)
                        while (true) {
                            val read = iStream.read(buf)
                            if (read == -1) break
                            oStream.write(buf, 0, read)
                        }
                        buf.fill(0)
                    }
                }
            } catch (e: IOException) {
                copy.delete()
                disposable.invoke()
                throw e
            }

            copy.setReadable(false, false)
            copy.setWritable(false, false)
            copy.setReadable(true, true)
            copy.setExecutable(true, true)

            copy to disposable
        }

        val result = kmpTorRunMain(usleepMillis = 100, libtor = libtor.path, args = args)

        if (deleteOnExitDisposable != null) {
            // Not Android Runtime
            libtor.delete()
            deleteOnExitDisposable.invoke()
        }

        when (result) {
            -10 -> "JNI: dlopen failed to open libtor"
            -11 -> "JNI: dlsym failed to resolve tor_api functions"
            -12 -> "JNI: Failed to acquire new tor_main_configuration_t"
            -13 -> "JNI: Failed to determine args array size"
            -14 -> "JNI: Failed to allocate memory for argv array"
            -15 -> "JNI: Failed to populate argv array with arguments"
            -16 -> "JNI: Failed to set tor_main_configuration_t arguments"
            else -> null
        }?.let { throw IllegalStateException(it) }

        return result
    }

    private fun extract(loadTorJni: Boolean): File {
        val tempDir = TEMP_DIR

        return try {
            if (tempDir == null) {
                // Android Runtime >> libtorjni.so & libtor.so
                if (loadTorJni) {
                    System.loadLibrary("torjni")
                }
                "libtor.so".toFile()
            } else {
                val map: Map<String, File> = RESOURCE_CONFIG_LIB_TOR
                    .extractTo(tempDir, onlyIfDoesNotExist = true)

                if (loadTorJni) {
                    System.load(map.getValue(ALIAS_LIB_TOR_JNI).path)
                }
                map.getValue(ALIAS_LIB_TOR)
            }
        } catch (t: Throwable) {
            if (t is IOException) throw t
            throw IllegalStateException("Failed to dynamically load torjni library", t)
        }
    }

    init { extract(loadTorJni = true) }

    private companion object {

        private val TEMP_DIR: File? by lazy {
            if (OSInfo.INSTANCE.isAndroidRuntime()) return@lazy null

            val tempDir = SysTempDir.resolve("kmp-tor_${UUID.randomUUID()}")

            Hook.deleteOnExit(tempDir)
            RESOURCE_CONFIG_LIB_TOR.resources.forEach { resource ->
                val resourceFile = tempDir.resolve(resource.platform.fsFileName)
                Hook.deleteOnExit(resourceFile)
            }

            tempDir
        }
    }

    /**
     * Alternative to `File.deleteOnExit` which allows for de-registration.
     * */
    private open class Hook private constructor() {

        private val files = LinkedHashSet<File>(10, 1.0f)

        fun deleteOnExit(file: File): () -> Unit = synchronized (LOCK) {
            if (!files.add(file)) return@synchronized {}

            var wasInvoked = false

            return@synchronized handle@ {
                if (wasInvoked) return@handle

                synchronized(LOCK) {
                    if (wasInvoked) return@handle
                    wasInvoked = true
                    files.remove(file)
                }
            }
        }

        init {
            Runtime.getRuntime().addShutdownHook(Thread {
                synchronized(LOCK) {
                    var i = files.size - 1
                    while (i >= 0) {
                        val file = files.elementAt(i)
                        files.remove(file)
                        file.delete()
                        i--
                    }
                }
            })
        }

        companion object: Hook() {
            private val LOCK = Any()
        }
    }
}
