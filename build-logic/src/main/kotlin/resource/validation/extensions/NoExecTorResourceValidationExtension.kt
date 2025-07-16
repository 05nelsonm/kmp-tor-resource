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

    private val androidAarch64: String = "13d6ea342b80a319f34f66e7e611133155a404579838eb8e1507ed61942876db"
    private val androidArmv7: String = "092c55336c1eec5fbc4ae0d72c27b0bc68ee18913e7935e9ad4a4903fc777814"
    private val androidX86: String = "9a38687d2b98d7d4a836bb7b895f0c11d1e14e654c5ed2cf0bfbd9cc1a1464b6"
    private val androidX86_64: String = "1c3b5e021f796eb69c48c8909a50fc68abd5ef86780a8da7b8aec0d064c8404c"

    private val jvmLinuxAndroidAarch64: String = "b33921174ae279955f7904d31614e71b11120d3fcfc2d61c355beaece65c4469"
    private val jvmLinuxAndroidArmv7: String = "ec8de0fd93d0b0c947885e7de2bc399fa4a8c0b94b966314308b55c6e8742c42"
    private val jvmLinuxAndroidX86: String = "363426cadd55b56ecbb2ac935604a0c6bc526621368e7ce65481c125b27585b7"
    private val jvmLinuxAndroidX86_64: String = "3ac71e3b4cd843c71c3fe88edb089503d48ad071f582b7ad3b090917bd9133aa"

    private val jvmLinuxLibcAarch64: String = "34d0c920d96262d2a23169b418ad954ebee5dc34444d0a3989023d4a27cca7de"
    private val jvmLinuxLibcArmv7: String = "8eea08235d16f74f278de4992e867f343c53222c62aec85b3e80558583d97def"
    private val jvmLinuxLibcPpc64: String = "cd79e237dc070eebeeed0bc0671185990e414bec4c79acdcd490e846f9679371"
    private val jvmLinuxLibcX86: String = "997e16f19379eacaba01342717193b6cd5f6543e3967095f95766226066246e9"
    private val jvmLinuxLibcX86_64: String = "e6a39c69ef446c908a0b9f868e19c383c06104e863ebb2027c3dc464dac5027a"

    protected open val jvmMacosAarch64: String = "3bf8b982a580c50f63fb6fd005b7140ee7ec9ecf7ebb4a1fb03856b4426a14a9"
    protected open val jvmMacosX86_64: String = "23aa82791655700d771b11d4b94576fe0b937fab443810d037e32280a9b82684"

    protected open val jvmMingwX86: String = "2d0eadd6e05b8931aa2cb1cde6f0bb049acb18a93728943ccf65784bd5b1b493"
    protected open val jvmMingwX86_64: String = "77c0e99f799239b4fb6ccbed818528baa816f8f68ec47e7ab5939b9d5e599448"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "c0c640be2a8f40f4e0a2e778cf650bd2c659ed6f785e86a9308bfb5a80f19f9d"
        override val jvmMacosX86_64: String = "b4a9f6afd312a8606793e5ae07f0c4d8957973ec27c24a751ca613f1451e9e74"

        override val jvmMingwX86: String = "9a50d7e3d7bad186c8a851ca206851d9010facf32f0b6e3dafc4227727d22873"
        override val jvmMingwX86_64: String = "ea1a3d5a3f2a0bc9ece970e19915e52c61c9f792022713dd282b713a350afccb"

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
