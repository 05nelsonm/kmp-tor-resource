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
package resource.validation.extensions.internal

import resource.validation.extensions.internal.ERROR.Companion.toERROR
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import java.io.File

sealed class ValidationHash private constructor() {

    internal data class LibAndroid internal constructor(
        val libname: String,
        val hashArm64: String,
        val hashArmv7: String,
        val hashX86: String,
        @Suppress("PropertyName")
        val hashX86_64: String,
    ): ValidationHash() {

        internal fun validate(
            packageModuleDir: File,
        ): List<ERROR> {
            val errors = mutableListOf<ERROR>()

            val jniLibs = packageModuleDir
                .resolve("src")
                .resolve("androidMain")
                .resolve("jniLibs")

            listOf(
                "arm64-v8a" to hashArm64,
                "armeabi-v7a" to hashArmv7,
                "x86" to hashX86,
                "x86_64" to hashX86_64,
            ).forEach { (abi, hash) ->
                val lib = jniLibs
                    .resolve(abi)
                    .resolve(libname)

                val rPath = lib.path.substringAfter("${File.separatorChar}kmp-tor-resource${File.separatorChar}")

                if (!lib.exists()) {
                    errors.add("Lib does not exist: $rPath".toERROR())
                    return@forEach
                }

                val actualHash = lib.sha256()
                if (hash != actualHash) {
                    errors.add("Lib hash[$actualHash] did not match expected[$hash]: $rPath".toERROR())
                    return@forEach
                }
            }

            return errors
        }
    }

    internal sealed class LibNative private constructor(): ValidationHash() {

        internal data class JVM internal constructor(
            override val osName: String,
            override val osSubtype: String = "",
            override val arch: String,
            override val libName: String,
            override val hash: String,
        ): LibNative()

        internal abstract val osName: String
        internal abstract val osSubtype: String
        internal abstract val arch: String
        internal abstract val libName: String
        internal abstract val hash: String

        internal fun validate(nativeDir: File): ERROR? {
            val machineName = osSubtype.ifBlank { null }?.let { "$osName-$it" } ?: osName

            val lib = nativeDir
                .resolve(machineName)
                .resolve(arch)
                .resolve(libName)

            val rPath = lib.path.substringAfter("${File.separatorChar}kmp-tor-resource${File.separatorChar}")

            if (!lib.exists()) {
                return "Lib does not exist: $rPath".toERROR()
            }

            val actualHash = lib.sha256()
            if (hash != actualHash) {
                return "Lib hash[$actualHash] did not match expected[$hash]: $rPath".toERROR()
            }

            return null
        }
    }

    internal data class ResourceJvm internal constructor(
        val fileName: String,
        val hash: String,
    ): ValidationHash() {

        internal fun validate(
            packageModuleDir: File,
            packageName: String,
        ): ERROR? {
            val file = "jvm".toSourceSetName()
                .sourceSetDir(packageModuleDir)
                .resolve("resources")
                .resolve(packageName.replace('.', '/'))
                .resolve(fileName)

            val rPath = file.path.substringAfter("${File.separatorChar}kmp-tor-resource${File.separatorChar}")

            if (!file.exists()) {
                return "Resource does not exist: $rPath".toERROR()
            }

            val actualHash = file.sha256()
            if (hash != actualHash) {
                return "Resource hash[$actualHash] did not match expected[$hash]: $rPath".toERROR()
            }

            return null
        }
    }

    internal data class ResourceNative internal constructor(
        val sourceSetName: SourceSetName,
        val ktFileName: String,
        val hash: String,
    ): ValidationHash() {

        internal fun validate(
            packageModuleDir: File,
            packageName: String,
        ): ERROR? {
            val file = sourceSetName
                .sourceSetDir(packageModuleDir)
                .resolve("kotlin")
                .resolve(packageName.replace('.', '/'))
                .resolve("internal")
                .resolve(ktFileName)

            val rPath = file.path.substringAfter("${File.separatorChar}kmp-tor-resource${File.separatorChar}")

            if (!file.exists()) {
                return "Resource does not exist: $rPath".toERROR()
            }

            try {
                file.inputStream().bufferedReader().use { reader ->
                    while (true) {
                        val line = reader.readLine()

                        if (!line.startsWith("    sha256 = ")) continue

                        val actualHash = line.substringAfter('\"')
                            .substringBefore('\"')

                        return if (hash == actualHash) {
                            // No error
                            null
                        } else {
                            "Resource hash[$actualHash] did not match expected[$hash]: $rPath".toERROR()
                        }
                    }
                }
            } catch (_: Throwable) {}

            return "Failed to find the sha256 value for NativeResource: $rPath".toERROR()
        }
    }
}
