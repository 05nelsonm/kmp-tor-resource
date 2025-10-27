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

    protected open val jvmLinuxAndroidAarch64: String = "00a1fac79b5d13584079c4796192c6d96df3d1eb5f710c43fecf39c503d2b70a"
    protected open val jvmLinuxAndroidArmv7: String = "a93fdfc8febc63eb8c46d47419a5d57b16adcb301b7b8b82c41cad4a941d6c9a"
    protected open val jvmLinuxAndroidX86: String = "b6130bcc5626889ccf966393a0758735703976ba80db566ea6f1d7d703bea4be"
    protected open val jvmLinuxAndroidX86_64: String = "b7fd0ddb91a875f6a2bb34727ec84a92ca7aca1baf1059d7099c8c3ab356c2ca"

    protected open val jvmLinuxLibcAarch64: String = "fb6acec0ea6336a9dd5fc1f0dbcd240b7c9dfd7f07dedab0389b25118649e0bf"
    protected open val jvmLinuxLibcArmv7: String = "af683b1e4a73229bdcd8161a1ae03103774259d57d4c45c2733e42081b44f6bc"
    protected open val jvmLinuxLibcPpc64: String = "9be21db83f4dd3ce71a4f5e2e46577c9936d3845f8720d636875f0c1b42f4787"
    protected open val jvmLinuxLibcRiscv64: String = "ee415f89bf39e860dc42f1bdd3e07fc295b0c41fe1d8aeaaa5c676d205b65b60"
    protected open val jvmLinuxLibcX86: String = "a18997d62dc08d79622e972f25f0c29952f435f303abbf39ed1e26b07d9d8a9c"
    protected open val jvmLinuxLibcX86_64: String = "1a0dc4ea819f105849e7b00e1866c4d66f101e9404f57db736a861bdaf005084"

    protected open val jvmLinuxMuslAarch64: String = "fa392a83d20f148259b98311e7ade839ef1af0273727d9614e8fe7186c1fd3ca"
    protected open val jvmLinuxMuslX86: String = "80ba52474218b34073c9e82daee933f87023d1c60e2b6351711ac7c5d88e3b0e"
    protected open val jvmLinuxMuslX86_64: String = "765ead11f161c0a19cd6901597e557117503623c3505ca0ecc5a79619c50b94d"

    protected open val jvmMacosAarch64: String = "aadd8e70b2d4fb099007f423191bdb6b9f7108fc7e0531dab5cd12cb1f2d7914"
    protected open val jvmMacosX86_64: String = "bacdf206052f32a2e578cdac97c5f12aa4855a4395e2599f218d238a1252c473"

    protected open val jvmMingwX86: String = "04000f72f9ebe53afa92d2b36bdc842bc73cd5ccb62243f13d7fbefc6b59b6a7"
    protected open val jvmMingwX86_64: String = "510fd067c9b80deb1870911ff5a04fca0c88ac614ee243f08e37dad24d4c7d4f"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "7a0ab91203f4bd0a4cddbdb4041996f9c473246789727abf853036e545329905"
    protected open val nativeIosX64: String = "2c0276e0101e0d2e1f9985187256abe846c86764c0eb665cdbd3df216ea85c2d"

    protected open val nativeMacosArm64: String = "3ed2685f18c96187952617723362f02529fbef33be8e33791fb3f8abb8b432da"
    protected open val nativeMacosX64: String = "e144ebbc773493902d0b36984d1d0546f5fd4b7182c650dab35fd4f5f833f5ef"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "6d63e1dda963f7a074ba1d6d76da0d3a4da9a7a48ecf5e2de3c48362b81b7dc2"
        override val jvmLinuxAndroidArmv7: String = "a288ed260608e5d9b5b8f9b94e667e9145babb4bfd6162c7ffb02794c60dd6fd"
        override val jvmLinuxAndroidX86: String = "fee38776e7d64e5884ee8a980794425c6c8cdf958f22f788442e3cfad82224f9"
        override val jvmLinuxAndroidX86_64: String = "33470f66cd2fff7e76b9916d1868e88a38995330ec72c60876fd24f966b8dd11"

        override val jvmLinuxLibcAarch64: String = "06dba1a3b01039315fe3249260e58a00914f1f8823d4b4f90a05862e8ba6b848"
        override val jvmLinuxLibcArmv7: String = "17b8d3dd955ab6ad3995393808912b9ac9720591a79cb084b35a92ec9562b29d"
        override val jvmLinuxLibcPpc64: String = "15d33b873ac42cb4f5c0b592c9062b5a989747f7dd642f25eace8a12bbe11a4d"
        override val jvmLinuxLibcRiscv64: String = "bb50604bcc9a496e31d202a7e761cda81cdb7c9f6c7f16462262215fa87331f3"
        override val jvmLinuxLibcX86: String = "59304151bb5c55391ff6f0831534d0d2c6821b24ecd0f89c512dfd822f2efbba"
        override val jvmLinuxLibcX86_64: String = "280df2183ef64a53afed6e8cff4c94884259bf24681e87eff53c3201c9260129"

        override val jvmLinuxMuslAarch64: String = "9b2d11c80dd0ff06b2b1ce36454f055d694224035d636dba475b72b3c12e16b1"
        override val jvmLinuxMuslX86: String = "3c70b36711abf3ec32c4e2a1fe3bc6279ecfc7ff6ff8033b07c566b8f3fba834"
        override val jvmLinuxMuslX86_64: String = "59c4d864a983e8ab5718629eda361f4b42c9aea267519225c11c893755c8e4e3"

        override val jvmMacosAarch64: String = "978da0e9b349b5bc172dc49dab02420a744d69a1a5aae58190544cf14a4ad89b"
        override val jvmMacosX86_64: String = "93c7ec216cf9ea86af92c338303e685094a277acd051123778c3eef78cc08637"

        override val jvmMingwX86: String = "a61574b26dad38effdbe790868d3936126d411ade5a0219e447a4d48aa7aebdc"
        override val jvmMingwX86_64: String = "b30a0b9b86dbb13d881c082f4273cb3e6337cf48ee5c1bb2c527b31d6911bc7b"

        override val nativeIosSimulatorArm64: String = "86aae3909b3b43424fd17906e08d6b857522df2ae09bbad8216a74555c4ba0fb"
        override val nativeIosX64: String = "947acafa1e9742bec1df8e5ea660a517edffcfaead38b35f255eb79b31680541"

        override val nativeMacosArm64: String = "80febc8dccb74b459360772a5f3f2b33966b2120189b47017132006749c63d7e"
        override val nativeMacosX64: String = "731f82e49dbc2297026e54cd47833dd72030c6349e307c881520164a142b6519"

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
