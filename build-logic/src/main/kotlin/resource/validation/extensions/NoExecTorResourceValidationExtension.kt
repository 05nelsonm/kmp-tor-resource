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

    private val androidAarch64: String = "99bdb2f687cbb0915ed6f765dbf8d235e370716daec4148d3712ced8a38e92af"
    private val androidArmv7: String = "6c5a943df02da1d153f88bc58372af8622c8e44fe3e6a855c96613281444c90a"
    private val androidX86: String = "c34f4da675ce3dc2ff41e63a1d35bc18aab531f492c0e4e5d502d617e0c9db86"
    private val androidX86_64: String = "88f17376a6821e230f20b428f0f80640bc4b3f41d560385c7286c3d788df7382"

    private val jvmLinuxAndroidAarch64: String = "97a7150973cccd235b7946fa478be2c9437e5964b9797f91e1aab03fdf142a5a"
    private val jvmLinuxAndroidArmv7: String = "13ae41e282078b54fb71df28fdd1f9e3e1a29f70b4c0f956352a3f1d97ed74b9"
    private val jvmLinuxAndroidX86: String = "0b31bae1bd584194546ed19abaef1f08f7d26d87325b8897331de1cf65975a32"
    private val jvmLinuxAndroidX86_64: String = "0fe46871abe7be07ab083c776b7efe4ada5fe282f098e1f0e2f897d885004f12"

    private val jvmLinuxLibcAarch64: String = "6592f57b1b03b66c0d917eac0401508ec213ceb8b2fe0655d0218bb9dd360703"
    private val jvmLinuxLibcArmv7: String = "e72415327069b70ee1717f3571cad852dc8dec53b6e854ecf130b34634ce9544"
    private val jvmLinuxLibcPpc64: String = "572b6c5816449b859f1851a8bda8353759e3785edb03c3fa613dda01ff492476"
    private val jvmLinuxLibcX86: String = "d2bcf7c32ae4447e4870875d7009e4ac80e004821a89a3fd3784164e82214f89"
    private val jvmLinuxLibcX86_64: String = "829f22f4369cb706a7ff0cfec7a98374846a1d52ab62cd77e7af28f499113036"

    protected open val jvmMacosAarch64: String = "fcbcd737da68e6dd69d81e424838dcae29bda220ef29115b76d28c921edf2cc7"
    protected open val jvmMacosX86_64: String = "f1fa1399eaf96a011d594011ef64853882fc49daec814b4b08a573a89c25a5c9"

    protected open val jvmMingwX86: String = "ac9e9325dce8dad78e69c05ee4835248fb5c6b7946df12e5a298bc009baff893"
    protected open val jvmMingwX86_64: String = "595d137d581343609a23b2c61ebdfffc828caef8b8c1505dd29d0d635af27f06"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "0baf2ebdcfabe215a98a539ba59f9917020f8478adf452f68fb90793bd026de5"
        override val jvmMacosX86_64: String = "609d13c6b1c8a030dc43161f3163d2cb42fd9700a0b40d8b0000db629f04128a"

        override val jvmMingwX86: String = "be51dc91f62112e776664452ae9e9a1d6c3fa020ea5f7bac7cbde14583a9410f"
        override val jvmMingwX86_64: String = "3530713212055474564661856bc96966160df427f7646033896975fddb9cf6ea"

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
