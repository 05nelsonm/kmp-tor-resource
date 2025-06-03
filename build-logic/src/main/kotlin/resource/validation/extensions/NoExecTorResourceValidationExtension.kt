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

    private val androidAarch64: String = "962c834535c4bcd56ee7f610709a4d3023cab4a98025bf7ec318c672240e6188"
    private val androidArmv7: String = "f88bcac130dfccb73896ddc828414d5c2d40e2e0cd7a661c49e9cc77d7f39cb6"
    private val androidX86: String = "4d1ff74c8ca3094b1ad364b7104c25b61825f7f7a803a17f812038f8b1101b36"
    private val androidX86_64: String = "7721e2e350ab68b310865b8cae58b16a4926f5123e674adf08b50f1f8bb0c872"

    private val jvmLinuxAndroidAarch64: String = "65d46c587da038f1f8843347d6bf801984a97273d5db4a331f23d1ee4557fa1d"
    private val jvmLinuxAndroidArmv7: String = "93dc6e44b7ddc2e3c90f0e3b60aa92772dc201e2f71d716e8a999e06f008e376"
    private val jvmLinuxAndroidX86: String = "78ca14ce906783178b255bb0a4bf78116df1594d075ac7ae62fcf0313299af30"
    private val jvmLinuxAndroidX86_64: String = "8a72a32d0ee68ef46de13e26c7fc9526ded7370c0262e59803b5c8fccec19def"

    private val jvmLinuxLibcAarch64: String = "e2cff9f9e2e96894dd912d40a958140aee1f1a9784af735fdc2a99d5f325252d"
    private val jvmLinuxLibcArmv7: String = "5213d541368119b5edba7f17a21793ba3c75eaeb0ea4ecb2ffb4c8a7adcd19fc"
    private val jvmLinuxLibcPpc64: String = "c1471706a574902b358a84849f410fe849276e20e16bf401f71ae3a1ac40481c"
    private val jvmLinuxLibcX86: String = "22b4e8a58bb304a3e1144e7073267eb17b5f6cbf9cdf7ebee999660d6ed17633"
    private val jvmLinuxLibcX86_64: String = "15f2bf4f155c849aec673cd4821feb9e4cbd8e4cd7875dba730af0507ad93fcc"

    protected open val jvmMacosAarch64: String = "c1082dc22749361edaeaec458765b6c215883bb8c0db703b5b28876b340e7090"
    protected open val jvmMacosX86_64: String = "ad55462542276f68f32644e737f2a74b87ee124169a5918fab1a4f43d5cfc5fc"

    protected open val jvmMingwX86: String = "12a00c592a92797f23becabd5ef875dfa6e40866fb1c613274c020dbe6a59dac"
    protected open val jvmMingwX86_64: String = "b03e0580a9f232b87ef9664bd42b16c5a810642154d2c4e10b0a6385cd8e7a38"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "bd14e59db7c721f3727e3b44b77e627b32cce5eeb42f155888273d2958dc538a"
        override val jvmMacosX86_64: String = "68a39f71825a069d017d1bfb1974f857da7b46d2dd4837b0c590835a202a796f"

        override val jvmMingwX86: String = "8bbf160e651c1ca54149f891908f984cc4925be66fe960cd5b4cb8df86042162"
        override val jvmMingwX86_64: String = "d1546e18f17a556fb1ef0477d9d694cf21492505251befb1d9969a3153450138"

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
