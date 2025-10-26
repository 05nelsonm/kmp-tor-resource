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

    private val jvmLinuxAndroidAarch64: String = "df6c7b3c9d348bd1f4667d9581be24d4db127a76ce26fec0a87a97e2ee1b701d"
    private val jvmLinuxAndroidArmv7: String = "ad16e6e662df8ffbb8e3568a1591857f805919b42fcc5ec87f0b507ac7cd3267"
    private val jvmLinuxAndroidX86: String = "2a09b41f866ba6d8c8b453a2c02da61f63f4e9462c6daf0af4170cee093ba43c"
    private val jvmLinuxAndroidX86_64: String = "4b24f9555d45cad122e87b7be2b94367a2f4bdaf1e8f6480c739a8e810617509"

    private val jvmLinuxLibcAarch64: String = "4eb67a0aa825c4737a8502e629823d456563d610b662bda3c39756a43b8c4046"
    private val jvmLinuxLibcArmv7: String = "2635cde02071851b3b26e7af1ca62d64349edc5604411d472f8bc816ea80283c"
    private val jvmLinuxLibcPpc64: String = "47588c8ca5d8a77b3eb868e1a8206a93ff594e9b045355d46a9e624efbbc0221"
    private val jvmLinuxLibcRiscv64: String = "a79912aeda2d09c31af8c03f796bba2ec85d4264b2ef1b18de2751a1f57c533d"
    private val jvmLinuxLibcX86: String = "7dbf5f18dc25a84dae0816af7de47685de8e4243d9a39cf6a5ba845c2695f469"
    private val jvmLinuxLibcX86_64: String = "eb6ac226e3e542b5ad039252ebbfd363ba7b5993e03179ccf78a8f1c0b113ea8"

    private val jvmLinuxMuslAarch64: String = "1ada319c711e3d74fc48aac36d3dffb080fbb8d8daebdd9ab8822ca3f60e14f5"
    private val jvmLinuxMuslX86: String = "33ae5f06bebac301bb78ef0507a54ea46f5897cc746f29a4b574ea36eae3bfbd"
    private val jvmLinuxMuslX86_64: String = "4772c877bafe8d10ec335cca23cc0080fb542bcaf4302a8de345180b2ce87a7a"

    protected open val jvmMacosAarch64: String = "d694a8c2576aacf12b70fefcadc154176a2ccd12beed931fe6998d7304f57499"
    protected open val jvmMacosX86_64: String = "26a11e15f3d7ad47468fa70834347578f505916c135c33c9855fb215c5661253"

    protected open val jvmMingwX86: String = "d97e26b2f479370875d12b03b2c44ad034389279c1528623d07dcabd8778846f"
    protected open val jvmMingwX86_64: String = "3fcb6bd2cca75f66d9c40b0db8455967e81ae8cf50024347e7fd41eb452a3ebd"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "6ac15714221e44f4db735411bfd6560c353d3f2add8096d2e037e1c1e7f0f7db"
    protected open val nativeMacosX64: String = "a7e547e0b9384c4666b1c23ee0258d149dfd0622bd724a795bb215e8d34ab054"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "0130d963b1c492e456aaf97b6191ba4a2dbaa235fed306f3ede2502844aa7c1d"
        override val jvmMacosX86_64: String = "43510932a297f1e3a56c3a05ed0feabd8aaca6e194d29e8d1f8647f5c59524de"

        override val jvmMingwX86: String = "ca819eea97766803670af67d63a63a9b4057900f6d5119e7385c0390068ab33a"
        override val jvmMingwX86_64: String = "2b6e4bb2429685709b83227db28ac74e893ee37f18a5c10a708ee4b83a9b8f84"

        override val nativeMacosArm64: String = "e8b23a23f14b8a4d712c0b58d5dbe72ace0fb8cf467535cdbb83190bdfdb6038"
        override val nativeMacosX64: String = "9dbbd6b32e891b15b026b44bfe20a7a9fb9368b5deff047cf83281053231faa4"

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
