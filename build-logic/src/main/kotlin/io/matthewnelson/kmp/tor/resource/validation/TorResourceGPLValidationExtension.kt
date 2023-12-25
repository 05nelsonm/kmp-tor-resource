/*
 * Copyright (c) 2023 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.validation

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File
import javax.inject.Inject

/**
 * Validates packaged resources within the `external/build/pacakge` directory
 * using [androidLibHashes], [jvmGeoipHashes], [jvmLibHashes], [nativeResourceHashes]
 * and configures those platform source sets to utilize either mock resources
 * from `library/binary/mock_resources`, or the actual built products from
 * `external/build/package`. This is to maintain runtime referecnes and
 * mitigate checking resources into version control.
 *
 * Any errors are written to the project's `build/reports/resource-validation/resource-tor-gpl`
 * directory for the respective files.
 * */
abstract class TorResourceGPLValidationExtension @Inject internal constructor(
    project: Project
): ResourceValidation(
    project = project,
    moduleName = "resource-tor-gpl",
    modulePackageName = "io.matthewnelson.kmp.tor.resource.tor",
) {

    private val geoipErrors = mutableSetOf<String>()
    private var isGeoipConfigured = false

    @Throws(IllegalStateException::class)
    fun configureTorGPLAndroidJniResources() {
        check(project.plugins.hasPlugin("com.android.library")) {
            "The 'com.android.library' plugin is required to utilize this function"
        }

        project.extensions.getByName<LibraryExtension>("android").apply {
            configureAndroidJniResources()
        }
    }

    val jvmTorGPLLibResourcesSrcDir: File get() = jvmLibResourcesSrcDir()

    @Throws(IllegalStateException::class)
    fun configureTorGPLNativeResources() {
        check(project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            "The 'org.jetbrains.kotlin.multiplatform' plugin is required to utilize this function"
        }

        project.extensions.getByName<KotlinMultiplatformExtension>("kotlin").apply {
            configureNativeResources()
        }
    }

    val jvmGeoipGPLResourcesSrcDir: File get() {
        val mockResourcesSrc = rootProjectDir
            .resolve("library")
            .resolve(moduleName)
            .resolve("mock-resources")
            .resolve("src")
            .resolve("jvmAndroidMain")
            .resolve("resources")

        val externalResourcesSrc = rootProjectDir
            .resolve("external")
            .resolve("build")
            .resolve("package")
            .resolve(moduleName)
            .resolve("src")
            .resolve("jvmAndroidMain")
            .resolve("resources")

        if (isGeoipConfigured) {
            return if (geoipErrors.isEmpty()) {
                externalResourcesSrc
            } else {
                mockResourcesSrc
            }
        }

        val binaryResourcesDir = externalResourcesSrc
            .resolve(modulePackageName.replace('.', '/'))

        jvmGeoipHashes.forEach { (name, hash) ->
            val file = binaryResourcesDir.resolve(name)

            if (!file.exists()) {
                geoipErrors.add("$name does not exist: $file")
                return@forEach
            }

            val actualHash = file.sha256()
            if (hash != actualHash) {
                geoipErrors.add("$name hash[$actualHash] did not match expected[$hash]: $file")
            }
        }

        isGeoipConfigured = true
        generateReport(reportFileName = "jvm-geoip", errors = geoipErrors)

        return if (geoipErrors.isEmpty()) {
            externalResourcesSrc
        } else {
            mockResourcesSrc
        }
    }

    private val jvmGeoipHashes = setOf(
        "geoip.gz" to "67cd2f146fbe6d572aedb394d966a0581d8ba040361fd475cdd7519a4216847f",
        "geoip6.gz" to "ee9b64481effb235dd10565dfc987f53fd175fc97615eabed10345707e50b61f",
    )

    override val androidLibHashes = setOf(
        AndroidLibHash(
            libname = "libtor.so",
            hashArm64 = "d0240feea2b4cf97684e115ead4e05a13548bd5ee25cff75e7cd47fb743e0883",
            hashArmv7 = "ee3af3336d0dce5d8b7f9f40ad3ec824699f3bc101ca82abd591351d589f6e85",
            hashX86 = "e901e6558a331bb57088a3212b7f0596c5f5f93d10b31ea239fe26f5eb96da75",
            hashX86_64 = "db66ec03f4829437723c6aaa8ad5880717c8d601a9c36b5423b9ed138931b255",
        ),
    )

    override val jvmLibHashes = setOf(
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "aarch64",
            hash = "5fc412d5151d267de744fbe6595d149af837bc368a603cd64510e23cf7fe76f2",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "armv7",
            hash = "0ef42bf73bb5a1d28e2fb9faef31032edf7d382b246d8eea92e59d8cb5c862b7",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86",
            hash = "ed6e59d7879cd602aaeebc9407bbdc92134de241d14627f4fe851ed5fc66ed57",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86_64",
            hash = "d0fadd15b40e294a3cd25cafd9cffd997fd2f63f1563e53363d19ca13222924a",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "aarch64",
            hash = "d3ed2d62992650c7c9aa71a4ad68cb16741f7019e63ef8c90298fd2180717beb",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "armv7",
            hash = "f33b71e8460d64a7a2d861260af354d3a9ac43c8ebfc37f5c242808949ed19a6",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "ppc64",
            hash = "a40db819557339116438d9fd29b1bc8309482516bd78de3173d18bf2d8deace0",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86",
            hash = "048f2bda2fe6000a7b5e5eb3ff110966305474e28911a67070d0c33e06bcd874",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86_64",
            hash = "3abc2786481d8fb266c1a8f0949e0405255eed17c43bbeb3c3726c3ca44bf61c",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "aarch64",
            hash = "7ae99cab7a67bb403bf246e41c119fb441eef67616af8892af636eb5f680512b",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "x86_64",
            hash = "fdb3b8d27ee70c8b11efe7ab46770d1b91da1dc4d4d1357e76c0c3550727b8b3",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86",
            hash = "74f20267ff849eea2fc6fc8560f18bcd9c3d457931d783e99b00bb8a2bb110e4",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86_64",
            hash = "3f4c7584e03b652b7322de396a46c5071809f78cbe1f280110d57c3d3800d97a",
        ),
    )

    override val nativeResourceHashes: Set<NativeResourceHash> = setOf(
//        NativeResourceHash(
//            sourceSetName = "ios",
//            ktFileName = "resource_tor_gz.kt",
//            hash = "TODO",
//        ),
        NativeResourceHash(
            sourceSetName = "linuxArm64",
            ktFileName = "resource_tor_gz.kt",
            hash = "d3ed2d62992650c7c9aa71a4ad68cb16741f7019e63ef8c90298fd2180717beb",
        ),
        NativeResourceHash(
            sourceSetName = "linuxX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "3abc2786481d8fb266c1a8f0949e0405255eed17c43bbeb3c3726c3ca44bf61c",
        ),
        NativeResourceHash(
            sourceSetName = "macosArm64",
            ktFileName = "resource_tor_gz.kt",
            hash = "f41d0c0b92f1c65300273d7c220b979f7aceedd5351d8b177f3e8b1b9afd9434",
        ),
        NativeResourceHash(
            sourceSetName = "macosX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "40c18eabb64654fd21ee3326ca5a7c067a3c54914f67da8249bb261b98335a7f",
        ),
        NativeResourceHash(
            sourceSetName = "mingwX64",
            ktFileName = "resource_tor_exe_gz.kt",
            hash = "3f4c7584e03b652b7322de396a46c5071809f78cbe1f280110d57c3d3800d97a",
        ),
        NativeResourceHash(
            sourceSetName = "native",
            ktFileName = "resource_geoip6_gz.kt",
            hash = "ee9b64481effb235dd10565dfc987f53fd175fc97615eabed10345707e50b61f",
        ),
        NativeResourceHash(
            sourceSetName = "native",
            ktFileName = "resource_geoip_gz.kt",
            hash = "67cd2f146fbe6d572aedb394d966a0581d8ba040361fd475cdd7519a4216847f",
        ),
//        NativeResourceHash(
//            sourceSetName = "tvos",
//            ktFileName = "resource_tor_gz.kt",
//            hash = "TODO",
//        ),
//        NativeResourceHash(
//            sourceSetName = "watchos",
//            ktFileName = "resource_tor_gz.kt",
//            hash = "TODO",
//        ),
    )

    internal companion object {
        internal const val NAME = "torResourceGPLValidation"
    }
}
