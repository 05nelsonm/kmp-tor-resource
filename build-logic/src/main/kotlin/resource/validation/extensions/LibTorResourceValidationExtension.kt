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

    protected open val jvmLinuxAndroidAarch64: String = "0a69463e9cb110afb5c373f1ac17aefe544d0eb9d2d2fe2dcd1ec956e183725b"
    protected open val jvmLinuxAndroidArmv7: String = "65500011ac60cc2d375d48adbb9d232bdb380227b8d9f52f4313ccc8e330211e"
    protected open val jvmLinuxAndroidX86: String = "4f9d537c558d803dda3449e52dba3d93f4794347ee348d364782391a3947dbf8"
    protected open val jvmLinuxAndroidX86_64: String = "7ceaf1f3aa8a7052d11ee495c0c6aa0a1e6f5eb64be54841bf3c4f1a4f4775a1"

    protected open val jvmLinuxLibcAarch64: String = "7c111c9d1ac2b412f9d1ab58be999388532845cd5f47c2f5cd92bcf4f1807dd0"
    protected open val jvmLinuxLibcArmv7: String = "1e291b55572500715acde48c0dec4adc00a652021d404c82c33a2992955c47ff"
    protected open val jvmLinuxLibcPpc64: String = "6b758c274893e6838e13706a958c93282792d3fb5e0417da6a56b6daf371b8b1"
    protected open val jvmLinuxLibcRiscv64: String = "35637d6f175356befe4bb006c4de5b06499a7c4563b1847d318048428953edb0"
    protected open val jvmLinuxLibcX86: String = "d4bcef499b9bd1164ac4d95dffd60707f06d30b73c2149d6be73b8972fa12e58"
    protected open val jvmLinuxLibcX86_64: String = "c0d1e626f8c59785697995edcee18150c78f3028f2f3bcc53906233aaf4f41ab"

    protected open val jvmLinuxMuslAarch64: String = "667e1d6b02f97435061a2abbbe61bf374bc8c710aa138a12438a1f9d306aa818"
    protected open val jvmLinuxMuslX86: String = "841cc9cc187dcd8316439acc71adc58ab6d1a907c4310226a77e0d302766663b"
    protected open val jvmLinuxMuslX86_64: String = "bc46a52cd817936b4eb08360ac1b7e9585a37a67314fa9103947ab5c86712e0c"

    protected open val jvmMacosAarch64: String = "ac8e7110d1d74fc133018805c721edb5c842981eef8ce28961c7900f1d1b6887"
    protected open val jvmMacosX86_64: String = "285e518b059759293329bcc200f805983d23ba9a5cbb51f871a94e4bf861835a"

    protected open val jvmMingwX86: String = "1fc6fb82e0666f7503fad2f23a0c24aa29e2db6ca2519ac6a035247b974ac05b"
    protected open val jvmMingwX86_64: String = "fa57c5f51551b25ffd163f7f49ae33204922fdb8abcd2b433c40bd9769193f95"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "d3865dafddaac4076c11ff8477656679423710a1937b2d3354d0f92ece13f803"
    protected open val nativeIosX64: String = "6ef646484d632472f24c5a3d9c57adbd19c5b73bddf2df4db9ab78ed4baf2041"

    protected open val nativeMacosArm64: String = "b325d0f2f5911f502b86d01d3ec28f0f37c7ae0fb0fceb41120fbd3856704f91"
    protected open val nativeMacosX64: String = "b18d4edf8fb1da26924b2baffee52e92155b609302e490e97de324acc6705c7f"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "636568b124f8f9375d553e6a3228e038ba3f440aa891f42ba669d858ac193685"
        override val jvmLinuxAndroidArmv7: String = "d1f83acecad1bdd1b00740464605b5dddf56c8224aecc56dff0cec9af9a4d437"
        override val jvmLinuxAndroidX86: String = "3018448f4f1df31ce7870ba285229d8a6e8b26f0f3bd7c7e8eae70e3cdce8768"
        override val jvmLinuxAndroidX86_64: String = "5f6f60da964219995dc1264e29b43956e8a890ffcbd00634affe9b4f8a8fe403"

        override val jvmLinuxLibcAarch64: String = "ef3550800bd3b61716562e0b4df579a95b5bb5c5deb8a5093adaab5974f69059"
        override val jvmLinuxLibcArmv7: String = "771fcc6f71668b5c43c8b329711507639a248e11e2f4ea4a5692f9ec5aaed586"
        override val jvmLinuxLibcPpc64: String = "651d434461f680a152289358c70f3e2afcdb30d82b5bf20182e4f70ada08c0c0"
        override val jvmLinuxLibcRiscv64: String = "4ff36e0ce51763095c09c2169662d26f0ac574bb4903666d94b4e1fbba653518"
        override val jvmLinuxLibcX86: String = "166d3a5a341fc1c8355ea0aac047d4806445e452b9309381273ca2c756de1faf"
        override val jvmLinuxLibcX86_64: String = "e70f00e5ccaf88a4c7d94e485d9cd8c1da47a65967cbad61b553792756f266ae"

        override val jvmLinuxMuslAarch64: String = "1f723e0c4ef44a8fe20bc7e3bbb68ccd7dc913ba4b02f68498448803b667ef63"
        override val jvmLinuxMuslX86: String = "db40cadccbdd590bd90d8ebb0190c603f23e5f704097c4b8b0e6edfbaf4f75ad"
        override val jvmLinuxMuslX86_64: String = "2b49b6b827dcfc6bcbc613d2bfde97597d4b9ea7b2229adbe42d575c7c67b056"

        override val jvmMacosAarch64: String = "c85609ef186c9cf52153bb49b8130a7348f092e6b911d71f85bde48d75c54040"
        override val jvmMacosX86_64: String = "f96cf05bf687b17ca0866f7b85b06699c4e067b1a763ec7cee0be4e96e7000f9"

        override val jvmMingwX86: String = "33024df4ea8cd63b9fbd8d47284f049f6831bc6540a36f36daa7d4008c5be647"
        override val jvmMingwX86_64: String = "c4f3f26628b0ef053617bc342d06d562bf29fc3ed1041aa78683b15d0b5f4444"

        override val nativeIosSimulatorArm64: String = "ec3cd5d3df6b7ff4cb8e5132f635dd66d9178a8ec3cc41901d1cf6a1e0cc15b8"
        override val nativeIosX64: String = "44cc34191494729fb18cf428421b542b7b6aaf1d64a3b4c14be0e6bc62b30706"

        override val nativeMacosArm64: String = "e1a1516abd96988033564080bde120928fdf44e40bf726e81a01a2a93b51689b"
        override val nativeMacosX64: String = "936cbd95c7317f647bdc3ef1c085cbf8fa404b044d010bda5a6e1fa29f7e6744"

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
