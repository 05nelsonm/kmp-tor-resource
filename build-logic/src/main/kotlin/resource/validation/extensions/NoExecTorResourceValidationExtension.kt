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

    private val androidAarch64: String = "ae9f18f2c939c1aa0225be52d798e79c8d998cf1e71d4064a16425fb029f8f03"
    private val androidArmv7: String = "b2b4c93e5f2f63068fba0fcc73d498c63558ae1ded4096c643d8d5e1bcc3c01f"
    private val androidX86: String = "f4c37390de03aed72beac643d7cd5550126706917b5614d618cb13e650fb536a"
    private val androidX86_64: String = "4903be21315a2fd6457c62effe8a6db195ad7109cdf41e5283717c5c610f1b9c"

    private val jvmLinuxAndroidAarch64: String = "264a0b5aee2c8410156525c2b44bb4d317f719ea3051741374028d83059587a2"
    private val jvmLinuxAndroidArmv7: String = "c576f140e756771fd5c1bd7c787e052c7a759e52c7382f370b22cc28a8ee1564"
    private val jvmLinuxAndroidX86: String = "e174292a42d6e912ba194c9805994aac74bc796b0f6adb632b56a407530fb9bd"
    private val jvmLinuxAndroidX86_64: String = "420cbc2102c88e3f8d92c3f14fea6f9f3a4a912ae9d968d420921a405149f752"

    private val jvmLinuxLibcAarch64: String = "b867fd5a76fe2a1384651472b1476c0ef49e43904d5f932dc7c5c32fb067173a"
    private val jvmLinuxLibcArmv7: String = "d5425bceb2ad9039659c81aa9b7bc74372eb32cec0231682d5cf24aebe139115"
    private val jvmLinuxLibcPpc64: String = "3e8a3c60849d9964d78857e964a3e87a5a3a6bfdd749b7f81a281e29f93fbad8"
    private val jvmLinuxLibcX86: String = "14fa95ccc9926ae3c22d591cf31a8affaae30e8a9c2af569025cc89e0dbf6963"
    private val jvmLinuxLibcX86_64: String = "7547b071f2b945f84a78eaf55c4bfcb6e39630dfb7d6819f87b79b9716933854"

    protected open val jvmMacosAarch64: String = "e45dff9d7b558e397a1fe56efbbcaaeabbc90fd7e7cc91480432f63c08a913ce"
    protected open val jvmMacosX86_64: String = "b791efc2920f6cca20c65bde9df816a347e88c026220d6cf59a5668ffc4082b2"

    protected open val jvmMingwX86: String = "c661c4f3a26c75403178418f06fe6da586c29a5a87094ff7c9a63cef1dbc3eee"
    protected open val jvmMingwX86_64: String = "6d52e9f0f06ee21b59489dca553672a87032549e9c1950142c2761dbbfd66b19"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "dd5c3574f409bf350a88068883c430e40dfb4254e18028e47bfffe723fae98b0"
        override val jvmMacosX86_64: String = "7c046413c2388b19cef80fa419f7adb107735933965a25ad2af41073005c7e94"

        override val jvmMingwX86: String = "0b78187bd72fa24c3c6df61d58afb869a042c50cd404ed325b90c510ad815b78"
        override val jvmMingwX86_64: String = "1e909054e155ecff19503af64419328d4868be4824201366013e8a297ede9bae"

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
