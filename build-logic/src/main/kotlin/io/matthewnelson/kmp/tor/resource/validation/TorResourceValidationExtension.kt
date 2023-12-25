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
 * Any errors are written to the project's `build/reports/resource-validation/resource-tor`
 * directory for the respective files.
 * */
abstract class TorResourceValidationExtension @Inject internal constructor(
    project: Project
): ResourceValidation(
    project = project,
    moduleName = "resource-tor",
    modulePackageName = "io.matthewnelson.kmp.tor.resource.tor",
) {

    private val geoipErrors = mutableSetOf<String>()
    private var isGeoipConfigured = false

    @Throws(IllegalStateException::class)
    fun configureTorAndroidJniResources() {
        check(project.plugins.hasPlugin("com.android.library")) {
            "The 'com.android.library' plugin is required to utilize this function"
        }

        project.extensions.getByName<LibraryExtension>("android").apply {
            configureAndroidJniResources()
        }
    }

    val jvmTorLibResourcesSrcDir: File get() = jvmLibResourcesSrcDir()

    @Throws(IllegalStateException::class)
    fun configureTorNativeResources() {
        check(project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            "The 'org.jetbrains.kotlin.multiplatform' plugin is required to utilize this function"
        }

        project.extensions.getByName<KotlinMultiplatformExtension>("kotlin").apply {
            configureNativeResources()
        }
    }

    val jvmGeoipResourcesSrcDir: File get() {
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
            hashArm64 = "9046e7b6938fcf4c88a1e242a4b4dc1495a3563d94f472b1512f8d6fe8327aa8",
            hashArmv7 = "24770369165f712810906f6e561f6ca9683ea522e78a7d666496b3b3b91ad6c1",
            hashX86 = "f2813e91c68cce65c0ba38c5aaf1f97979a14615462a885b936189049152daa5",
            hashX86_64 = "93c839025409a62b5f1657ae4be1692267eb988fea42359b07c81ae8fe660af1",
        ),
    )

    override val jvmLibHashes = setOf(
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "aarch64",
            hash = "2a745aae3c2e0985d38d258fe5b74b996908b17826b047d335d4ac984e3fc5cd",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "armv7",
            hash = "7f4d761e6aa134c248195444ad60fbccf406d23a1d6c1d68928ea59d38f3dbd3",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86",
            hash = "81159423eadbd161c5e45fe69ff0908b5b5098e4d0964e1077b4c24783b2443b",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86_64",
            hash = "c434c3d861fc0c848280c8b688c11babb72075d7b33f033d574b88496521fbb1",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "aarch64",
            hash = "341ddf5e66ff86077b00f4a2c71f83c25dcad9df5290a3eeff49c7df33a5bb9e",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "armv7",
            hash = "ff2bf19a88bd64ac3fecd45625a078679c2f13bcaa3b28ed5afdbf4084defb8f",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "ppc64",
            hash = "ea81effccca032bc6776e77725b6685f9c333e93f9e80714a4995e8e35dd0b26",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86",
            hash = "490a8a0a7f4ceb14f553726be7d4b357c391bfac4086217b0f0f93944c0f50d2",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86_64",
            hash = "4d453d77f122c9a73664ccfe9356faef7b5df2938f0575912b57be9d469f9863",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "aarch64",
            hash = "a81e210c14937197949a5e6eb9979338962aedb4cbb03010390eec7570e180bf",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "x86_64",
            hash = "762a11f117f2237732915ce630d60f2d69e49aa2553e71f094ac3aed58b034d6",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86",
            hash = "e093a36db1e6410541873b32d1e1143a33633f377de15dc4afc38e60e223ebbc",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86_64",
            hash = "7627c3826333946e421ada71f813f51e672651577c2fa688575fe1046d10a17d",
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
            hash = "341ddf5e66ff86077b00f4a2c71f83c25dcad9df5290a3eeff49c7df33a5bb9e",
        ),
        NativeResourceHash(
            sourceSetName = "linuxX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "4d453d77f122c9a73664ccfe9356faef7b5df2938f0575912b57be9d469f9863",
        ),
        NativeResourceHash(
            sourceSetName = "macosArm64",
            ktFileName = "resource_tor_gz.kt",
            hash = "7658a9e7af98e38c5f701226a5597e08963d1507cbd624c4d562ad4ae31dbf17",
        ),
        NativeResourceHash(
            sourceSetName = "macosX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "c062ac1e393cf688f36bedf69684bfe851ef9d4a5160b19d07b74b4963e53402",
        ),
        NativeResourceHash(
            sourceSetName = "mingwX64",
            ktFileName = "resource_tor_exe_gz.kt",
            hash = "7627c3826333946e421ada71f813f51e672651577c2fa688575fe1046d10a17d",
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
        internal const val NAME = "torResourceValidation"
    }
}
