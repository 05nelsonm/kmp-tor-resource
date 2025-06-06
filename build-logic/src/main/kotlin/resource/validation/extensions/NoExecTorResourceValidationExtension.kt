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

    private val androidAarch64: String = "2cbc4d88144469129583413725409ed8bd16a2dcdcc8ffc50e5d9e0b573e47d9"
    private val androidArmv7: String = "9d495ad8ffc2089c96a15991e850352591c84fe00168aa4fe157c6a902a23e1a"
    private val androidX86: String = "add67506060f65b8e978654b5705e028f2b93a0b4388657295b394429142ab34"
    private val androidX86_64: String = "5b804640a1fecd24703cc120d7a15a4143403dbc42b643fc5bb4c5b468ddc786"

    private val jvmLinuxAndroidAarch64: String = "f0c1a4c6e6cd635ff95c0eed678df5cbf2ffcd06a3a65f87c48d69dc3808cc82"
    private val jvmLinuxAndroidArmv7: String = "3868dd66af3e6efb80f7553b50b9f8ad65c6868d92a290675a0ad6d84f05c608"
    private val jvmLinuxAndroidX86: String = "e50963c6040ad7946703210e3f81be5a739912f0deda0df4d87f354fd3158b4f"
    private val jvmLinuxAndroidX86_64: String = "e733754610672303860920c5098308151a99f7b5a5606a7036f283069c94b3db"

    private val jvmLinuxLibcAarch64: String = "9e8fc340e6b817f76ad27bb544bac6bf4a31189a937f6612f1831f8f8c816735"
    private val jvmLinuxLibcArmv7: String = "f7850ccfeb60874190d19f6f590de2eb02e3df72e23fbfc88cdc955b5b34ccf4"
    private val jvmLinuxLibcPpc64: String = "060220c6ab29d32290cd8bdb9909592f4f58ca12cf0a240cf86d68ab2cb3b00d"
    private val jvmLinuxLibcX86: String = "cb507b05e32bfa5627754d587febd0714215972d1b3c34ac24dc2d22d4134851"
    private val jvmLinuxLibcX86_64: String = "a0109272c34c2f7e6e71bc9327b21a173d6c20256ebea090977b3c99af7029d0"

    protected open val jvmMacosAarch64: String = "6ad50969534cda3268710b29aa46a4dffb04ae3f21a8f416305a15ec0142186d"
    protected open val jvmMacosX86_64: String = "a461bc1711fdd965064d6e5173b3488ae4abdaac3f7b98235e5c4de8b99d78ae"

    protected open val jvmMingwX86: String = "ce687a56abbbe866707bff988fd55a6488e99eae909061bb09ddb12b89861921"
    protected open val jvmMingwX86_64: String = "edfb2e184c2bd2bdda6e1b6916a295fb8f5eb80e9278eef1e7a0c9c1a5287760"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "a833c8067b1b2fb60a246738f5e9452395c952af7ac15a19b4b3af5c66f2e28f"
        override val jvmMacosX86_64: String = "e1204a6bd8d54c43922a84bc37a5371ccca1201c3090c7ca4da81bc1f9df65da"

        override val jvmMingwX86: String = "a2a7ac3c5577960ae774b8b05c7d5de9de55d4195a9f334e9132b2c4aa5dd796"
        override val jvmMingwX86_64: String = "141b538301ccc43150cfb3cf89693e13d5f3ed8a6f961b8b7e73fc3bff875ab0"

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
