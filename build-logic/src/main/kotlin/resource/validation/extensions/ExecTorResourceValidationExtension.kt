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
@file:Suppress("PropertyName", "PrivatePropertyName", "SpellCheckingInspection")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-exec-tor`
 *
 * @see [GPL]
 * */
open class ExecTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-exec-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.exec.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    private val androidAarch64: String = "aa266faedf79b22b9c8e91f99ebb9f36b43ce098169d8a07590c73ed30fd1c62"
    private val androidArmv7: String = "c0958e9ffce00fe750b03ab6436bacd7267d74d0f3c4640fe85c9657f7d2ae94"
    private val androidX86: String = "a17d03b2ec666e9cf3da298a12145154ea6a9ad22c8b3186b60d6dbd7b7e6afb"
    private val androidX86_64: String = "6092becfa47fc3f169e2508947824904264c1ad00bba31233df57b7730dae428"

    private val jvmLinuxAndroidAarch64: String = "97a13132ab0df12a89ad30224631fdbb0287d22111f0449efde2cb0ae7a22ba3"
    private val jvmLinuxAndroidArmv7: String = "af4784ca100ba684699d24f2e82b6fef7e436678774b67e2d615b6918b278464"
    private val jvmLinuxAndroidX86: String = "40adcab2e30e184111d2692965dfbebf750f28e81d2588c89b8c5acae2320ea9"
    private val jvmLinuxAndroidX86_64: String = "b3c5a61e6b92c7b50afc8fc3c7ad4cf1dd0104f65c2b48cd2ecef7de2cc6dacf"

    private val jvmLinuxLibcAarch64: String = "adb1c5ada168b986c8735934239194251b1fff9073d14ce30104459cb1ece22f"
    private val jvmLinuxLibcArmv7: String = "bfe4dec565480d14c2baac6418f48af6cb4833b2b72d8d3abffcd431176ac774"
    private val jvmLinuxLibcPpc64: String = "c62f97d528785d043d01ccd3a46c9a2e4b4feafafbbf906dc6b5858af4445ac7"
    private val jvmLinuxLibcX86: String = "d8d3071da5224a4058f88ffbdac1f9d784f1f1fe8bc1865e2d1919f199a2a747"
    private val jvmLinuxLibcX86_64: String = "a0a5bfbf85b40d4b5c6e7bdd3ca9d7f7984d16f0b799e305c55f84aeab8794a2"

    protected open val jvmMacosAarch64: String = "9aba291adad12e1f4cd0ea6f134e12535de386371e599970bff1afa2fbaaab7f"
    protected open val jvmMacosX86_64: String = "dc748fa1c099f26fd3efe24a911143eb05a4610ba89eb285ded7d3997ef334e8"

    protected open val jvmMingwX86: String = "9f94bfbc8ed6a11ec5753c9a76b2e23d39a24f29218ad1fccf98859c3bb7c166"
    protected open val jvmMingwX86_64: String = "8e7b0b159868deb1b4ba27101b852fe9df27ff350a202b876a1b6d2f15bd1e55"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "0b664573ce661726f1f0a5f1319513fe89674dff78f2930d8b7640f8246e75c8"
    protected open val nativeMacosX64: String = "2d553c80fce4ccb441ccf78e3be1e5d2a923d42c585b79a10638e8d2541affdf"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "b5bc3f2b3eb3ab162907c8ef29cf1ee86931a41b6cecea24df5dd928fc6cafd1"
        override val jvmMacosX86_64: String = "19a5ff84936bd26a0c7c1a423c1ff3220c4816b350a4375727ce90e78d8a3e16"

        override val jvmMingwX86: String = "96361edc9966622b6c76e5d4288af0435067da4e48e708b8873a7f9a493f9f7f"
        override val jvmMingwX86_64: String = "3a727d0ac475ae9cdf294e995774fbf88d7c7d65b0b591a28cab965ffe5e2ca1"

        override val nativeMacosArm64: String = "d0e449836d99c2ffcac12c26ee5dad4fb22fb26c70c2c812e5ca7608bb40a4c3"
        override val nativeMacosX64: String = "24a6d855f40a13dea89309f843d51aa50570a5674d7b72693cfcfe7876900031"

        internal companion object {
            internal const val NAME = "execTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtorexec.so",
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
            libName = "tor.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // jvm linux-libc
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "tor.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // jvm macos
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmMacosX86_64,
        ),

        // jvm mingw
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86",
            libName = "tor.exe.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86_64",
            libName = "tor.exe.gz",
            hash = jvmMingwX86_64,
        ),

        // native linux
        ValidationHash.ResourceNative(
            sourceSetName = "linuxArm64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeLinuxArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "linuxX64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeLinuxX64,
        ),

        // native macos
        ValidationHash.ResourceNative(
            sourceSetName = "macosArm64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeMacosArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "macosX64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeMacosX64,
        ),

        // native mingw
        ValidationHash.ResourceNative(
            sourceSetName = "mingwX64".toSourceSetName(),
            ktFileName = "resource_tor_exe_gz.kt",
            hash = nativeMingwX64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "execTorResourceValidation"
    }
}
