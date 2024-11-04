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

    protected open val jvmMacosAarch64: String = "60c976ce44689d1883e57b8c8b0b907e85e5cdf5b34b20315b2d7fd3e7bd2dc0"
    protected open val jvmMacosX86_64: String = "5d9af4677be452f705121c4a78afe31aae88fa1e779f08c936bdd911be5d19ee"

    protected open val jvmMingwX86: String = "a0cf6557ab2f87b9115e1309f5833086caea7458f591e192e8ddd0f70440d3d9"
    protected open val jvmMingwX86_64: String = "53617dccb242e60d513f55d2ce400216ccf031a70b28cf2b6ae7bb5afead8419"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "ae5983fdf6c5c93625805dca978b736b0ed2012904361006d820d9da8beeef6d"
    protected open val nativeIosX64: String = "6bb5d43d2048d474b669181adf70805c83b910fa7b2259f0d032445a5b59af66"

    protected open val nativeMacosArm64: String = "8f0a79779dd522e59e951becb466797f8607853ada2f1f103d85a7aab7d25131"
    protected open val nativeMacosX64: String = "4059ee51f80bf68982fe796a8d4f279014a46a344786baf166b47c298f866a22"

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

        override val jvmMacosAarch64: String = "dc70cc017d1c3e7cb85f8bbda4d3fa8080238d29b9ee892ba948839af0fe49b4"
        override val jvmMacosX86_64: String = "84ac408cc435b8d34f713da7274c0bf58dcb0ac63d027f2cef11010e9788696b"

        override val jvmMingwX86: String = "324ba74612238d7e20e64c7a5700fa453c3e4a245e3fa0e84f0987b6dfc1dac3"
        override val jvmMingwX86_64: String = "a279a263fa4abe063127a565d35e886968332ed1a778bafc24d62e26f7ab6ab7"

        override val nativeIosSimulatorArm64: String = "a1e8c46b33ba55ef979b262d9c82fdc24bc76aea133d62e436a95ec791fdc7b7"
        override val nativeIosX64: String = "70310680145d7aeea674eb2d60faf4c2e68f21fba7a65abc964909d4d1b19f43"

        override val nativeMacosArm64: String = "c9f307d0f4e13c547b80e2169cbce88084cd009e34299e8ebc9f86f91e696906"
        override val nativeMacosX64: String = "16b2cbfa797839aaceeae0ed39e39a0de9f10d6255ba74bf01a14a9a37699547"

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
