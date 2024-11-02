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

    private val androidAarch64: String = "69bd94d02a1e2009993f41d415ae485fb5f57dcb1b99d2002d7026375fbf41c1"
    private val androidArmv7: String = "9a1cc306b5b2be697b058ab94910c8a8eb807e63c096b7fbcbfe08725cb22715"
    private val androidX86: String = "0af8c31ae2987b285b7bf3d10c1c47c9c7f14ba95ac988c608353cc799bee13d"
    private val androidX86_64: String = "ffeff43b0283b4a039dc8c0b56a3ff153a583262a54c3e65fbd5ba48b42092d1"

    private val jvmLinuxAndroidAarch64: String = "eee3e7ba1a0c1cae4850a22c55eb00f95b5a34bcd59778eb31b8da1b1e4bc032"
    private val jvmLinuxAndroidArmv7: String = "853bb043117e279d6a3a09aaab6e506e5699ea58e7e5b3d2e5c7246cb414f59e"
    private val jvmLinuxAndroidX86: String = "94abc92a7db4d9d346420b35efaddc7dd87c899b52301e72decd7fe15881b3c8"
    private val jvmLinuxAndroidX86_64: String = "7371425ecad4428398c111445364168f2c442595a5b0f71a5be1d53f8dc08b29"

    private val jvmLinuxLibcAarch64: String = "1e15bb5593200825cae549f0373a23facc934b13b2e0fa25c1d3e6f54671b455"
    private val jvmLinuxLibcArmv7: String = "ca138ecb7908945a7a0a3ddc9447ff74fe7dd57480a33e0a0561eb9f67e73cee"
    private val jvmLinuxLibcPpc64: String = "5d4bfb7f00391aaf91dc78207820f90cb60fcea9d55aaccebfc6b0fc58e06bdc"
    private val jvmLinuxLibcX86: String = "46837d373b3750a81fdb82a07888b1ebdddc1fa37b0755aaaab0d949f3f73e4d"
    private val jvmLinuxLibcX86_64: String = "4a410b3802359c2c2fd2d4b16457ab23ed8aa53db5dca4b4eb410ba7a9455cd6"

    protected open val jvmMacosAarch64: String = "c8932f621527ef7bb44f6fb2ad4b9466a1ac76dd3d25205d5d688bf18b53a27d"
    protected open val jvmMacosX86_64: String = "ab0750fd5eb66f474cae59942f02bd3c06832452fd72b3036695ddcba7823adc"

    protected open val jvmMingwX86: String = "16222ce9ae5c0a5f55049b8c0bc0d28bd45fe2e30a2578802ac1b84809bc28b5"
    protected open val jvmMingwX86_64: String = "e8f94c3ab3fa8cdeab947556fd2e18e651178b89dafacaad99c51456c6b5f8b8"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "b0525cd1625e4cf801d2e81ea2b5d4433a817eb6ab4e37eeab183d0a11870644"
        override val jvmMacosX86_64: String = "3eec7ca0d65ae98210370f07880514a8542f77d634f7aa23be2b7297dda9b98b"

        override val jvmMingwX86: String = "4c861dbb8d0a20ed66e95ff2787e5ffe1b37272f2809186d1e790c6615e6bb5b"
        override val jvmMingwX86_64: String = "5787fa870dea2e8ddc46c90dfc60a80ae78bed951e988a2ef1b598e8c629285f"

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
