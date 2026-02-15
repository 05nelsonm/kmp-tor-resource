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
@file:Suppress("PrivatePropertyName")

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

    private val jvmLinuxAndroidAarch64: String = "df6c7b3c9d348bd1f4667d9581be24d4db127a76ce26fec0a87a97e2ee1b701d"
    private val jvmLinuxAndroidArmv7: String = "ad16e6e662df8ffbb8e3568a1591857f805919b42fcc5ec87f0b507ac7cd3267"
    private val jvmLinuxAndroidX86: String = "2a09b41f866ba6d8c8b453a2c02da61f63f4e9462c6daf0af4170cee093ba43c"
    private val jvmLinuxAndroidX86_64: String = "4b24f9555d45cad122e87b7be2b94367a2f4bdaf1e8f6480c739a8e810617509"

    private val jvmLinuxLibcAarch64: String = "67d64240c27d4571935ab5d5a05ac870cf880b1f6f8189b06ef52d09979270cd"
    private val jvmLinuxLibcArmv7: String = "2635cde02071851b3b26e7af1ca62d64349edc5604411d472f8bc816ea80283c"
    private val jvmLinuxLibcPpc64: String = "04d1d5d14a8a0a7a2f56d5b6d587faa20f4a40ef203bcb2557960f6f757acb50"
    private val jvmLinuxLibcRiscv64: String = "a79912aeda2d09c31af8c03f796bba2ec85d4264b2ef1b18de2751a1f57c533d"
    private val jvmLinuxLibcX86: String = "7dbf5f18dc25a84dae0816af7de47685de8e4243d9a39cf6a5ba845c2695f469"
    private val jvmLinuxLibcX86_64: String = "eb6ac226e3e542b5ad039252ebbfd363ba7b5993e03179ccf78a8f1c0b113ea8"

    private val jvmLinuxMuslAarch64: String = "1ada319c711e3d74fc48aac36d3dffb080fbb8d8daebdd9ab8822ca3f60e14f5"
    private val jvmLinuxMuslX86: String = "33ae5f06bebac301bb78ef0507a54ea46f5897cc746f29a4b574ea36eae3bfbd"
    private val jvmLinuxMuslX86_64: String = "4772c877bafe8d10ec335cca23cc0080fb542bcaf4302a8de345180b2ce87a7a"

    protected open val jvmMacosAarch64: String = "d9be052fd27c1c9815ae6f9077d2e7de089565e9fd2b0e6c16b07bef16e63a04"
    protected open val jvmMacosX86_64: String = "5cdd86efc3043e998b12e82b88574b3879374b7c5785a3a75bab5cd577d56d08"

    protected open val jvmMingwX86: String = "52b86ece03b65a554af2e4f290dc12e04aacd9d22a7f74ed0ef3e1d81e164468"
    protected open val jvmMingwX86_64: String = "f1ea35f018ca6bdbaceaa8b373a2f71d6a5a86f02352072030f2912cb12a9a2c"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "52a8b63c6b69e61e612774865164c5397a95c083394431814c037ac821fb6c8f"
    protected open val nativeMacosX64: String = "b7695172fd48c9b5401e7765a080a122dd13522c56de92d61fbba371dd62eb16"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "ff7e844827b641b99ec83ce96cc3d73682d40513d882fd8bf0a19b0bd35d83e1"
        override val jvmMacosX86_64: String = "07f8d62f5ebb220ac45aa2516057719a5489c82293d26d005793278f25234028"

        override val jvmMingwX86: String = "369a8b64748ff89f24cd2e07e6370bd58aa4963c48bfbc5ced7216c96ec7d36e"
        override val jvmMingwX86_64: String = "9dd9b6892e9252cf58a7a5da16723558e1362533a2f5a9ea5aba48717a5597e8"

        override val nativeMacosArm64: String = "388ac822267c4464373ad9b3987e388f7f6d722b109ddcd4c2aeb9231869a08f"
        override val nativeMacosX64: String = "582ca719acdc1fe44257299ddbe9af7ccea0d45e979856c57e8a05532992fad9"

        internal companion object {
            internal const val NAME = "execTorGPLResourceValidation"
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
            arch = "riscv64",
            libName = "tor.gz",
            hash = jvmLinuxLibcRiscv64,
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

        // jvm linux-musl
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxMuslAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxMuslX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxMuslX86_64,
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
