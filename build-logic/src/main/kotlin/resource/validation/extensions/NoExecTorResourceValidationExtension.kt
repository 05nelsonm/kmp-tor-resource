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

    private val androidAarch64: String = "3c216a26add47efdb6b608a2e6f6021c33d768911207c12445fe98cf9b691722"
    private val androidArmv7: String = "523639a6557f2c4e6fb80d6c523e8b9eba61ef33cea9db7d1d9a4c779aee96fb"
    private val androidX86: String = "befdc653ad2e6b0fa9aee0c4267139b98ac8cb479741d28e720e6eff92f5a80b"
    private val androidX86_64: String = "341b1483e85d2defec0ca51da2ebc91065c4c3d6b369d413fbc78c30129ebc97"

    private val jvmLinuxAndroidAarch64: String = "49e2345d950508f4cd6f851f22df03c1311945f2ee21c0b6c73cf0ab86fc6524"
    private val jvmLinuxAndroidArmv7: String = "ebb9844a9ebb80771ab30869b4efadb50a870e7f1fab256c8d38d2cfdccd17cc"
    private val jvmLinuxAndroidX86: String = "b73b784c18fe52bad9c6993b53a110c80db7ad0bc8fee72e813eee0412379e2e"
    private val jvmLinuxAndroidX86_64: String = "896eb6c8b2d3a61854398dc9263fa495818ff8755db3fa1e62582850ea6b18c6"

    private val jvmLinuxLibcAarch64: String = "8f14eb2b764dc74edd34bf2c41d2fc88a4d61abfb143870ef21fa6cabd954bb2"
    private val jvmLinuxLibcArmv7: String = "5045391d210084c4ab28aee9fbff92dd8bec7b4eb5397d3abe766686ec1c7fc2"
    private val jvmLinuxLibcPpc64: String = "059beffda514b8914c1b1e21a1a4d9897af2e152473f6220315022dc19b23adf"
    private val jvmLinuxLibcX86: String = "ac0db6d043fe2ec92ed9fad396375a571750dbe25341c464b84c3916cb4fc22b"
    private val jvmLinuxLibcX86_64: String = "ee4852e1436332574bcd69c7faaad11e9910fd50d983beb1d620407dfe570558"

    protected open val jvmMacosAarch64: String = "fe4d41f54f76ce753982251b89c8efe2a3e60ebc73983d02a7d2b24960862b39"
    protected open val jvmMacosX86_64: String = "4f6f2e3a87c75eb6a11532237d69cbdd4ec49ff5220dd192ca6af48eb0b970d4"

    protected open val jvmMingwX86: String = "a7f7941cdd075bcddbb187a58d48593bdfa39c361b94650dc9e71e7c5dca5007"
    protected open val jvmMingwX86_64: String = "971225a24b0c9f9a154ce089eaf4a72d05434cfff81d24c7c86b12384658c261"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "c0ba75e7fd4ba8d73b91a72ee594c2da159307a4829cac4034efc228e825b371"
        override val jvmMacosX86_64: String = "33541d34cfc481dccf96e4a97fea98883d466025515d6e9c45af98ffce9bd1d4"

        override val jvmMingwX86: String = "c83099a74b28758e479cbe570b835624881263108b1127ca3defdc7066bdc0db"
        override val jvmMingwX86_64: String = "127dbf9120b775a520224cf7e7af26a34bfb642eb77b1708ef7c584dccc8ce07"

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
