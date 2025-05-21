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

    protected open val jvmLinuxAndroidAarch64: String = "db1a139f0967bfbc0264d16c25bb6bef57aecbe789249ecd72033791c7cfcea7"
    protected open val jvmLinuxAndroidArmv7: String = "238e8e215325d8644a7cf96c0c5d9d90d857d25f0fe5302b18ff9a8f93d71aa5"
    protected open val jvmLinuxAndroidX86: String = "cf86914e5821bf1abfee6ca1b082f3bbb4d3fecc802b68866b13301a96e23bd5"
    protected open val jvmLinuxAndroidX86_64: String = "ad1febf9ac27f3cac1af4c2b087af43bcc6e90cd07076c3ecbc6972dd7e7f99c"

    protected open val jvmLinuxLibcAarch64: String = "edade98a680ea7bc2bf5e1c67745bbea66d2674a33266458589fd5290748219b"
    protected open val jvmLinuxLibcArmv7: String = "340e1b8ab860529a50dcbfc59124b074d7a800e2d2b163579b7bcf37bfb3d562"
    protected open val jvmLinuxLibcPpc64: String = "5a82ee16d95c0edc4dca77d8564bb51515b2c27c946a971cce695a358f615d10"
    protected open val jvmLinuxLibcX86: String = "3cd07495b4f3c63911a2687d8d99ccd108167ba241607724a52f4a633cd61fae"
    protected open val jvmLinuxLibcX86_64: String = "9cb61355631cfa0832c3719039d4eff40442374efd548be1d50899be943ff3f8"

    protected open val jvmMacosAarch64: String = "c34e579aa9a5b68fc40dfafe7eedd2ca5888f09990051549b39296fef5b13adb"
    protected open val jvmMacosX86_64: String = "0b19c41ca46351344521355dbb6947bcfc9f9288ef8482a0c84529e4bca7ae06"

    protected open val jvmMingwX86: String = "a8aa8cc345544cc069cdaaa51bf688be1b1deaa391815025e6e93244eb5e78d4"
    protected open val jvmMingwX86_64: String = "59ad1a3bdf3c78df8ada596fbed497d4750e03966891416352576f959a35effa"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "6300c38aa9140280629c7038ec0e1076ace56907d0e94b5fc0ffd178db467e87"
    protected open val nativeIosX64: String = "dcf347728b64d72b0f48d8141c898b6e05921e93d522f0d1d50de2ef3686c324"

    protected open val nativeMacosArm64: String = "2bcb93ec39704411b870382a5b739cc0042d6e97562f2f4d94ea270ac4653cac"
    protected open val nativeMacosX64: String = "414d681934bced03284d2966ac1c6d4c547a42868dfc13f979606e84ca712462"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "bc3d75ff9731431d7c2a442feb2623284e0a95ec7a6040f41497e823def190fd"
        override val jvmLinuxAndroidArmv7: String = "afb3b88f5afe739ea547301de6f2797fbee6e9b43835dce8660b0f61cb036790"
        override val jvmLinuxAndroidX86: String = "7fc7e5b740fcefa13048777291354d1d4e6464d3fb28e02df55ec5e9a267f85b"
        override val jvmLinuxAndroidX86_64: String = "4f5a58279a0be6f3f6f433885669b03c5425ace6d1c8f638e493518f047515dd"

        override val jvmLinuxLibcAarch64: String = "b32bc4d133426267b89a1140134860e6c93da5d724877d2a2c0dd0d368f02ae5"
        override val jvmLinuxLibcArmv7: String = "6f3a6828bd9bc92c64cbaa3c3b6513d0c7e6fedb967caed9b37bb909943aaa90"
        override val jvmLinuxLibcPpc64: String = "eb175b307fb535af28cb8ff875b96c1e4c8d63ae32024da4141bc0efa7ab0653"
        override val jvmLinuxLibcX86: String = "b8b44c26c2b1babac434c8a23a39a28d977974621955647f390c7818453e0fdd"
        override val jvmLinuxLibcX86_64: String = "cfb045b4ec097bfac55865f17134e704ad74188556424ef2263529a9d9f83416"

        override val jvmMacosAarch64: String = "2ed3cce3b322de895d9e862e0ec64b217ec719a1b83e1c731822696004cba078"
        override val jvmMacosX86_64: String = "68f1f496bfebd19a2e6ff5ed3026de39146c0b57b318c982101f7b2fa0a32b2d"

        override val jvmMingwX86: String = "a043efb62aeec1b9c9b6dad60cde404d1af31f62aa89f7669377be006413bf46"
        override val jvmMingwX86_64: String = "381f7bf2848305433fa96a6816c2aae32984bb339138392d7be06f1f86756371"

        override val nativeIosSimulatorArm64: String = "35de2594ecbf7cc2f16bb46116013878a01aeddafa3066936c70526298da5881"
        override val nativeIosX64: String = "f9c73b0f08b747dbc557809e2b060be9dc6c9b2f2a82ef8695e9838af7f2d402"

        override val nativeMacosArm64: String = "d693e64a7bd106c388f36cabe91565ce3a7b56826b97802696b13b0e4cd569a0"
        override val nativeMacosX64: String = "a9223e2764a6d56cd126ad27d1be86d682ccf880919ecf8af1fe524ae53e8d4f"

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
