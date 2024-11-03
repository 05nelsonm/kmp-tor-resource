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
@file:Suppress("SpellCheckingInspection", "PropertyName", "PrivatePropertyName")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

open class NoExecTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-noexec-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.noexec.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    private val androidAarch64: String = "954aa49a97775022b7716ba062e466a61f259bd90f7d93da3fd4d7bfe60cd705"
    private val androidArmv7: String = "262771a12198068091c241d5cb002b04ec4ae5880e0afb0964ae074dffa36c71"
    private val androidX86: String = "17287656a1eef5afff913a8672d4d3d1291a29186cbe41d8847215097ac139c7"
    private val androidX86_64: String = "7f028d863e67888c62cb42f5c374dfdcd7032b3a6c7e70e7fab3a56889365fe4"

    private val jvmLinuxAndroidAarch64: String = "d46d2ed65d0fbd4ef2fbd61bd14735cdad58723997198ff0f67c71adbffe1fc6"
    private val jvmLinuxAndroidArmv7: String = "056656f5b1e40c3bdb849c06f56860c2ca59db08fbadf9fa957cb26769c519ad"
    private val jvmLinuxAndroidX86: String = "fae7deed08e24da75fde8fd78019ce255d75e87a2c6b00b92729c6dacadbea4a"
    private val jvmLinuxAndroidX86_64: String = "a69259e6bdd8f1598f66ff576bf53d15d9a957cd142e7c7e5e24773e8f22d325"

    private val jvmLinuxLibcAarch64: String = "5df34af967d4a94a6f0707d87e306583d69e95215efbbecca5816013380d0329"
    private val jvmLinuxLibcArmv7: String = "4abb3f63f39034cc777ad97e81689f25a272658c5611f495d942cb0fe11c3ea9"
    private val jvmLinuxLibcPpc64: String = "f0962020a45c77148aeefcca529c8a4f27f8776a2474d29eca143f60e869d1af"
    private val jvmLinuxLibcX86: String = "4d18c853cc3f712dd3bf20a39a0ae7e3348300620d362dd1940cd40d8dc10ad3"
    private val jvmLinuxLibcX86_64: String = "16f90819fd4fd3f95cfdb6caa64bb55d909c5ba419861556867d34cc2bacf3fd"

    protected open val jvmMacosAarch64: String = "7285c04b382e7fe3d05dfc8d3390cc9c1b7fa79c7f688b420e8cfbe2ba6369a8"
    protected open val jvmMacosX86_64: String = "99d3861cfda909c4ffc87fa201b7e6a5ae82ace3f860a96f1d54af1bf84c755c"

    protected open val jvmMingwX86: String = "7e3ef1dd0e199cab2f536c29f6480ac2d909da0f7e8a0584243f5a13164e840f"
    protected open val jvmMingwX86_64: String = "fb074711c76276ed13d6f435d1e913c5e5a4474691cab3b13e3741335af5e21a"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "8f2b6fb8071c9c5447f69a99f6b59f13c2764f44c0ec260207a50b3406df1292"
        override val jvmMacosX86_64: String = "ed6a60a04cbf5bd774a731c3dfa703c311e7641b39c2923af56acf8f4f5121ee"

        override val jvmMingwX86: String = "2133d7b606a378a3ca3dc2c238bd8f13df4ce260ae2732bfea91c8d0a911be89"
        override val jvmMingwX86_64: String = "1f328a2927701efd04da55c37ca97951d75306df6784468b57f90c30dd9c5895"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtorjni.so",
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
            libName = "libtorjni.so.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // jvm linux-libc
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // jvm macos
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "aarch64",
            libName = "libtorjni.dylib.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "x86_64",
            libName = "libtorjni.dylib.gz",
            hash = jvmMacosX86_64,
        ),

        // jvm mingw
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86",
            libName = "torjni.dll.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86_64",
            libName = "torjni.dll.gz",
            hash = jvmMingwX86_64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "noExecTorResourceValidation"
    }
}
