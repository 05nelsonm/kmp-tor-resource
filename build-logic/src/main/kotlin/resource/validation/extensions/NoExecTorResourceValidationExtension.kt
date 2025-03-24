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

    protected open val jvmMacosAarch64: String = "1fde2564d8528a7cd4f070cfd26f68bdeef0e59d36c180407306703ce554bca2"
    protected open val jvmMacosX86_64: String = "8833651ce44c91ba18288dd16e6ced31813919fcce466fe9825115d0ea2da52f"

    protected open val jvmMingwX86: String = "11de57908b0c94c3678f372b25135e534e357a150c1dc7c6ec3a13750dfb968e"
    protected open val jvmMingwX86_64: String = "ee0f6fc269d50095b7c47cb738fc6dc5fbac56664c66aa73eeb4402ff44a74f3"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "a0189b50e9cb3aa3ca274cd1c7554745087dc8f8745211287f7fb05b93a62090"
        override val jvmMacosX86_64: String = "96d69aace089089ffb9878d4f968eecd8f04fe62d3c94aa409cc42c79fdd5365"

        override val jvmMingwX86: String = "2de45d95e3d213e9acdb2bde8bcbcf4667bd08d03c2ebf969b73444e1c00bb99"
        override val jvmMingwX86_64: String = "9020837e72bc5135f3c32e3075854a9d24c5d1d08a87843301ccb6cdce876fca"

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
