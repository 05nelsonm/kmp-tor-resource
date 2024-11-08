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

    private val androidAarch64: String = "ba127ede73bf620c43d5b64c1591e54ba956d57df4630ea8a9dd2c780a73ad7e"
    private val androidArmv7: String = "6f17fb682139599f3bd2dd1fad9c3dd390ab89245f253829897e19b2ca932bc7"
    private val androidX86: String = "dd4e236489566369d690e061636cb575fb50c2c0ec8e432f4d7c496faff50d8d"
    private val androidX86_64: String = "0efca35cb17def80bac1703d84bb663f957b4d3bdd481dbce9cbe2dd414ba45b"

    private val jvmLinuxAndroidAarch64: String = "b1755d8ab49362846f3e786b827679fa71538eac50d08500bb4bda0ed3dac5e6"
    private val jvmLinuxAndroidArmv7: String = "e2f556aacef7e3d2a7dc5204e97816af06b949cbbec1d86c6d00daa3d0576fea"
    private val jvmLinuxAndroidX86: String = "536dc77b9d002e261a4bd23b65552b17b46e3785741e2861e8cde76d8bb10f34"
    private val jvmLinuxAndroidX86_64: String = "663e62909ffa7979cf4d95be7a943cbb0b1bfc1864bef9f49ad6f6cc65b7969f"

    private val jvmLinuxLibcAarch64: String = "451189bef5932579918420d850a1e313b151c1800d399c664edbbe8188b62d0f"
    private val jvmLinuxLibcArmv7: String = "7f59c1665a3660422a5c3e96e4e0c922f07fccbe8e2e5f9e3fb81eaabe226ecb"
    private val jvmLinuxLibcPpc64: String = "ec766c60d54105f838e4e7d4c3118370508b909a9847de97bc9a30906f325ed9"
    private val jvmLinuxLibcX86: String = "e668b070471a25332c9ed0bfda18f6de4391f575e3dc6122ea151ae1425f31ab"
    private val jvmLinuxLibcX86_64: String = "6a95ed84ec0f4696452ff17a66d83113f79ad4ff7b697785f2f4d6989dd9067e"

    protected open val jvmMacosAarch64: String = "73f0a266dccb5d2ea6e9e1396cf9688b45bcae7bdd0dbd610fc3bf3ac2525dd4"
    protected open val jvmMacosX86_64: String = "e96731195e9ca7cd1b8bc6be5d28ad1b387cb1eab4517cbba625c46b148283da"

    protected open val jvmMingwX86: String = "d8339c28ff5831aa53f60f69aa18908d71a5017ed2a0785619573bc87543888d"
    protected open val jvmMingwX86_64: String = "4877aa31aa6aa4ea82f6ca74f85cdceb91e515966fd2340509abccc2fcedfc41"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "2671b50985deba8f2865a205aaf78691b2f182396eabb5d770c508029e04761d"
        override val jvmMacosX86_64: String = "0d989ead2fab17cdfa2ccf38a90fdb34d2ed09c1493eb7ba538417327c42c6c2"

        override val jvmMingwX86: String = "4013aa45d1008bf14c7f14265e268c634d0d831e02816bb46e3c21149bc03d91"
        override val jvmMingwX86_64: String = "935a58e0efe8d65c17c1b55031cbe44d094b56d11f69b0e93245c55cfd354068"

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
