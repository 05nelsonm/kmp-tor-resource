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

    private val androidAarch64: String = "d8d52c6ba1b05e0b94810237c58f090f7b36876241472bb8092e531f58c24f8e"
    private val androidArmv7: String = "17b4ebcfdad81177f0157412219dde8f378239fc656932bd6466391d7608bc75"
    private val androidX86: String = "1b8faaf38a7f3f2b32d9cdcf081621c2101404939c9d4f51f41f8bb275f23c21"
    private val androidX86_64: String = "9e01abc01dc2e15fa6f284bd39ca2b7c697abefa81170c1b06050a4c363c6199"

    private val jvmLinuxAndroidAarch64: String = "fdd12e71e00b918f35b91bbb91585052f33a53da23d077e334bf86475a1b1a4c"
    private val jvmLinuxAndroidArmv7: String = "6069138ff6a1a3c8a4a69952a18ddce5467e6b626db8279cfe81d0b2f7e550f1"
    private val jvmLinuxAndroidX86: String = "63b6181defbfeda9b3a6976102c0636fd39ee49674fc47f6d0f5c7d8fb8d92b4"
    private val jvmLinuxAndroidX86_64: String = "72ffb54f27f4a7234227f19a0761ae99e4a912a143a9222d78dfc5bd00121659"

    private val jvmLinuxLibcAarch64: String = "fbcc18c192841e492fe11b96f3dd448b43189c77c5d81b8a35875bf389813865"
    private val jvmLinuxLibcArmv7: String = "37dc736f484c0ac5b5ad63da3c306d93defabe5aa457acbf9514b6840c549759"
    private val jvmLinuxLibcPpc64: String = "4a7c4ef18e635c0ba6c62a4c6391f1c633f38f9f024de1b4cbab82f738034c52"
    private val jvmLinuxLibcX86: String = "0f9a7f42092a46156f14bd2999bbde464f157ed844d1b2c3a51e24ea9bd157a9"
    private val jvmLinuxLibcX86_64: String = "fb1d298de31070d3b38af6864212c5932522784ba0f2e8943e7e891c921ac76a"

    protected open val jvmMacosAarch64: String = "3174eecfadd6fb31b172f64c5a36faae19f985d0525736771abd4d12593b0c03"
    protected open val jvmMacosX86_64: String = "86cd82dc25649199d79d8a42c9030a7d5bb66e1f209a758acfc2043d4241f472"

    protected open val jvmMingwX86: String = "50ed701f7e4d8197e5ee0e6dca78c83096179765c2a6e3cb676731150062e5b8"
    protected open val jvmMingwX86_64: String = "04572b75af87cfccfef3dd4863326899515675e06eed23e1268f5ca338d2b653"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "9d5c91bba1409399aab2e8b161dacd1e5a367e82b92083e2464dc2a244c800d3"
        override val jvmMacosX86_64: String = "ed40a9ed6ac732547af888e42401a5efc80c4f4db51e6f79b87bbefaa89143e2"

        override val jvmMingwX86: String = "52d9c1d22564f135304f3f7514eee5792ade33f3c668d3e1b39b3a1cc680f9e0"
        override val jvmMingwX86_64: String = "4f23312aa32c4be51f054ef67f697f53d22a7964feec3ab93fb881024a3c823e"

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
