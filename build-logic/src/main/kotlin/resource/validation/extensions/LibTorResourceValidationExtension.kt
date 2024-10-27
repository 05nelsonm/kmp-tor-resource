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

    protected open val androidAarch64: String = "6e4213f90cc495d1e4405454a0b3788331f9ef43629b1937e6f3676b0865dc1a"
    protected open val androidArmv7: String = "605d6222f4e288603d50f820842568414631179bb2062748d3eca2eff78926f7"
    protected open val androidX86: String = "35c047ce725e5136ac07cb66fa4b0dd89f7b3eefb770696fdd9897836e6135bf"
    protected open val androidX86_64: String = "bc70bf325bc9022bbec5a1cca97b89cd891466f6cb678344c2e82195e63cbb20"

    protected open val jvmLinuxAndroidAarch64: String = "d27f5a342c5b6578ca2863ccab638b0b2464d42b13e2ffa8e7e87a0d10d0b80f"
    protected open val jvmLinuxAndroidArmv7: String = "dd98f3b3b50e8ed31676cf151b94d09040687b3f3ec1e7e10ea5117421847d01"
    protected open val jvmLinuxAndroidX86: String = "89c39ee4f6f1c31f741122dacaff181b8e61a0f0e38a328b90c4803e6c9fd416"
    protected open val jvmLinuxAndroidX86_64: String = "e2244162ca06df31e386d31f29771f977f032c89f21a8e566fd0858739cb8674"

    protected open val jvmLinuxLibcAarch64: String = "5525bdcaf2d59f30a9844050dbde59c6528386cc6351a148375128dca47821fb"
    protected open val jvmLinuxLibcArmv7: String = "4f4396445e3f4886dd432350fb60df92caa12860ff5d50792ab29f66ad4b1f4a"
    protected open val jvmLinuxLibcPpc64: String = "d2b903e996c52a124a19464cc95eaaf6c428d974439a22ce281a6658e81e85b5"
    protected open val jvmLinuxLibcX86: String = "70b063f22b868558c5c72301f9a6c5c20d16cd851b09ff202533ba87cf107c42"
    protected open val jvmLinuxLibcX86_64: String = "92831a1a51b33582f32f3654b6033c3681b3632573471a116bdd92d08b408e55"

    protected open val jvmMacosAarch64: String = "30a02e2aa31d6e35626223307a1b1a89106cf29ea732d17a165497fab30927f3"
    protected open val jvmMacosX86_64: String = "d8aeb8a92a1541f0644d19cd75f8568e54d6e2aea7d5de93990496caf1467987"

    protected open val jvmMingwX86: String = "2bca08ee9414f236eb505dadd928fcd32f61eb62836a111d684f50b923770a62"
    protected open val jvmMingwX86_64: String = "9dfb7676292d37819fc7167217068b4bb3c31cc83c1edfd59bcc739c1e6fffab"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "e78d4ddd80bd059065a3f6ccdc3705b927e3476057514a05cf9fb6f889dfbe6c"
    protected open val nativeIosX64: String = "585411602f818e7c8c1ab9ae75cd0f2bdbaae1ada4423a9208d6c7a7b5ce8f06"

    protected open val nativeMacosArm64: String = "44756a54a68e0535f75fd9bff09cfb96a75c5bf71ac2627f7e88695a4cc54db9"
    protected open val nativeMacosX64: String = "71b007b2e233bee56a493a6d0083f1ed6e0d6fb3dcce1330c0b8e313f2045527"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "f79964b6c2afb953ecbe3cad8bed4f2504458873960303f5b0ee530cc39a7eb3"
        override val androidArmv7: String = "7f888516cc95361f311a61fc6e090bb4e81e4313d3aae97adf0a5ec250285366"
        override val androidX86: String = "cb0d47bfda7df8e7daa8af7a2c3bd8fc979b9818a22d17b1e71f611ee1c85271"
        override val androidX86_64: String = "03e415cb8960427b2d32e31456943da2476d93faaa0d90b0bf482c9f5b180cda"

        override val jvmLinuxAndroidAarch64: String = "218cc862ea067aaf26aeb4fb11d7b56f87996179d87e1b56405e04728b60c2f5"
        override val jvmLinuxAndroidArmv7: String = "c4ed60b592cb938ecf2b12f4e3de4a5e0afae942103800f6196e26a72687fc9e"
        override val jvmLinuxAndroidX86: String = "dacc0c2ae35dc3df94d18a53f4697883abca1e877481388178fa70e4b20061c7"
        override val jvmLinuxAndroidX86_64: String = "873ca84f97ccf8b38db563e0a247fde584b8efc7dbdd691ebae6de7fb3b88261"

        override val jvmLinuxLibcAarch64: String = "6926f351d0b999fd3d8fe8e1b3d378c82065ce0eac93056949b7e2c326c9459f"
        override val jvmLinuxLibcArmv7: String = "90405f143450153523aa6b9b6ffce83eb77eafe1864ef4f0dc1671267fbb3615"
        override val jvmLinuxLibcPpc64: String = "358847083e681fb34242fd674ab159542e5a9ceb2d6bf407cc8e1ddd4e4ef543"
        override val jvmLinuxLibcX86: String = "6300a5eee783717e3dc6bcbdf7d5633b55d656c0c295074821c8d417c1812a45"
        override val jvmLinuxLibcX86_64: String = "77bbb26d4dfcd4223e2a99beee4e05b26acbf23ec8e6dc9add7eff25f1c37564"

        override val jvmMacosAarch64: String = "bf6e4a2c321a8e1d891efab02a2dee980243afb63b5133ac7c933545bcd4a72b"
        override val jvmMacosX86_64: String = "5faeac7e58333f505143d03248789efc3b708eadd4efeea3880145d25f52f146"

        override val jvmMingwX86: String = "9f1a9fa6d2267155cadb3537ade9fbd53c0fedce7f219a7385cb5a48732ed0b6"
        override val jvmMingwX86_64: String = "7a3bfd0691b849292313b968a24c0bd511c034ce592f56884d5ccf0e4def7ef3"

        override val nativeIosSimulatorArm64: String = "c33cd85b0d549570e9bca3f91a429c558f8cbca5ab328717253cecf3966eb1a7"
        override val nativeIosX64: String = "4902e8af754f987f00a318c34999c5146e83c5d2baa6b9289dacf69caadf0296"

        override val nativeMacosArm64: String = "35283bfb7b5964de78f7791b666c08f845dbec52a9b95db240de0a8a9ccc77a8"
        override val nativeMacosX64: String = "c4802d41e8ef06f25d595c785f7034a401613452ad59fe206016e4d2b4514eca"

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
