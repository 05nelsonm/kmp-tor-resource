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

    protected open val androidAarch64: String = "c72d313c10f410a38983c3a286efa4359b4ea52ad5c7b54b00b9c341d9eae8fc"
    protected open val androidArmv7: String = "ee98d0f717dc5808b1d08ac0b4bfb2594c3127384df3d2d18a4b6615e38d85f7"
    protected open val androidX86: String = "92b92264260280949289b538c60e0811cf59944e9401141199e08da3c8d8b1fa"
    protected open val androidX86_64: String = "110aae29725bedad5cb1dffd82590adad0b5e5c30d4e04e2e0ce88534e0d3869"

    protected open val jvmLinuxAndroidAarch64: String = "d80889d6320f1f95ca6ead8dbdd065595a7838ceb3f6dce22c546c5edba2ec5c"
    protected open val jvmLinuxAndroidArmv7: String = "01d85b8292d3a0e22c6d8387b67891f429f19cf138eb959a769bd874dc549d41"
    protected open val jvmLinuxAndroidX86: String = "8bbcbfbc74ac7e47a0403f9dc59ce42d3c1461fde4c78f88f644171de527a9bd"
    protected open val jvmLinuxAndroidX86_64: String = "97c028fd5b29249bb38db931a310ca160b13066849828f521b1fd751695ad8c1"

    protected open val jvmLinuxLibcAarch64: String = "6a4ab688ca31dc39f3e4051938df8dda23b947d42c33616c318a5378d050a1db"
    protected open val jvmLinuxLibcArmv7: String = "b302080ef95ee8ff088aef034741cabb08ad2eb6256502daeda45a57a4fe1d89"
    protected open val jvmLinuxLibcPpc64: String = "f562e9ca0de1a22f6414f2265bc2ffd12a3cdb2d91e5dcfde83bb57b188a2a01"
    protected open val jvmLinuxLibcX86: String = "89ee8ec67f0c5952e2653df9db7319e7a95256f0ce02468f9080b875393d0089"
    protected open val jvmLinuxLibcX86_64: String = "46d65dc1a13d2cdd2afc599982aaaafe2bfe68a0b6b7a8c6ebfcd0d8ada6e3b7"

    protected open val jvmMacosAarch64: String = "898b2915b26a2cdaa4fd7d64d33af4aef1fa434621169b29b0525e3afa3412d6"
    protected open val jvmMacosX86_64: String = "37fb605aca49b5f13baa79c9081b03931840563125553d0980558111782d44e9"

    protected open val jvmMingwX86: String = "15362ccad5b55671b5afd68d35f49c7fb5185663311dff03ec2e14ff0fcb40ac"
    protected open val jvmMingwX86_64: String = "da80045b773c6f0434b0e9d68b00c3137bde4ecc203ebbc3a74d28fcbc36362c"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "85d4b02082bff45f92b65346ce4b02ca3a0a0c4cf89d66f4db726454790a3a39"
    protected open val nativeIosX64: String = "bcc173fbd6c6051472f0763960d73fcbebfd5e8c4374f4f98d901da5844b5193"

    protected open val nativeMacosArm64: String = "98c4bc289643cb63910df38a661d3882994cdedb355f6efa192afec2960fbf6d"
    protected open val nativeMacosX64: String = "8fa7c50129eacc0fee61bbdbd47c7fa8ffa635f2936a4f7be0f520990fa6b1ae"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "086692013f5c6c21553eba9486267ce35bc564db8c988ae0c7e7611b91e43650"
        override val androidArmv7: String = "8597fb7531c5f545af80a2da45aa0f7552e8933c5a4e506cf21ffb3c5390e16b"
        override val androidX86: String = "c15031c9d623bbc86edbdfa775ce9a1eb20f9021132a5f8fe5fbfcc1a392c730"
        override val androidX86_64: String = "3d08b211d5346ac6e0be9bfe340f4c77ed462329f13a5a798a92e95bd54c74b1"

        override val jvmLinuxAndroidAarch64: String = "51b888211c2241d909f265ee687631e8ec629a07838733d28859758899d12f02"
        override val jvmLinuxAndroidArmv7: String = "585fa2a455044148a1db935409a79aee7f97d0ce9f7f37c3750fc9e85f9fb953"
        override val jvmLinuxAndroidX86: String = "b2c6be3a9d6da3b19f865fa2eb31e9198ca6d6242c89b8d78a6dde146065e548"
        override val jvmLinuxAndroidX86_64: String = "a7e52c571c3079523b751c45b522d538a990ef3e1aba82d159ccd7973ee75f29"

        override val jvmLinuxLibcAarch64: String = "6e48650cae34fd1b9423e6240bb2a460642975f47d771d1589865cfbeeefde51"
        override val jvmLinuxLibcArmv7: String = "bd6185ac039fe18571263ea13723049363d0cb9060b698e7e316cf98a43b1dc4"
        override val jvmLinuxLibcPpc64: String = "1cd4ca959effbd67813a47c6857de97ad8e8885e5e3eb8fce7bb7f764d2e9a30"
        override val jvmLinuxLibcX86: String = "dc0d6e45cdc7f77fd8e1e422a5a3fe184fe325dfc5e8d505a933fff90ea5bbb8"
        override val jvmLinuxLibcX86_64: String = "09e571bec872d80c7bc3ced57968aea3110a8a8e9aeec3a09fb7c1048e5a1326"

        override val jvmMacosAarch64: String = "7fc6fd13bed2f82cff05f21f22ba699e6c175829ba1cc15bf150633aaecd205a"
        override val jvmMacosX86_64: String = "cf2c2efed616789da2cc0652de1a55d7d71044d8f169bbdbbe823433c5882f1e"

        override val jvmMingwX86: String = "fdf54d4bc0ac8aa23b6f347580014773f4fe72db64a8c789ce5136b42538575d"
        override val jvmMingwX86_64: String = "1747a5ce3e8f3d4539aca0b00c94e57612c814cf464581ac9e21494aba3319a4"

        override val nativeIosSimulatorArm64: String = "1b4106e1d43567e62c15c080323082a80249ce256f3bf569a1c277d78c116adf"
        override val nativeIosX64: String = "d23e76c37fb55d35b779474c07b2314533200e9ab19e719a2568df66534a127e"

        override val nativeMacosArm64: String = "a14f9af329616ea1f1430fda496f53339a9e557e64214d517d76c54015ae49b0"
        override val nativeMacosX64: String = "e3d30469dbc6588a529d059aefbd8a2c90f8e4823348997744d7909dcb6b665e"

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
