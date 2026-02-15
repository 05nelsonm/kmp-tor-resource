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
@file:Suppress("PropertyName")

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

    protected open val jvmLinuxAndroidAarch64: String = "18fb818df7ba527ca0c7a09b06928539f4253abf04913d049b8d15c1fbc92845"
    protected open val jvmLinuxAndroidArmv7: String = "637422e0304a82927496073b63739e2e6c6bdb79c0cbf8b60e3808dc0d1ec8ca"
    protected open val jvmLinuxAndroidX86: String = "81e06620bd5b14f498b2e3bcf66cc619015dc1e999b515e76418561c290b29a1"
    protected open val jvmLinuxAndroidX86_64: String = "970a4c1d920de22778bbeb8b7b9bedf4eab494fe82bfbdbea30451c2f00c9ea7"

    protected open val jvmLinuxLibcAarch64: String = "680c806032f0fbd9c31a1df89469f398726ae312d41d569ee147d31b31d46ded"
    protected open val jvmLinuxLibcArmv7: String = "1065961facea13c76e59b6b5fc45f3c01d0b4fab465f16a5333d0c687220b039"
    protected open val jvmLinuxLibcPpc64: String = "704899efe756a8fdfc002361d15ab5b1084ad3b7703c528ef86023c0f319fa88"
    protected open val jvmLinuxLibcRiscv64: String = "d1063c7be325744e90b5479df6e14b6b342390f140696e207250182ec290c3d5"
    protected open val jvmLinuxLibcX86: String = "d39eec6cc9153a0c3413f80117de7d9d1140211bd78c3e444563026c4f94e992"
    protected open val jvmLinuxLibcX86_64: String = "ed26564b85252a31a2d93a2f7e2e7c3ec5e30abca94fa0b951a1db38605b5880"

    protected open val jvmLinuxMuslAarch64: String = "7f317b91bc12c05aa78897329a3fe815e9ba70e04e0b9803e5feb6dbe38a4bbb"
    protected open val jvmLinuxMuslX86: String = "5a7667e5f9dc4392004c0df0d5f3556426a210dc1f102c7be873e6097a532dc0"
    protected open val jvmLinuxMuslX86_64: String = "791dabb17981072fefdf6b5b2803e407df95bde4d73445a97f05bb09e8634c9a"

    protected open val jvmMacosAarch64: String = "9fd5ec72619f5c3b7a4161849b9501353cd949ff768c88f4dd38f3b6357aebab"
    protected open val jvmMacosX86_64: String = "1a227ba63e415b51fe7c3879b4d9ea37b7a9e7d70bb4a4911306ca72b123964b"

    protected open val jvmMingwX86: String = "7ff57fc5f7e5767395985cc28bdc609ff95d9482c4d67b48bb54d7a809b64819"
    protected open val jvmMingwX86_64: String = "c42a1b3707d825d57320c01b3b8e93a40d075f1e986d3483cbdd7763ba86e3e6"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "cf0f4708e6f8533c009391fda756ce22a9d41d15a7fd09b2145cfc1124ebceee"
    protected open val nativeIosX64: String = "5f0d052ab714293056c1cdd266fd4489534f1de08c99cd1a9cd403383cfa6cab"

    protected open val nativeMacosArm64: String = "ff3b97a27585871dcbddd2cdbc9e5c4288789e95be83f73023192fe85ab2893a"
    protected open val nativeMacosX64: String = "39ac641a061ae31145f41f9e0774c9f443ac5485e76bebcc6dd8410e4dbc6165"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "18d02d4e4ec6ee370a7362984da083297e498d9106627d3825b97bdbab19d504"
        override val jvmLinuxAndroidArmv7: String = "a3d1e820245b20660b702368f09aa6568ec9846aa06833897fb5f2a6b17ac9fa"
        override val jvmLinuxAndroidX86: String = "8c88593b2d7e11fc8176328db2b135049b4fb488546487492de2a109f3f8feda"
        override val jvmLinuxAndroidX86_64: String = "fc1b1bee9296ac5568a6194ad3b8bbda39ce320270aac9424814b1070ba1f34b"

        override val jvmLinuxLibcAarch64: String = "f830e34c24ca6ad3c0ac05ba4a7ff01406836254106fe7550c3c36c46ebcc2f6"
        override val jvmLinuxLibcArmv7: String = "97b0bc23f7b1f11a2b3362dd96030b0a237fc072522f146eb5f7b8bb1f8f06d2"
        override val jvmLinuxLibcPpc64: String = "9d520c0dad93175e82d423ee019991a0c85c68eeb6f8969628f45bb041dcaa69"
        override val jvmLinuxLibcRiscv64: String = "a0e285208cda350fe2406913bac25d68556625bf01f0a034f1a0c002d4a2552c"
        override val jvmLinuxLibcX86: String = "f2c99e975f2aefe7894b0f9f34d14560232981474205aa4ac7f52c90792c0308"
        override val jvmLinuxLibcX86_64: String = "caff05dfc12a9d4ab0adf12f389978656a0c026120be34114479aa502d74eada"

        override val jvmLinuxMuslAarch64: String = "a41bf6081de89460f3b20a437926c69e995b5799bdf3816ed09a44e8e15d2579"
        override val jvmLinuxMuslX86: String = "5dbce56c2227bf52cbda1ba2cba991252e42cbb8ecbd817ac06790b245c86cbf"
        override val jvmLinuxMuslX86_64: String = "6f10d9a31e78526e988f3b7a5955b3364a912ff3c423dfb5cf5e2d82f9618982"

        override val jvmMacosAarch64: String = "2a4d9e2210042b941eeec119058a34d4a07ba5ba5c701e8de4e81383165fc34b"
        override val jvmMacosX86_64: String = "9eab276d0e2aa52de2d654a5f3b96d6d5350249d96521fb3b25432c56d38b180"

        override val jvmMingwX86: String = "cdaaa6c5d376b5478d2be3f63679deaa4c50d4b5e3c42c227a40d614fdc691a0"
        override val jvmMingwX86_64: String = "aea21a96817bcd92a65a391bbd924c6d0436b371583878725042974dfd83c2c5"

        override val nativeIosSimulatorArm64: String = "b1457f50060d8d995c0c52d859610e978e1b465380b2ad7beaaed4e1f678bf74"
        override val nativeIosX64: String = "62ac13ee01f82840c9bbc264b9e1a194bb852865d8759620b4600d59abd5fc4a"

        override val nativeMacosArm64: String = "d473632a0c6e05f3dc8202b9f50964e50251cb5254ca31f21fc179eee1b24e1a"
        override val nativeMacosX64: String = "d8dd398cff8918e925787b4b03367e5172fec34f5d5062c3542a1b4dd4f4be82"

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
