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

    protected open val androidAarch64: String = "f517be2c852e47c0a02160880616c1a462222f604b437387ef9b17294eb00814"
    protected open val androidArmv7: String = "5da9714bc0b0bb3c846b679835efe4e07bc71c08b87cc6e7a1368f23bdd16c96"
    protected open val androidX86: String = "4308fa616d2ca61421fd607d609941e53baafed961cb359f23c2cba3874f582b"
    protected open val androidX86_64: String = "de70e7ced8eccb67ce7c0999281e3e096d66675286a78126d83da2c8a9f7e5a7"

    protected open val jvmLinuxAndroidAarch64: String = "30056266dcfbc357bbab140c58f70410ad5f6fe1f41abeed6467cc9d9d460585"
    protected open val jvmLinuxAndroidArmv7: String = "83b81eaf1f2dea919ae4428e31cb16ee54f4a2f06539d6eb9a43fa6f26827c98"
    protected open val jvmLinuxAndroidX86: String = "40eac6c0bf2a05776b230d8f24ebfcb778ac0a0aed3c49f04b11a9019e803f30"
    protected open val jvmLinuxAndroidX86_64: String = "6096fcba3ef06a16e3b4b382126984387848f3b469ae4a552887994b0d318826"

    protected open val jvmLinuxLibcAarch64: String = "6d40879a90c8f2d12d05c64371ebe879ad17c344257b59d2f7bc3e4b935b6328"
    protected open val jvmLinuxLibcArmv7: String = "796c13e353d030694930781f6c125af588a68d24efe585a635e07e47c073c4d3"
    protected open val jvmLinuxLibcPpc64: String = "4ab333bf7480adb159dc16f83edafc9dc02ce6b3ceebd48b9adc8fbd5d0743de"
    protected open val jvmLinuxLibcX86: String = "22690c98d05619f0da3ca4736cce58d2746f1e690469aada125170ebbdbcb2eb"
    protected open val jvmLinuxLibcX86_64: String = "c37e0f5bf1c13a3d2491e17290373690e7b56cf621ba10d5926cee084107d7aa"

    protected open val jvmMacosAarch64: String = "615020f5b7b243dfb5c383f17f6593840ed0caa96cf8e9724d08da0e59e6a996"
    protected open val jvmMacosX86_64: String = "535d90fb776a9ffd76d8bbd99db927ace813fc945a009a5508210d211951c0cb"

    protected open val jvmMingwX86: String = "39ca966d7edbdfccdef3c6c5ff1b408a5a0b3a01e20c88976d16661e2ef6e7e6"
    protected open val jvmMingwX86_64: String = "2c0316909fde1932e2af720b40873b4a7b60fd1dc1e0eb42b1f908ff9b78cd9b"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeMacosArm64: String = "586d8de9163dd7e805727c6165c1d83a6366cb5f08a38b2607c8a5e4d4d85a4c"
    protected open val nativeMacosX64: String = "857a53ff4809c01cd20f9155099068e9acdb0f32565f17df0a1e0c252b9c5c7d"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "20fda615d1206fd98e25171f058f5463540bc5022185cb7246ec8fbca073ee21"
        override val androidArmv7: String = "91ae57650efea385c2e4f4304fe1a4a157e52f4c8f0922ae0c5cf9b4eb5892c4"
        override val androidX86: String = "27374ea88c63c821e25bb883da469f9eb86bb1f4a769fb00e51184e17c30d18f"
        override val androidX86_64: String = "12526dc959d9d42ffffbfd70b55646bf38161066298516d7f293c9986bd4d694"

        override val jvmLinuxAndroidAarch64: String = "42273f710840ca2bb36d5c414cc1bba5649b1412b95445bd067a279a60e9d8c6"
        override val jvmLinuxAndroidArmv7: String = "0c1c313522d693c9afd8b54d78c3083b7a509a561ed9848cbcb02c4fe0abc2e2"
        override val jvmLinuxAndroidX86: String = "7e2896c93349cc14ea2bff568d4807fef357ea52c559d4bcec5e5eb22f489c9e"
        override val jvmLinuxAndroidX86_64: String = "ecd7cc03258e7d6f0eb194e0b08627a4cc7be23586e4d4fad994fcb74757b972"

        override val jvmLinuxLibcAarch64: String = "fc7c44b5fd7cf2eb94cef1cd55ae435deb6acb422219553e1782e76f27e4d44b"
        override val jvmLinuxLibcArmv7: String = "aaf4342c09ced301d424aa38bc8bfa17f57b3e3a09f114f11bf4e071b92643d7"
        override val jvmLinuxLibcPpc64: String = "c66e5e5a7647ad6c222fcb47562789466f079f4ae093d0d4dfe304956413961d"
        override val jvmLinuxLibcX86: String = "028b4d2f5957b8b36c202dfa572fb651e951643de168bb737ac514eccbee02f3"
        override val jvmLinuxLibcX86_64: String = "37678df338f1eb1577d1c5620dbb1c4901783beff85a1621a67b738687260713"

        override val jvmMacosAarch64: String = "e9dc08a7860af3f9867d0a007d79e5ce3efd82d71f1d5547e0503b956f168dd5"
        override val jvmMacosX86_64: String = "9f5fb498759609acdef931ec2100fff636da7860c3418d18c173c3f4f885d442"

        override val jvmMingwX86: String = "db774e8f4064a744eb497ac3c9ee4a35daee438fa34e90033fb58319b5ae3cf2"
        override val jvmMingwX86_64: String = "2bf1a6c099132f5139ef4d6688d110a723afacaa87bd2fa84971def71a119cb7"

        override val nativeMacosArm64: String = "406d60ca039c0f4376d9fecf11963bfc6d57e26c7f70b1a0dce680c903387ee0"
        override val nativeMacosX64: String = "178a26b5e1dc4a93ef1a4b061e598f2f33ca884dbc2de0ea11fa600891ec28dc"

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
