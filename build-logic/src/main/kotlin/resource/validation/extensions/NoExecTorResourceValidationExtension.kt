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

    private val androidAarch64: String = "52399962b96b574bfe0f6330aa4463733855be3b1bd1496bb4a1dc1653dcecc0"
    private val androidArmv7: String = "fefe75ef2df61d144157a352716616e9a6a9985f31a8cad1b62e01c3726ba084"
    private val androidX86: String = "9f1764542ee7b0fa4ec75fd22e1c76518257591892f597d8931b2a895d24093d"
    private val androidX86_64: String = "aa124fe5192c84653c563ae43685a030b09af45e22a86638699f20379dacf059"

    private val jvmLinuxAndroidAarch64: String = "865e1aa8945b95c6d1c87b2768b3142546930b5f91e5e99ae95f0ca501ec0dd1"
    private val jvmLinuxAndroidArmv7: String = "b086fe20ffba8f94fcf6670fecb8959e08828e6e22b97c5660c8c7532ed0a65e"
    private val jvmLinuxAndroidX86: String = "f8e04011b89efac909cf8c34b026c3d408beda91041ace826060f0cd98d31c5f"
    private val jvmLinuxAndroidX86_64: String = "7349dffcea1a13767f260aa1329dcfad5fea56f6572f19c4dce43e268f4e6245"

    private val jvmLinuxLibcAarch64: String = "9097a53ff48e40675619f84b92e1cbb281fe9a41975849a4c56d3ad4f036298a"
    private val jvmLinuxLibcArmv7: String = "de54bf6e1c1dad82cf82c1c3eb0675023ee754390d306356dee20cbf7b879740"
    private val jvmLinuxLibcPpc64: String = "578a4bc5a1c4125f27e3a58fc74d7d4f7fa55067124fd40b1a08fadfbce4a62c"
    private val jvmLinuxLibcX86: String = "521bf751551d8767c49458e89f9fe0a81ec0df4b359593f8a7ed1cc48e0ea18d"
    private val jvmLinuxLibcX86_64: String = "f8408fc5a218d3184facae1d5282c4a16c1b8ac1e96a5a3a22355820585d5049"

    protected open val jvmMacosAarch64: String = "bea8ab99f9b9f48faa0860906c48e53e197a19adb5f9d05bc71204cf1a5f4875"
    protected open val jvmMacosX86_64: String = "2069a7b085aea8d9a60b0f24560ba1f4a355ac802ca78cb77e519db0770e4624"

    protected open val jvmMingwX86: String = "69f177a0b16b6adae365621cbfe60ad5166d941eadf26b7e4c89e9c2c79ccd94"
    protected open val jvmMingwX86_64: String = "4fd2ff23dd875c855718e9b27b27c7ae62b7ececbaec7647eb5901653e3b3f66"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "c7a38bb4ff0ee3c9fd40803a0b9388676269abc2dd469f67e00d5fb1e5356f07"
        override val jvmMacosX86_64: String = "68cbb42aedb2a218189ca7d24f69fe0ce9fe3ebccc612199bc22261204817c81"

        override val jvmMingwX86: String = "6dba5c41c15a4fa2f43a749ab6797640b3fd8a17c6a74d78b102d756354be5dd"
        override val jvmMingwX86_64: String = "49c2b9bb5a471d0c1a0dd270b949cff636388364f4b957672222b29f8e30c8ac"

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
