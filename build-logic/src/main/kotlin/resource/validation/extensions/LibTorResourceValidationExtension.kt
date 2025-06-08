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

    protected open val jvmLinuxAndroidAarch64: String = "79edc6df3da0b35fb919593b4e8cb99ac2d255c3befb7d48d15e2f0e3b75b612"
    protected open val jvmLinuxAndroidArmv7: String = "f1886e2171c4988bde23a848b268429e05d7f9c78edfa93347e1705b560a3fa8"
    protected open val jvmLinuxAndroidX86: String = "3f43cef5368ca01c858ea891943100c1cf141346521765c47fc723b948c6762b"
    protected open val jvmLinuxAndroidX86_64: String = "15069ba65730f498446856cf05d45c83937ef25a859e9ffa31e773dc0278a83f"

    protected open val jvmLinuxLibcAarch64: String = "9d29d97eb60295a0e7cd7f6a49821b1783d99da1115b5073b2c815b1fd003dd4"
    protected open val jvmLinuxLibcArmv7: String = "25d90fb1fdecc1d049fff7f0f1ae708f14f022a10b7624a8750b9c66aba5923b"
    protected open val jvmLinuxLibcPpc64: String = "ce452826a6709fdf1f5376d627b93d4e19dcbf4b84953102c49de5cd397ec565"
    protected open val jvmLinuxLibcX86: String = "91dc39d10d4a90b96263d6219152fdea950a27cf20f3d2a5110ce59507c50b47"
    protected open val jvmLinuxLibcX86_64: String = "0b50cdef2e70e326d054633247d9482c5d5450d972e026565af8e1010911537b"

    protected open val jvmMacosAarch64: String = "f60814583c13ff0c14ede80a8714a0b4be0c763db3c0ea8495ed91b66b52e469"
    protected open val jvmMacosX86_64: String = "c6b26d37398f1a40ba1a9ce8bbd379decd9f9b655c5f419ce04e385137fb841f"

    protected open val jvmMingwX86: String = "094ab1ddd3aede283cca4ef4867487c9c053d844677a1462fa0e1179ded8b49d"
    protected open val jvmMingwX86_64: String = "28f1dd58786d6e94457ba5773a74e24b2ee4c2f297d7acb9c58e1a7e3ee0c276"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "49f6bb0975fb8af7b51787379ae226f357acaa2b26775a1e887932e4adb4ec3e"
    protected open val nativeIosX64: String = "1717da39bc80df76e245ba5dd4261de02fdbafa38b48c3acc47e7ae09a787ca5"

    protected open val nativeMacosArm64: String = "7eaf0c9bdb81e2ed4d3f3940b4f308d7dc411884052846a97152914dc2355774"
    protected open val nativeMacosX64: String = "6747754afb886821e1cca7b68465817fdfd2e752af22cd6d96623040ac9e2b28"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "eef3eb1d182c969102e33af988623faa518bf8e9141cdecf6d47b4006ed6c8f4"
        override val jvmLinuxAndroidArmv7: String = "aaf986e04dcfdd97c26138c86102dbf01f3c7f17e8e2dbdeffab1c7be4d0e350"
        override val jvmLinuxAndroidX86: String = "ea6051d1f1a74aaa2257d015c9217ae727818f0ca9bcc81834a3dd42981f5244"
        override val jvmLinuxAndroidX86_64: String = "eccff6b9af87da64801fcd2b3bde365a7fb100350e6e885c78ed9f53a40ffd8e"

        override val jvmLinuxLibcAarch64: String = "d0860e6ef00d7bbff9e498141d08b84e055af0b6f3e1d3cd7078cb09afe5f9ca"
        override val jvmLinuxLibcArmv7: String = "e4cc6dde317931a8c90a1ce434468d00011c7cdad282d4b29b896fc9fcd358a0"
        override val jvmLinuxLibcPpc64: String = "878e1bc45579df8f20acb153d1ace32281bae39bd9ce60c66115841cc9433b7f"
        override val jvmLinuxLibcX86: String = "eb81fb12d29f4ca89417d43239f529bc1dac36311fb4fba99b7e1382c9c3548e"
        override val jvmLinuxLibcX86_64: String = "ae9093141ce17fa54f9c97309959309a9a65e37c73dc149b295f038de9584db2"

        override val jvmMacosAarch64: String = "c2cfcc692440cf09ecbb78af087053955d43fbe90f4227ceef0a38add310ed07"
        override val jvmMacosX86_64: String = "c32867c304c256e09df219333c44ba7e2ebf72f351067b67e4eaf1ee5917108e"

        override val jvmMingwX86: String = "419f9324271d85c3d0b9734728bdf69d6621a31a93959f46b43d6a73210b1d63"
        override val jvmMingwX86_64: String = "d2f13b96996cc75752590cca0ca77b9ddd9023ccd3355c7fe74a3e31849cd06f"

        override val nativeIosSimulatorArm64: String = "53b8376cf85f9a55a5d26a7984236a999e72534550f9bb0b1850e893bd10ffe0"
        override val nativeIosX64: String = "152158afa6061ee00300c7b5de698d7c0128d75fb1e2f2b89903218ab8c4c6cd"

        override val nativeMacosArm64: String = "34bab321840a985092e31911f4be7dde5e2a85c7ffffc77be0e6b91cf61a9c47"
        override val nativeMacosX64: String = "21cb9a883fb0f75ff60d13deaf8bf28bce2831e9db68af383126614bb9d81b49"

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
