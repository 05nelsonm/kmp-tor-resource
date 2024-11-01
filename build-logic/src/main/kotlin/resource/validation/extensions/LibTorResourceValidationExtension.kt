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

    protected open val jvmMacosAarch64: String = "45118290a2a31db19b5bbf6579ecb26ce51ea57778534188c18cc338c3cf728b"
    protected open val jvmMacosX86_64: String = "0389b8fbb1fca91e65f57be996626c03d111d20b54b2fdbfcbce462c48c5b85d"

    protected open val jvmMingwX86: String = "3285c3d3bc04be991545e2ee01586a5b3024dc85b9075863d297cc00b776ecbe"
    protected open val jvmMingwX86_64: String = "7bd671593999b5bfcaf98fa3bb773e953c7addbe029458728e85942f5310c497"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "9b3f4852caae0c4527d0e11f9e4738fa38018cd2631a539029b9b588ffa32a90"
    protected open val nativeIosX64: String = "00fdc0cff9ce591cf7e66b1fe0d431c613e9cc0a4e5c7087ab1ef912b2fca4c9"

    protected open val nativeMacosArm64: String = "2c642bf316b6fab8bc78fc491862ac7e56f54497afea0c51c173f425d85770cd"
    protected open val nativeMacosX64: String = "f8a51e7feed0f2a7c11e8e44518b3534e7aba1c11e4deb6214a4f6e4023cbc69"

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

        override val jvmMacosAarch64: String = "bb36e66a543488c1732d5a1df6a299883c4ccd07d84b6c537e5bee2398bdbaea"
        override val jvmMacosX86_64: String = "bd2b03316ca6651ea800a00b980fe7534daffaa9aeac1e32a40f25fc6e291fb6"

        override val jvmMingwX86: String = "7d620d979d69baffec35a439250e035b08d5f9a4a58570d151849ecd05dc0c55"
        override val jvmMingwX86_64: String = "bfdb816e193a94cf33cd1f29408db8e77900aea80baa37ecbe518bb2c26f1fa1"

        override val nativeIosSimulatorArm64: String = "34e695bf42ce545a5b4e86b69d10e38e17c51ba8320005892b5886ad5797a024"
        override val nativeIosX64: String = "3db2975d4f9a45d816f565cfb0372a3da0cc6423a06a237b678ff903ef73dfe6"

        override val nativeMacosArm64: String = "ad47965f04a701048df64930e7cda52e727eb0eacd66615a3f24e46e347fefb5"
        override val nativeMacosX64: String = "37f6ca8a9f5bd71acc6431cc31ad21ccef256db15ee652c0d4824cbf8c6b7ada"

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
