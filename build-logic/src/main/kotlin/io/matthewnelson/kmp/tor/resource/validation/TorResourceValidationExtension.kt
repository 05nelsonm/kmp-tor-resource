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
            hashArm64 = "4c1220735f75d6f7eccfe74ede27496afc6ec1ac2f43e63a7509b2d83e71cbed",
            hashArmv7 = "28f2b9c003c0bac9fec43d8b1e4d8a8e6bca7aab986b363abc854ce202232302",
            hashX86 = "d876aa9773dba2d402a9d80622d5729917d776f694362a392b42804245567ae2",
            hashX86_64 = "d3b00d0a0b38429beee86aa787ef1a6e03130e47dbfb3c33d3b6482733d2129e",
        ),
    )

    override val jvmLibHashes = setOf(
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "aarch64",
            hash = "1ab797e4f78e44fbdd9472d405b0e08c832a5595ecc672d1de5a3dc6b7c85572",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "armv7",
            hash = "e96410b419471898afdffdfe5f33acd8a53f84624fc6fb4553908ffb3a45efc9",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86",
            hash = "e23c5be50387d3ce5e2fa9e77bd306e21f0d45675998b493fd904730e3b255a4",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86_64",
            hash = "4bf23e988a78506b2cce5d7a53132f5fe9c3b175150f41bde93d3271ae57498e",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "aarch64",
            hash = "35044b7c561f30299572e3a94c17fe42a922667cb6bbb8be04ce1c6b3eb6b0f0",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "armv7",
            hash = "64d9c568c8417b750cec4fcfb23dfaa7cec2870ef3ee01378ed36a2bbe7e3d47",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "ppc64",
            hash = "5ce4c9d52f5ff999faefe581e850ed113976b853e94abd42ca8109c4d6c16478",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86",
            hash = "7b7b9bc2a0f9c1ba685d37468d4a8b7a246e81a7768ccf3864f4cf37c92b8608",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86_64",
            hash = "503fd825e65ee18dd7365be67f7f53b7adba982613c0561c0977c21f2c8dc597",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "aarch64",
            hash = "521b29b4ad70bbdf1315fc65b14e4caf71ee66691372406e8024224c8cc12fc8",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "x86_64",
            hash = "7a3fcd66c0d455e5b001487017d81fac7f90c815a563ad46fd60755dc71171a8",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86",
            hash = "62de94b3255ce9123c5aae769619ae64ca40d21d0179bab64a6fef2e6d3f02ab",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86_64",
            hash = "c8afe454419d1f9e92716c5964a587dbb726a651b572066a701b645df7316841",
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
            hash = "35044b7c561f30299572e3a94c17fe42a922667cb6bbb8be04ce1c6b3eb6b0f0",
        ),
        NativeResourceHash(
            sourceSetName = "linuxX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "503fd825e65ee18dd7365be67f7f53b7adba982613c0561c0977c21f2c8dc597",
        ),
        NativeResourceHash(
            sourceSetName = "macosArm64",
            ktFileName = "resource_tor_gz.kt",
            hash = "378751be79e0f00a9678508537f683a3d41914f495c6002f652c5c9063bf1c08",
        ),
        NativeResourceHash(
            sourceSetName = "macosX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "dc0db550995a75ff4d2cc70f3e16d4644f4f45b6cd6ff59e6c977f7f4dc86223",
        ),
        NativeResourceHash(
            sourceSetName = "mingwX64",
            ktFileName = "resource_tor_exe_gz.kt",
            hash = "c8afe454419d1f9e92716c5964a587dbb726a651b572066a701b645df7316841",
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
