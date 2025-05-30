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

    private val androidAarch64: String = "c18dafcee19e1444a75c0e56c196c1e064f72b6c456849d796d04859b6440481"
    private val androidArmv7: String = "1db756806a0b2b4ac6535b10ab4968a350b8c185b632d1c087dee18ac8b23c9e"
    private val androidX86: String = "41db5ecad99271fada6675443cfbe357c064eb6808fb7c8a1079f4a27a904279"
    private val androidX86_64: String = "d84fd7b131824f48c37370ebdea0225da59214e38e50d4e22e216481168e231f"

    private val jvmLinuxAndroidAarch64: String = "8d5b57c790e90fded7077aecaa089379b01ab421429e4e10245a80c85c6a702a"
    private val jvmLinuxAndroidArmv7: String = "1191630ed6b84de7adf57d63968adc7092e38374c4da8a194698e402c10fef34"
    private val jvmLinuxAndroidX86: String = "15958220d573446d883835f3089d8e9d930cc32666cb289b3515d7b5c570ca64"
    private val jvmLinuxAndroidX86_64: String = "8890be5439151b93f6ad027ed021ef06a1d78f57ead7150915577f9c0eeb2783"

    private val jvmLinuxLibcAarch64: String = "64ebf5e60eeb0603b65b2aed60a088f26765442e9d40863573bca1419beedd35"
    private val jvmLinuxLibcArmv7: String = "c1148b76834fb1b27d2a03befa1c161526db630fb90a1b6518e4fe770794527f"
    private val jvmLinuxLibcPpc64: String = "12e1fabbc52b2b0101847bd28b731155bfed19fefec2163b8dcc4954e68111fb"
    private val jvmLinuxLibcX86: String = "2e5fee391af24915d14e45149a2cd2e84e36cd60bd0bdcd315aab62078ad31a8"
    private val jvmLinuxLibcX86_64: String = "b8a5a6ec83b577108f74da1cca22bd72de456dc77964a47e28871dffb39dfcd1"

    protected open val jvmMacosAarch64: String = "6525821d263ec86195dc542e4f994f297aecdb61c31ba502095b9b053d6dffc6"
    protected open val jvmMacosX86_64: String = "c2d26b1c932abcc5d1621cd832a790c2b20d99f7791a4f44a66531152a606b61"

    protected open val jvmMingwX86: String = "c12b4b2886961298c755f888b28380d440a81253ef8115373e90389a7df9aa82"
    protected open val jvmMingwX86_64: String = "0db63290080497838ddcc5999352b27120309832bbb5352cc67089b6e6f84969"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "94a527d4781e3685c0c6a66a5a9f1066bc8d4e2b1608c83eed46498f81550e3b"
        override val jvmMacosX86_64: String = "d3f23bcd61a1c08533818f92ac5fded02956b8f6f98aff0ba4a7ed1baf0413b7"

        override val jvmMingwX86: String = "2b7b7c47fd29ef75177503d54acbeef8abb8402b63c77d69fb6c0f1face3a3f3"
        override val jvmMingwX86_64: String = "dde13f2b273d77f4f23ede4ece92807ac56d8d3bf8873f14bb7fd2eac4f058bc"

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
