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

    protected open val jvmLinuxAndroidAarch64: String = "f90bdbe04b351e80dfa01476392f5327a82dacedf155a3555dcd7d2325a16429"
    protected open val jvmLinuxAndroidArmv7: String = "f73226f29f47beb95be9514c4412a670f329db0e9c6ca04e7ea526a4e9071cc0"
    protected open val jvmLinuxAndroidX86: String = "fdd3b7d529f382bb9bbdc6227bee22147fa23c365db11512ca932cdb9ff1a0b9"
    protected open val jvmLinuxAndroidX86_64: String = "3318b521d7a16c3c4f1ba57e1db8e677a310cee1dd87ea9769ffa3797980d7b9"

    protected open val jvmLinuxLibcAarch64: String = "593461ffad1b474aa58b08bced77cf29f9627212a1bbfe643b2d2b3e0c9ba2d2"
    protected open val jvmLinuxLibcArmv7: String = "4933cdc506805492558d6a008632fe7daf021de8ecad222db427d245f2eb15c9"
    protected open val jvmLinuxLibcPpc64: String = "b70fe469962acc51f1a3b964e38c69bd20d0bcb6c18997a7c3fe39c4585c7a29"
    protected open val jvmLinuxLibcRiscv64: String = "2e7467172cb02151b766e1de2bad01beeeed3d55fe8bc7648a1a2dbff59917a3"
    protected open val jvmLinuxLibcX86: String = "04c03a56bf2ebbcfd1b79400ce4147e415da5b9139dbf18819bd5246fafa509d"
    protected open val jvmLinuxLibcX86_64: String = "605695fc01cc9c2b96b41440cd8f6522e45da934a0f57365aa2824bbd77b8fd0"

    protected open val jvmLinuxMuslAarch64: String = "fd1d360e6a5628b1f679cfee4c150c9a04374bbaf44b029db02d1ea31583d1b3"
    protected open val jvmLinuxMuslX86: String = "33b9d07ce729d56f48debe88a44fbbec2ddf086c40c4deda9c7dacdd9a4acfa8"
    protected open val jvmLinuxMuslX86_64: String = "ac24e170132b65c3c5b29154c2445e1a14fe7fa2aec488a39da2fa8bd0817a16"

    protected open val jvmMacosAarch64: String = "75e74b643ea33cec70fa00e1cb0b37b667ed92f1d716a395cdb9c2c7389f6bc8"
    protected open val jvmMacosX86_64: String = "b7d6fe39e0026b6752b9dd57b81cab6e6716b5730420e84f3c7881b024d15766"

    protected open val jvmMingwX86: String = "2e0eb525bb0e3d6dc423393770376fe2d534313ed0ac8c6c88adbeae60627348"
    protected open val jvmMingwX86_64: String = "c916e6eff98741347259f88722150cfe8ba3b696224e5e537ef94bca06eb7e26"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "d2bcdeb05b3ce678fb9db4566bdfc8b7c78d3757aaf195193682e29aa4c2d393"
    protected open val nativeIosX64: String = "58f989f81f80cd99e4301bcac7ac5374e13ad98abe751ed70bddf9fcb1d189bb"

    protected open val nativeMacosArm64: String = "dd4884602a29dbd8d6fcb9c54ac887fcda785f86e06c1ffe351e0c3fc27a1bcf"
    protected open val nativeMacosX64: String = "6c97c8aa5fb248abe8bd86f98706bed8fd1e31f7c8f388203c01ec7b2a31f2bf"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "85463a4073dce54e0f3b9e8db8d48c40ad2cccb92a66aee1b6b6b80d8119ffa8"
        override val jvmLinuxAndroidArmv7: String = "8ea8fd92cb227ef97a9543d0cc96ed2206ae0e14bcf2c04dd2b9d1f46e07969c"
        override val jvmLinuxAndroidX86: String = "18bf2301e4c5c588d48f00d91c7eb17e56005ca97ae2de323a3e424e9e89bbcf"
        override val jvmLinuxAndroidX86_64: String = "263110eaacbc7bbdf8793c57f59e2033a5dc50eb48875031e9584ffcd9166e3c"

        override val jvmLinuxLibcAarch64: String = "e732fb9af280421bfc2c6d329f86449c39ac4bb35842b733f38d794496f3041c"
        override val jvmLinuxLibcArmv7: String = "c76a4b93486c57bff91586e4c32183fceeb3b55d15cad40b643c2ebb24bc4dcf"
        override val jvmLinuxLibcPpc64: String = "c8055efc97833d682c2fba6daecb6dd8c3a48c2ed6308131c90f0851ef8fbba0"
        override val jvmLinuxLibcRiscv64: String = "4d0798b14ec75a2a98869680a2d334f5526d8643f1d2863ff5c47ed22ec68910"
        override val jvmLinuxLibcX86: String = "3cce02df5b428efadbfd865a4f646a0d06d52898877c9a9154887a8a16f82530"
        override val jvmLinuxLibcX86_64: String = "2c8eb5eae767d52d911ce75801f7a5d62493586b910704efa9b1594a422f930f"

        override val jvmLinuxMuslAarch64: String = "49f1ce28c3f3b9aeeeb58930f56ccf33a4ab55de696073a1aa895b8d97a00110"
        override val jvmLinuxMuslX86: String = "1b4aab102699d84b4cf7e31caa8577b408672e0a5e3878a49989626850104576"
        override val jvmLinuxMuslX86_64: String = "b18d5be09a0ba281a09649ed21d199f4fd72a1671288e096e7d68bc2fed4f194"

        override val jvmMacosAarch64: String = "e1e8dbab2a7f716e0406908e5e900413b0d6aa11e3cb4a22f14cbf13b3790195"
        override val jvmMacosX86_64: String = "53835badcd8eea456565f6ebb9d3cf1aadab21a873c4a565cc9e7297145ad70b"

        override val jvmMingwX86: String = "5c5abe23b8da3e647e92dc32b4d180f921b4ba08b7a462e6c7a84ecdde1d14b1"
        override val jvmMingwX86_64: String = "112261ad68843558d27c676fdc6d8674b3c1a9a85e1ec6f145102280455fcf41"

        override val nativeIosSimulatorArm64: String = "60e8d8a4381ea3c84e222bfe65262a588edfd2425d59585405e757a103408b79"
        override val nativeIosX64: String = "a06a29c77d96723f6fe6ce18fb3ee4e7cab8b4abea59915bf6f80515dd89e89c"

        override val nativeMacosArm64: String = "85b73067bab55e9d5d8c04f0526d87ad8c14fbb77fb058553faa6f1d690a50ca"
        override val nativeMacosX64: String = "0f4f18a6c82da40c5829d7a6a793493acc0df161bc79b0dc88b1c52631f1710f"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun errorReportJvmNativeLibResources(): String = errorReportJvmNativeLibsProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun errorReportNativeResource(sourceSet: String): String = errorReportNativeResourceProtected(sourceSet)

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
            arch = "riscv64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcRiscv64,
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

        // jvm linux-musl
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslX86_64,
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
