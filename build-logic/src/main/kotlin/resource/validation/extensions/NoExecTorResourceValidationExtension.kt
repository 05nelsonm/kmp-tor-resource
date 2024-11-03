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

    private val androidAarch64: String = "04cf45721bf41cc08740f68bebc8c44cf0960d50eb816111de99818f543f9e5e"
    private val androidArmv7: String = "50663a7cd25f5002751b71a75ce5445ed153b7c381095a06863a8b1fee7474a7"
    private val androidX86: String = "41fe721a2d51f9b7b83a22bb41e17589a41af7d52bb534b4bb94f22aa81bd058"
    private val androidX86_64: String = "6a406ff4b6c10ebc96236dc8ff58503bb94d329eed0a0e1929367ea34f1a1fc0"

    private val jvmLinuxAndroidAarch64: String = "71223a57b770e5c16edb5c43afda2b62d0317e3985401f9a4d0c93eeca212f57"
    private val jvmLinuxAndroidArmv7: String = "044ca8a66c286a860aec2c65fc54a269dcb7c2c8aad16d875828daca35ab31b3"
    private val jvmLinuxAndroidX86: String = "c7146ff8779313e134c41880396dae42998a20eb2f677577202b04911db52069"
    private val jvmLinuxAndroidX86_64: String = "89cb4905e78eae7cc2dfedd3624ed10f48e48e3835c89576712691cc9a34f173"

    private val jvmLinuxLibcAarch64: String = "e366ca617351a370cbefc78a21056cff9a6a14f971fe081f90f7ad613838ea44"
    private val jvmLinuxLibcArmv7: String = "7e824c90ac844626cf3b3d69f67356cff4031be79f3406bb204d12855efed3d8"
    private val jvmLinuxLibcPpc64: String = "f931e1fdc8cac96efa91dd0db6b343d1b7fffe8a910b140943c48de4905aacb8"
    private val jvmLinuxLibcX86: String = "28f40c524dab2cc9efc502ee72cf33c6100ab3e874527034373b2e8fd5d53737"
    private val jvmLinuxLibcX86_64: String = "bbf1e7efceafa296dafaf833b1630e32333fee2d1220416cd1ee586b7e37fb51"

    protected open val jvmMacosAarch64: String = "0dbf899e8d9e927e2ca3c8ea84d8c2229e67b2a221fbfbd7d55b7803b8f595b0"
    protected open val jvmMacosX86_64: String = "c592dfb3fe8eed9e204f686f374788b7eb52b722ee9d0f4302ec465cc1363683"

    protected open val jvmMingwX86: String = "052f50f7cc0c4bd88bfb9caec048d98527bb9e3dad12e9efccbb5c0fc8d01e7a"
    protected open val jvmMingwX86_64: String = "b86d22db149db22059f201074c8c761ba9c427171606582b49bd5c761f380da1"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "46db835170a89295aa85063b2989f9e818b1dd1e8ff965d69880e77a455239e8"
        override val jvmMacosX86_64: String = "199c21a9451e15e8747858bf0abbfe07fa7842c4dde6a805c0e8a24b1ea918c2"

        override val jvmMingwX86: String = "8b32f6366b95ba72ab9add30f7e88a9a6f3ebd88b4c5b35e9bab8b83705aeeaf"
        override val jvmMingwX86_64: String = "03fac80aa9cc5403f3c5ad44c28d4bcf5f98efd0d474e11af8dd8b1854f6ee73"

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
