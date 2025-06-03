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

    private val androidAarch64: String = "78915f9207c3cfb14edaa8c66c45063eb14251a3bb90fa67e868ef4db24cc824"
    private val androidArmv7: String = "96d1ba202e2417f1911c975b15f5fa89387caa412917aafab5e978e9c58d330a"
    private val androidX86: String = "1ee9d0ae3e3cb60ee9c03a2c157e76bb919071653286a94f4ff94faea55213a3"
    private val androidX86_64: String = "2ecaecf559bee567f2528e301820d3310d6042a89c1012ef2354248b3b5cc31b"

    private val jvmLinuxAndroidAarch64: String = "cb9ebc9e0133957c82547b9ebfe17824969a29b2ad17ff55ac12f5f7883264ea"
    private val jvmLinuxAndroidArmv7: String = "5345881ca7b27418628cf86d288736c1e0d45b5a4784e792fa14d69145b23397"
    private val jvmLinuxAndroidX86: String = "bb5c97be60c85f64f9773f4415b0c48848b5b7d17f8566081449ecfc2a4c3815"
    private val jvmLinuxAndroidX86_64: String = "ce6b12b8e4c9245d96ef5ff74867d83ccf124e896be3983980a1c355fbc3d187"

    private val jvmLinuxLibcAarch64: String = "b2a7c0af87bcf86b6f1db3fb9bc6e7e5cc7ef694708c9eb2493543892b595b69"
    private val jvmLinuxLibcArmv7: String = "50236756309434ac9899a420fdefb61727b856f71c0d4f025cd95e13c168fd6e"
    private val jvmLinuxLibcPpc64: String = "e930411d664e85bf3ce1131bcdd53897a4e1e399645cd19f28aaa89e465fe8a7"
    private val jvmLinuxLibcX86: String = "1f1070d57d892f841bcb5f142ba2630b9190070ddb9588e9ba89c4db1e12d3ea"
    private val jvmLinuxLibcX86_64: String = "a59d99bd0cbb36ec5876623d2733bf5c4321b8635341b20738c2444b291b30af"

    protected open val jvmMacosAarch64: String = "9c2618c26e95060b1bbb5a88bb423c95ea99fce5708b384c32b65e1e3cc5600c"
    protected open val jvmMacosX86_64: String = "c0c6e32d2f58c236de717ce506b739f7634d35d6d1227952b850e504a3feda94"

    protected open val jvmMingwX86: String = "f85ae13ba7cf54fb0be8a7eb0b9f63297e70743252d358772a8328db7f0bdfb1"
    protected open val jvmMingwX86_64: String = "0bd2cfbb9940366c20484ead3d67819fd980ee1ac857086a3f9ec7b0fbaddc57"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "83fb77174b7b4b50ff548e2e8993dc14f1368d4e4754f6b75a9bde7beb35f55d"
        override val jvmMacosX86_64: String = "9927b6854422ee3ca27da337bf5e6bd2661fad1ec443d43a33cce681ee79759b"

        override val jvmMingwX86: String = "b0472b9e0f2794b74cbb2339800267b679facd3559e066c124f9b8c7c75e8845"
        override val jvmMingwX86_64: String = "816731b6729cc70700655e31027bbb38f23c0e95cc79e68a9814d254baf9a9bf"

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
