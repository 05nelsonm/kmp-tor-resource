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

    private val androidAarch64: String = "8a4eb402a872f21fdb6de60b59cc783503a1f49555c381e48fcb32a11a8f73c9"
    private val androidArmv7: String = "3172cf145ed7a4060f7fc20c1ee9463224fb564b9f674df1fef9b4c3a5d72796"
    private val androidX86: String = "9c919fb4aa36c391d6aa64ac67ced5b220392597eb40d565456353edcc45c8bd"
    private val androidX86_64: String = "d556c33cb1d6ffeb951c6aaea441b80cf804c207e2728d3b3b47b99b3c78fd1c"

    private val jvmLinuxAndroidAarch64: String = "b2e58a4396fa2b65dae429abe0e3a5db6aa58dbc700878038f67e53b27401c06"
    private val jvmLinuxAndroidArmv7: String = "55a35586b66c923097c5d6c23e15e0adddb88004b07091b05c9f15127ab67705"
    private val jvmLinuxAndroidX86: String = "1d844a7248131a4b9cbfa52bdf7a6e3367bf8eead18f96868715f6a0618f8c7f"
    private val jvmLinuxAndroidX86_64: String = "13b3475be7881f3d3dd035d9980b53730d6d1ee3fdc38796799e5dfb0b9b6dfd"

    private val jvmLinuxLibcAarch64: String = "8e611ab853d6f8ab70479ab4512ab6b0a14fb7091cb11df645059059440527c9"
    private val jvmLinuxLibcArmv7: String = "5e93c86b6e9b4158956312ec5a97fd4c89ccf944cf0fafa7d705485691864a36"
    private val jvmLinuxLibcPpc64: String = "3f6a9e2e3909f6fd23fa311516980bb8b324eed45e26bfbc3634457145562a49"
    private val jvmLinuxLibcX86: String = "a42765cf6cd35f8ca4ac619e288e57d0c7284d1fb32720a9cb9df72fd05eb070"
    private val jvmLinuxLibcX86_64: String = "355e61199f19b9ecd0c1fa3f65e7c18d173780a30f694f1fb031ec9e4cf1d900"

    protected open val jvmMacosAarch64: String = "33ce3c066af3fd0dd60896ab9f5e0843af5074554d933b48d34483f6343e81c8"
    protected open val jvmMacosX86_64: String = "eb65d4f3bfc2f9047d610b4ff6497598a0bb5823629c2931bbc2d6f22060f017"

    protected open val jvmMingwX86: String = "be116051c75e34cc46c4f3fb513dc07fc3b40d56c522fa317b8c546fb2908372"
    protected open val jvmMingwX86_64: String = "8556e3ae3e1fe4505a3c6e91fb08c8f6237dd8b2292c6b0db12fa16890b844fa"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "3fd82f85c8c5800f47256fb97abcfbb8f80ee823e1d6ae467afce5dc6f901bc2"
    protected open val nativeMacosX64: String = "501392fe1c3eef8dba0882b668ca88d2c630265ea46f7efe0d8316dd83e3a424"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "ba84603d352c19141f237b8255317ebd498cbcc72be470d6c718c2d92c8c4398"
        override val jvmMacosX86_64: String = "ad48ab80b7071fcceb0f43bf1bdcf2eb58be49725249f18b894e276c9d1e9f55"

        override val jvmMingwX86: String = "7b405c02c8f246dc766fb0aa47f4c9d4ae4d395c1367e8431f5d9f16eae2b738"
        override val jvmMingwX86_64: String = "03b864358fac77639e1e37468bf2feeb4e5d91686bb5c441e9da3d01126020b9"

        override val nativeMacosArm64: String = "e33109f2ff7db3126045ca7a7c1c5e90d47e77d74fab55650d53c8ef283c5dba"
        override val nativeMacosX64: String = "82dee47b59375a9e95225965d067e80a6812645d9b1eeec68daa2f7c9a58d95b"

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
