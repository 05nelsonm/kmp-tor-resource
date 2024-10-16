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

    protected open val androidAarch64: String = "48bc1e6a879ff07d42800c195362a0e8bd6818f340700610afffebb01aecc684"
    protected open val androidArmv7: String = "92fb5496005fd94e39f1528342b37d09ee290a3009dc67add90071cf1c13d573"
    protected open val androidX86: String = "c7f8aeac6327d47e9c558ae31adbec10d3a6159b5acd124ac76b7b70b166c5ed"
    protected open val androidX86_64: String = "af2563ddf3d9a91a08f70ec6cec4ed166549c59acef508f7ff0d1334a387120c"

    protected open val jvmLinuxAndroidAarch64: String = "094ef3ad893f0f226c2a78736eaae2c710ce5455323ab1a4b81c4b3d22aa1032"
    protected open val jvmLinuxAndroidArmv7: String = "fa6a01741ad8d352ff083b0a43745eaf848ece7cdb75a2eb399a5fff877e704a"
    protected open val jvmLinuxAndroidX86: String = "d281d2a7b24a5f0abedfe1795ef62443864fbc90f344240e1e943594fcdcc14a"
    protected open val jvmLinuxAndroidX86_64: String = "65bcc2ae1a2a2e6fc871a297090187ccc5d51f4afd69641ef5b5322a8e63a8ea"

    protected open val jvmLinuxLibcAarch64: String = "ee9933462de74da23a19fc225e2c259ce6f1cd6106fe5fbc5b2dbd9df76b12f9"
    protected open val jvmLinuxLibcArmv7: String = "0b962a01c95681c16f1990576667feb84c58a6e1b9c32449d13c59247cb5abbe"
    protected open val jvmLinuxLibcPpc64: String = "1502e25b0455c43235f79880707075dfb3671508d410213f015a6c707772c05a"
    protected open val jvmLinuxLibcX86: String = "5b1a5f7046bb97494a2d52a136d213d16ed6e5e77a73bb13768bb886b0030bfc"
    protected open val jvmLinuxLibcX86_64: String = "841c2b1617faaa8e1438d932efe04837312f659f5b28d4d50ad2d9e7f63cd9ca"

    protected open val jvmMacosAarch64: String = "5d5b8f8a8576d528beb5353f89d5c846104752251a37d9a424d251fbce500869"
    protected open val jvmMacosX86_64: String = "b52de9b4c774fe2552e1b59448ab79f5156aa118170906321fb32db76b223636"

    protected open val jvmMingwX86: String = "fc75cf2792534e2660360d24a5f1bec9d2541987812a87af2e02fb28a2220ad0"
    protected open val jvmMingwX86_64: String = "bb811d8ec30ade7c41a381f8ca6452aa604c4b5d99f26de6b301bb33ceb51783"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeMacosArm64: String = "5b046e9c4637e4bcf36f93f47650765075c19a3d15b10b3a704f8b4b72e8c81e"
    protected open val nativeMacosX64: String = "f20bdc2a5e810f6210c1bfe5b58fa7e3285610081b7bc58107b819aef37af188"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "f59755eeb053c2ddf6be48ff46059625d709ab9b3100a9093ebe70c9500cc594"
        override val androidArmv7: String = "7f617cd40e301c56fa886234eee38e73e56ad97bd628d8408cae6657d702c902"
        override val androidX86: String = "7e7bb8447c01c44e5224289967dc61fd6206ea0b5c4baec07f66cc9246e021da"
        override val androidX86_64: String = "721ce9be1d9142d3f71c9cb6ab2ce0313e8968a52e87e1dbe1b5f8067c16a47b"

        override val jvmLinuxAndroidAarch64: String = "a976b4440feabf39d55762c182b3c8704c05a88c85691f4c544cd6bf7e6a1d94"
        override val jvmLinuxAndroidArmv7: String = "b1e6471e966963e10ef5df353ffb08e902c1293d9717dc4268f1817cba7797b1"
        override val jvmLinuxAndroidX86: String = "48ef4d36f09dbba80c69699ceda78bc56d3d0b8b5f8fd1f8e77533fec642d716"
        override val jvmLinuxAndroidX86_64: String = "508b0200714f125ed309fcb371ccb5b5b948aae7be374322127b7f4fa12b8f9b"

        override val jvmLinuxLibcAarch64: String = "8bf2a573f24f46c6057f31cbef66940ebba375eca2913809add310e3e98b37c8"
        override val jvmLinuxLibcArmv7: String = "32b6c8e97bb67db13be2901d61d0300458ed072ec639650c0119deff9ac193c1"
        override val jvmLinuxLibcPpc64: String = "7773da9f54e9c18b7e2a659454ce361b30a590d01201f404db6e6f3680dedf07"
        override val jvmLinuxLibcX86: String = "752a2e85a352371fe84772957995f0f3242a05e073705ba6913bdb3406923a9d"
        override val jvmLinuxLibcX86_64: String = "0c18d997a01cfa749a021a110b663f816d16feedf7859fc580bbfeac9bc467be"

        override val jvmMacosAarch64: String = "9be9ab753e121425828a6412f50d22726b0eaa116fde58c50424476b651c7f4e"
        override val jvmMacosX86_64: String = "842b212a8b41895be50cc099841affe486046773edfc90b0aeade68abfee9ac4"

        override val jvmMingwX86: String = "0d5ea02f2c2d1d2089388f3b984cd4792a39544a38f73c53420243fb8fa360a6"
        override val jvmMingwX86_64: String = "d89c63b1473ee3640fa501661efdeff451dac1094d58268a92f91ea8405efe95"

        override val nativeMacosArm64: String = "adb888f95a10e52ef5494673d2a2c94a93994bfc23c350170e161197adfd50c0"
        override val nativeMacosX64: String = "af077dc2cd02d624dc89f8862d6fc32093225e8e2c20b3bfb05f6440bfcfe3fb"

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
