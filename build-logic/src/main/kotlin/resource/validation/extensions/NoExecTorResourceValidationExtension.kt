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

    private val androidAarch64: String = "86602f874723cd6285aa5bd0c10adcff8971f24fdf388851b3d657ff81ea23de"
    private val androidArmv7: String = "6b09fc70a25e0e5f99ff7235406ca39e27ec38f153eae9db94008cbd0ce4def6"
    private val androidX86: String = "e4b2b8ca9b445829db0a56a29224c1715038a9ec46770efde01334bc9e30758e"
    private val androidX86_64: String = "d77b526e0e7cbbc7372854a10a12423e9030a9e4782e28fceb96679b56dc4222"

    private val jvmLinuxAndroidAarch64: String = "2e8d1bbfcc19d7ad00a9f18c243034496c199a3d3331101d592fce43bb517699"
    private val jvmLinuxAndroidArmv7: String = "73b3807648b0a7b7fd01c9cd1bdaece6f9091772ea1e1aa597959d8c77ea8ab0"
    private val jvmLinuxAndroidX86: String = "48cbad67f122b1c87d648feadd88be026edbd35f05481e87f1fbff4286a5b21f"
    private val jvmLinuxAndroidX86_64: String = "a52cce030e091cad52cff02993aa2964ac33ba8963f8ddaf20d12ceffe1f655f"

    private val jvmLinuxLibcAarch64: String = "2d60be09f3da21b548fd7f474b70be34da5a3649f48372558cabc55617e503ee"
    private val jvmLinuxLibcArmv7: String = "8620645f17f3f46b336ea3e4f17645ec180dab6cc3b167c1ba452598bd1e0474"
    private val jvmLinuxLibcPpc64: String = "f2b644ec94ab7efd3cbb630220d117b27eed4095b852dec9ae22e220849905fa"
    private val jvmLinuxLibcX86: String = "9a936c931bf6748138f1c915768237e8165c9f4fc28b30898d7c38c7bfad2c2d"
    private val jvmLinuxLibcX86_64: String = "e11384e238755df6170e68f5c23b4a77153d7b850e414e9e1020a410ba50313c"

    protected open val jvmMacosAarch64: String = "1c499b3664bab9d6e5f74b283239f6f260fceeda973018bdefa72ee977e1451f"
    protected open val jvmMacosX86_64: String = "ddd41961b5ef9cf765fc97aa055a1f95ea84cdef07375940678c1ed782d7ba07"

    protected open val jvmMingwX86: String = "da5a3f7e35d0f6564f0d2c5894899eeb515f9bd19650c76460c31b26ed295efd"
    protected open val jvmMingwX86_64: String = "fd6eb62cb1b1072b962f9d685ffd9d826f3e6a4c6b8a197e728b721fb03bbfc7"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "047f4873b9f72180dd54ef8d8ef4a75634b2fb737e5759e66dd393e04721c954"
        override val jvmMacosX86_64: String = "8304564d5423fd834db5f423693ce44797b1d101b75e4ea3bb2061820fa83a02"

        override val jvmMingwX86: String = "1a36943a7461a9c3ec114083682c6060b562c367b73370d5ac89c944cbe17915"
        override val jvmMingwX86_64: String = "f6040bc74950d0a8681d262bb94118279891a34cea706f167e46f3a1771988dd"

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
