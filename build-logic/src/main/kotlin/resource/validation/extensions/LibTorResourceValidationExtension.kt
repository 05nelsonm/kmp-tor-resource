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
@file:Suppress("PropertyName", "SpellCheckingInspection")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-lib-tor`
 *
 * @see [GPL]
 * */
open class LibTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-lib-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.lib.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val androidAarch64: String = "5127392aec380033a00dd0510ec5df23e7415e55c383b48d1240e2fa42e10257"
    protected open val androidArmv7: String = "bc118464a72fc00ce3a80017601c82cd3df5017e49bd7f27a08b75c97ea430fc"
    protected open val androidX86: String = "c244c177a2e5daeaafdfe72ac2d6da7aa343b8e12c6b9645409de49e22c8db02"
    protected open val androidX86_64: String = "881e614df98371ff5ff80ea70bbaf0bec84f3c0898ad908597454b75adc970e2"

    protected open val jvmLinuxAndroidAarch64: String = "5aa167b05ae6f010956b6a9cb042bddf10c7831627599662c2df610eff19b73f"
    protected open val jvmLinuxAndroidArmv7: String = "432006eb52e3b6e6270124e82a936552aa18d5a0c8cebde637612842e27d0f79"
    protected open val jvmLinuxAndroidX86: String = "718bd94c68d8dd1a1f8882d0f6b155c42c840e087b7afd6ef405b923799e8321"
    protected open val jvmLinuxAndroidX86_64: String = "e3a7a8173faaac455a6d7bbe5c1a5baa3824023fc373e6abf464fed2ceec734c"

    protected open val jvmLinuxLibcAarch64: String = "56073d0b08044eff9bc24cad8190b14bff6d0a2e20ab557ac0fcffdb5faca126"
    protected open val jvmLinuxLibcArmv7: String = "87c1d80d185cccf472b1ce69195a33144cfd66385fc8279fdd8263d74086bf79"
    protected open val jvmLinuxLibcPpc64: String = "e1b5da4de8acb01a449d592f4612fa008038fc617709509e9b75cf6df0dcc5a5"
    protected open val jvmLinuxLibcX86: String = "a4ca7cca2ca69b2541fb50a547d2098c0685ad08e4c6e06bfe24de343f02dadb"
    protected open val jvmLinuxLibcX86_64: String = "14e70f2f7ede4a53aae09d1d60e50e13f333a9957ea0f5dedc68bfd01c630295"

    protected open val jvmMacosAarch64: String = "f58f58d04716b5c47c7dd32b9f564162b5a101be570faa504906c39f3acf3b49"
    protected open val jvmMacosX86_64: String = "a4ac0e10ef6dc36a8f0db11503b43ae426f7337aaa7b7c40ef39185415b6736f"

    protected open val jvmMingwX86: String = "bf1ed6d98416301840b9e5fd4469b820e4c861b5e88f69633c56130f5be7fdbd"
    protected open val jvmMingwX86_64: String = "0f28fbb24c0907360f1bd1c0f63fafdd4c9eca69cc74d6b2080d63d5c1a64df2"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "cd4e5c19fa9a526cd54fa3dc1a11b732034bb175526fdc34eb4511eadc68583e"
    protected open val nativeIosX64: String = "e80a809be2abcd7e13439ea552858fca7f6c53a9b930ebf47a448358138fc6fa"

    protected open val nativeMacosArm64: String = "52b3a2cd8454bfa7530809bbf5d5163ca5fc475f19965b7eceddfb997954a2b1"
    protected open val nativeMacosX64: String = "393df57a161b9e2094cf8f226508cf3bd97090034b4fe80749656053fddec1c5"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "124ec840e7e0f05aa4f096ff3da445be7f639c2afe3940227c410f958560b8bf"
        override val androidArmv7: String = "fee0eece9d6cc2620e998b86d30834c0ae26cc21137b68e4e39019d37c30da00"
        override val androidX86: String = "eaac39e794afad889b7051988ffe2130126061ba7c5cae2204a9b4220cd785fe"
        override val androidX86_64: String = "5978594abdf95c1fc0ac3559757dac9d5f43ed2931343ea6640efe279d17f0f8"

        override val jvmLinuxAndroidAarch64: String = "70f52dc385c7f99ffd3da60427692098c0ab8a3298eb2e4470cfc9bf23c9c677"
        override val jvmLinuxAndroidArmv7: String = "6a6cb4f9bb9497d3436a7c7b5dc5b5b4f6ea721853a8156beaf3f12b00361884"
        override val jvmLinuxAndroidX86: String = "9125c46aad5abd8914210c8b4577faa096aadca65b8cf38e2863b9403b468822"
        override val jvmLinuxAndroidX86_64: String = "67051f3492a3104969080bbc30486dfd37ab15923f1ad1ecb8e1e9665bfbc275"

        override val jvmLinuxLibcAarch64: String = "4f003885d4d3e29c5d524cb1cedd34b9429337d68e06e84b7883c7d61493eda1"
        override val jvmLinuxLibcArmv7: String = "89ac4566301d61a37bb0ccf312ffa5bc4857f9c92d33be46f42a15e6a6a68cb2"
        override val jvmLinuxLibcPpc64: String = "79a3044cf87ccc6473159ff68b6f50764c8776664f529978ef09330c98bac369"
        override val jvmLinuxLibcX86: String = "a8f2ad6b94e2189984dada687f9a39f1b8bdb0b7ad7b12d87b886206098c7915"
        override val jvmLinuxLibcX86_64: String = "e65a94c30ad87ecfddd67ae676abb49114e61f1c0d0085f243481120e4d0e30e"

        override val jvmMacosAarch64: String = "818a8b7a055c3d43fab53c395fa069cf412c3a2d1380d0303db6f6c59da72857"
        override val jvmMacosX86_64: String = "332f2b0691d08956228780d8877371fd738dd98f8a707ca42e4050fd573e9fb1"

        override val jvmMingwX86: String = "8e9581d0cad4ec46a4d1d0b0aa2afdff139a95beddfe3eef3980b7582b40f3c7"
        override val jvmMingwX86_64: String = "d425b8e9a6428b96081dfb9c2d2262a0c8596d5bbf2d0990a4983e078533be56"

        override val nativeIosSimulatorArm64: String = "2acabe95a9871116a95ea44079c6353225c4335a8cdc9ff88df375d591d03e80"
        override val nativeIosX64: String = "048d84f029a747d54d1270e860794af1188ffff1db823d98938b845a600654e5"

        override val nativeMacosArm64: String = "89c308b6683b16377a2c496f453aa0927ce7e2f1a21413f5339088b6879fa0c9"
        override val nativeMacosX64: String = "d51301f30418394dd7f804067a3641d0587966bdfc340c60b51016df9a3c9b9d"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtor.so",
            hashArm64 = androidAarch64,
            hashArmv7 = androidArmv7,
            hashX86 = androidX86,
            hashX86_64 = androidX86_64,
        ),

        // jvm linux-android
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // jvm linux-libc
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // jvm macos
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "aarch64",
            libName = "libtor.dylib.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "x86_64",
            libName = "libtor.dylib.gz",
            hash = jvmMacosX86_64,
        ),

        // jvm mingw
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86",
            libName = "tor.dll.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86_64",
            libName = "tor.dll.gz",
            hash = jvmMingwX86_64,
        ),

        // native linux
        ValidationHash.ResourceNative(
            sourceSetName = "linuxArm64".toSourceSetName(),
            ktFileName = "resource_libtor_so_gz.kt",
            hash = nativeLinuxArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "linuxX64".toSourceSetName(),
            ktFileName = "resource_libtor_so_gz.kt",
            hash = nativeLinuxX64,
        ),

        // native ios-simulator
        ValidationHash.ResourceNative(
            sourceSetName = "iosSimulatorArm64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeIosSimulatorArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "iosX64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeIosX64,
        ),

        // native macos
        ValidationHash.ResourceNative(
            sourceSetName = "macosArm64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeMacosArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "macosX64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeMacosX64,
        ),

        // native mingw
        ValidationHash.ResourceNative(
            sourceSetName = "mingwX64".toSourceSetName(),
            ktFileName = "resource_tor_dll_gz.kt",
            hash = nativeMingwX64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "libTorResourceValidation"
    }
}
