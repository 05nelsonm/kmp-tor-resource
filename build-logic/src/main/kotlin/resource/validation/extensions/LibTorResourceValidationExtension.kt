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

    protected open val androidAarch64: String = "3d3137cde09db30b6ce0b9f02d420c0aa73c9f32993f6f448994f8ed14570506"
    protected open val androidArmv7: String = "c3d4bb1aa8af9ebe03cdabd6721ad63877bbeabedbb011663fa7024780e89513"
    protected open val androidX86: String = "f332f341a832e428e3cc45a58f4549d271f5d00f597729b8020172c62ef39aee"
    protected open val androidX86_64: String = "160cb7b936b5c4e2e596394b379e7d1e6dcbb9b4ddb55a39845394134bd9d431"

    protected open val jvmLinuxAndroidAarch64: String = "3899d2b5d36efb678f8320b18ee5ae38838fd038e32890e06c1a45ced0ceef9e"
    protected open val jvmLinuxAndroidArmv7: String = "4a1a1e1f3930f79fd478494f426800201815de2be45009968f688d791681d789"
    protected open val jvmLinuxAndroidX86: String = "a252ac555253296ada2a388cc1483de91ee60b60ebc42f39b9d40cad6059d548"
    protected open val jvmLinuxAndroidX86_64: String = "b37769bff11b21ecead86b9300b14f4234cc90ecf0596a7e46a851dc67653300"

    protected open val jvmLinuxLibcAarch64: String = "bb66aa0bc3e341873b6fcd3345d30d9ee5238e900e1c30fe7cc16d2dd58db0a1"
    protected open val jvmLinuxLibcArmv7: String = "d29b129be5646c0089f20833f9603957cbe045b12905db8ee6f8f2d62ece697d"
    protected open val jvmLinuxLibcPpc64: String = "70b7c54edb9a391d2c567fe36ebe0b7b34c902db74c8c6c4939480ee04ec121b"
    protected open val jvmLinuxLibcX86: String = "9b99fa59555ce4f7dfdb74ab7bb9357938f95eac7424fdab793a8d27e46a7440"
    protected open val jvmLinuxLibcX86_64: String = "3c800fd909ebc9f2738b6d5316cdfe036bc7e9e61078bade256d83cb824db759"

    protected open val jvmMacosAarch64: String = "52eb828302d33843b0b9c6bdd4f1f5719d3a1f9ef50ae95289554d1433b95628"
    protected open val jvmMacosX86_64: String = "1c2fdaa9c6e15f63f6d0becccd2876700058ca6489a8407b5e81995747bd73d4"

    protected open val jvmMingwX86: String = "2d40627ac861cc2977e47e7a8a3e61c25fbfb7707183d8cca00d5ef286370c35"
    protected open val jvmMingwX86_64: String = "4e0f3ad76d0e992fef20881b2d34d353fa46ab6415adcefc13dd0e84e98e33c0"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "264206b39a8e2e338dd4d317b3b4a93bcbd03cf7b8975385cf1302aaa727cdbb"
    protected open val nativeIosX64: String = "48bce67afd601ddb4c2609e845e180c507238f0439d7e853976e83d0015353a0"

    protected open val nativeMacosArm64: String = "ee2c7478f450ab075cf7553710677d0d4589265f115f36e48234893f02dd6158"
    protected open val nativeMacosX64: String = "e5788eed1023be88f526b3785307d64e087ce85128d4ce366017b7474cec1ffa"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "4818494ae242c5e13fbda7ea01fb472670e20334b29e5de965b0fe8094bb9620"
        override val androidArmv7: String = "c8671a5d231361548674388753ff03d230b69460d25b78a71a923b6f91ee74c5"
        override val androidX86: String = "3125c6d9f73a19c3f6f7a12faebb39d9dc351854fbfa0f49524e07dceca9c520"
        override val androidX86_64: String = "af1c77a2e841be12f3664cc57a1cdc5f1db1de563452d7a5343bfbb7be755250"

        override val jvmLinuxAndroidAarch64: String = "4d0762611d5144f6369ba275a216fc1f4f13fe81557722f8101d3152ab1952d3"
        override val jvmLinuxAndroidArmv7: String = "510b3d439544e434a34f92c785437df00257385ee0b907895e4ad986dd7370a2"
        override val jvmLinuxAndroidX86: String = "2fdf1ee363bf49a35c5b5305d61cc37c7c10b89fc4e4402292eea50c8e288d93"
        override val jvmLinuxAndroidX86_64: String = "0baa06a8ec8023648af3da7314481f6b840815c06d8ccdd58bb869aab24d0b61"

        override val jvmLinuxLibcAarch64: String = "77f49385eae4f836202f76fbd78e4d3fe516fc3e6961b0d1688f8fb8c62f9440"
        override val jvmLinuxLibcArmv7: String = "7e51ce2155f1625690b77a933cd97d95e7da487d319b06fc60db81a3bd463921"
        override val jvmLinuxLibcPpc64: String = "5be309cafdab7463c623cc4909356a7089b330a4150e490878d7cd0289db928e"
        override val jvmLinuxLibcX86: String = "067c29e0f5b53ff16c46eec182c291fd248eb5ec86a76424bef4e1cf2928d79c"
        override val jvmLinuxLibcX86_64: String = "ba3741ad7f157ba62eb8747a10d53105fc82dc29298279b28ebb2353764858cf"

        override val jvmMacosAarch64: String = "448d5e1c7b5e099a636be7c101c0a64fedaee5de6d63b8bcb8301c9df4180b59"
        override val jvmMacosX86_64: String = "d81c42b3df706542eae779c6a73cbf312039bcc9d7d37896938594a97f64e53a"

        override val jvmMingwX86: String = "3574b04bf3381dde984861437eb6d50e49104328620e4219d337c94faa4db6ea"
        override val jvmMingwX86_64: String = "cc854f964edcc2f91e8a3535f3ae77f55f45863f8828ab610e36c34b9f96c7c6"

        override val nativeIosSimulatorArm64: String = "1474918458dc8cad8c43754d5c948bba39d266a50b816dd8c99fa11785091d1d"
        override val nativeIosX64: String = "f520d82dcc1c3b77bd28722db2316ad2b32644b3659db6a1015f34adb2e6fd97"

        override val nativeMacosArm64: String = "f2562c1d52691cd83e5bd3fc777cc79d8d7cee59954fc5c4978d8c4efc71c10d"
        override val nativeMacosX64: String = "3e88b3c35ea3e114e9e2c4f063ea5c8d6092b5ab0e0814a3e361eb0c60bcb485"

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
