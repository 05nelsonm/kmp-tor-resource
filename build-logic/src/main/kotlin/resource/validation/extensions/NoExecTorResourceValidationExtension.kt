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

    private val androidAarch64: String = "dd97395ebbb3f0dcc1500a0da582b25095e6af9431a076907ffe33c0f1d9191a"
    private val androidArmv7: String = "603e91f4734fa6fbfc4c04df9c18b4aab30ceadcff5555da176de46086e456aa"
    private val androidX86: String = "92f507b1f78d938d8d48cc27cffddeb3a0602aa59329dd779bc9f288f4c768e8"
    private val androidX86_64: String = "039a608ce5d5adde9ab5a5d598a39ac3e5f99c372726771e1f73373c43fb0ebc"

    private val jvmLinuxAndroidAarch64: String = "54d070343f844542d44e679afbc494d5be4f8427d2c60d5a16b5b1bff6244c4d"
    private val jvmLinuxAndroidArmv7: String = "d7bc54ea65a799dabfa061c03c31ad988147cf8ebe88fe095f053b57cfb3af22"
    private val jvmLinuxAndroidX86: String = "2ecdf3b4c3a7dc92d416f091d12d3def6f0f2a98f5bf7c9368c8b356e0dd12a7"
    private val jvmLinuxAndroidX86_64: String = "4097a3712d1bc10925b2b7cef5d6e37c95a9adc35f76982ad7049ca7c31e2eba"

    private val jvmLinuxLibcAarch64: String = "3e61c38c910bba3af85a62f15819f5fe29ebecdadacbed1024b17c5403c74cc3"
    private val jvmLinuxLibcArmv7: String = "ad44e78936dab5d391609b0d399324953a46caf5dda3f96bd029e69e1c927089"
    private val jvmLinuxLibcPpc64: String = "ca78eac1e4742e97fdca49432879dc06bd88ca1639224de8f2323e3646c06e40"
    private val jvmLinuxLibcX86: String = "053cef7a3a39633f1a5a6d11803701835d9a39d093334ce83a1b84179bf8de5b"
    private val jvmLinuxLibcX86_64: String = "ea57eaa54b162bca90c54cbf316c939da4e3220bd2721eca9ef323de5fb04737"

    protected open val jvmMacosAarch64: String = "174d815e0187993f45e01dbdacbba0ad52363d4df1634fd59b07c61af2bbf451"
    protected open val jvmMacosX86_64: String = "d1d99d8a76ff0d43f91ae1a6663a31ad667726c0841e5e090d83311eede1cfe1"

    protected open val jvmMingwX86: String = "dbdfb0e8c68e2d941f87c54c3df2b7ec65e8d023cba1571ae3ca265c7c149116"
    protected open val jvmMingwX86_64: String = "02f32d213f181ef5f74d1d6ddbc02c60d80f660e5f193b68e0ed1c5d45620fc0"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "97916b49e9b0f8fb200974c2c3deb3cc1ed15a6d4b3ebaf490a63c29dfcb9638"
        override val jvmMacosX86_64: String = "b0c879bbd6dc2540b92d2c604249ab95291e0cf7a23907fb39f2c740f3c25a72"

        override val jvmMingwX86: String = "3a6bb0e6cd6752acecca61244479a724360100f9f73d1cf3b1ad6c5d77176b0f"
        override val jvmMingwX86_64: String = "b2d9d1f997e0f5d17f3733d160ec7555a5af983729ae89756f863f13ba9a923c"

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
