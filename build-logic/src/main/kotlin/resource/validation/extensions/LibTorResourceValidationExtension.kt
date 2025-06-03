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

    protected open val jvmMacosAarch64: String = "c8bb67b06095be49c0bbd0ae1b8455e1a76cdc32c23d06bf7b37055a7ea662de"
    protected open val jvmMacosX86_64: String = "fb71a538ecd45c638d5d3c1becfbc50b00d9acbe4fc22e4c43aaa8130a318737"

    protected open val jvmMingwX86: String = "de96e02b425d49dc4ebf5248c201f92d092e4ecfe4a391b5f41ba83851d95c56"
    protected open val jvmMingwX86_64: String = "5d81dee753b466ffa71ac15244bd565b6af9ab9972d73043e01fe7b6df5f05fe"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "e6637d7aa0cc2b5b353618f2add73b8f363b533c79f7c8b060e121b7e0a6df72"
    protected open val nativeIosX64: String = "b474ab2ac03a0d356c795e62354d78c4467b39494da93369d47dc4c346dc97f4"

    protected open val nativeMacosArm64: String = "0a00d53e3146e6c12437fcb40f7acd3f14ba56219600957fe41e357c9d1120ba"
    protected open val nativeMacosX64: String = "73ff036ef448c0b24160c71371e7729ee154817a2f67f1c569d44f51eafe1bc4"

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

        override val jvmMacosAarch64: String = "ae463dc9fd96766e024356b28570917c6001908c26a67d3a20ed272d4b22651d"
        override val jvmMacosX86_64: String = "15d3c0ced31748c78c0e70c11c260b518824f9d7c22ff42dc5a8055f314f6e16"

        override val jvmMingwX86: String = "b3f024eacad5752f773c031211b890ba33a3be63d403d45328e96578bbdbb4c4"
        override val jvmMingwX86_64: String = "2d53b567756436bb74b5c498aca406b05925cdb9bbf9c6b41747e998994e696f"

        override val nativeIosSimulatorArm64: String = "0667b8a0216239ddaba0321cda919a4072f79a8ca5bdc4596037f924cb98a872"
        override val nativeIosX64: String = "a359ea4fd99dd6d5aaf3dede2fd53574719a0ca1e18b00d5ae51252344977a81"

        override val nativeMacosArm64: String = "e0c1af04538274bc563890024ab78dd3ecc5ad8fbe5d2d0a6e3a26398e777140"
        override val nativeMacosX64: String = "feebb5b329c5855fe277b3c94f73c905bff8c4c538ffb4987d3ae6900f62fcd6"

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
