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

    protected open val jvmLinuxAndroidAarch64: String = "07ee1ad22e383e0d92cba3109fd73d58c13ccf45e8ff8ef17afff9375b71f987"
    protected open val jvmLinuxAndroidArmv7: String = "823850843ff86270de750063cd075f32c64e8ed3b9e44489556a166623452dad"
    protected open val jvmLinuxAndroidX86: String = "0679783474b2b1e59fbac65095e9dc1cacf6acd077c6eedf5f92d5630bee8878"
    protected open val jvmLinuxAndroidX86_64: String = "bc8b0f59f7fbe3aba86f0932835fb1266a37e13ab6a084bb11c7e963662dfba5"

    protected open val jvmLinuxLibcAarch64: String = "d4e86c577993653f2c82e4f9c4b30beb40ae46a5924bed5de5100437d5de16da"
    protected open val jvmLinuxLibcArmv7: String = "589eec923be03ae34d3c01cd23fbceb5be9056babbb97aca3c661796ec998a2a"
    protected open val jvmLinuxLibcPpc64: String = "a134c06f1ac2bc8f0e6b2ff779270a582838ba7b46f46db8ad4a9c3350ccd3c4"
    protected open val jvmLinuxLibcRiscv64: String = "e7c33bbf336b23904775c9d9e05791209a8e7b13d603a2351a90d133b292c0c0"
    protected open val jvmLinuxLibcX86: String = "fa8c9fe26b8f6747905288c06dd0f41bb06bed24783c53ebb94d9c632f595eb3"
    protected open val jvmLinuxLibcX86_64: String = "a75df0b5d5e4dc6a7874924806ee1ff7d05c2e94f8eff30e2ca42526c8a57efe"

    protected open val jvmMacosAarch64: String = "1f0415656954b34d1e8eebbaec447b239572b5f9168caff2e46e353e19c58260"
    protected open val jvmMacosX86_64: String = "d6eec8ae519e3349cc914ec64ae8d97d702bb08c6a537a897af8f373c019be9c"

    protected open val jvmMingwX86: String = "b766b6c194add641caa850b1df6571de27ba1d6d9e826717727bee4f70d13758"
    protected open val jvmMingwX86_64: String = "0f8ae359fb52c91af31cb259220f6e57592273da85697cb34f1a19f99cccc40a"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "ca25ae5e50d07de77f09000196e318670147df8e350fee84ae815a0a310c5dab"
    protected open val nativeIosX64: String = "29f8362e44e065efcf28172361573f6bba2eac066e12e36f9c13523fbcca6891"

    protected open val nativeMacosArm64: String = "dc80c16740b046bbd97bec3482577292cc72131bba56db3950ca3d1448ea626b"
    protected open val nativeMacosX64: String = "db23fe5cd09473a01a13fb8cc6cceaea6ce8a5233fa4eca7d8aab6da1d1f5d98"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "520b35d2a18e67b6dd1b5d5e69ad08cdd60c40639b88f7116ac91d6d69c0a7ba"
        override val jvmLinuxAndroidArmv7: String = "7f49d185e72b2ad092ff73039e03fa30dfb6f89ffcdbf8c3f97f4af36a89d5cb"
        override val jvmLinuxAndroidX86: String = "831f03b6e347fe1e64f0688ae1626df312f9683c50264c694b18bc9b144b4f66"
        override val jvmLinuxAndroidX86_64: String = "85885d644f22d05b20c8a8c2c8f09c4c7ef8dc6fcd6d4366309aac9e28815269"

        override val jvmLinuxLibcAarch64: String = "c3db1eb896e1d9e8ed9644a88afaa17574fdc54ad99a1bee901e1cf8ec4335ac"
        override val jvmLinuxLibcArmv7: String = "086b71590aa5a3dde4642daf66062aaf7b0b80e8b87ccf8d3ac8fbe2ff0771a8"
        override val jvmLinuxLibcPpc64: String = "db880b6bb55bdb51f5fbf7e806baccfda89415d2a7efcd71a524c5c15e2f7639"
        override val jvmLinuxLibcRiscv64: String = "31399610ce8371584cf3c4abf60f432635b8b6350fb41c3fc2798df5debcb788"
        override val jvmLinuxLibcX86: String = "c01b2d9a0a256db6f5c864a0819dde87a0264e7537f317f3e0c516f42210f060"
        override val jvmLinuxLibcX86_64: String = "fdb076ac32dacad9b4064fad6f570a18587dc40c85791c30604e2f38341366cc"

        override val jvmMacosAarch64: String = "eaab1897a085e90d1f06571ed0f89cf71c6d2e94b886657e4b963c7e204e2460"
        override val jvmMacosX86_64: String = "9791136dc71d4400532e86f20beb578f2c069e516c281cce5f35c214e3a94cb3"

        override val jvmMingwX86: String = "15de9a41a6733a96fa46c17d609226d64c7b8c62498a43d9fd3f93fedc183f99"
        override val jvmMingwX86_64: String = "f935a222e7f758a3bd116a8cedd8cfeb6ce7678fecb2d15f2064f1b41ec8e8a5"

        override val nativeIosSimulatorArm64: String = "74e0912adfc6a72580d51449c375839a692e1ba0ec2b1bb4379f5e1b43564a28"
        override val nativeIosX64: String = "5ea406471c4bf6bf9d8da643d13bc48fe816dd4e2109269915e17642ad9f6add"

        override val nativeMacosArm64: String = "13b052a37140b43ab343cfc19b9eec4beee7ad07b18e737a6f1a08a10d4a8e1b"
        override val nativeMacosX64: String = "014fd580dfe2494a481ba4f2cfe37da685b07897d8398ab92deee80385c41828"

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
