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

    protected open val jvmMacosAarch64: String = "f5e26b30a878482b7eb3591271af13d0635db9b48f5d0313c1e8da8f11d5fcad"
    protected open val jvmMacosX86_64: String = "39128c1e1a6530078ae8014662293c8ad664ffc6ced90330c4c16d23cd47d8be"

    protected open val jvmMingwX86: String = "0f2f584b221dd74b9972d899b8658b65783280e10ba4a3db809815a3b3db102c"
    protected open val jvmMingwX86_64: String = "cdb54c84fab26c2ff5ebe522081f905c1f48254402a4a3b6ed6afa38f6029d3a"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "ca4cf42b66609fc1f059c4d6138479f523ef62f45d2614b85dfa39cd6bea1ac5"
    protected open val nativeMacosX64: String = "4c29aeee4cc9fca4117fa824f49fd0c83861c03a8ee751e38e9fbe444521b7f9"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "270dd4a88f3c90b9a69efc9eef1a15b74c6bdfb74f443a2258bfead30ca64b33"
        override val jvmMacosX86_64: String = "011bb47d05d553d571a3959b2c2425e1b7930294181cf766d4e3ef7dbde9ea1a"

        override val jvmMingwX86: String = "24d95adf4b80fd55be4617c7da8419ace6cde5f575f2d34a37c17dd330ff9dbc"
        override val jvmMingwX86_64: String = "40f017ef989e0784717536736243a7a8e12120e37ce0f8ba3413c3b8c83284a6"

        override val nativeMacosArm64: String = "0bec0546a08b93f1f2c118d59d2505ab6fcd54439ba8eef92fc27bdd369c4d61"
        override val nativeMacosX64: String = "6eaa7cbdf48bf7155fafbe08e769f9468d708a54fbd2c3c0aa1e97232ff72dfc"

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
