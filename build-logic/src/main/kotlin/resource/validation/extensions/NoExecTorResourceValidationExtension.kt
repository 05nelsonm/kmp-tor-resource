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

    private val androidAarch64: String = "ae9b895e1337d129fbc2175983dcea4a57802379886a30d72d0158c8bfe2e3b7"
    private val androidArmv7: String = "3f98f03d98033971c25ff42516d67d6e13702976596c0a625ebda4d006b57a3d"
    private val androidX86: String = "e9d5df5a9de701432616a0a7426cbd9c5c1636bee7ff9c0e91bb895f69f3721a"
    private val androidX86_64: String = "294f0184881fa9390d1da689cc89581ba1585db7ab7c2463cf22bfb492828724"

    private val jvmLinuxAndroidAarch64: String = "c0961b0376a0e4612e16cacadd57a2f40491cbe11e325ec6c71158f4094cbeee"
    private val jvmLinuxAndroidArmv7: String = "30bbbfd1814a8a2b0943afdd8db8f070e547be2622811068dca62c179995e939"
    private val jvmLinuxAndroidX86: String = "c5f7b3b15c8821b3babee153b5d2d40ba521291bf2ea9c3be47be02ce6b5d5b9"
    private val jvmLinuxAndroidX86_64: String = "77acbb514a9afcd48f7a9813133a7354e150cdb4e6d5faed104d5e6d1d97c86f"

    private val jvmLinuxLibcAarch64: String = "3431f8a3ad9a810e30c4c121eff017d1c30213efee8a2e6b8246a70599b8ea03"
    private val jvmLinuxLibcArmv7: String = "d4907549dee8f80bc0886e149540ff2d3f3d8cc390ddb19689acd0f40127433b"
    private val jvmLinuxLibcPpc64: String = "6fd080871ce7cdde68267df3f4cbf5a738ae4cd475c454fbe96f1cbf0926d116"
    private val jvmLinuxLibcX86: String = "1572a45d1fd02670a854afa43144bd5d57787eba5aa347e7564ceb9bff328cd6"
    private val jvmLinuxLibcX86_64: String = "59e7266c82454f620caf545fa073943d3ec1e66423257ded293f87f305a362b3"

    protected open val jvmMacosAarch64: String = "886045fed17d16185a6cec8ea803e387bf1b2bf4bf628949708ba0fd20b421ee"
    protected open val jvmMacosX86_64: String = "8ffd1848837ebdb000b44bf5a275de32daa01554388518680b9511fbb9ef5a80"

    protected open val jvmMingwX86: String = "e357eafa1a5d7079d0a262cd7167fc4a1ca32f46cc6c62a4bb2cc4ef373ada1d"
    protected open val jvmMingwX86_64: String = "d41c72738483512ec6084391d9d5b2885e4a73b0c012d45bd3fd483143eeac29"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "37c4a0c1c7c79bf2a0cb24da84b1da965c2301d10bcbfd8a4c69990cc303abf3"
        override val jvmMacosX86_64: String = "1b9851cd6d9008a39f8c96923181de5b5068fd055a9e6b2156fe51e4453a96a5"

        override val jvmMingwX86: String = "d0e52e3dfdfce4be2432f23989f5651afcfedd4c9c7f5de4653742a1e833cad1"
        override val jvmMingwX86_64: String = "71d7ca567f96be6e4f6d7183844f5a38581e07ebc5e145fd9e4d90750466ea29"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun errorReportAndroidJniResources(): String = errorReportLibAndroidProtected()
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun errorReportJvmNativeLibResources(): String = errorReportJvmNativeLibsProtected()

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
