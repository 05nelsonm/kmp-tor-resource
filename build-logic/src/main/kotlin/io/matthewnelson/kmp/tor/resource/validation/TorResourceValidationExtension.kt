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
import resource.validation.extensions.internal.sha256
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
    private val hashGeoipGZ: String = "e3873cefd2810175e9cffbf5b3b236a809c198c564cde81110bfb940b6accc37"
    private val hashGeoip6GZ: String = "9ac7c1e81c7483f288be894ff245134c49b7a5e06439d728622c00057ba81be0"

    protected open val hashAndroidAarch64: String = "13daaaf147a098bcb035009f61b2f222be1a7962aae50d3a1d7e2a7ea98efe5a"
    protected open val hashAndroidArmv7: String = "b43c389df59039d5eeaf2d679f312144fa91a1651b755471751d9b6a81ad09d7"
    protected open val hashAndroidX86: String = "cb1d49f4fb68e430b277e81b784e980a9dbbcf88bf7e6f6bf8b393d651ce6ee9"
    protected open val hashAndroidX86_64: String = "9690a709c3b555d7a47092f023bf8bbd0bd133814e38dab30c395d56c5a8ec89"

    protected open val hashJvmLinuxAndroidAarch64: String = "57ad474d15946d9ec2709b28c162ac9da24ff0c55a4b1643ce72881883e9d8e6"
    protected open val hashJvmLinuxAndroidArmv7: String = "e545ec3553ba31fe04e9c620fc246024570d2c594026c12ee150c14b36b5976c"
    protected open val hashJvmLinuxAndroidX86: String = "2e2648f7cb8dc7459fd518228327e0d46f63fb30a3211ce2310c62798583aa94"
    protected open val hashJvmLinuxAndroidX86_64: String = "0064dcbaaff0c3d92d753fb9bd6017b829ed2b2d12da2b7cbdd72872b3a1bcbc"

    protected open val hashJvmLinuxLibcAarch64: String = "49ad1505c66ea17eccdc48761e406eef75a4cc5575cd19d48e7d183f1faf9d0d"
    protected open val hashJvmLinuxLibcArmv7: String = "a9afec260d5569278d38af8f689fd83140fc44789d2418418e8ee8e998606e96"
    protected open val hashJvmLinuxLibcPpc64: String = "64bea20250126dfef49db744f3b5444024353c5959615358c450aad5c6e74a74"
    protected open val hashJvmLinuxLibcX86: String = "d52463086a118cb9c72b0cf0dffa3c410f5cb321246480c4cc4e99a0f6e5b1b9"
    protected open val hashJvmLinuxLibcX86_64: String = "ccbcb36839f99f5dbcc4f0d3d5bc719e180b00b8bdc033c72c21aaaed09bf364"

    protected open val hashJvmMacosAarch64: String = "1a01ab2de1279f27e246cbbc3154e12d9dfe3f174351f685158b35ae0c3863bb"
    protected open val hashJvmMacosX86_64: String = "89aa82f9290924873adc4b7de21d82b54bc2e2fc34f8c03272b565e6a8da48ee"

    protected open val hashJvmMingwX86: String = "5d42022964c527c994c3b78b6d43d882bf726b5a3fff2292ea60c6988a277482"
    protected open val hashJvmMingwX86_64: String = "3b69341dc453057f3b8cb51e4957bc8d72e04ede543413123014ef0fd320f877"

    protected open val hashNativeIosArm64: String = "c2f1f4438dbbe99a724c58dd224b1da157febfd69d4ae122a83e8bd59e367191"
    protected open val hashNativeIosSimulatorArm64: String = "d0ccb9ce411d9a46d841f78da519580a676669743b7817f57255dea305eacc48"
    protected open val hashNativeIosX64: String = "2251993b856df25dbc2b426e755e843887760125a2d3c739090ee6278166c7c7"
    protected open val hashNativeLinuxArm64: String = "49ad1505c66ea17eccdc48761e406eef75a4cc5575cd19d48e7d183f1faf9d0d"
    protected open val hashNativeLinuxX64: String = "ccbcb36839f99f5dbcc4f0d3d5bc719e180b00b8bdc033c72c21aaaed09bf364"
    protected open val hashNativeMacosArm64: String = "d0ccb9ce411d9a46d841f78da519580a676669743b7817f57255dea305eacc48"
    protected open val hashNativeMacosX64: String = "2251993b856df25dbc2b426e755e843887760125a2d3c739090ee6278166c7c7"
    protected open val hashNativeMingwX64: String = "3b69341dc453057f3b8cb51e4957bc8d72e04ede543413123014ef0fd320f877"

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
