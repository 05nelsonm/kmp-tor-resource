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

    private val androidAarch64: String = "89790631b34fe93bd1c410348988ebb648ccc5ffd14e874b7f69ce742d32849a"
    private val androidArmv7: String = "e220a3d9347ab9310886da7c2871894e65782a8c99e38d66d78b4c5570d7b58d"
    private val androidX86: String = "6820b59a9e650f6befbc95977fad8440bf2e4472079d225bbd70cc09bf2f16e1"
    private val androidX86_64: String = "686c14419956994e0dc90a3736820c5768e5ade5e6388f0e49d08abe9885c37b"

    private val jvmLinuxAndroidAarch64: String = "6607655dfc27336d13a66c364c0339053fe58727b759d8283edc836ab88d7441"
    private val jvmLinuxAndroidArmv7: String = "1bf891b2c6bce2d79a4f93ff82dd68cd9906cd5e3167626d5a3424ca78609a79"
    private val jvmLinuxAndroidX86: String = "7db14fbe39aec56ec8084838884c12080bc711b94e7ad2f3605f9f1d7cc7c6f6"
    private val jvmLinuxAndroidX86_64: String = "9024590f84c933f2482663c6a70d588573a1c513c719f51a324eb99e6540f7ea"

    private val jvmLinuxLibcAarch64: String = "496834875f21b0e5650542bb28bd64d65bc9ca5236d25df605b298b004c38c0d"
    private val jvmLinuxLibcArmv7: String = "03b58b4790213c990c64828c8ee9cb380eae980f551f5cc939f166cb6eb255fb"
    private val jvmLinuxLibcPpc64: String = "e2a79e0dbcf57ed79fc2a3efc27c142007c840a5663a8def6cd0c39da0a78982"
    private val jvmLinuxLibcX86: String = "345bf537ae2faf0e11d872598b6e724838acea02e37dc0b291a76556b0ad693e"
    private val jvmLinuxLibcX86_64: String = "d7843ed3d712cb5199ad0137d617aaca334663cdf5288074fe61dec6a0fea9e9"

    protected open val jvmMacosAarch64: String = "06dbd06e755990e86526cdc57eee02bf85848a38d9edafd0c949bb3fcfdf1de1"
    protected open val jvmMacosX86_64: String = "332e9147cffec3b8635ff226add0824a7acf02645a40ff4407969434d190ff53"

    protected open val jvmMingwX86: String = "54bb9f5b45bc8809c187d0b016c3c37ac228de375550cb2edcddfea254a45d3d"
    protected open val jvmMingwX86_64: String = "01e2bbdaf529f79c65ccbaf159304ca2d34e5134b8678351b8331e516b01dfd4"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "f20482c35ab2b7e4b934f28df4637c64e1a5c6c86abff86ca9712274ebe8e2b9"
        override val jvmMacosX86_64: String = "ec964fd3e2fffb120f5eae92fd70f9d3805be0b3ded7f44d0c68dfefaeb4b94d"

        override val jvmMingwX86: String = "3c2ab20405c5cda983fb43dcbd0db0e58c44100e9f9e70208a0dd71924a3d77a"
        override val jvmMingwX86_64: String = "6902d52f14f707e3adcd35ea3141ece05c7aebacb14fd87009e518b52e9183fa"

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
