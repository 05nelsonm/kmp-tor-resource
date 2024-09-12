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
@file:Suppress("PropertyName")

package resource.validation.extensions

import org.gradle.api.Project
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resoure validation and configuration for module `:library:resource-shared-tor`
 *
 * @see [GPL]
 * */
open class SharedTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-shared-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.shared.tor",
) {

    @Inject
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val androidAarch64: String = "13daaaf147a098bcb035009f61b2f222be1a7962aae50d3a1d7e2a7ea98efe5a"
    protected open val androidArmv7: String = "b43c389df59039d5eeaf2d679f312144fa91a1651b755471751d9b6a81ad09d7"
    protected open val androidX86: String = "28063bb378c48d8e6ba55679ba719586c79e8d1eed194b8c0fa20ec2fe720f4d"
    protected open val androidX86_64: String = "89fe1efd3921ac90a83cece7f02f4db14b9c8d458257bba1d33a28ece3b0dc93"

    protected open val jvmLinuxAndroidAarch64: String = "57ad474d15946d9ec2709b28c162ac9da24ff0c55a4b1643ce72881883e9d8e6"
    protected open val jvmLinuxAndroidArmv7: String = "e545ec3553ba31fe04e9c620fc246024570d2c594026c12ee150c14b36b5976c"
    protected open val jvmLinuxAndroidX86: String = "6f95d3b88b9ddfaa02e6ddddbabeb118675d56e44e957fa6dabf6a2d248e0185"
    protected open val jvmLinuxAndroidX86_64: String = "ad7f06279cb15cb506986aea2f1c34ab7779cb69a535dec11d9d4a3d68dad9c3"

    protected open val jvmLinuxLibcAarch64: String = "49ad1505c66ea17eccdc48761e406eef75a4cc5575cd19d48e7d183f1faf9d0d"
    protected open val jvmLinuxLibcArmv7: String = "a9afec260d5569278d38af8f689fd83140fc44789d2418418e8ee8e998606e96"
    protected open val jvmLinuxLibcPpc64: String = "64bea20250126dfef49db744f3b5444024353c5959615358c450aad5c6e74a74"
    protected open val jvmLinuxLibcX86: String = "d52463086a118cb9c72b0cf0dffa3c410f5cb321246480c4cc4e99a0f6e5b1b9"
    protected open val jvmLinuxLibcX86_64: String = "89fbc41597ec2e4ee57bf7239fdb6ade034aa5188322d0e61bd3dc67bf3a5ecf"

    protected open val jvmMacosAarch64: String = "1a01ab2de1279f27e246cbbc3154e12d9dfe3f174351f685158b35ae0c3863bb"
    protected open val jvmMacosX86_64: String = "626439753c3e4902420e18b95a3ac8cb570484f66446a5a248578009e5fc77f3"

    protected open val jvmMingwX86: String = "e58b793b169b5352b0f9e5f3e9186c589ced8b77a1a57d7a6836a3a8ae15da4c"
    protected open val jvmMingwX86_64: String = "6fb9d8e0637deb072c58b768a23720671bbff06aa3a6592206da80da401e3078"

    /**
     * Resoure validation and configuration for module `:library:resource-shared-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): SharedTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "c8d6e2aa51b90a3406364254e2af2fa507fc23f96a9afb9bc230a1fecc9b968f"
        override val androidArmv7: String = "c8c1e0b9a436fa293733a83e060015e6e5f16290d471fbe714b874e6e18e173a"
        override val androidX86: String = "212d6bf9d0a371c759295a8de2e6904a15a3ec5255554bc6ffc6052022eaf779"
        override val androidX86_64: String = "f683a442623ad8efed8008aa6661a70069ab4031c024a34637cde055631cee0a"

        override val jvmLinuxAndroidAarch64: String = "b23c860fe832b6055eb24fb523e3568dee2305948034d0b957348882f8c47fb2"
        override val jvmLinuxAndroidArmv7: String = "c12d75d5ccab45ef2e44dd73c5bcd835d97e5e965eea4cfa97359ab21cc52921"
        override val jvmLinuxAndroidX86: String = "fdd07b932e44909d818b855ecf414c38e0541b8707ba9d0120f8aa2d1c84876d"
        override val jvmLinuxAndroidX86_64: String = "57fe94f667e3c2c032513d05381bb0f087622f8ed2f9805529038c65ad41a872"

        override val jvmLinuxLibcAarch64: String = "c8492f1e2bdbbe25d8b9787fd571d17d557ff5c157dca7db5dc3b2bb181185ec"
        override val jvmLinuxLibcArmv7: String = "4c4d9fc5c55e5189773f690e5fa6bea23806370c9e90eadf5a7e2befb2f07908"
        override val jvmLinuxLibcPpc64: String = "4af1becaf64483ca55ab8605fbee93a7c54869e4af5726cef0f238786fd969f1"
        override val jvmLinuxLibcX86: String = "3caa33ac51195495163e0ffcc714185b638e4532ce4d807ff2525dcd6a5d4acb"
        override val jvmLinuxLibcX86_64: String = "fd3e615d9191df8fa453343752918e8c1243966bc2cff6f63f178dd76a0c5829"

        override val jvmMacosAarch64: String = "a81d824e305a12fd4cf4af1281ede74ca8ee5888a76dd3335a297494a0bc1e07"
        override val jvmMacosX86_64: String = "b06c54b8c3fbe5e48b9f9e54c338eed6dd80903061c80dca75b6b2d8a4e8d71a"

        override val jvmMingwX86: String = "acfb1b93473fefa3b5651f3317504ff76ecfde80bcff16ed39f18abacaab047c"
        override val jvmMingwX86_64: String = "82334cc857d82913d464487e5c4071e8f647535254746aab70a8e2257853e34c"

        internal companion object {
            internal const val NAME = "sharedTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtor.so",
            hashArm64 = androidAarch64,
            hashArmv7 = androidArmv7,
            hashX86 = androidX86,
            hashX86_64 = androidX86_64,
        ),

        // linux-android
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // linux-libc
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "tor.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // macos
        ValidationHash.LibNative.JVM(
            osName = "macos",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "macos",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmMacosX86_64,
        ),

        // mingw
        ValidationHash.LibNative.JVM(
            osName = "mingw",
            arch = "x86",
            libName = "tor.exe.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "mingw",
            arch = "x86_64",
            libName = "tor.exe.gz",
            hash = jvmMingwX86_64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "sharedTorResourceValidation"
    }
}
