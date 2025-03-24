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

    protected open val androidAarch64: String = "a68e788cd99aa0d91f591076d40275bc34d58b2190cbaa74a09d867ef1f00e02"
    protected open val androidArmv7: String = "72877a156ad5cc7aa7b9aab5f165799d6a88e0feb5def14d3acb58fe18af648e"
    protected open val androidX86: String = "85219b134e7340bbde713face194665dcb8156cad0fd440b8943cd1e74d81cfa"
    protected open val androidX86_64: String = "320ec564e7b111ff3572603e65a7d694581218cd2e88b7bc42c12050dec0e0e8"

    protected open val jvmLinuxAndroidAarch64: String = "d68c5aa4a5887d524c9da1e99f3944688ff9a36c0b052354ed47a60592f86619"
    protected open val jvmLinuxAndroidArmv7: String = "34556eb22d387ffc9491e153dcc8f6e9a025463e640f094fc2b55e2cfb59d059"
    protected open val jvmLinuxAndroidX86: String = "d6bc8dbb944e3a7efd23328844cc8299074448fa08f88ed82b31b189579c898c"
    protected open val jvmLinuxAndroidX86_64: String = "cb0208a4c158f032d4a4157fc8bc9aaab98e55f5557ec109670418f2f8b8f966"

    protected open val jvmLinuxLibcAarch64: String = "0b1b11d03c4e207bc70941cbd454a6bb23cb169c6bf1116aecb56feba7a78ae9"
    protected open val jvmLinuxLibcArmv7: String = "1f6dbf3fd3378f3ee7fe7cdd5cdc6a7d0102ca4b4ea6275c08121ccfc6692e10"
    protected open val jvmLinuxLibcPpc64: String = "8c3f31005354c92b74df5492e28c3bdb328db1f7751b3bd31955e22c66f35998"
    protected open val jvmLinuxLibcX86: String = "9d5e687d769be0625bb8fc4f760daa0db97796bd239575b474219f36f291c26e"
    protected open val jvmLinuxLibcX86_64: String = "c10148151f6d9643e80b29cde66838920c8f84dfae1ce62eff3902e2aa58cf7c"

    protected open val jvmMacosAarch64: String = "e70abc834690b83145c20ab60ab086d13e222ae8bce81c7217ebbb45f1976c43"
    protected open val jvmMacosX86_64: String = "1eefb8b60cdf0f5839fc069bd068dc2c528bedcd5169a73661906394925de2bf"

    protected open val jvmMingwX86: String = "6dbbc404ab9b63244290b2a86e167bace96020e9258df47714a7f8cad17a8999"
    protected open val jvmMingwX86_64: String = "c0bf11a3430ed29d733936e8bc653b4944e4908d6efab14851ccabc2b7d988d9"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "e1709333efe0e6f8ac4753e6fcb63f87023d62d768f3caf205807f531d91bffa"
    protected open val nativeIosX64: String = "ffbb934fb313fc97b43d311792aea724a27c6635594a5d30c418518712955a7b"

    protected open val nativeMacosArm64: String = "33fc96b0457513a542c5169beaf57e12de15b1db6124013075ba0597046d7454"
    protected open val nativeMacosX64: String = "cfade3515425207b2159a0ff6bc416d6165f0dff9191c17faabaf9e565e3ad25"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "8bd1a17cf0c0bb1f5156eb4d52d65142f9c78b019caf6bed0626f93e1782c6fc"
        override val androidArmv7: String = "02de5db087cd8abac8896993faa25d04646cd51d3f3a3caf56a666502a07d9d5"
        override val androidX86: String = "6b633ac9e85e4829972f06d15d186b5bac230aef0b743cd1fbf7563fd5b8be2c"
        override val androidX86_64: String = "de9d30e26f956f6eaa14b6918bfd25bf284731ba1b92585f9450f51774a5b9e2"

        override val jvmLinuxAndroidAarch64: String = "4e83273450b0b4e6e756838c0cd2d37fa39e46a31ce1f5ca8935c85d964e1c96"
        override val jvmLinuxAndroidArmv7: String = "2c1a5ac1231cad992c6a3184e9ebef015437d59a4ca040d5bdfe2aadbf02a67b"
        override val jvmLinuxAndroidX86: String = "1064677a2e87dec383dbef5e88254def2e8ffdf37ca71cf0def698f7c43e94aa"
        override val jvmLinuxAndroidX86_64: String = "9717e98c54e7016b39bc281c8165f806ba0eecf999ef11bb34c3f717fd5432f9"

        override val jvmLinuxLibcAarch64: String = "0909974bf899452834eb501f81126eae26d4514d200f162f4ff1a0d3c224dca6"
        override val jvmLinuxLibcArmv7: String = "96d695431575643d16cc8a8f2af8514afeb70f40ffdb9048afe39d6297dee4c7"
        override val jvmLinuxLibcPpc64: String = "7fb98e060768bbc8525c7d73415961ae36baac6636515f14ce43a013e09dc68c"
        override val jvmLinuxLibcX86: String = "231463870652cbd35848ac4229a6cf7fdb3217573d197843344cfe8caa1301b4"
        override val jvmLinuxLibcX86_64: String = "fab07991125bb11c626e65b45d6a1d5b427598655e25905aa34c361063e657b6"

        override val jvmMacosAarch64: String = "72ce4455b86e9e11f65c85207ec0119ed028bd8d08da09f7b97f7182aff8e121"
        override val jvmMacosX86_64: String = "541442ebf273e7a89efca4e1f621b1ebcb732130db4fcf0ebd47b359a0106900"

        override val jvmMingwX86: String = "9c7b46b7baeb8042d52f5dd72616c3be1de3981032123750d55b88e7bfc8c562"
        override val jvmMingwX86_64: String = "b51bcf5faf071e2fd5c8f3b45bd0a2d5a5865c8e32d2fd01907a73872975bde6"

        override val nativeIosSimulatorArm64: String = "7ff4243c4c431db8e82feef8b40bd4f3cc7e3fb70e7c6d4e3b7e44a4093d2783"
        override val nativeIosX64: String = "ca093685a373344054b6f102409bc7681d0d82f6749c628a25ce1ffd536c3d12"

        override val nativeMacosArm64: String = "8bebfa7cb49325ace81e83a504c9ec0ffca6669a6a87900d8b1532c48ecc5261"
        override val nativeMacosX64: String = "bc38b7f85a744368f1a96d94c793ead015ad979922ddeffb37cc9b8e26337e13"

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
