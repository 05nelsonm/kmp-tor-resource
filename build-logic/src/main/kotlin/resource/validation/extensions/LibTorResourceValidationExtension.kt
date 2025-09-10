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

    protected open val jvmLinuxMuslAarch64: String = "688eadb47cd5d8a20107039cf04d068153bcdb8a8fe16600a9e378f435c246a4"
    protected open val jvmLinuxMuslX86: String = "a52e07fdd21669b5c75a52c525fff623caf510ba95853e70ef968a519a0c5bd5"
    protected open val jvmLinuxMuslX86_64: String = "a29c925eb729da2ea340556e6f83495e52aa0d7e069734fc37839bbec9e82574"

    protected open val jvmMacosAarch64: String = "d231f22793256095c2dc969d6c1821d8f9fb06d09bd008b0db81490baf1c9b35"
    protected open val jvmMacosX86_64: String = "11106724a11b63a003e4f5d91eea4babe059d1d304539b444f193730ce3a7b4b"

    protected open val jvmMingwX86: String = "68be869812d0f9693a2bbce6782071f5050d730d10e07925023f9a998c21c61e"
    protected open val jvmMingwX86_64: String = "f3466194666bcfb0ef001095a6742a4b57ed967decf14609e9bc59ddab4cf766"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "adc893793eed00064373f859f8e6417a3be7644b46a641065777bc8f36d0294d"
    protected open val nativeIosX64: String = "07e8318f250fb33479b796fe3c3d835765a1d4ed11c29d2a60698e3c2b65b7a7"

    protected open val nativeMacosArm64: String = "1d680a08bfc8e60e9ba715afaa03ba8f0d15b2936b1de7519d3597740e6701c4"
    protected open val nativeMacosX64: String = "955537ae39de7d4e74b05b45e007df302a61a40897946d756356c8078df8bd80"

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

        override val jvmLinuxMuslAarch64: String = "7256723129ddf794c0ef0bf944ffd45ae5878e6e9c973372540b6f9a99148f6b"
        override val jvmLinuxMuslX86: String = "1b17535f721810f6efe8177fab795ebe216e560a1f1fbca90ec57f4450348a55"
        override val jvmLinuxMuslX86_64: String = "9e09ac5dbb19f982d76bcb3cf3afa9d09743ba10b30561f251c607e8bd287592"

        override val jvmMacosAarch64: String = "c87ed7501b9d203bfc334544c9945268b9bb029918188aa3e208950ff8e0b840"
        override val jvmMacosX86_64: String = "a7ed0dba979c52be144f84bdeaa572ace8345ab6a87269868cb0f7bf7ed32d35"

        override val jvmMingwX86: String = "12eec0ab6f171e14e5845e461f2fb4713f236efbe292f7cb16a401b490f06a0d"
        override val jvmMingwX86_64: String = "56874dc98811929b2b6041ae27c3a12b9829d1a8894aeec8f641acbae7bf1aa3"

        override val nativeIosSimulatorArm64: String = "876ebbd1b7c2fc405acff037a5826f286e1b04a6b2c5e7f568c2e6a7a9ddcd6d"
        override val nativeIosX64: String = "c282fda55f95fc139732e4d5cc269a1a3bc2570852850dc933f87ec76fc12198"

        override val nativeMacosArm64: String = "875f11c4377aca2315aa99c66ea47145c6fcdbb96c04ef1b74eb878b0e50c758"
        override val nativeMacosX64: String = "c6eb6c6dbc6376e3bfc5228e8496dc69d6c39baa9abca0ed6e8e3ec769067043"

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
