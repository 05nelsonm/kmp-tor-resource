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

    protected open val androidAarch64: String = "30508f94c4e3f0977e94d75673a59db20d20c10b2eee9de01a0985f64c904724"
    protected open val androidArmv7: String = "796a3c64cc3b1ad2cc40d1dfdd0e64661704e63bfae5df11dc9329c595c72b05"
    protected open val androidX86: String = "826cd2783a01a20bf162ba911cd3cf281250a88ede0f54bb4276808a4e887166"
    protected open val androidX86_64: String = "8da3208f6c1efeb5c9d838cb41ce9fc09d8a3a769d8e6d28902a43d127b6faae"

    protected open val jvmLinuxAndroidAarch64: String = "db1a139f0967bfbc0264d16c25bb6bef57aecbe789249ecd72033791c7cfcea7"
    protected open val jvmLinuxAndroidArmv7: String = "238e8e215325d8644a7cf96c0c5d9d90d857d25f0fe5302b18ff9a8f93d71aa5"
    protected open val jvmLinuxAndroidX86: String = "cf86914e5821bf1abfee6ca1b082f3bbb4d3fecc802b68866b13301a96e23bd5"
    protected open val jvmLinuxAndroidX86_64: String = "ad1febf9ac27f3cac1af4c2b087af43bcc6e90cd07076c3ecbc6972dd7e7f99c"

    protected open val jvmLinuxLibcAarch64: String = "edade98a680ea7bc2bf5e1c67745bbea66d2674a33266458589fd5290748219b"
    protected open val jvmLinuxLibcArmv7: String = "340e1b8ab860529a50dcbfc59124b074d7a800e2d2b163579b7bcf37bfb3d562"
    protected open val jvmLinuxLibcPpc64: String = "5a82ee16d95c0edc4dca77d8564bb51515b2c27c946a971cce695a358f615d10"
    protected open val jvmLinuxLibcX86: String = "3cd07495b4f3c63911a2687d8d99ccd108167ba241607724a52f4a633cd61fae"
    protected open val jvmLinuxLibcX86_64: String = "9cb61355631cfa0832c3719039d4eff40442374efd548be1d50899be943ff3f8"

    protected open val jvmMacosAarch64: String = "8e33b518ff547e5324baddb825d260e65555c4e95e2f8b2b16fc364c3b53b721"
    protected open val jvmMacosX86_64: String = "27a20a442fb31b7d83f3d4f6bc769017b07bca6f7f6f7ea23f14564229e0720f"

    protected open val jvmMingwX86: String = "a8aa8cc345544cc069cdaaa51bf688be1b1deaa391815025e6e93244eb5e78d4"
    protected open val jvmMingwX86_64: String = "59ad1a3bdf3c78df8ada596fbed497d4750e03966891416352576f959a35effa"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "d0655369a197c135c5e6512b80986eb69e302ca8292b9238a71f34a0831309fd"
    protected open val nativeIosX64: String = "cb695334cc6f33afff1192c1e84d4533f6cef6fe102a379e7fae7e7cb5d718e1"

    protected open val nativeMacosArm64: String = "ed362e6d38b0786db2c6c7f081fefcd8f4ef90264996ec19bfc870947c373e1e"
    protected open val nativeMacosX64: String = "e055e37f75679e3e6383d73c28353d9be3770ec8aab765ef5a99204219e86771"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "52077ebe59cbaa9607a1acc032e3f70b6c1d59c35ae6ea75cb96be189252f8ef"
        override val androidArmv7: String = "bcb8cc88c51d864b34548cfa81d85413f7f9de1a9597908bd105e2921b654bec"
        override val androidX86: String = "eec3b73f9610b6bb357a96dc2e14b056305f9b3fe33ab670e58a069ffddefc89"
        override val androidX86_64: String = "3dced05aa1deb5b2544944701370f23078bc81e9a7154b678b2ff1bd5e338148"

        override val jvmLinuxAndroidAarch64: String = "bc3d75ff9731431d7c2a442feb2623284e0a95ec7a6040f41497e823def190fd"
        override val jvmLinuxAndroidArmv7: String = "afb3b88f5afe739ea547301de6f2797fbee6e9b43835dce8660b0f61cb036790"
        override val jvmLinuxAndroidX86: String = "7fc7e5b740fcefa13048777291354d1d4e6464d3fb28e02df55ec5e9a267f85b"
        override val jvmLinuxAndroidX86_64: String = "4f5a58279a0be6f3f6f433885669b03c5425ace6d1c8f638e493518f047515dd"

        override val jvmLinuxLibcAarch64: String = "b32bc4d133426267b89a1140134860e6c93da5d724877d2a2c0dd0d368f02ae5"
        override val jvmLinuxLibcArmv7: String = "6f3a6828bd9bc92c64cbaa3c3b6513d0c7e6fedb967caed9b37bb909943aaa90"
        override val jvmLinuxLibcPpc64: String = "eb175b307fb535af28cb8ff875b96c1e4c8d63ae32024da4141bc0efa7ab0653"
        override val jvmLinuxLibcX86: String = "b8b44c26c2b1babac434c8a23a39a28d977974621955647f390c7818453e0fdd"
        override val jvmLinuxLibcX86_64: String = "cfb045b4ec097bfac55865f17134e704ad74188556424ef2263529a9d9f83416"

        override val jvmMacosAarch64: String = "c66a7c684de29f31897f0d6ec078dc5b68297e619ae112c8048737e114a795d6"
        override val jvmMacosX86_64: String = "304d5126c0bd691a3d7cf0f8eb1441ad88a3cabb4e5fdca4f05d53aff7a38d73"

        override val jvmMingwX86: String = "a043efb62aeec1b9c9b6dad60cde404d1af31f62aa89f7669377be006413bf46"
        override val jvmMingwX86_64: String = "381f7bf2848305433fa96a6816c2aae32984bb339138392d7be06f1f86756371"

        override val nativeIosSimulatorArm64: String = "cce9d71c4a5a62fa48c0070694fd9dea40764a5a72079e600f9a6fe0e018988f"
        override val nativeIosX64: String = "09e4094d44fdd1c17270c9207b0034edda61d2bcfedb2e5f9e9c185340bb3854"

        override val nativeMacosArm64: String = "53ca42696ba2cb2252a06f5702862cbfae57b14f6d4547377e2a3717528b0c54"
        override val nativeMacosX64: String = "2cb4d7c2a1a7782bac9262a2ba8bdd61ac0ae20aef8d170c4b67f179eec19eef"

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
