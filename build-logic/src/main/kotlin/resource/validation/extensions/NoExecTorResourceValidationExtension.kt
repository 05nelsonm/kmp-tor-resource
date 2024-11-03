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

    private val androidAarch64: String = "52e663e3173c8c56193e2255fe39a730fb930735d764a72accd59c1832779636"
    private val androidArmv7: String = "b77f1e091cc951794c76d461fe8f92f70255952273fa19699d0df2938a792ee1"
    private val androidX86: String = "ed67574fdaf404f357c71f9bc7e5026e5c9bd9dcc5ba517c54926878a989e66e"
    private val androidX86_64: String = "788a9ac612ac7dc4091b221fb76ddde6654154a2af2296184ed45d148caf7535"

    private val jvmLinuxAndroidAarch64: String = "bee966ded150285d4f8016c7e2c94ee7ad450f3b851fd53311408f0aea36ee23"
    private val jvmLinuxAndroidArmv7: String = "a1cf7e729f7b547f2854fd1fb9a44fbe6a5d0cd319b01ab2b2d0a7dc4f437272"
    private val jvmLinuxAndroidX86: String = "9088315205071014fd40fe2cf75cd9bf98259429920ede4d26ca7c478e46104f"
    private val jvmLinuxAndroidX86_64: String = "bb6771308733527bb08500b997da6966ee8de44f77c85c61670773542f19db2a"

    private val jvmLinuxLibcAarch64: String = "7f9eb27a2d9aed1f00f1b03b7b0b6436f01686a666a5554a6bf6125c96dfbff7"
    private val jvmLinuxLibcArmv7: String = "30d35043e84160d0d41a2dbe330457703a8218069932d68d95bc39f1f8ace2eb"
    private val jvmLinuxLibcPpc64: String = "402fe386fd9c4c48dfa1b148e5ab2e5511e6baa7c64f59c52dfc540b393ba40c"
    private val jvmLinuxLibcX86: String = "bfb65e6aa0fad8bf081e9691af8dc43fbaa702435b3a68594c325a248e619e64"
    private val jvmLinuxLibcX86_64: String = "d67994d219f09cfeeca514b1eefb6ff0f996bba4499bdb8e24697d8e426ef498"

    protected open val jvmMacosAarch64: String = "ae9aafbafab7d400d582b1346e7cf37eb64bf4ff2a97b483ef4f15fe08272b8f"
    protected open val jvmMacosX86_64: String = "645a5fbd8827dd3623ee6dacc18e8e933d1c64a1e79b6eaeccc5f680a08373df"

    protected open val jvmMingwX86: String = "3018caee43d0e1d49da80928aae48d9b887206134cdf890c32dbd57fdbcf62ef"
    protected open val jvmMingwX86_64: String = "9d96b4f1096b5f0ce786c6f657ad08fc2a4889a2125b58425b39cca0fd7fd715"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "4992e02a16b885950b85baab5547446b94538cb556edc1f3633d7ec365858984"
        override val jvmMacosX86_64: String = "b0eecb71b3f584f12a00a328aa77c7a5da9f4286d1c5e1d38fa34e9d4e8ec3db"

        override val jvmMingwX86: String = "151949b5c67fdba2c902c98cb50fa0b7e23016836789ea598243775a149c70e4"
        override val jvmMingwX86_64: String = "ff1f5938e7c4bac72c10497a71c8ba16421b4a62cad0128d0422b3c15d6b388d"

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
