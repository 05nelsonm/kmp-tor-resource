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

    private val androidAarch64: String = "d8b5bb3fd86a02771310fcbaaddfcd8f7c3c2384e03b490c4d91b5063ca8c6ce"
    private val androidArmv7: String = "21a89ba7860d912c584a1e76417537c6a9f2b0c86c798ad5377a532966d8fb30"
    private val androidX86: String = "6fdb0325424cb43f17bb9030f11f732117801e31726df39c4e86ab0da78b4bf9"
    private val androidX86_64: String = "1d81aa328fbe360c38ccdaeea28bd62e4a09d0ff246b6db0152c6f6bbb2fb5c0"

    private val jvmLinuxAndroidAarch64: String = "55359b6bf21b2419e14ecaf4afb37193476591488aa72b1401f38e7e3f11e318"
    private val jvmLinuxAndroidArmv7: String = "5f04a2eed6824f1867b3358772b764dd9b8bdb55e06f621c652fcdc0db6d4144"
    private val jvmLinuxAndroidX86: String = "664eed7b11f7f3ae14d06ed2235f61d536a153683bc9d7cba341d2974929d9bd"
    private val jvmLinuxAndroidX86_64: String = "feaed940bde98bebe36b0eb829749124528d88f4a555ec728905b8dc052fa6ad"

    private val jvmLinuxLibcAarch64: String = "67d0c95450e440d16cc9f5d522302347556ad356a6ce53c7f0ff09c73ae0671e"
    private val jvmLinuxLibcArmv7: String = "5a35d406a803574baf37e250efbf751ebc0cbbdee308529543d940d6d7e9730e"
    private val jvmLinuxLibcPpc64: String = "eddc3811cc5279a9962cbb05c487e6b6a2fdae78ae4691ccc3548055ad71d28b"
    private val jvmLinuxLibcX86: String = "685d62cf81154dcac5b732a51a94f055c941b2ca246a862defeb533f27df06a2"
    private val jvmLinuxLibcX86_64: String = "10f0d6b1bb81e933accda3a12b691d9985b64b7e86cefc3867c832f7c5edbdf9"

    protected open val jvmMacosAarch64: String = "de546a2a320768af1e33490c9aa557df8bc24d4921d32f8719879e431a2af07d"
    protected open val jvmMacosX86_64: String = "5ea116549a60e59c58c029a48ad95b4520f51e567141235b6ab4bfd5b48a9a43"

    protected open val jvmMingwX86: String = "de66dd77ed9a977df3c41e1f4e0119c5082698245d9ec634ad0021eee9e7dc40"
    protected open val jvmMingwX86_64: String = "b5f328f239f0d0cf8df57451dd32a804b278591186614e135c643e1c5d54ce9a"

    private val headerTorApi: String = "c346e767d3e6dbad44d1802579e7e4a8cf1b1ff8595152ebd4679b05d2de6df3"

    protected open val iosArm64LibTor: String = "497093291b5d897c55ae1ef977fe12617d9283bcbd12398719036caffc7babf9"
    protected open val iosArm64Orconfig: String = "aaed57d74fcd679a223d2620c84e859118050fa72a2e7e2ff32b287efa15fca5"

    private val iosArm64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libz.a" to "05666663a43c33bfa61f2791a643ffcae4434f8b643a2d3deb04f327a6746663",
            "liblzma.a" to "284a1bb452da5621fcca422128f45d1ad12e7a3455a92155145f465a8959debb",
            "libcrypto.a" to "7a4c7dfb4ac0bc073f8f56b8bb6663ea732254a98c49e8c7e61d2efd6d4b2b53",
            "libssl.a" to "1a7936975b333013bed051121327cffb68a1624c2b8df052caa2fe96177b9198",
            "libevent.a" to "b3f29311cec007d570de011a203fc13bc0a61e7bdeebed6053bc22cf19efe6c4",
            "libtor.a" to iosArm64LibTor,
        )
    }
    private val iosArm64Headers: List<Pair<String, String>> by lazy {
        listOf(
            "orconfig.h" to iosArm64Orconfig,
            "tor_api.h" to headerTorApi,
        )
    }

    protected open val iosSimulatorArm64LibTor: String = "86bab132334005ab3f77dd7f1764929de0cc646ef28f737234bf10380f3b8cfd"
    protected open val iosSimulatorArm64Orconfig: String = "b9173bc967240e9755a3984e1774a4d7e691f3b5221c24029859959e9086cd3b"

    private val iosSimulatorArm64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libz.a" to "f211bf9b7a3a48e9b2a8abd92d6c825973dc39b6f387120b3ec3159ebcd944da",
            "liblzma.a" to "65b355d6e116367c3c2d9ce77764c9c548aba69c4fa5b9de92c087596116d988",
            "libcrypto.a" to "040ca19c100b9a74f7615fff198c38b46299f1d26f6fcb893008fdb5a7309fd3",
            "libssl.a" to "559407a01914d6a3657230e4a39d4d998dc1ccad416ef54e58a3b18ad78afb4a",
            "libevent.a" to "056acfc70a2a07b0863a7904a56cb97b7fddefe91c06bddb56f817365fa21253",
            "libtor.a" to iosSimulatorArm64LibTor,
        )
    }
    private val iosSimulatorArm64Headers: List<Pair<String, String>> by lazy {
        listOf(
            "orconfig.h" to iosSimulatorArm64Orconfig,
            "tor_api.h" to headerTorApi,
        )
    }

    protected open val iosX64LibTor: String = "644f43c200e59e7fd071ee7fc94324da452f15ed295fb308738fce4e6672b151"
    protected open val iosX64Orconfig: String = "a66c8eda726b24668e26e3f356e0c098bcd8cbc75ef742e49ef48fff1587cb68"

    private val iosX64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libz.a" to "7a3b233b60ea0c976273fe66f0c32f4281aa5968c396bc853a0af21ed4c9650c",
            "liblzma.a" to "96cf01a54696ee52bc1a80122ccbe22e3b286e700506114ebee301bcd05467a9",
            "libcrypto.a" to "e006a4576074956aaa1e0a15b644d9b9799b39f2a0ab87f184b8fdd969e218c0",
            "libssl.a" to "b3d67167c15ed9b2750634f8cc55db8f7a2df2b2668f91bfd55771080f9222e4",
            "libevent.a" to "e8a3c697b96b579720dbeb10d132c431557e89f59e9c2828d416b6bf489e4cc7",
            "libtor.a" to iosX64LibTor,
        )
    }
    private val iosX64Headers: List<Pair<String, String>> by lazy {
        listOf(
            "orconfig.h" to iosX64Orconfig,
            "tor_api.h" to headerTorApi,
        )
    }

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "162918786c53be222e342203091514d732a694086e0eaeb92030002e1c0cd7db"
        override val jvmMacosX86_64: String = "e81ffdc33f9742d714d6369c28e34b58c74b7c0915c9d67840c3216461dc7db2"

        override val jvmMingwX86: String = "09d7a0120f42d17d73f7162936088d092739f74975c117256c6ab4368160005d"
        override val jvmMingwX86_64: String = "48800a296b50fe77d052eea37d8ea1e92ef332583531bce158605dbb5ccf314f"

        override val iosArm64LibTor: String = "1e4c4b41dbeeae08d77655dbb1a267f27c6e3297631b1a9ce7199728db8fb4c0"
        override val iosArm64Orconfig: String = "b968c615cad5b2ae02db58c4e722941d18b67e56d6ffab2afe3379a0ac0359f1"

        override val iosSimulatorArm64LibTor: String = "7b9a300ac0b1db365981bc59afc253f358a6aa105107c375534dbdc73d64eff8"
        override val iosSimulatorArm64Orconfig: String = "914828f9ae8a61f103575874d6e7bced84f0f02c8b8da089e8bf088ee05a5f0d"

        override val iosX64LibTor: String = "429c23710ed3561270e49cae5fb79b96210adc7dd38acfbedfe845544667124a"
        override val iosX64Orconfig: String = "576fcf9eab9a745d3bfc3f0e7b4e46fa310d9c7144d160f1f93f3d04810a2003"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeInterop(kmp: KotlinMultiplatformExtension) { configureLibNativeInteropProtected(kmp) }

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

        ValidationHash.LibNativeInterop(
            defFileName = "tor",
            targetName = "iosSimulatorArm64",
            staticLibs = iosSimulatorArm64Libs,
            headers = iosSimulatorArm64Headers,
        ),
        ValidationHash.LibNativeInterop(
            defFileName = "tor",
            targetName = "iosX64",
            staticLibs = iosX64Libs,
            headers = iosX64Headers,
        ),
        ValidationHash.LibNativeInterop(
            defFileName = "tor",
            targetName = "iosArm64",
            staticLibs = iosArm64Libs,
            headers = iosArm64Headers,
        ),
    ) }

    internal companion object {
        internal const val NAME = "noExecTorResourceValidation"
    }
}
