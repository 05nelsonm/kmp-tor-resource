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

    protected open val jvmLinuxAndroidAarch64: String = "a73401dbdcdf014e64878fb00307e21cdbbcad4a0a3bbad66449372cfdeb2d1d"
    protected open val jvmLinuxAndroidArmv7: String = "5c94bed99c75e8083bc8d14aeba9d58d919a676c6634e5c82cb3b94e853a7c4d"
    protected open val jvmLinuxAndroidX86: String = "340bf1f6407c728ab37fd702a99ecce0196781a1d6b00f6675c90ed2318a3a74"
    protected open val jvmLinuxAndroidX86_64: String = "a5890f02d77f8c9a6a58be59f1de11f13a1b82c9afbf4384f4c6c541e06f7bd2"

    protected open val jvmLinuxLibcAarch64: String = "d8c85effff701abb86164a880e3e0b26352d0d7f9976a3a04d25a61647ee7ab8"
    protected open val jvmLinuxLibcArmv7: String = "4b9c3e90311c12ef01e67cc10f4d15cffe4cfc9e8fa484b90ee9c173129ddf70"
    protected open val jvmLinuxLibcPpc64: String = "f6c78340239d622ecac781f725743f39fa811c1ad4d6340445ab1430e4d906ae"
    protected open val jvmLinuxLibcX86: String = "4101bfcfefb60a989d4c504c739c7760ce08bbb2b187e433b440cfa73365af41"
    protected open val jvmLinuxLibcX86_64: String = "127f66781fc32300bf5cc9ce3b0334a3896bd6796e123e99afe8375dbef2717b"

    protected open val jvmMacosAarch64: String = "2d80ae84db417e448e0b8e7ab52d558bd471d7832868f840475f47992198ee4c"
    protected open val jvmMacosX86_64: String = "628697c7a1c5947873c72f4cf9cd52b8714048963ae8f70b1a072cd3ab403bb6"

    protected open val jvmMingwX86: String = "50e118fc57509be6efcae6a5348eb44909ba591cc871e649e8df8fdc51029708"
    protected open val jvmMingwX86_64: String = "4596d3a0f5ab2af52dd8bf45b0108368d27de70396c7342c6d1f20a4f0df3d37"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "bf3043d9b9e1febe76767d7d779cfd2cbb02aa48bdea6d9f2df3ed3fd129336b"
    protected open val nativeIosX64: String = "0b70f17b902f6f2f7ae0137bb6f8df33debdf9c446157014a6dfb1a4335b6ad7"

    protected open val nativeMacosArm64: String = "2fdbe3bec72c5ee1eac081e13b37e662ce0d72dc1a742e99874684ad82636a7a"
    protected open val nativeMacosX64: String = "75fb65ddd1ecc8cafaf269acc23b27a4f194d6a51b24be20edabe42c7d1fc751"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "a2d8cd29ca1381a4526b82af5f96752b27ed76d73577f4ab0063102ccb7ed92f"
        override val jvmLinuxAndroidArmv7: String = "17f5d41e1a92623e668eb0dd522da45deee7814bebede54d70360266dd53363b"
        override val jvmLinuxAndroidX86: String = "919bb1026c73c042b3a87891db3a5c14e581c30d2f401e358630ff8dac14a3bb"
        override val jvmLinuxAndroidX86_64: String = "2c03c865926540c2d315f3de2cdf59104938b93c4afdd16f767e6a5fe7921682"

        override val jvmLinuxLibcAarch64: String = "36bae26b9ac1e6b8dc8969eaa9f6147674042fae56455b3958cdf1f2a65d29c3"
        override val jvmLinuxLibcArmv7: String = "07364aca21aab09611cf09fca919730b367d4809aef2a269eeb44d5bb0090703"
        override val jvmLinuxLibcPpc64: String = "d824aa74f3b374519c7ce812dec1e0a5029caa8483bf1651a3c58385ee523496"
        override val jvmLinuxLibcX86: String = "e44d925a6c8f1e6d71d24843faa5a2272aaaf40aa4ea31ba53d983c6b300d387"
        override val jvmLinuxLibcX86_64: String = "6d6ec899d5d10b31ed9cf111d160bdbc2ace361cb9f95409e04b0e8fbf4956cf"

        override val jvmMacosAarch64: String = "a552c21d26d5ce6be360fb555eba737dfa8a263ca74e6082144d00a1f69c1257"
        override val jvmMacosX86_64: String = "63de5b6a446c0efdf7c4701f77ac6021ddfb027f376f340fba9ccda1c3ac8ed2"

        override val jvmMingwX86: String = "3cf5632d115e67d8e51c5334ea2a02bf9ba6a698495fd8bfe55a66158ee03080"
        override val jvmMingwX86_64: String = "43f62fdccd9d8efe957468de5e14ad546cf69ecd2e36afe1e90436ad80127d44"

        override val nativeIosSimulatorArm64: String = "5ebbbbab3b92adac737d60a30f3b55341c21aa701dda3cf4c664b43dfaaac05d"
        override val nativeIosX64: String = "357057f7c59c7dc8e3401fde40bea97d8ed6272caa63bce6d223f8819375610b"

        override val nativeMacosArm64: String = "dd8a6333ede51dbf719a9cde6b2220cb8baca86f1741816d774d1fb7a6abd264"
        override val nativeMacosX64: String = "a5a1dae834e4f1964c8d0fa6bc2a0f0f9d995ff9779d81a84466fdeed9666aeb"

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
