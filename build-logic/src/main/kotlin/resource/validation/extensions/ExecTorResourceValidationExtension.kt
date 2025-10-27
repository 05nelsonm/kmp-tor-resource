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

    protected open val jvmMacosAarch64: String = "387d2884750fa1707b0b3329724e6556dd120d797ab1973a6619d545193272b1"
    protected open val jvmMacosX86_64: String = "512928c762bd45e7c0253475afbca8051b036690c5c86c3ab18cbe1fbb02ff74"

    protected open val jvmMingwX86: String = "77441b2821b2c6e275569bbb1e608ffca594f7eb98fa41d24f1cb0a578cc523e"
    protected open val jvmMingwX86_64: String = "8a173419e10f7028455e4a05169e96731fcde610cb29743c60706c03d25af6ba"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "3d5ddd290efe4f211ede63c0c2d74fff6cb3cbb8de91a06309f35e4a106ef3c7"
    protected open val nativeMacosX64: String = "abed43755ba418082a88e3c3de939ec386af8bd7bc4d3c28e2f3381585c38190"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "f5a2b09db074f35fb0d94e4b047067f9afc3a2c883bbdbff02f4fd5559d8615e"
        override val jvmMacosX86_64: String = "7cbd78f093e87e449fb3bae171588d40e618baa9f23868b4b2158c99c69b8c40"

        override val jvmMingwX86: String = "40ed7e9c5089a95a4b98e2329d478fa9cffa6fddf6fc3b6632d62a6287305946"
        override val jvmMingwX86_64: String = "67561b0b0ac20d931a88aab4b5a4fa513100e46488031ac690a13c2f62819956"

        override val nativeMacosArm64: String = "38cfbf7b7c5e5b65e75a0e4cd1c86710f46b618bda76964a285f3d7b9680008f"
        override val nativeMacosX64: String = "1a17c90f6b772286f3c6fabbcd8b1160668d705ea87038700c41211f6ef7b859"

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
