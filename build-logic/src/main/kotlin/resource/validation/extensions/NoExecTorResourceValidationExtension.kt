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

    private val androidAarch64: String = "c28cadd9956a683f84a086a60de5cef51df4abea3907f1cc9ac7640c7c529652"
    private val androidArmv7: String = "b51227addc64e65d4ea9d7e531855483efd3f3b8c5ac1aa27bc2d1e33e07f778"
    private val androidX86: String = "3a72ee0dab3be34bca4cc261d7a68a9ab6442fafa1b8e5eb4be2fc7cf7975003"
    private val androidX86_64: String = "12c694941fdcb2fe728571513297443f85b9ca2a1fdfcc9882e3f9e6108b9859"

    private val jvmLinuxAndroidAarch64: String = "a76e77336e37478966346c621b6360614b8541cdf6a3e18a63acd4cfd9d7a6c0"
    private val jvmLinuxAndroidArmv7: String = "351c58bbd68a26c051bb68d11990d254a06085d25c3eee24b0bf4fb0c8a884ed"
    private val jvmLinuxAndroidX86: String = "4aad12f6e619712848ec7baf28a7f60502b54996dbdc1832f9ea882e866b2adc"
    private val jvmLinuxAndroidX86_64: String = "6080ec6d88a5268c79d72cf09fad6ff912d9908f0256945edab6b4554ab3ac15"

    private val jvmLinuxLibcAarch64: String = "6720a11a4922e4c1271bbe697890b827ec6f0e8ae01d0a8ae6ea4c883ae91519"
    private val jvmLinuxLibcArmv7: String = "bec6e76e9cc68212ca1d32f26815629525649c6422b5f928862386331bed5e27"
    private val jvmLinuxLibcPpc64: String = "2d967b98beea87207fd0deec38ed2cc4a89012844dbf68caf64bcfbe7952d4ad"
    private val jvmLinuxLibcX86: String = "fbc060038a565450c92114705c250913e3e7a996de3faae9d1b14312eed084f1"
    private val jvmLinuxLibcX86_64: String = "3172210eea50199bbe088f44895245e1654fde851d3b3f52f863f645c888bfff"

    protected open val jvmMacosAarch64: String = "ce26ee914f3d7d2db4aaca3a0e89c7dad454d649cf80f6b916303d8168f6d003"
    protected open val jvmMacosX86_64: String = "4d611c8ff20250b3aa1a60e37c8d32461eef0f7e425bd1939982c07929cad0bb"

    protected open val jvmMingwX86: String = "fd56d48a40b8fabf0710f40fad94c8e1317f8cf1a4c25f6c87ec84970762e52d"
    protected open val jvmMingwX86_64: String = "9eb66ecf9de4d9fae28693e93daaa0f8bff66c46fd84e31827a944ee9e46be3b"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "a5b88b311038c31e5baacc932d472f3aca88de513d26fb29c95e00dd0fc8ce96"
        override val jvmMacosX86_64: String = "aad46edcd5deed65b7fafc74eece6d51622726c61751e26aae1f88b50bb727c5"

        override val jvmMingwX86: String = "50e15f1150e525e2ab80a785b0966fffd758b6093c017bb5e9bc3db6afb028ae"
        override val jvmMingwX86_64: String = "20be880fe240a8cc601f5d5639b7e64ca7c37458246689d348da0c6ef9d87384"

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
