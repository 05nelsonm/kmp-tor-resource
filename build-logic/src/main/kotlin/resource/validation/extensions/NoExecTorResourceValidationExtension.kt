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

    private val androidAarch64: String = "1afa604c3dcb3b33c24d4d9ed7348d49b52ab764f279b650aaafd0737164dcc8"
    private val androidArmv7: String = "0dcf85ed8bbf07b00d08b5e20af19cf354ac0eb85637ffd3ce615b2d82106a1f"
    private val androidX86: String = "3a3bd8842f9a5ce17213cea478244cce5f6a50e8171ef810f94cc35eec156d46"
    private val androidX86_64: String = "654c8f475d42326e5ee9aa45452dd775c5f6ece6fa3ae00cd3760982b2bf81c0"

    private val jvmLinuxAndroidAarch64: String = "352bc9d8eb011269a44cc270e60867f576585292373d072b74c84ccb97150030"
    private val jvmLinuxAndroidArmv7: String = "6e08c1bebfe6c9a60edb12c4951ca10346e75b5caf655a66a50a0decd865cffd"
    private val jvmLinuxAndroidX86: String = "a574f8d4416636f214610d77e6a5a8548e40f964683b789ecd258d7e105c2787"
    private val jvmLinuxAndroidX86_64: String = "deeee93670eb2faa277031967e499f24d9d7b7f2fb5fac40bf7c2a104ef050ef"

    private val jvmLinuxLibcAarch64: String = "887fbfcbc319c8a1beb34cb060779776590ea63c86d681b18e9063f773f86235"
    private val jvmLinuxLibcArmv7: String = "ff9b7bc7c7958baff8fec9122c71b65305e94ac40957a8e32ceb84da72d8c621"
    private val jvmLinuxLibcPpc64: String = "e15351018b9ff85c12e14f40e974f542157c7e33c4634cbc808814d36951d4e5"
    private val jvmLinuxLibcRiscv64: String = "f2e06d9463bc74d289d2f68c0598226f72e96946b560671adadb905cd5cb5418"
    private val jvmLinuxLibcX86: String = "63d28c9eac8cb180729d6a95e51d13516583a860444e53a2ad82d446c7074874"
    private val jvmLinuxLibcX86_64: String = "81c495605fe1921380c38e5ab1ff272c6b4dbe9f96c1d86335b71d0366bfbca1"

    protected open val jvmMacosAarch64: String = "3a7f148b3b3afc82986f341256998cf9513e367c4c251c6386cd27f6de4a92da"
    protected open val jvmMacosX86_64: String = "d5b11ba70b589bc05dd21eaab5d73ea7b3eeeb9c69844165f66d082bb46cd31b"

    protected open val jvmMingwX86: String = "fd6dacd8458088dbe06dea7f6816b5fb32a6fff3b0083b86bef7260cca532a7a"
    protected open val jvmMingwX86_64: String = "63a58a92ec11a89a59a45855fbb91929baf73d8cad957b90f470e53ff5607ace"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "fe06c3768402d66a8fe57ffb53b48affd837dfb0994a1c3c4832175fc8f2a69c"
        override val jvmMacosX86_64: String = "b3749c315cd66010358cd238d1c7368ef1eed1a88d8ad6b6fca482b4725d84ad"

        override val jvmMingwX86: String = "ee1c8aeade1bc914ad709e7c3d3b6bba3e4a8cb99f81e018c7f97363f4ea2ffb"
        override val jvmMingwX86_64: String = "bcbee98b8369c49f89bfccb66f0b42a5d8db735c7bdb08ebead60745811a5f56"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun errorReportAndroidJniResources(): String = errorReportLibAndroidProtected()
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun errorReportJvmNativeLibResources(): String = errorReportJvmNativeLibsProtected()

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
            arch = "riscv64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxLibcRiscv64,
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
