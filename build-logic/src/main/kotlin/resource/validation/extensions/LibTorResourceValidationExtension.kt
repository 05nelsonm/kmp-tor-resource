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

    protected open val jvmMacosAarch64: String = "edbe9b89b0a3b28c32f8cc9163b99be78230ec4bd842b00313903718b41cd009"
    protected open val jvmMacosX86_64: String = "76d33a506b8aee863068c806be80a07dcaa33ee943a6b2a1f287ef5d5131b650"

    protected open val jvmMingwX86: String = "a24347f47f1d6b7d180e60ece1110b1cc5ca20b0bc5511ce297df89b428edb6d"
    protected open val jvmMingwX86_64: String = "2e87b19d75e020f9dc537359e5b6ac5e26afa440d39cfdf30af46fe3a8b391ab"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "ec9e0afd97f44b70680feccbf048650c19726924ff417e7b3af3248b0e83c922"
    protected open val nativeIosX64: String = "15bec4d476e29a676d97b7061fbb135d66f31caf3aba3bab0cd2c9f8d7a83aeb"

    protected open val nativeMacosArm64: String = "bc4c71726c17fad1f5ee25cc5deec1691c00a538ec1ffc8d454da5c487b85ce0"
    protected open val nativeMacosX64: String = "a4d146d9ca06e9c1e43bd4465d5a9cb54bf7389d0a3b95c90ea872dcfb5abb01"

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

        override val jvmMacosAarch64: String = "f67e07a881aa89c8a8ab9420984dde181c6281c2e6cbee0e913be0b685267841"
        override val jvmMacosX86_64: String = "3e86d1dc492fedf8ac1c4b4b261b7c1c0e7649ce9de4fcb2ea0593697ee3a5ab"

        override val jvmMingwX86: String = "a34b030fdf9b89de74e89405f0d4564534a9e3401e0685b5bffad4fe6d49415f"
        override val jvmMingwX86_64: String = "aac35c2aa396aba1e85b09fca70676146d3aac03df5192122ab60eb1efa3ea49"

        override val nativeIosSimulatorArm64: String = "207cdda0d540cdef7f16c65937c6499886577b9ca429c11bcad32586ee042836"
        override val nativeIosX64: String = "e1ace587a332bb54ec6fafbf3545537cf3f939ac8187936baf6a4417c30e0e49"

        override val nativeMacosArm64: String = "7e43e2c72147baabad03c1b3104d6c40979b81ec69199f73c52f267c6e854827"
        override val nativeMacosX64: String = "57de2de3992e2de750e33f82fc28bae2aa90c6c043cfb045832b982f6226ffed"

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
