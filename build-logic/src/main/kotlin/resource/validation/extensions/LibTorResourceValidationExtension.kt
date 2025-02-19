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

    protected open val androidAarch64: String = "35bc037047290f44144aadc20b5d1561a2b80b022dc9b524e5ba76a5cfc8aa29"
    protected open val androidArmv7: String = "eca9e81be28948402614a496442b16663eb5532b587eef9b7e4627956da55dc6"
    protected open val androidX86: String = "e04ee053ca8c08d06953b77cfc7ed9d8bc162cd0ffea711f88e703b249e5490e"
    protected open val androidX86_64: String = "550d04fbbb03e7de4cbad501596562c70d40cb669c98fe0e8cf26b5adcb246e4"

    protected open val jvmLinuxAndroidAarch64: String = "db0b3bd2bc3df4c10327b7afe7b5304b19414dc7844457d493bde65c53f968cb"
    protected open val jvmLinuxAndroidArmv7: String = "0ddfb6760c7fe5b04e78c8e72e13b89f1841ed8c7b513188b57631eff16c3100"
    protected open val jvmLinuxAndroidX86: String = "ccacc817437762bf09eefdd4a1b1138bbf63df5d120326de0397c47a58c26ab1"
    protected open val jvmLinuxAndroidX86_64: String = "cb8a553ec7f574bcc842fbe30ee252fb1376365dd291e0d39d58b484b3a07f8f"

    protected open val jvmLinuxLibcAarch64: String = "f6c452236783c22540c74ba90e3d173b879bccc78ae0ba5a9e9efda949396e84"
    protected open val jvmLinuxLibcArmv7: String = "3ddfd9d8bdc98e9f024fc500955b638be07fd19430d90405358ab4259d92b24b"
    protected open val jvmLinuxLibcPpc64: String = "cfc5006e6ea211eae6d69a0c6e9acb03ec87bccc1d3f0a14010fff742d0307ac"
    protected open val jvmLinuxLibcX86: String = "1f27caba9b3514cd7eeb220402f0748a2eed8c3a56bb324ac1ccd11d7fc6631c"
    protected open val jvmLinuxLibcX86_64: String = "6127530ec0f93ca1589ea349a05150df45fb818f5a60defa2bfac75bf7cd794e"

    protected open val jvmMacosAarch64: String = "caa5fd1c50219f476c164d0c2e5cf4d025afd357bbd996b5532d793801032e0b"
    protected open val jvmMacosX86_64: String = "dce87d774f1697d800b174c2a4487c9e5e344ffc1d2b049aba6d592a3c2b2639"

    protected open val jvmMingwX86: String = "099773ac22aabd305b4701fc67b54f49b08ff6a473abe6a029718f3a033e11a4"
    protected open val jvmMingwX86_64: String = "ec3be8ce2c339db2470b18636d30b316b7c79e871a8c2d0f44f13c0525c640e0"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "896846a8a7bc0963620781938c43eabdf9bf8e3d78be2d7444be8cad08195974"
    protected open val nativeIosX64: String = "7aa233e490a1e24af6d4dd84662f6ad5119659730b432cef2ce33f68763eba00"

    protected open val nativeMacosArm64: String = "717990349d29e55eef0dbf4a458ffc89d8489e36b739ad8c213a9a17bf996fc0"
    protected open val nativeMacosX64: String = "559f68a84f87d2412abe99c72f23d52a3abaef88b436a7d9bd8ab9664d79ed29"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "50eb5e7e70d4e754927a7f19798d9500cd44002088862caaa3b96cfe6c286351"
        override val androidArmv7: String = "b463b44afe166c22855eacfc0eb0736fd63c550f6b12120913c91c59a581ba4a"
        override val androidX86: String = "c831af49b0f28fc4365deb6f399e77c58fb9dc63bd98e5350d7e9e6a20e777d3"
        override val androidX86_64: String = "fdf1f6d2f21d0ad772eb8a4ee6cbeb401ed4a15e5e60b67edb77b7ad8afbc713"

        override val jvmLinuxAndroidAarch64: String = "2c5c726ba153224c848bebff9b083b8f101972d4bd8dc19fbdd01306e5c962ed"
        override val jvmLinuxAndroidArmv7: String = "f3e6dc52edb976bb7930da795486de70582755cb6089f016b57b238ad53b975d"
        override val jvmLinuxAndroidX86: String = "a1320d81f1c3e8537d8fca08c144f26c6e219f381d52d191d1872debb417177e"
        override val jvmLinuxAndroidX86_64: String = "609a3e6bd807b896f52d509e5c0c1b06d65bdbfe4e1bb053e4bf003902091ed7"

        override val jvmLinuxLibcAarch64: String = "5444be4cfd16c044a237ee6a39b70c7753514f755a5ba87a0316cdf8d2dc89f3"
        override val jvmLinuxLibcArmv7: String = "5643e8d00f04b3e38ddf07a8207fe87d598cc420fc66d9d650b5e6e3be1a18ce"
        override val jvmLinuxLibcPpc64: String = "b53f80ae8cb42c5ae8b0b7da92572d18c2127fb3d4c54dc54ff64605d2cb9be7"
        override val jvmLinuxLibcX86: String = "7241624e9c0f0553ee2f78f4d669e47133b95199ce63b87c1c5e3e30e0f922e4"
        override val jvmLinuxLibcX86_64: String = "119016f01d9d1cf4e7535857b3d71b47c60b5c579b0db8a5fb674a3c1dfc1f56"

        override val jvmMacosAarch64: String = "9371686ddb063fe2a13429d374cf0ac3565f9bfd9b78448df5eadf4730a1a2e2"
        override val jvmMacosX86_64: String = "a74f0f9a4b5c3d642020734f5b842b088ef8896ac840c606252cbe3ac91a5822"

        override val jvmMingwX86: String = "bd04210972d64e4c7a576f38f422109bfbb49c52b4e519a3d49d8defb72c18e0"
        override val jvmMingwX86_64: String = "c27b94e91c7ee4c8d9686781c98fbe52312b6a4ff567416d7b4b6c3a5576f71e"

        override val nativeIosSimulatorArm64: String = "f7233be54559ab9573b9921d27252e66b4bceb54bd615d28b7bab74187cfee96"
        override val nativeIosX64: String = "6c5d2f15cb6a0dc4263f9499eb9177a019363bd81648d28f4db2d6d8667eb5e3"

        override val nativeMacosArm64: String = "cfe843a8bb425efa166229b1e74c5b7da937a9ce28a17675998f680b2c4aca74"
        override val nativeMacosX64: String = "8be9e44114e6bc9b44b1c4e0af50ea1f679b86719b7b0ef7a5608c7b5bcc9eb4"

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
