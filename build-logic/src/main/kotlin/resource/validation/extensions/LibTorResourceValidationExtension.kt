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

    protected open val androidAarch64: String = "6685e0bfcd89a567d1dd8d9318d42ed094e6c01a1963f32bf70ffb3e2c3e7ef6"
    protected open val androidArmv7: String = "a01936a2fabfee41a9864fbdd4a88033af469d95eece58bfb38e07ec495cd719"
    protected open val androidX86: String = "bd9cc7df67e3f701c80d26ac8e018406d07308b93c6ba92bb613df938df2efd3"
    protected open val androidX86_64: String = "9bb6a748b235be2b0d2b1a481dea10a2b399431eef6717e242d72376a0d2389f"

    protected open val jvmLinuxAndroidAarch64: String = "8049bf5a46ff874ce7977dcea9b3c67895daa7f907f84eed68edaa20d22e0f59"
    protected open val jvmLinuxAndroidArmv7: String = "a399180c60e48e7491fb958a02ad218e24e7e2ad8cd4da0e5069851e12e424cb"
    protected open val jvmLinuxAndroidX86: String = "019e8a4ce0df482d3d26a4d7a4dcb10e7bb61067cb4fd9275f7f5059f489edde"
    protected open val jvmLinuxAndroidX86_64: String = "49876e4d72029789cfa5e7b6c51097c9ada06100b7add68391d6d3222d2bb6e1"

    protected open val jvmLinuxLibcAarch64: String = "8a0b6f94254bf84d3c376b46b8d3bebd86b8d8770feb7d3219df61d7624c3717"
    protected open val jvmLinuxLibcArmv7: String = "8193f9982d0715d2e6ce488997d75fa5f8acc21c7384eb7776badfa3970eeec0"
    protected open val jvmLinuxLibcPpc64: String = "f2f743bdac47863acf27918d962a9cc720ae0e1c9fa8693086deed55f4bc5f2b"
    protected open val jvmLinuxLibcX86: String = "55fecea053dee1f55b3f7110a6d0c29a1df0495b81a64fd4101543b36a4fe2da"
    protected open val jvmLinuxLibcX86_64: String = "1c70e76ddf369d11a239b031dc6fc124fa1bee4dc8890643f997215ac5d5f3af"

    protected open val jvmMacosAarch64: String = "d64e18bc078230f20de8c6fd32e7debef0fcb09ef7f6850066a73952b5a83d37"
    protected open val jvmMacosX86_64: String = "c0a515b8bbceddc8ef7cfddcf8932ced1dc31575b319bb24b21ec03ff157e4eb"

    protected open val jvmMingwX86: String = "d7efe0f256a266a3d0827944264ab7cae574211a998b49a41457543907df196f"
    protected open val jvmMingwX86_64: String = "52ee9cdecfc9dfb17e9f138e5ca1bd6435baab708f96c4d829998bdec678d33a"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeMacosArm64: String = "37de1785b39f12666cdbc002a36ca051169ce42ed839564bff55476bf766763b"
    protected open val nativeMacosX64: String = "be5267338f6c651e663390b86f932b55f58a0746fa1cec4b8037bb882c3c46ab"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "b4a15ccc4ac03d99401c57821574be2dda67210604123e91ccd365d682a2435a"
        override val androidArmv7: String = "a2db1f67830ea0c1fe57f690259a9f4b99b441dce48f0886bef00a4e1d6bd6aa"
        override val androidX86: String = "b0f1dee3f807a0a54f6cd21063d661110ce0d3b8a4b36ae7569c79af36626cb4"
        override val androidX86_64: String = "79c4bbcd4f35440fef5bd1d02f1116b97adbb27483902fc418ff6e661b7a6f3b"

        override val jvmLinuxAndroidAarch64: String = "2b3c43322dfd382aad5ea1ab6574a9af8a0b356034b1253660627fc3b8816d62"
        override val jvmLinuxAndroidArmv7: String = "88dd6c69a04a3c594de3a5e9367a95d8c79607cd53312936054ef920989e67ae"
        override val jvmLinuxAndroidX86: String = "492c90f43aea9f57b422f09a071fb40f9bf9e33959c261bd6983c2049d2ab87b"
        override val jvmLinuxAndroidX86_64: String = "f9662f0d8388273485dfa1089dd30a5ecbe869c95495d7868dbd098a3b447eb0"

        override val jvmLinuxLibcAarch64: String = "93959c9dad91e536f3c042300710ea2a0e89087146afc463f362ff39167f9c75"
        override val jvmLinuxLibcArmv7: String = "4ea3c1778c1fe996c466d1aa59c2cbe3400f286b0a0c88c60fc2c7fd44eaa361"
        override val jvmLinuxLibcPpc64: String = "8d487edb0bc524101b3176585061a20d215acb1c2787caf5cdcecfb2d5d9e1df"
        override val jvmLinuxLibcX86: String = "1373f20b5e367bbaecc4af1dee5d646f359291cf1726489c59811e4ef002c3de"
        override val jvmLinuxLibcX86_64: String = "3b902452cef54317f4ba27791c2e9eee6a94a5a613cad2262a23121761217adb"

        override val jvmMacosAarch64: String = "b1edf8664af8444af083aa3478e6e2dac865c88d98189dd764808313409f5ed4"
        override val jvmMacosX86_64: String = "a7b30a9be1297b6212518104a6beef6ed305971acb21d420d4936659b2d980cf"

        override val jvmMingwX86: String = "cd2dd86a3bc4f948bf757bcae0aabbfd227683fc01e00bad6ef1d3cf0e20020f"
        override val jvmMingwX86_64: String = "8b3af4b2aa50d0ca1e4dabc601572b9eec258a1c85d794f834017da821ad59cd"

        override val nativeMacosArm64: String = "57731b7d6e18f3807812e17d2bc255ef213c83cd3005cd2336bf9a6e5db95214"
        override val nativeMacosX64: String = "9ecd927f4ad8554a1fdcee7905010b44ddc8eb93d59029d4da08a97cfbfacd40"

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
