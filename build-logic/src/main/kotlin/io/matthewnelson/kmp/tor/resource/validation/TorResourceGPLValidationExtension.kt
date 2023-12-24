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
            hashArm64 = "924fcc225e53bb14145e8dc9b080435478849dae1dbddb45aef38f7123f54980",
            hashArmv7 = "2c3030e3aeef1dc643c507e559fa0fb3a3fa25f68c33e3f08458c85870f87501",
            hashX86 = "a796c9d30acad1fc5d5dbf4c4859eaaad87768bdea9d1e4789a3c1644c29ed03",
            hashX86_64 = "7944c0308eddef0b25e84252fd77822965433783b4886757985b0dcdedc2bd12",
        ),
    )

    override val jvmLibHashes = setOf(
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "aarch64",
            hash = "46bdf6264df06cd349119736d0dd7c2d90159fb32369266b91c6834f0f5dbdb0",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "armv7",
            hash = "81f2ecbe98b49b15e16d55174173fcfb13bcd18be568b74a013da89f1a9ff682",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86",
            hash = "913004c43ed697bb4b9b2a935e7b464c45fc01d02b724ad466599eecabb46f0a",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86_64",
            hash = "b0afd0c844075d71219f7bbacdeeb2f97c4bd880b0b3f170fa1bde301e61dc4d",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "aarch64",
            hash = "2b482abf9f3dc9e0957a724f83975f5615ed1c8ffc539deff6ef9d58a653c495",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "armv7",
            hash = "fdfb36321d97a9c77e52d0cbf79ecb6606b16a5779b8f204082c18c6fa1bf1a3",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "ppc64",
            hash = "9654d15967ffd2cc9381b2822aea2a94e566837adc1d53067965bddebf997678",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86",
            hash = "56a4ebbc27f84481d574d5ac3541136c789715a1fe4edbe4dcbcb5ce0c04a26d",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86_64",
            hash = "3009ca44d5a544707adc61ea73d9e9ce48c157d60a1a45a794d7ba526a7f8d8f",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "aarch64",
            hash = "9e2134c53f37741fbde4ff263199dffa013114e051b53d1ab247fb2b017a5673",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "x86_64",
            hash = "742bf43a5210a3dda540cfb0c9166c9ae0c9c526c5c3abd4bbd879867bf26a7b",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86",
            hash = "281a704409bd58c354c3a0bb05bf939af88a6b8ffc81afbf8dc7af817213ba12",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86_64",
            hash = "8b993c24899bc3da7fbb36870e28f47f0b204e4fa208096a3ab2e6015a6f36b5",
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
            hash = "2b482abf9f3dc9e0957a724f83975f5615ed1c8ffc539deff6ef9d58a653c495",
        ),
        NativeResourceHash(
            sourceSetName = "linuxX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "3009ca44d5a544707adc61ea73d9e9ce48c157d60a1a45a794d7ba526a7f8d8f",
        ),
        NativeResourceHash(
            sourceSetName = "macosArm64",
            ktFileName = "resource_tor_gz.kt",
            hash = "ca74cbbb292489b0b90bc2b0e74d93acc9eeb4a315829857ead4617936ec20ee",
        ),
        NativeResourceHash(
            sourceSetName = "macosX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "d66122d18d980861b4d46893b488a9908cd63592fd9a98bd85929addf33b96d2",
        ),
        NativeResourceHash(
            sourceSetName = "mingwX64",
            ktFileName = "resource_tor_exe_gz.kt",
            hash = "8b993c24899bc3da7fbb36870e28f47f0b204e4fa208096a3ab2e6015a6f36b5",
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
