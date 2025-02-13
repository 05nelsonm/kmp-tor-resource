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

    private val androidAarch64: String = "0222ff507da1320d01377406160cf98f69e18b310b188d9903591c8ae863e51d"
    private val androidArmv7: String = "9d062586342d9f78604fc9e054f2fdcbb3ed25ea959d3e19899a495411affdf6"
    private val androidX86: String = "f6438c4171c719c2f9ee7238bd8beb0da93370e67b007949fd091af7557c3877"
    private val androidX86_64: String = "29144390adcf26e049b2bb2f442b7d5f8e40239c1f153d7cf3c844373d452d24"

    private val jvmLinuxAndroidAarch64: String = "41cb3fc8e1c5103be31985d84c2ffa06525764ff6628329a835cfb48c067131f"
    private val jvmLinuxAndroidArmv7: String = "8e0df26819593d5558aef8ab1af9056bd59cf94ea0c7bc9eea875a9809561a89"
    private val jvmLinuxAndroidX86: String = "cf1cdc0b0fb493a6fb27c5f934a317874729a213a91332e43122f39d07dd25ba"
    private val jvmLinuxAndroidX86_64: String = "dc4c2ebd860d883a315474d7886ebd53fd5c338af819b5b35f4b8688310d7a71"

    private val jvmLinuxLibcAarch64: String = "6720a11a4922e4c1271bbe697890b827ec6f0e8ae01d0a8ae6ea4c883ae91519"
    private val jvmLinuxLibcArmv7: String = "bec6e76e9cc68212ca1d32f26815629525649c6422b5f928862386331bed5e27"
    private val jvmLinuxLibcPpc64: String = "2d967b98beea87207fd0deec38ed2cc4a89012844dbf68caf64bcfbe7952d4ad"
    private val jvmLinuxLibcX86: String = "fbc060038a565450c92114705c250913e3e7a996de3faae9d1b14312eed084f1"
    private val jvmLinuxLibcX86_64: String = "3172210eea50199bbe088f44895245e1654fde851d3b3f52f863f645c888bfff"

    protected open val jvmMacosAarch64: String = "febf6d0478f7c32b7d9b1e479ae4236e64e7bd931b8ea09828abbc9415ca1b0f"
    protected open val jvmMacosX86_64: String = "8fcce77d9f0a9030895d38b17b2c946bd91bc0ee20113390346595b3c54f0310"

    protected open val jvmMingwX86: String = "9cccc53b18d87d1c8e58559ae78c2ebf67e4db2b9229436fbe13950812ad73a8"
    protected open val jvmMingwX86_64: String = "70512e55a76684387de0640ae32e566b813d56bf8b8c646b837890c35a6fdc9d"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "1d938dcde6826ab876992353f0f3ff3bb4c7bdeb12d6c4670bcaac438450b533"
        override val jvmMacosX86_64: String = "bb36dfb0aae3b93f1c79e13d6ae830c71ab4b20737b0eff2ebe52d82e86810be"

        override val jvmMingwX86: String = "40d8ead61fe0e28de8050ab54579a48da4c522b21058235ee1dc95f24439d493"
        override val jvmMingwX86_64: String = "0da22735995559904088c1d2d50fc51c50af6d2a344015a60fc4b68754201e52"

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
