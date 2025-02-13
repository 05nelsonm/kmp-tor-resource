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

    protected open val androidAarch64: String = "8231ff66b269e994f94c5adf4a446e9cf9178b8dcf6994a04ccdcd8fe381816b"
    protected open val androidArmv7: String = "61cd7d49bf0b482c978bc2ec199ee1634d9a1dac4cf68becbcad878361801dd4"
    protected open val androidX86: String = "85a04d0a5bee84105f74c29cbf3d433eaedd9467c96d3404172f7dc88282ae74"
    protected open val androidX86_64: String = "0abbe17b74dd3b59326ecf58b5a8c9e80714f30c1ceca162d3d885fb126d146c"

    protected open val jvmLinuxAndroidAarch64: String = "a75ddbff2d66c74ff26bc97984ac3bd5c0911a192f93e8f02b5a17a53b7b6d10"
    protected open val jvmLinuxAndroidArmv7: String = "d9c3cbf9fdcb2e5878833816b8173602a25d675e447c5b2b1a0be19f84e89b9c"
    protected open val jvmLinuxAndroidX86: String = "64e63dd01f39989c8a69cde521e60d93b31a072cc569be6fe9e9ea51617fc81f"
    protected open val jvmLinuxAndroidX86_64: String = "073c0d6f213f65c9f22636282c1eda9d8cc70189c60a0c4540b8d6d23db35a22"

    protected open val jvmLinuxLibcAarch64: String = "4e7b61d03cad45c3f88ba76bd14994c6c6a39e4ef5f472ae6856a1df509d71a6"
    protected open val jvmLinuxLibcArmv7: String = "e16a68760b33b5c59e0f3c99d7dbb5a911be3c098b88b6647be3bd19354a4988"
    protected open val jvmLinuxLibcPpc64: String = "b0d33c0bc13760dcb966f51b28b9057addd887064eb45e0a0ae41a7087cc11a6"
    protected open val jvmLinuxLibcX86: String = "d6d046bdb17aeed2a61f52d65cea80039b7fba3cba03dbe9f927fbf2cad2f82d"
    protected open val jvmLinuxLibcX86_64: String = "b9fe5b160ea659f1faf03fb0c5104da77f05334807f985c8b1f72769d267d96d"

    protected open val jvmMacosAarch64: String = "f525a192513ee03ff628db1b7bac2d36653c730f2faeb1048a5c50271e4d68aa"
    protected open val jvmMacosX86_64: String = "52512e401f51f5e7479739301739e89528bfcdc5f0842f4abae49375d752c540"

    protected open val jvmMingwX86: String = "eaecc32b4575ecafd431386b62b8b6dcb6b799b8d4b22f9e6603649713afcfd5"
    protected open val jvmMingwX86_64: String = "fc258fcfd8b2f1dbba62c93786c01ee7eccef941b11536efca44b2e4064c7045"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "b06b902321eee9d8bcede0635981ecbee3ecf8da7eef621a8e764703766cc189"
    protected open val nativeIosX64: String = "bf9272d47f6400b1096036f5c0f00df9bd1b55163584fc17f0ffc0e8d38ede61"

    protected open val nativeMacosArm64: String = "c343831b2fea013688f5e0e10231acc9fa6ff887f20348d4b2ac7761ee663beb"
    protected open val nativeMacosX64: String = "6e376bbe8eacc328f8f59942b2ff9ac4e3b7de6e3dca96d9bbadc1280492a5c2"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "d8e3c10b19091fc7685a80b09b554ca4ea42efb6fb30a510678ec7f381798b45"
        override val androidArmv7: String = "d4e9fd0f14f003d0856e0b278ae4375fe402e3cf10c1de1223ee7b610d6e939c"
        override val androidX86: String = "c0d91129518a1eda7c0add06ecd76985ee6d9950f2f23481f9c21112e39dd07c"
        override val androidX86_64: String = "113a08031bc2f1f3fb4c80ee93f38a9aa49f318c9bd92878e5fbedc8ef1c433e"

        override val jvmLinuxAndroidAarch64: String = "a5a3cf83e23393055aa5ab812bad44d46b278519bc56890f3aebe9981cbbd4b4"
        override val jvmLinuxAndroidArmv7: String = "54bb70a291161d8c0d01e38e4d9d30e2575dcac6f10b60c3312227ed42e1d362"
        override val jvmLinuxAndroidX86: String = "b8776a9a0c91e1db40640c58deed00212f9f5b22548c8645cc1fab510ae857fa"
        override val jvmLinuxAndroidX86_64: String = "d068095a25f5d0221a693639565513778868f925d7286f3e7bdaab48efbedfa3"

        override val jvmLinuxLibcAarch64: String = "64bd5fe8a9a844361c4774561976ee8dcb062a5640a61bcf615c2bf3a749b945"
        override val jvmLinuxLibcArmv7: String = "c8853c0fe55fa29eec53acc7894f9ea0f54f6af9c92f8d62154c8958d6494848"
        override val jvmLinuxLibcPpc64: String = "adbe29b215fdb5581994c8ab87835bd8e7ae67acb9f459c2adcd3f0e84224e75"
        override val jvmLinuxLibcX86: String = "21a28e99c0ec250f35836410bde5e02dc921c08d26055eede9b179d2ef1834d2"
        override val jvmLinuxLibcX86_64: String = "75d1a334da2a6d77d50c14ad5bee38a3b466c16e7b3d3fd56410405acfaea76d"

        override val jvmMacosAarch64: String = "277aae644d8389ee08293c91f56cf7a74f56431df95b6a0f5eeebeee1cc5dcce"
        override val jvmMacosX86_64: String = "a5f6cdfa6487c5fe935d286f4aa2d96f7308967c00b5fed6adecd6da08c86971"

        override val jvmMingwX86: String = "b46daaab06d5e3d919f1fffd2c0e7742493294af2ea2bbcd7e4304861ddb2a0f"
        override val jvmMingwX86_64: String = "3fc274d7a1d2cc99023fce26eb993003c573ccdff0ccb5b433241cb6b2190ee3"

        override val nativeIosSimulatorArm64: String = "5b55e550e0f8edf44804063500fa645863f76a327c2d98ce3d19b99228221175"
        override val nativeIosX64: String = "a791243f435b17980411d298735bed3659e970c7049f63691cc8c043e09867a1"

        override val nativeMacosArm64: String = "e2fbd1c22325efd469f0faaf3607947c5cc63c170980c9fb6c143b503f6653f0"
        override val nativeMacosX64: String = "3d1caa46147e93e162041128ba89f960d4ffe6cb22c6ed3a73f7cf94526a0289"

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
