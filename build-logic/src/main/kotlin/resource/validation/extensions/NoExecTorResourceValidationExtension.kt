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

    private val androidAarch64: String = "ec20ce1274e7992b7b0de4a99f0d00f756990f161c077f774f3fc6cf77efbcdf"
    private val androidArmv7: String = "4ff00329b42defbd41a396e71182c483ee8055a8753c300a87ac65c6a22f1fa1"
    private val androidX86: String = "6b68afc8f67c01a5a15834ebbde0f8a587aa39e0ba71f2312325a0709ab7f88d"
    private val androidX86_64: String = "fb99c5a293ac41977fb965ffeced0ee4f9fe5b2badb8de28f255096ebbb2fce8"

    private val jvmLinuxAndroidAarch64: String = "b905b864fa47264cc5444c0f4d4d59ab1ceac70697c21e962fb826537d6c60fd"
    private val jvmLinuxAndroidArmv7: String = "98908d5183930cefcd5974681c893fad718bc2753d622b5c4888929b5b208faa"
    private val jvmLinuxAndroidX86: String = "2b02fbb00c2e43f4e24feb7cdb1e7607b02545a7bd8a41440755b6d8c1da3459"
    private val jvmLinuxAndroidX86_64: String = "0be41cdae3868077bc6b5a8b63dbd02fd9cc426172529114598c2ec0b397d6ae"

    private val jvmLinuxLibcAarch64: String = "3d25a5324b71af4c5e8b0666cc765fc66c6bf4f3a132baff0fd2a616b638ceaa"
    private val jvmLinuxLibcArmv7: String = "2636b8fd4d723c30bdbeccd83a97b98072de675d4574f3801b2c09438471bac5"
    private val jvmLinuxLibcPpc64: String = "c70defb9e7af5b78cba76d4528ca65be235e7586dd1cc47111cc74c3e1e3ebee"
    private val jvmLinuxLibcX86: String = "7de3ecc972e7be1e2721f2bbcbb73e3d4b8e776f605f5e10b0e11d90d2b2447d"
    private val jvmLinuxLibcX86_64: String = "1dc1c06cfdafbfe9d5ccdf8b57a1ea651b80ff655dce36d92295c1c6995ddfff"

    protected open val jvmMacosAarch64: String = "25c3280c419353e0782e8b8cfc2245911525c026e5a73f084a3253bec741801d"
    protected open val jvmMacosX86_64: String = "9b937672d9839870e780af13688cbf9f42fb9f4209ac54f031eb6139d594a72d"

    protected open val jvmMingwX86: String = "e67d7755c4aa0b409f7175841aaee0ec52d1137c0bf3b3b715874a23cbdffe89"
    protected open val jvmMingwX86_64: String = "ad10e4d141b0a7a4097a308ec761bdfcf8a5398f1ab1eeaa166b1ad540833ac3"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "9fb7954fc52de0fd52625dd26b3453a062f0d69cee6502d026f22d646d19239c"
        override val jvmMacosX86_64: String = "bc791a62b6feec9cc1f83b14563158c3aeb044fbc1b156ca0eff1d03060c7261"

        override val jvmMingwX86: String = "dfe2e79843a0e71c0ed5531acc4ba85d5fc1b213c0bc56f245c93f7f0ccf1944"
        override val jvmMingwX86_64: String = "1870abebadd0071ce9add249c41d33836c7cd6e99a6c6264dc88075b0e834ed5"

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
