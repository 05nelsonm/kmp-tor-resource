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

    private val androidAarch64: String = "d8be580f711236877fe444f119e98d7cb579a23b12fbd6557abb809dba50c5cc"
    private val androidArmv7: String = "0054ec36d1ff51accb322a296718bb21cbbc1f2664e0f7bff83f11e4f41fb36e"
    private val androidX86: String = "842ba1aa276a47c5a1be75af328ca6f70a9adfa574a5d6517d3b1b6e622a1b01"
    private val androidX86_64: String = "2177940ccd095a884ad1cd2de7b6d8846e942428d1de12a057454bca20cc503d"

    private val jvmLinuxAndroidAarch64: String = "f982e6d87a75bd5535db3e42dcf1ea6eb82e9f0b76617a77ec88849d67106e14"
    private val jvmLinuxAndroidArmv7: String = "897d1830fcc2d247665290a1878c2d6c3271f85d608195211c6a623a220348fa"
    private val jvmLinuxAndroidX86: String = "cb9e1eddb7d227f0b0f1c1754a3f028a99adc07a1e052cc0d885d25d7ffd078d"
    private val jvmLinuxAndroidX86_64: String = "4f6fa52ada6cf4564be19aaf2d0bed0461fbc26715c425f100f27e3f5cc4dde4"

    private val jvmLinuxLibcAarch64: String = "589bb28a13709eb243de9f31660d0929c48a0a0f719d3a905f70e2ad506d7f6d"
    private val jvmLinuxLibcArmv7: String = "23696467a4f89e93479e1c9937449d93a64de3f93f8379e75cb266c93da156ce"
    private val jvmLinuxLibcPpc64: String = "ab8c177841c8aaf8f5916b9e912e50e16700544c078c672cdad42a01a95a33ae"
    private val jvmLinuxLibcX86: String = "582e52d16315379c9c14540bc3d1342e719c5372dcd7185dcca7167c1b740112"
    private val jvmLinuxLibcX86_64: String = "9af9b947901980545469597cbbc5d7303bd862f0f0cadf87e6605d32d56cb233"

    protected open val jvmMacosAarch64: String = "16b26cdf2b32e6810d707679adbed0a92c7396b8ab6a2c0006905e89337d8a22"
    protected open val jvmMacosX86_64: String = "885a2d384aa9f46e909cb2dbdc46a479a1d00e8e5cd6446c4dbb02ee6388121b"

    protected open val jvmMingwX86: String = "856d6ad6e3e34ba8a9f4d3f444684d7b27686b2218bafa90428024f5c171f083"
    protected open val jvmMingwX86_64: String = "39731f8ea426541c8535349e3df70e5fffc8bf29c432bac6de5b4e3ec1774bb7"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "3d397ceabf679e322ace3849aeca12210b28f49e869569b1801ead68eab928f9"
        override val jvmMacosX86_64: String = "3d0340bbf665e9e67335f5ed603556c6d13c9485f5ec92b89b40b001b6fc2779"

        override val jvmMingwX86: String = "cfd7e29ffce4f654c406d28585c9e92cc2a4f879ed6b308a290de7cd453802cd"
        override val jvmMingwX86_64: String = "4c9683cd4be0a97db875dd96c136bb252c991cb960c1ebca6440eff8f74244b1"

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
