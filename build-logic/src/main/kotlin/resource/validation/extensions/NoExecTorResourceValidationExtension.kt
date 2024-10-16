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

    private val androidAarch64: String = "75efb14cb2019313a640580fdb80d2aa5c8a777a98c1049ba79f133dd580b8f9"
    private val androidArmv7: String = "450022b4bb15e3b995cb8f2c02085779bd14c0021f1dd7b5bba1511890d980de"
    private val androidX86: String = "f96d5c3d3a75db03ebbcbd633a8288ee3bb6f6cb222b11ce79de08fbff60659e"
    private val androidX86_64: String = "a013a361877c26cd91d6b3aaa9501e9974fe9b4b21ce53f3d1950e068d122727"

    private val jvmLinuxAndroidAarch64: String = "5dabb9b42bc4d2f8393e498d55ad3690db726b39e82089bb8150b3a523078bee"
    private val jvmLinuxAndroidArmv7: String = "b0a2464a1936edd827797eea24767bf1a2f43f07ef98f3837884b2836c0bacb4"
    private val jvmLinuxAndroidX86: String = "dc71d7d54467ab93100de9b36482d59abe2b2653448687427db8803f69f6dff2"
    private val jvmLinuxAndroidX86_64: String = "1597db5189e31ef23b85127851831bd62d3a4a3e307647a60dd11a16698eb07e"

    private val jvmLinuxLibcAarch64: String = "1db7b8084547725ad7babffd668e5b371b34505c2a47e49ab0555801dc550733"
    private val jvmLinuxLibcArmv7: String = "4ffa817f456365f00c4fcd3bc5a3b26b27514dca6a43714ffe10e35eb649a3ff"
    private val jvmLinuxLibcPpc64: String = "cd1d6d930eef1118459554e89501fe18296d2668b2f04522e5121611e7317ee0"
    private val jvmLinuxLibcX86: String = "4dda0a9752c9753f9f39ece963d4d34001ec62503907916ca1ab7c545883a6a6"
    private val jvmLinuxLibcX86_64: String = "59c427d372dec67dcdfff46ffd1432d67488a7a3ea0b51900d7d2986e1db8bdb"

    protected open val jvmMacosAarch64: String = "e1ffd272166397d04af519e392328fde55f79eb46159cfb03fe93813477dd464"
    protected open val jvmMacosX86_64: String = "c6c5a3ec20bddd7c59e45938ff6b06eb0c20c2f3d95c34cb8b6fd77781634f5f"

    protected open val jvmMingwX86: String = "4c4948eadb2af88e44768abd1e535cd2cb051254cb4f36697e440007afc15b2a"
    protected open val jvmMingwX86_64: String = "d1c7c9665bc53d30f9f988fd47d0354c1d66ff0ddc56055b3b792086e7eb3ef0"

    private val headerTorApi: String = "c346e767d3e6dbad44d1802579e7e4a8cf1b1ff8595152ebd4679b05d2de6df3"

    protected open val iosArm64LibTor: String = "497093291b5d897c55ae1ef977fe12617d9283bcbd12398719036caffc7babf9"
    protected open val iosArm64Orconfig: String = "aaed57d74fcd679a223d2620c84e859118050fa72a2e7e2ff32b287efa15fca5"

    private val iosArm64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libz.a" to "05666663a43c33bfa61f2791a643ffcae4434f8b643a2d3deb04f327a6746663",
            "liblzma.a" to "284a1bb452da5621fcca422128f45d1ad12e7a3455a92155145f465a8959debb",
            "libcrypto.a" to "2b34eb2d7b4eb80b2999b79a3c732537828335f91015797ab15c45387f3748f2",
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
            "libcrypto.a" to "51f11be25f93a663cb11b73121136e8fc70ccafad30660f1ce458679ab0dee59",
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
            "libcrypto.a" to "a9ec21506a7fdfda43156a65596c29c2e56bf3acb505ece068f19c1ad8b1b4e6",
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

        override val jvmMacosAarch64: String = "a5d7ec02e1d0c8e57313b8d3c029aab80c891f30ebcb78b92a76c33b1edfc085"
        override val jvmMacosX86_64: String = "94ae220126992d42d25ff9a2f7acf289553d342edebab24ec232552246dde162"

        override val jvmMingwX86: String = "dce36cf1357f533753d8a52582fbc0070c0a520d5a6b9d0514499ac7595cea5c"
        override val jvmMingwX86_64: String = "91c4cead8ab565026534a90a68fba2100648f9a301d898ddbfd9afaaebcf9281"

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
