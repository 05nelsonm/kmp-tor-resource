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
@file:Suppress("PropertyName")

package io.matthewnelson.kmp.tor.resource.validation

import org.gradle.api.Project
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
abstract class TorResourceValidationExtension: ResourceValidation {

    @Inject
    internal constructor(project: Project): this(project, moduleName = "resource-tor")

    internal constructor(
        project: Project,
        moduleName: String,
    ): super(project, moduleName, modulePackageName = "io.matthewnelson.kmp.tor.resource.tor")

    // Hashes for geoip/geoip6 files are identical for GPL/non-GPL, and Jvm/Native
    private val hashGeoipGZ: String = "67cd2f146fbe6d572aedb394d966a0581d8ba040361fd475cdd7519a4216847f"
    private val hashGeoip6GZ: String = "ee9b64481effb235dd10565dfc987f53fd175fc97615eabed10345707e50b61f"

    protected open val hashAndroidAarch64: String = "9046e7b6938fcf4c88a1e242a4b4dc1495a3563d94f472b1512f8d6fe8327aa8"
    protected open val hashAndroidArmv7: String = "24770369165f712810906f6e561f6ca9683ea522e78a7d666496b3b3b91ad6c1"
    protected open val hashAndroidX86: String = "f2813e91c68cce65c0ba38c5aaf1f97979a14615462a885b936189049152daa5"
    protected open val hashAndroidX86_64: String = "93c839025409a62b5f1657ae4be1692267eb988fea42359b07c81ae8fe660af1"

    protected open val hashJvmLinuxAndroidAarch64: String = "2a745aae3c2e0985d38d258fe5b74b996908b17826b047d335d4ac984e3fc5cd"
    protected open val hashJvmLinuxAndroidArmv7: String = "7f4d761e6aa134c248195444ad60fbccf406d23a1d6c1d68928ea59d38f3dbd3"
    protected open val hashJvmLinuxAndroidX86: String = "81159423eadbd161c5e45fe69ff0908b5b5098e4d0964e1077b4c24783b2443b"
    protected open val hashJvmLinuxAndroidX86_64: String = "c434c3d861fc0c848280c8b688c11babb72075d7b33f033d574b88496521fbb1"

    protected open val hashJvmLinuxLibcAarch64: String = "341ddf5e66ff86077b00f4a2c71f83c25dcad9df5290a3eeff49c7df33a5bb9e"
    protected open val hashJvmLinuxLibcArmv7: String = "ff2bf19a88bd64ac3fecd45625a078679c2f13bcaa3b28ed5afdbf4084defb8f"
    protected open val hashJvmLinuxLibcPpc64: String = "ea81effccca032bc6776e77725b6685f9c333e93f9e80714a4995e8e35dd0b26"
    protected open val hashJvmLinuxLibcX86: String = "490a8a0a7f4ceb14f553726be7d4b357c391bfac4086217b0f0f93944c0f50d2"
    protected open val hashJvmLinuxLibcX86_64: String = "4d453d77f122c9a73664ccfe9356faef7b5df2938f0575912b57be9d469f9863"

    protected open val hashJvmMacosAarch64: String = "a81e210c14937197949a5e6eb9979338962aedb4cbb03010390eec7570e180bf"
    protected open val hashJvmMacosX86_64: String = "762a11f117f2237732915ce630d60f2d69e49aa2553e71f094ac3aed58b034d6"

    protected open val hashJvmMingwX86: String = "e093a36db1e6410541873b32d1e1143a33633f377de15dc4afc38e60e223ebbc"
    protected open val hashJvmMingwX86_64: String = "7627c3826333946e421ada71f813f51e672651577c2fa688575fe1046d10a17d"

