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

    private val androidAarch64: String = "e08c6036d984f8cc970409f4dd61aca59df255e51abeef31fd7b0d4e7d243b25"
    private val androidArmv7: String = "6bec591f068c2ef9bee63779d78772094eb716e6d6bf8e5258691d5ce6109d1e"
    private val androidX86: String = "5d1b45db1341fe7dcac5a51125b8a6b235578abb3deefc76f6095ca503adc430"
    private val androidX86_64: String = "6ad2ebdf75a470a430e841a450c235a1c89bed7318e9c5fdb6cb7b77779a3c21"

    private val jvmLinuxAndroidAarch64: String = "f8df331ea5fceda82ffc63b13025647c6ed173412ce6a67d61cb2d85ecfe5653"
    private val jvmLinuxAndroidArmv7: String = "4bd592f25cac871cbf816e9b2e36dbf0c621dd75b405fee834635ca4a44ed388"
    private val jvmLinuxAndroidX86: String = "a4b33200b7dd6f4dbee39bc84e1b5cedd0a04fb75bfaa6ad9668cb4ba68deec5"
    private val jvmLinuxAndroidX86_64: String = "371074a042490f39ca26fefd3fd60493c24284b6b3122d11763253164fb198ea"

    private val jvmLinuxLibcAarch64: String = "95e4fbe0030039df5d5d9aea8cd613b3ec478594f018cb9b76d2ee7abffb67c4"
    private val jvmLinuxLibcArmv7: String = "7f4dda031d753248853138b914656a58a72863f8f62aeb6126035a01cfeb951d"
    private val jvmLinuxLibcPpc64: String = "48e662eae8a45401cc5b529b9b2a91cda706fbea3093ac9d98b0f3141b629a86"
    private val jvmLinuxLibcX86: String = "e9e2f974b88153eac6f9938a15668e7bdd05a503e00b4e76f80590622143fe00"
    private val jvmLinuxLibcX86_64: String = "582e00d33346b7ef0d9ec18822f951601621bf32d597ba4aee5f5872cdb0d7c6"

    protected open val jvmMacosAarch64: String = "b355a6bbf97410f9fcc01a5316df570edf4ec06a5db3feb5d20b3c921708e82f"
    protected open val jvmMacosX86_64: String = "20e492c86ebf5be2ae8d8020e85e5dd68c5232b19975e323e4448ee7eb73a707"

    protected open val jvmMingwX86: String = "49b7ea22011fff713aac29bcd98ce54ca1c8ec5dad83c0ad389e286b05a3e81c"
    protected open val jvmMingwX86_64: String = "07480cc119c54703a91c7d0f89e32a9604cae9adf17bd66af4bc93edec70f8c0"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "e15f6d5d4430748d30a6de99e76764b6e9faed058f33a60bfcbbd8682c82ce1c"
        override val jvmMacosX86_64: String = "18d1c9755f77179a570adb9883120351d186fb9c8c0770fefcd1f11e310ef70f"

        override val jvmMingwX86: String = "9a9ff66c8c483594dd85849ebb0939c3b41a252ceb9875639781bbe7e054424a"
        override val jvmMingwX86_64: String = "b875b4cd42c9be7c452f33af1f697ea95847e39fa788d625f53359f1da041b98"

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
