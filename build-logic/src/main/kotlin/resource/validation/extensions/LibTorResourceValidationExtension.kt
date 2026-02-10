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

    protected open val jvmLinuxAndroidAarch64: String = "075f90ec9262a08b5aac41a26edcb73a0492128feeca4cb9ce8124d038343c72"
    protected open val jvmLinuxAndroidArmv7: String = "390d41d4acf40318c04c34b0da9ae6dcbde2fc2b2a2491d4ee88deb15f03382c"
    protected open val jvmLinuxAndroidX86: String = "91d1696bb163b45f526d9c569818ff209d9b4f66f6e90a7c861f8e810942c5eb"
    protected open val jvmLinuxAndroidX86_64: String = "80cbc6d6a2a08c060111a605d003a381e7650e0ffe601e7b1c105b7e31bb5615"

    protected open val jvmLinuxLibcAarch64: String = "c70f4b086c6d15854bfd9eac9acbc8fa3f81fe2aca9ce5f76c0cd1f6b6f74b8f"
    protected open val jvmLinuxLibcArmv7: String = "89d539ef6b988b72a4eb25f37d1c5db59f3d561e30a9b7473db2e07800703109"
    protected open val jvmLinuxLibcPpc64: String = "7d2dcb58f877b040ec4b0ce71f5383015444387ad09ea1b024c44d576b8d9d31"
    protected open val jvmLinuxLibcRiscv64: String = "338fadc668463243999f87ab3e165c0455ac192960baef1ba498c39a13cbbe2a"
    protected open val jvmLinuxLibcX86: String = "9a60bb6e0e0847d5c2dabf390237d9eef47a2863d80eb4c24b65da5ebbfd3c67"
    protected open val jvmLinuxLibcX86_64: String = "5add36f4ac35124b050f819807b339c1bda2c4bdadd1b020c3729af16d215c10"

    protected open val jvmLinuxMuslAarch64: String = "7ba9b440f1759a85cd297f5505b8933c5f10fdf5c7cbbec5e550daaa8aaf17a6"
    protected open val jvmLinuxMuslX86: String = "be6572490aff470ef3eaf6b4db7385f8fe7fab859bfa09db9da2b55eb8e89f2a"
    protected open val jvmLinuxMuslX86_64: String = "69c8f80c902dc15bd85f2463781360bea10285116a5330074fd2d9cd04db0897"

    protected open val jvmMacosAarch64: String = "b5ede32cfac8fc283c3e4f5a3c9e4726335f4636b791340fffbdeebb0655b927"
    protected open val jvmMacosX86_64: String = "3c1f769685f519149ebf56d3532ed63a28416949616a7ee123fc7d18906d2d7f"

    protected open val jvmMingwX86: String = "3f77ed49e8aa44963ace3f47919f01d7f735c5381b84d1c4a54ec364e6d103c7"
    protected open val jvmMingwX86_64: String = "93489e300e2f5c2a63ee19c8231589f0ea5f380c300618104fccc4b9a8de5c00"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "3402772126ab12fcec2a488ca3c223a3e043d2a392b250e3e1935f2b9cd1b255"
    protected open val nativeIosX64: String = "d58b2bcdf01fa057c5b1cc064349e1df88df28ecf1b2956599c4ae5592180660"

    protected open val nativeMacosArm64: String = "95cc0fdc823b2ded4dad3f17bf1b71162d07f4ab4e936e37348cc23cbbb2192f"
    protected open val nativeMacosX64: String = "a1027685485a1caf50112571851d9f2cfc2272cd3ff4f374a4c82ad38d10e334"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "dca406d42ffcfc182ed7e59f7aa3e57f815fcbe5d146953d6e402fc3ec5b3e1e"
        override val jvmLinuxAndroidArmv7: String = "183225261156887bfb5c6ef00a974a0dcb9cc62eabce6b6317e782513dde1f8e"
        override val jvmLinuxAndroidX86: String = "015c1d524b246eff58f6c38b654eb3d236ccd3dae1401123ce64ce43654fbbc3"
        override val jvmLinuxAndroidX86_64: String = "4c6358f90cff9394b54464354525fff3cbf6ddfa5a5d56c6af18c78a805dc211"

        override val jvmLinuxLibcAarch64: String = "66f39278889811c6b3e0de69bdaf37e6c8cae7f11681dc47e45c745fd5db23c3"
        override val jvmLinuxLibcArmv7: String = "22f7f1f9fd820b0ffc28e6a6c6892ceaf9989e811ff213288045fc96bb36366a"
        override val jvmLinuxLibcPpc64: String = "85019ab0b7c2f2e790e742243ab90d8f8905803e9e49600c28ec8cce81ff1f61"
        override val jvmLinuxLibcRiscv64: String = "5541eb99dd0fcd09d0ca7ba25bd49874f81b8d282724f856a4f91c366bbeb679"
        override val jvmLinuxLibcX86: String = "3d77ceac61330345de303fe761faf297f961334053a1f0ec768ddfd289ea69a8"
        override val jvmLinuxLibcX86_64: String = "59eb2d32151b4e57e1591cd1208c5337eddf0403d684341375e4c513e2ce50dd"

        override val jvmLinuxMuslAarch64: String = "9a11cf3c6efd32bd78d9e631815c5e70b5bf2154858b0ed53819a859889e8daa"
        override val jvmLinuxMuslX86: String = "927e38f7d315bbeca31f917b68349f2f52d3d781dd2d211d074c3f55c44f7e7e"
        override val jvmLinuxMuslX86_64: String = "b548bfea65d1fcc05e86a82734245d400c05c4b6d142a63bc14698236f2efb76"

        override val jvmMacosAarch64: String = "a27fdb4334c0e26014c4999172d7794b523aa3b4aa0e8066e5b97f9562677e42"
        override val jvmMacosX86_64: String = "0ee58fd4d0879714b7a43a395fbe2fa2aa180b99ce73b05f073a91a76e88cfed"

        override val jvmMingwX86: String = "dfbb59d33edb098b11b80146fe0cef9906456117bed489bfdf3c4c4717ff688a"
        override val jvmMingwX86_64: String = "d929f216d6bd6b694b1c6e6a32ac797117a98ac58ddba172782e261bc68dc828"

        override val nativeIosSimulatorArm64: String = "a08a6f08f479d8faa9c6e2b1f1dacc1ad61502acf32a8a123084b3fa70b75ead"
        override val nativeIosX64: String = "7cb3847aa011b342acad9edd6d6ca5e0c6900ae22b5476990651a0c2a677595a"

        override val nativeMacosArm64: String = "48847f7f8518164803702381c63649514cdc92449c1c860067b41eb73cc8c1a0"
        override val nativeMacosX64: String = "bb343286bd879790ed71b09a5355cfe9106974ab718e0dd05e0909184a5687be"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun errorReportJvmNativeLibResources(): String = errorReportJvmNativeLibsProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun errorReportNativeResource(sourceSet: String): String = errorReportNativeResourceProtected(sourceSet)

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
            arch = "riscv64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcRiscv64,
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

        // jvm linux-musl
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslX86_64,
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