    protected open val hashNativeIosArm64: String = "7df6e525f18e33331b40faba4a8e98c876428b7eb34c2d4e248068e96fe9d50f"
    protected open val hashNativeIosSimulatorArm64: String = "19d40526da4c82883a15d1731057cfc31002ed8b2505bdc4d8eaeefcaa0f1202"
    protected open val hashNativeIosX64: String = "5b6d6792b9f049ebdc054899896d948a33144c772c2b60eb60140f82121dc8f6"
    protected open val hashNativeLinuxArm64: String = "341ddf5e66ff86077b00f4a2c71f83c25dcad9df5290a3eeff49c7df33a5bb9e"
    protected open val hashNativeLinuxX64: String = "4d453d77f122c9a73664ccfe9356faef7b5df2938f0575912b57be9d469f9863"
    protected open val hashNativeMacosArm64: String = "7658a9e7af98e38c5f701226a5597e08963d1507cbd624c4d562ad4ae31dbf17"
    protected open val hashNativeMacosX64: String = "c062ac1e393cf688f36bedf69684bfe851ef9d4a5160b19d07b74b4963e53402"
    protected open val hashNativeMingwX64: String = "7627c3826333946e421ada71f813f51e672651577c2fa688575fe1046d10a17d"

    private val geoipErrors = mutableSetOf<String>()
    private var isGeoipConfigured = false

    @Throws(IllegalStateException::class)
    fun configureTorAndroidJniResources() { configureAndroidJniResourcesProtected() }

    val jvmTorLibResourcesSrcDir: File get() = jvmLibResourcesSrcDirProtected()

    @Throws(IllegalStateException::class)
    fun configureTorNativeResources() { configureNativeResourcesProtected() }

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
        "geoip.gz" to hashGeoipGZ,
        "geoip6.gz" to hashGeoip6GZ,
    )

    final override val androidLibHashes by lazy { setOf(
        AndroidLibHash(
            libname = "libtor.so",
            hashArm64 = hashAndroidAarch64,
            hashArmv7 = hashAndroidArmv7,
            hashX86 = hashAndroidX86,
            hashX86_64 = hashAndroidX86_64,
        ),
    )}

    final override val jvmLibHashes by lazy { setOf(
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "aarch64",
            hash = hashJvmLinuxAndroidAarch64,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "armv7",
            hash = hashJvmLinuxAndroidArmv7,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86",
            hash = hashJvmLinuxAndroidX86,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-android",
            arch = "x86_64",
            hash = hashJvmLinuxAndroidX86_64,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "aarch64",
            hash = hashJvmLinuxLibcAarch64,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "armv7",
            hash = hashJvmLinuxLibcArmv7,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "ppc64",
            hash = hashJvmLinuxLibcPpc64,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86",
            hash = hashJvmLinuxLibcX86,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "linux-libc",
            arch = "x86_64",
            hash = hashJvmLinuxLibcX86_64,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "aarch64",
            hash = hashJvmMacosAarch64,
        ),
        JvmLibHash(
            libname = "tor.gz",
            machine = "macos",
            arch = "x86_64",
            hash = hashJvmMacosX86_64,
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86",
            hash = hashJvmMingwX86,
        ),
        JvmLibHash(
            libname = "tor.exe.gz",
            machine = "mingw",
            arch = "x86_64",
            hash = hashJvmMingwX86_64,
        ),
    )}

    final override val nativeResourceHashes by lazy { buildMap {
        put(
            "iosArm64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeIosArm64,
                ),
            )
        )
        put(
            "iosSimulatorArm64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeIosSimulatorArm64,
                ),
            )
        )
        put(
            "iosX64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeIosX64,
                ),
            )
        )
        put(
            "linuxArm64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeLinuxArm64,
                ),
            )
        )
        put(
            "linuxX64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeLinuxX64,
                ),
            )
        )
        put(
            "macosArm64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeMacosArm64,
                ),
            )
        )
        put(
            "macosX64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_gz.kt",
                    hash = hashNativeMacosX64,
                ),
            )
        )
        put(
            "mingwX64",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_tor_exe_gz.kt",
                    hash = hashNativeMingwX64,
                ),
            )
        )
        put(
            "native",
            setOf(
                NativeResourceHash(
                    ktFileName = "resource_geoip6_gz.kt",
                    hash = hashGeoip6GZ,
                ),
                NativeResourceHash(
                    ktFileName = "resource_geoip_gz.kt",
                    hash = hashGeoipGZ,
                ),
            )
        )
    }}

    internal companion object {
        internal const val NAME = "torResourceValidation"
    }
}
