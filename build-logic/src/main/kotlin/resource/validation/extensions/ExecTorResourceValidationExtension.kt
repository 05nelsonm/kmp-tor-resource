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

    protected open val jvmMacosAarch64: String = "06fef369cac7d82871cd9bae352a6a9515a08b6a86e2066ff9905825ddd47893"
    protected open val jvmMacosX86_64: String = "2bb70d2f0ccdc78bd5e09cb8832daf3c700a59f64e5be6ea5f70002098ddadcd"

    protected open val jvmMingwX86: String = "4ad376b83e8e30b8b5a519080b3472f6b74a96451c61985607afaf15cf9f8eb8"
    protected open val jvmMingwX86_64: String = "d9642deea4b337ba3ed99479287f18b246571ffee97599d2ec5ac59cae29504e"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "7d6fccb563e12552839cc629c0423c90f9323db4216243c4bd472c0f5fb87e61"
    protected open val nativeMacosX64: String = "aa1ffc21fb69b3efc776993349923a06bb5ed9e068f23d07522ab0dff602a3c5"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "9c7d123f6e9b0949bbba769275103852db042b4a2b38ce93abfe656f749a848b"
        override val jvmMacosX86_64: String = "0cccec4166fc8a19f203c788408a87a01e573d038186c7a8748f89ecb6d8f6be"

        override val jvmMingwX86: String = "b0c53f728ae1446d2c5ab85588be7d76b9d47ae8b9314e8d50128416521b825d"
        override val jvmMingwX86_64: String = "b90969c996a8c8cacc7b7d0535cd15c5fa0c5993ac6c4ae637babbfcaf14c5cb"

        override val nativeMacosArm64: String = "bfe3e50b4c218fed3c20ba67f9c81b20a5ba39c67272a50f341fe5bf75bfcd12"
        override val nativeMacosX64: String = "f61ee69a540dda03d12a9171dcf4be91745f9d95243680eb81a75f237ce1f99a"

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
