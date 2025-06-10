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

    protected open val jvmLinuxAndroidAarch64: String = "bf45da1a793bcf0908e03b1da009ba604c86fb772433c4533207f4e1ed737f03"
    protected open val jvmLinuxAndroidArmv7: String = "9f3cf516d2d0f156aac2cc944fed73f0a189115cf7015fbaae6fe918dd3ffc66"
    protected open val jvmLinuxAndroidX86: String = "cabbddbdabf54e4f709341a8c164c805c06ceedecd03677cd138a7cd158f3fbd"
    protected open val jvmLinuxAndroidX86_64: String = "6d8afbb9e113b509c5c54df6a58c5d7285f77f33606bb920ed69bb38382ca06e"

    protected open val jvmLinuxLibcAarch64: String = "92ca17318df7c97bb0be5d063bfe9aa178636f5cb1fd477c5fdaf4dfc5e38622"
    protected open val jvmLinuxLibcArmv7: String = "5c444ad5551f35f4443df6c90bbdba8e3c6abdfbf3122c967ef248f9d06cf9fa"
    protected open val jvmLinuxLibcPpc64: String = "765920073273aceb70eb849ea5ca6e529f55382f61fed29de3fd1e4823cfbcbf"
    protected open val jvmLinuxLibcX86: String = "0c54f2dcc675a08cc88f4b8c9edc919199e633d630ad005df5d4ca0925652172"
    protected open val jvmLinuxLibcX86_64: String = "56db54c943f28dd4cd983981ffa08168c2ca653091a62acb3319c7126d5d28ad"

    protected open val jvmMacosAarch64: String = "98124480cced58ca497695ab5688fb007cfae79a1976e3ba33ad2a99802a1046"
    protected open val jvmMacosX86_64: String = "690a6d245793f3c658da092b509cc517a166e635cb3b7d2fd2e9398e0cc3b7d4"

    protected open val jvmMingwX86: String = "b6e55af6be991250c6e8e55e51178a46ccfd6cf597e389744c02dd9897e3134e"
    protected open val jvmMingwX86_64: String = "f2ff5c48bde17f7036f3466ce5ce105675e80d8b4236ef01d2ffa11accfd85b6"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "a5e992000ca9fa76663e6dd0aea720efbd753a32787a94b22331d0e0da6adb75"
    protected open val nativeIosX64: String = "d9d716969c9aca501b64ce14073e6769cfac99a93867650af33f581c5e1ba629"

    protected open val nativeMacosArm64: String = "1ddd0c2116a0c3bd719b08ce85522eb83cfa024311caf0e722d8df3d4b212d86"
    protected open val nativeMacosX64: String = "9b1c76305270bbb2cb92194cfea25c9d3cf143824d846a90736b6cb95e3915e1"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "56801ac4ffc8d9634e52874dcdc66ced81da340c0c06d38b31fb5152f75ba463"
        override val jvmLinuxAndroidArmv7: String = "a6c6078a67a9a430397fef7521d583349ee83b27df8fa3b153701f7e1316d30d"
        override val jvmLinuxAndroidX86: String = "3ece02e3f95423605f376ae283d25accc2c20f4dcc69e3164dfbf53d70d40aa0"
        override val jvmLinuxAndroidX86_64: String = "6e9a7b8b2808073be63cf9eecacc0881fa7846011f98dae6fdd74512f4b72642"

        override val jvmLinuxLibcAarch64: String = "2da77541d55acb6ec7e1ce1fdb73f4ae914cfdf69624a0d5a85376a9e534aba1"
        override val jvmLinuxLibcArmv7: String = "d939db4e753a5b8cd56f40156eb250e7d8f39f281932066ee4c05566fd2f67f1"
        override val jvmLinuxLibcPpc64: String = "094019b1c0c9e291004cad57b0db560fad5ae8ccb84b88fbf441400520c90ef3"
        override val jvmLinuxLibcX86: String = "bcba3c83580f825984d4a9b1b36ec617adb03059c8adfca10ed5b5e2c3723aa8"
        override val jvmLinuxLibcX86_64: String = "351a1ee4639580075615b43d250735392847baa05be05837c55f99c28b918d50"

        override val jvmMacosAarch64: String = "87c420ebbf48e8c38e10476ea7de11474cca473e6fac9a97bbf259f3e1299d23"
        override val jvmMacosX86_64: String = "83c97d61f294343e066c792a71308ba9c9cd06f0be766e635661ee399b690176"

        override val jvmMingwX86: String = "3934c20826793c7566f813b237b185ba7a94a29e0256efa2b616cd147a908a14"
        override val jvmMingwX86_64: String = "69792f6e9c313dc73bf4e3f2e8210984478b8e98ea878c10fa168012cc9eadde"

        override val nativeIosSimulatorArm64: String = "0e93de2dfa2384d0a2ec4eaa538fc7c954152e9a161e64269e45fa9ad38a8046"
        override val nativeIosX64: String = "10fb896a46d9931abfbeae30a67ff3bf30b9199cab563263843c3ddcc7e8cb5e"

        override val nativeMacosArm64: String = "abfa5ad57f19292816e1bcabb490885e8e3e81a74508485e7ac1a04ff8493e05"
        override val nativeMacosX64: String = "e25dfac40640e15c632a2559bb325e40da4749a44c06641cbc601a221f791985"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
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
