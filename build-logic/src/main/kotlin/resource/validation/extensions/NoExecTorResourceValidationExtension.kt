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

    private val jvmLinuxMuslAarch64: String = "ab969dfde1579ee3c1afdaf6139eb8f4bb3d929c77f22f3958a3eacac1df78f1"
    private val jvmLinuxMuslX86: String = "ce6631cb358524e136452e4e1cd5169f1fe656f8a8e27f821440ee931b47153d"
    private val jvmLinuxMuslX86_64: String = "3dd42b4e744fc0a45df4c89bd64ddf2ad62ae54b0f3830f50fe5979b30c05417"

    protected open val jvmMacosAarch64: String = "bb364a83f5d894259c237ca498bc6ba94e3ce1584244bcccb72dc6239af09728"
    protected open val jvmMacosX86_64: String = "d379af5a37b5bd8851430441b40bb9a5e3530f00ea9cc35b63e5b7af44a2afbe"

    protected open val jvmMingwX86: String = "0a92d437f1ff2481e766ca988a736972dbb1ed5963725cf2ca9a9166b2af21b6"
    protected open val jvmMingwX86_64: String = "e28fee829cdf16b5c7b89b63d35a4ad22129989c340369fdc92a3ed1d7bc0ffb"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "5cdaee589f0a2c65679f778b76d43c5cba199d884c5ef64cde88f0e04dc2666d"
        override val jvmMacosX86_64: String = "862fab9b54e9f902100deef41e9531ef1e95368acf2729c1b9ea6b41de47207a"

        override val jvmMingwX86: String = "49c42d8e4cc04799693150ab1301cb62723243f4542c7d037dbf86e5ce7ae2f4"
        override val jvmMingwX86_64: String = "9357266df8525b9ee7f251c9c1f7e7ff84e9a5259d77854ff1f41e59bdfdedfa"

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

        // jvm linux-musl
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "aarch64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxMuslAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxMuslX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86_64",
            libName = "libtorjni.so.gz",
            hash = jvmLinuxMuslX86_64,
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
