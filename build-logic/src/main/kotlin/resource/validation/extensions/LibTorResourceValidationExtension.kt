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

    protected open val jvmLinuxAndroidAarch64: String = "fd738cb8957ec02f0d44bc19a1f78bf9330f3643baeaec0837a2004615b746c7"
    protected open val jvmLinuxAndroidArmv7: String = "23e78efce0340ecd2d189069cc5eed9c959ec7cf897503d57e80d212c4f974f9"
    protected open val jvmLinuxAndroidX86: String = "cad83b3f7a38e1bffbcbb8dc52110e227c2ce3a38c64545c2f6fe683f6306027"
    protected open val jvmLinuxAndroidX86_64: String = "e59fd3025be3be2ba6a2480a41fc96c037de6922cf43c63788e33973458b9205"

    protected open val jvmLinuxLibcAarch64: String = "471350010802ca1ed4fff76232dfa4dfd62c2d846efecc9ba476ed82800f7abc"
    protected open val jvmLinuxLibcArmv7: String = "9bd0eb6047fc07fb1d7e7093919469d0d035da0a1456fbe8750e1151da717849"
    protected open val jvmLinuxLibcPpc64: String = "2f65c5626f5b3c9734cd3e0492401692ac4998c3d6aacb91b123742bb274eb70"
    protected open val jvmLinuxLibcX86: String = "a98f35fb270ff9e0c06f4fafa046baeda79976769f31d49eb32a8989237b4f1a"
    protected open val jvmLinuxLibcX86_64: String = "a9193276c412833b2e3d5dd6fe35f952f5ba72bb881305b42bc4bc7a0aadf0a9"

    protected open val jvmMacosAarch64: String = "cba8fd3737512bbc16877d84d312028d31c912ae4d8dca0352b97a4864bcfab7"
    protected open val jvmMacosX86_64: String = "f1cf10e6b6beb08d33143ca34afde363387e7cb3c0a97563b65b2f79cac39e6d"

    protected open val jvmMingwX86: String = "755f65c60158ca4c3df85847e5fa25470eb4081318cded45de6bb61aa87c30ef"
    protected open val jvmMingwX86_64: String = "5c5ff591cc0aed0626fac300d8f6fd355c07084684a782cc8a586845c3323f2e"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "f594e06d719e4dfb2292f5949bd191da1e422d859ab667a29fbba72ddc4f8e5b"
    protected open val nativeIosX64: String = "149f53467955e877984c436ca26c6a9d0129bceccd3f228697ed8a67e5083bbe"

    protected open val nativeMacosArm64: String = "8dd0e9eef1bd33bee9eea253773aa2cd4a3797334e5798743581fe3276967a0e"
    protected open val nativeMacosX64: String = "3f334711cd1fcd7abcdf9501ef8e72d2dcc75313ee01414eff2dc291150a911c"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "3bb2cd779098aa2ba3f45986ce20190e650502dfd7b0d24a482c12053f11d68b"
        override val jvmLinuxAndroidArmv7: String = "60c6bfac2feeae2b8b7995dc52e797be7cb8756bf8a0426cf867fe86927f624c"
        override val jvmLinuxAndroidX86: String = "5509a14584b36ad863dcf11304e49142c1f152e9fcb91a5d54e74671643c283a"
        override val jvmLinuxAndroidX86_64: String = "46520c6f6b5486a27956060a6ea56d4f7590680a3975c3bcd079baced5a03124"

        override val jvmLinuxLibcAarch64: String = "eaac60859473d83655aba678117f2f8d53c78fb52422a6d219cba087a7281891"
        override val jvmLinuxLibcArmv7: String = "d047f4e52ab60c2fd8974ce91d9039cfc3f0a08327a9a9b2bc7a6dc4f5a85c8d"
        override val jvmLinuxLibcPpc64: String = "7f2bf0e0ec6fb1660f15b5e1d876311114f6d497d7920d7999450378be7807fb"
        override val jvmLinuxLibcX86: String = "53572804d3846db3cb34fe7ec22dbae7f782b7568b25d681cb80912b64d15405"
        override val jvmLinuxLibcX86_64: String = "0b62f14299bde62b845eb2f9d06fe9c2b5979744464772b88b197538766a431d"

        override val jvmMacosAarch64: String = "bf57dfcfe81a94439bd6890df1e20c07f6a3e0057ff82b06ac392a257210363d"
        override val jvmMacosX86_64: String = "a8aecb0b941f8d7112fb28c513356626a9cfca96111254a20a10bc82a7425d29"

        override val jvmMingwX86: String = "c29297d462cb8fa9081e907870fb73fd703ab063ce84e5d16ef8643bf2c14f83"
        override val jvmMingwX86_64: String = "26dd21690a58543608916bf2772cd7eef167b0d68c7d2a98af769599cdc70478"

        override val nativeIosSimulatorArm64: String = "40b065ae94bc9242abdaf9ee81c06be970cd6b2ca9246c58438dea0dcb495b27"
        override val nativeIosX64: String = "8dbeb58218dc6a32fb8ca4a7855bb190bb9334de40a65f3c65b1f5f158698562"

        override val nativeMacosArm64: String = "0197c6920cfac340dbdeae30e9e6f70842529834bbc95036bb1419753a78e06b"
        override val nativeMacosX64: String = "11c5cc6bcf5f7b913ab247970c9769c2d19387fb483cba025995e760cc010be8"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
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
