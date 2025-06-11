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

    private val androidAarch64: String = "124c78d8518530e4480adb5fb522ad16662c8f051de3cf327221e18c0ed0b296"
    private val androidArmv7: String = "46c4a03fb1c5c9b84805eec695c4e16c2908795d808ee4500d3ed6de30113211"
    private val androidX86: String = "eb9534cd658dea73df14416e4187fbf6b01a8919a25e6c2d2311eb2491525466"
    private val androidX86_64: String = "c86eeb631059c21958b65b9b231639f004a424fd6d0d9739f9a56d4c3758dcd8"

    private val jvmLinuxAndroidAarch64: String = "bf54a6c0e77f1126919eaef9449fc70d50ad54325d94a1e8dba0dc88b28e5ce9"
    private val jvmLinuxAndroidArmv7: String = "8ced9c00d1318079a674531ce4a04de220a3d54566cd4816d9c4a3bb7eeab0b7"
    private val jvmLinuxAndroidX86: String = "806cc14f4c32ce1c3ec488759299a8a61a29f3b30decc1befafc74696f66f85c"
    private val jvmLinuxAndroidX86_64: String = "b0751283a4e1d70cb7cf9375c7908762f49b19400532811aa5d9bad9cb77815c"

    private val jvmLinuxLibcAarch64: String = "00c9277476e9ee600c8f71d90a520dd486b198455dcb6a89bfb7ee95bb3cab7e"
    private val jvmLinuxLibcArmv7: String = "a5b8b7615b20f127ddecc9f95d20a586846688b5af7b1a0e2c9b7951025a1268"
    private val jvmLinuxLibcPpc64: String = "6b692cc017d23d33c49d15a32a0fc79ffe35a977186065ff6d0d7861300a8d86"
    private val jvmLinuxLibcX86: String = "acde9333aaff529bd6d66c557f8a1393add43ebbe7ef7e67aec0e1ac9595a147"
    private val jvmLinuxLibcX86_64: String = "8c6043a7ea9349a94953fab36cba80ea6d2c91f8cda207326fc9f3fb00b2656f"

    protected open val jvmMacosAarch64: String = "787792d72e5882b0b77fff46bb13d4325ee804b9d10cabe21448b0269a74bc39"
    protected open val jvmMacosX86_64: String = "f1b4378f7a8a034f05461ab577a0e10b45a9a3da1308aeae12076c945663ed4a"

    protected open val jvmMingwX86: String = "d56cfee5cedce8f63269cfbc93a11ac2dcc258861f87de9f257e04e3a2e485aa"
    protected open val jvmMingwX86_64: String = "23e9168c975a59a8e9a0fdee310efb42d83963ece17023b75278d8cc0b671f65"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "f63a2f1a28522aecd2e79b669a4aea0bb4d645c58309f06b3fcb956579761e60"
        override val jvmMacosX86_64: String = "867fa97188518f853e2ba1583470ef63ba8f949150983d877c00cbbc1ab3fcdb"

        override val jvmMingwX86: String = "24deff0480366a44d7ab7765b327814626fc20c23f9a2614dc89f535f28b09d0"
        override val jvmMingwX86_64: String = "241c1af247ec8a151fd13930cdda65271a201c0afd5a8fd75d6cd5dc16107ed9"

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
