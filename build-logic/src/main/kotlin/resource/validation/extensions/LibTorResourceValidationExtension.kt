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

    protected open val jvmLinuxAndroidAarch64: String = "8753b1e44531f4aab4d97839bee8500cf4a144555929f4ce08d49f79bd82b441"
    protected open val jvmLinuxAndroidArmv7: String = "e1f2080fc215213b66769830a1001d643b3f96ec19d280c44a2fc74ee90cf93c"
    protected open val jvmLinuxAndroidX86: String = "37570fa75613872902a8e13efc874c0c2189939f7f47f8c4c214e21c882d6366"
    protected open val jvmLinuxAndroidX86_64: String = "25fe774ae5d50831c85a639dff285eff49373a0938ae97af69c944bf8be5f952"

    protected open val jvmLinuxLibcAarch64: String = "8ce24bd1aa863ffce8cf0384a2325873a89d8597fce0f8ccacaa36a654b5d740"
    protected open val jvmLinuxLibcArmv7: String = "7b906248a849b368b709cb0e033e27f25232a2b1b5d4c32a38ca9146c8365f4d"
    protected open val jvmLinuxLibcPpc64: String = "5d20bb4d3447a3867104c77acf8858b76f243475b6c43b134700bde5ee397545"
    protected open val jvmLinuxLibcRiscv64: String = "02b714a9d7bcc24dd1d7278d19ec7376f56d61907d997b727f1282cced212202"
    protected open val jvmLinuxLibcX86: String = "11cb16cbf81ce24e19c5d526a655b620c2a95ee1eb9485ba7fdd9a2a0876da63"
    protected open val jvmLinuxLibcX86_64: String = "8b110c0454d8883db08bdee5d228504f4d006844eaa7bce767c69ea3b0828f27"

    protected open val jvmLinuxMuslAarch64: String = "a3c958d84a16af8e528f25a5a6b1a65f5cccbc856f0e677fa821297174f82f36"
    protected open val jvmLinuxMuslX86: String = "07cae86586c57e1a0319e9ceb82de83cf34fef70b2d3d9cbbc2ceaf0ec7e534e"
    protected open val jvmLinuxMuslX86_64: String = "e24dc376729ac1ed4c3821285f45236a56e9c0a154c44314256fdc3cb1584d49"

    protected open val jvmMacosAarch64: String = "6483721b2629893c9583f216ae33b88ac816fd0c5561896ea22fef09a15c9091"
    protected open val jvmMacosX86_64: String = "d4c69dd1d4baa688466263d809acda009c2f5e3323bbfdfcd6c1534de2c41f1b"

    protected open val jvmMingwX86: String = "3f24e8b3f2339b8f1df4a0cb6a830d3bb8d0dfc3475a19fbd19aae33ddd9b2f9"
    protected open val jvmMingwX86_64: String = "87473ebe3ac121a2cc452e4090e9ee5be8d5f28ec42bd791d022c1e871fe107e"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "24078ec6730112e3d64b2b813f8f04ed1c9524e9c54e9815f1fe484df7855f15"
    protected open val nativeIosX64: String = "5e715e5156164bd2761bc5140e1f2d75a41be01ac627b48905d05b33d9abf6d2"

    protected open val nativeMacosArm64: String = "24f45a96807b6467719289b2b943d1ca6957535428b27e3e199b0d5a2a33949d"
    protected open val nativeMacosX64: String = "a4cf27c0822b225f6c9f98d3f226851cba0f5e538592064f4fbedff3df6f8bbc"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "7267e4f42ff0af0a145eb8498efb355cbd66282164ce66292b8579dd2dfc18aa"
        override val jvmLinuxAndroidArmv7: String = "1a796108e2ccce63aafec4a3ed1de8664d460398af6a52053edef67a7ed975c2"
        override val jvmLinuxAndroidX86: String = "90bce77588718c3e3f78a321fa72c1b3cbaed0ee8dcfe9a6edb885468ed1574a"
        override val jvmLinuxAndroidX86_64: String = "3ae75800bf561804b8820b18c6098a89efad33418295c0721d53e61a2a6c5826"

        override val jvmLinuxLibcAarch64: String = "5cb89c83b8a6924fe2d667b5ab68853f31bb5f7e293741d476bd8ffaf7bf0b93"
        override val jvmLinuxLibcArmv7: String = "3a98e1f6fcf9b76c0f00ea214553679759f5299662644f720ce82898a4d9078b"
        override val jvmLinuxLibcPpc64: String = "6c98306452539f6a167b38ee41925adb79a1ae802b628abf6553dbc3f5d04ac1"
        override val jvmLinuxLibcRiscv64: String = "fa88e0b783ff0e01e9f98491c70e01f26d9af79c0c9ae65ca88dce7537e524f2"
        override val jvmLinuxLibcX86: String = "851f21b3f077b30cbb6daf8833ba624d2f9bbc89e72eb61b69873f67d75d814b"
        override val jvmLinuxLibcX86_64: String = "d9cba55eabd6cb89a9c34ed152940acac8764ec73e93220e620a2846d1425eec"

        override val jvmLinuxMuslAarch64: String = "3fa375e5e0b93ea8f206b42fd4470ce4a1b4586782bcbaa18536ae620672c958"
        override val jvmLinuxMuslX86: String = "e06d304b66fb3b9796b3713309f685fc7841b16edce292e6defd47a1185aadaf"
        override val jvmLinuxMuslX86_64: String = "e46154ff7e83ffeaf7cea4f39fd2f86919db8e48e07326e0dff644d674bf94d8"

        override val jvmMacosAarch64: String = "9ff9222f1f679a626be1b4deb2b60d578867ba901d1b6c8420e3f199fe8db35c"
        override val jvmMacosX86_64: String = "52fb8e6325caac48e8e52b502e6a937b79acb13cc65d7c3b42ef5e7312226253"

        override val jvmMingwX86: String = "6e3f6f457aa39fd6c64c8f4e29999962da171876f65c0ff06094a8f65d16c3c5"
        override val jvmMingwX86_64: String = "a2c36798b6fe3c9ebe2ea174294f3903d6747d51da302bd8809eb3b26c14a02e"

        override val nativeIosSimulatorArm64: String = "b27cd0cc3a54da349d6f6f16877fed5d429628c0e9ccddf696180796a4338888"
        override val nativeIosX64: String = "c346f7d8a249c273a1863bd821e233058c2722e784b4777660f59359fec26c47"

        override val nativeMacosArm64: String = "cdd0ca55040b93173d26dae1fb0263f6efd2461d9ab03141ad7bce20cefe86d8"
        override val nativeMacosX64: String = "41d63d4e69398e931341ddc232e8dc3f4fe0a7336b00b73b7b8a2c9a50ec196d"

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
