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
@file:Suppress("PropertyName")

package resource.validation.extensions

import org.gradle.api.Project
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
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val androidAarch64: String = "637236f58391ba88e70c325f719ebb622068cab6a50d714bafe8fdeec03a7502"
    protected open val androidArmv7: String = "142a40cadab9477abba5e8104e98ba705283fcb15c908345dc1d11582425c56a"
    protected open val androidX86: String = "2f527d66a70715696eb6c384f67d3cd4a968f1a30baa11a6becd5c5d8369146b"
    protected open val androidX86_64: String = "54b871f9368c7134ec65c1e324b90fd35324fe60cb84eed74b2e36b83ba90dc0"

    protected open val jvmLinuxAndroidAarch64: String = "d2a1ece70dcf2d80e047c52134133635fba01e5ddde8f8d4ad395aa79bc36aae"
    protected open val jvmLinuxAndroidArmv7: String = "bd956c9caa2fa963f7cd7c5a374d838cab949c3a184a60d71b3532a0c347d0c1"
    protected open val jvmLinuxAndroidX86: String = "d20ef2fc5c6469b75106dcc5bd9622435545998b97c8ca4a2fa1fe5e4f11b42d"
    protected open val jvmLinuxAndroidX86_64: String = "6259644a57d4762143c198d4c663f0a8f6da8e20b7b35b538febfcacb3a11005"

    protected open val jvmLinuxLibcAarch64: String = "1639575ad7a9b360fcf581b0449c80e3a4df578aabf55f57f2fedd5884055a3b"
    protected open val jvmLinuxLibcArmv7: String = "6f44a753638b3c74ac55f03b3e61a6770ea53418eada3c2afe1ac71a456129c8"
    protected open val jvmLinuxLibcPpc64: String = "cd1573d83d956cb1ce3b322e8ec7c2c38a9ba537a9407bdfe6f0408d43575212"
    protected open val jvmLinuxLibcX86: String = "41f4c2c8c3c9ac13e979eb173f2f6834c8b6e4f056f89a9e4f3a9836fd5e2048"
    protected open val jvmLinuxLibcX86_64: String = "400c7eb4075c75c5cfce924c578673213a1d169667413eccd88299873853859e"

    protected open val jvmMacosAarch64: String = ""
    protected open val jvmMacosX86_64: String = ""

    protected open val jvmMingwX86: String = ""
    protected open val jvmMingwX86_64: String = ""

    protected open val nativeLinuxArm64: String = "1639575ad7a9b360fcf581b0449c80e3a4df578aabf55f57f2fedd5884055a3b"
    protected open val nativeLinuxX64: String = "400c7eb4075c75c5cfce924c578673213a1d169667413eccd88299873853859e"

    protected open val nativeMacosArm64: String = ""
    protected open val nativeMacosX64: String = ""

    protected open val nativeMingwX64: String = ""

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "ef7437647df872e34aaeb586fe3d7bc4b6ae63bc922b7e1885c89ee62daf03e2"
        override val androidArmv7: String = "84598f95b030483bf19b1186a54e686da88003a156f8748aec0edfb0fa2ea45a"
        override val androidX86: String = "056297b5c0b8943f98e2ae301da521e28a52b4d10499f5a1be546702d3726574"
        override val androidX86_64: String = "327bb92389b009b5b015a4e270e8f9d34cbd4bbaf46e3ee7034a5d66fcdc570b"

        override val jvmLinuxAndroidAarch64: String = "671c6c3c5e9af457bce3547682f9e2af8bbe0715a1992924e3f8df57cbf3daf9"
        override val jvmLinuxAndroidArmv7: String = "4b7144c62a434bfa938714ab081c0750e5b92a210771f22e02e3dbdf95b38e02"
        override val jvmLinuxAndroidX86: String = "11a1b34ea6ffd09316870345f1f92f0c96f95e59f8264c09b1947ed681cc3d40"
        override val jvmLinuxAndroidX86_64: String = "50ec8bff4a9a1092d1ee93cd349eba91b094bdedc8223d83babca76e48385a76"

        override val jvmLinuxLibcAarch64: String = "5f8ffb0a195b46288f6d161298c9a71772b6427db7c7c86eaec0010a4e1ddd8d"
        override val jvmLinuxLibcArmv7: String = "b9a2b0f3954776822630e9ccd75091ef59ebc9d28bc2c46441377c13b87d3e39"
        override val jvmLinuxLibcPpc64: String = "d75f618c1db4f44b505b6242f039f81d221c3094fe360f0a4511d2a96104c37a"
        override val jvmLinuxLibcX86: String = "925bdab8babf9dd53d787f917128c8c4b86c9d4026c54e0f765f3a26d2a364be"
        override val jvmLinuxLibcX86_64: String = "be92cbd0be140b3311418fb06db9aa9bf21ecc4b6c4a216441b27489ca667bde"

        override val jvmMacosAarch64: String = ""
        override val jvmMacosX86_64: String = ""

        override val jvmMingwX86: String = ""
        override val jvmMingwX86_64: String = ""

        override val nativeLinuxArm64: String = "5f8ffb0a195b46288f6d161298c9a71772b6427db7c7c86eaec0010a4e1ddd8d"
        override val nativeLinuxX64: String = "be92cbd0be140b3311418fb06db9aa9bf21ecc4b6c4a216441b27489ca667bde"

        override val nativeMacosArm64: String = ""
        override val nativeMacosX64: String = ""

        override val nativeMingwX64: String = ""

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources() { configureNativeResourcesProtected() }

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
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // jvm linux-libc
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // jvm macos
        ValidationHash.LibNative.JVM(
            osName = "macos",
            arch = "aarch64",
            libName = "libtor.dylib.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "macos",
            arch = "x86_64",
            libName = "libtor.dylib.gz",
            hash = jvmMacosX86_64,
        ),

        // jvm mingw
        ValidationHash.LibNative.JVM(
            osName = "mingw",
            arch = "x86",
            libName = "tor.dll.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibNative.JVM(
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
            hash = nativeLinuxArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "macosX64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeLinuxX64,
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
