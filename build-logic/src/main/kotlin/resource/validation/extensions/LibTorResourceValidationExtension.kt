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

    protected open val androidAarch64: String = "72a7ccffed79a9182209be34b3e5b1dae6a98eda98807d5666a9045e0df035d7"
    protected open val androidArmv7: String = "e2953949b545dae85ddb2e7f107c333881e61f70f6e212dfebad28a94ac7810d"
    protected open val androidX86: String = "49ea0eca1e16741163d1081700afd1d06a6541cc1f1d6f5257bdb71f644fda2f"
    protected open val androidX86_64: String = "a2d3a40732508d2f5f911c793a6d36e1f42ced1fbc2afc627bdf5b9363b89fa8"

    protected open val jvmLinuxAndroidAarch64: String = "81b1aa62e74542d204994cd78e48c90257bd5fd4448a1b52080965bdd2cda265"
    protected open val jvmLinuxAndroidArmv7: String = "7fa29d1acffcf369983754cce13c509555874f230a6db52674b3ab524d2cc976"
    protected open val jvmLinuxAndroidX86: String = "8e1a6b195e166bcfec6025568480031cac60f53796d33122830b558a6789eea4"
    protected open val jvmLinuxAndroidX86_64: String = "1d1ab0ea80dc766db2217ba7073f2f053c501d6374ffc18d1fd412495ad51a8c"

    protected open val jvmLinuxLibcAarch64: String = "2c186b33fee12e11d089f2172a4c62c7e452d4127a903aba5e94d6c9631b97bd"
    protected open val jvmLinuxLibcArmv7: String = "b753e80a790efcad9c6d11d5d0dffc3bbd2dd66ca386303d7a3104686e14b415"
    protected open val jvmLinuxLibcPpc64: String = "4e9719bcf04b0f256695b825c977a65aa8d056bc3f4a2f519c5c1f915ef6be69"
    protected open val jvmLinuxLibcX86: String = "59e5d187aded37ece858fb984bf05795c9cd83b27348ee0166563368ff7ffb0e"
    protected open val jvmLinuxLibcX86_64: String = "a346636ae47a9a5228c7db5c23d2a16410fa7d81485a16c634e07b606734b8c1"

    protected open val jvmMacosAarch64: String = "d0d426f91f8b0f98236e58ed0c66c2bcc0b5eff955368e65a257559d2cf21f1b"
    protected open val jvmMacosX86_64: String = "64c73eaf43bcc5680a794f5342dd56942b85ea14c57b91ca6234f861cc276f1d"

    protected open val jvmMingwX86: String = "0e0f4a2bb8f09982a69508c016d6059ebfaee53ea7729a48fcf0ac3366ce52b1"
    protected open val jvmMingwX86_64: String = "4296743b066bca212fc6acd1068a226ff7cf2bbd9c135592c33602b1829f7a25"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeMacosArm64: String = "fdad4edc89290ec784fe3ebd4e79551c2f78081a7f2ddf235bc6fcb5a14454a4"
    protected open val nativeMacosX64: String = "c46dba64db917a03bce277c3d4f3339d0da629ece213a80686a67e07f418e824"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "ce65aaf5246ac0a5eb1b0f3a1128acd765bfa281937883da1989305bcbace5b2"
        override val androidArmv7: String = "bbdb57dc5c3f5fb1ca63dc1b8969dc4dcd91f37ba43dc5dce14ab2d3a2b9e2ea"
        override val androidX86: String = "ff4f2b7b10ee5ac465b8e7d28411246b4631346786e315eb9ffa681948467211"
        override val androidX86_64: String = "1003e017ddebd2bcc00a110d098b7aaf758809a78330571762f5563ed6fed921"

        override val jvmLinuxAndroidAarch64: String = "badada28e6d4c23aa0cba6d54a9e91ff0d418ac5f274f65db24ac5838eb82254"
        override val jvmLinuxAndroidArmv7: String = "0eaa2bdf8d712f44fec976bf8dc8d27599226e087c2ca68364ac8f61e509d332"
        override val jvmLinuxAndroidX86: String = "41265a92987e0f77fe21c640957ce441b0edcd2a53fb3dc318e974bd464abfb8"
        override val jvmLinuxAndroidX86_64: String = "652f6233f7923ab37c8cd1b40f33c0ac48b6f540089b564bab226a26c9e60b36"

        override val jvmLinuxLibcAarch64: String = "e3f91f8f510515c8b274f47056523849e6e59d4fcfb5989427a1d33bd5d16fc0"
        override val jvmLinuxLibcArmv7: String = "ba33c7b0c71001096e92dd535dec8ab7d96f160b68141c0733287b7c6121eee6"
        override val jvmLinuxLibcPpc64: String = "a3f12daadc97fa5c6d5eece8316ae68085ff37951ba8f27b56ea6b3d4e17065c"
        override val jvmLinuxLibcX86: String = "82a40421cc40bd6b94c70da49e137a1786c5703cbde4fdf24123cecc63248f16"
        override val jvmLinuxLibcX86_64: String = "79a2eeab9e1dda9496b9d8cee81bba11c156eb41c48f3682937705b812f546bf"

        override val jvmMacosAarch64: String = "9aa77e57911a7948c8952da3749ee4c6e9ed7436fc392ebfc458a36fb9749e11"
        override val jvmMacosX86_64: String = "3b756ffc9bb1a490a7126a4ed031126f2b354e67e78d03a61b9d7218f377a611"

        override val jvmMingwX86: String = "85880460ef6be3e94296793069e3ba2d4ec91df90950da7f2865760b943554e3"
        override val jvmMingwX86_64: String = "ce351edefe807b7b67a621acbbf92691dcba3f4708d361a17a6d9c126758a16a"

        override val nativeMacosArm64: String = "85ee0dba355552a27a6075e5daeb6a22a868efec8d6be9ea20b05a44c2c7c76b"
        override val nativeMacosX64: String = "d14a5cf144e2ad3e72d50cd4d9287e3116a406cdf00ddb5092b88d3cdc6f0bb3"

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
