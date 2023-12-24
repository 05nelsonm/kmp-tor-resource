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
 * Any errors are written to the project's `build/reports/resource-validation/binary`
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
            hashArm64 = "7baf8b9711ddb5ce5c0ca83aa2e68e1f2a0b72cbe95bdb5b0902cebc35ebc9c9",
            hashArmv7 = "55d7846bdabad21e9e2bad0237d8af5033a2e3f9cb6b6f91755a79d47b7b9f9b",
            hashX86 = "55dc360ec73584eb7c04ca01cf51a71ba4935112647489d2c1e5c189d542172b",
            hashX86_64 = "32a55003d07165aded62a4dcb37e066d00ec057ccd19da866581cb5e0086a501",
        ),
    )

    override val jvmLibHashes = setOf(
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "aarch64",
            hash = "9e07caa9d049e24585530405e3fddebec186f6945e8401882c60eb53c9140d14",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "armv7",
            hash = "e8b1258494178ad3cc16b8fba7d39230090598649fa6b6ccb7d903a41ab40644",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86",
            hash = "695e6e84a9369a41600508fc70411d58458bf146f803d9c948f8a20d47616f1e",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86_64",
            hash = "f0a4fc4c3c7968a4c515b151be49c53f3a555f6773144c04751bf02b9b6ac8b3",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "aarch64",
            hash = "93e0e59ca086664fc00a9715770fab376f4b6cfaf7d71d7a35099c3e7c24109f",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "armv7",
            hash = "5822adecd3007b332bc00b3d9a53cd3ae157b8f57f2aea58bf2a07395e3a8853",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "ppc64",
            hash = "707246f11bfe71eac59bf3300f89ba009fbee9cf6ed8a2201e2102cbcf4de4f5",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86",
            hash = "218b2f7434650c3e8b6e455d36bb3f682d5f80beba538f7f5a7f7dfe012a891b",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86_64",
            hash = "b62970f7baa07d0fc849286dce3d81d7c4bab9b0a9254e29214f3cec816971eb",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "aarch64",
            hash = "33f4b699763d9d90212ecb9b8534f90fc58156f0b7999c51ee0f8821026c0871",
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "x86_64",
            hash = "6a575ff96fbc59e238271430bdaf75283e4b2657ac34c0687c130daaab9386a3",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86",
            hash = "67a4284a6a2fc291bbf43618026424c1123e6c706343e17d60200caa81f99413",
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86_64",
            hash = "d0002391ca9922d574229dfbcbdeacfddcbb7500367dbe6286c2b0548aa8d1b8",
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
            hash = "93e0e59ca086664fc00a9715770fab376f4b6cfaf7d71d7a35099c3e7c24109f",
        ),
        NativeResourceHash(
            sourceSetName = "linuxX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "b62970f7baa07d0fc849286dce3d81d7c4bab9b0a9254e29214f3cec816971eb",
        ),
        NativeResourceHash(
            sourceSetName = "macosArm64",
            ktFileName = "resource_tor_gz.kt",
            hash = "9fb25ce70789c7f43a4038e5c8304fb6221234b38b1384873833e131c09697eb",
        ),
        NativeResourceHash(
            sourceSetName = "macosX64",
            ktFileName = "resource_tor_gz.kt",
            hash = "97c7c0edbbd13a1a2abcb6d883d68e7357a477c09ce644030955646c94632174",
        ),
        NativeResourceHash(
            sourceSetName = "mingwX64",
            ktFileName = "resource_tor_exe_gz.kt",
            hash = "d0002391ca9922d574229dfbcbdeacfddcbb7500367dbe6286c2b0548aa8d1b8",
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
