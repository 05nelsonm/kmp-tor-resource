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

    private val androidAarch64: String = "340062d65f0ac6c34fec14aef7836911ab7b9920ab02e73c78eae8bab9673696"
    private val androidArmv7: String = "5af569e2d55cf9eccc8ac8021e9b913f6150c6cb16819d177727ab6a30706fde"
    private val androidX86: String = "eda6321f570f57d99863e04f44cd8180215944be9633e5fff054082afa4d972e"
    private val androidX86_64: String = "0f002914816d431c5dbe1470541e90d058790647a355e727903d9ab39ed3f33e"

    private val jvmLinuxAndroidAarch64: String = "4ed2b743ef7a0b0f8d2b6ff9dd95ac0aa247cd6b9605c283b85311c4b556dd93"
    private val jvmLinuxAndroidArmv7: String = "6b73ec8f3e703477b5875680d638089d39669db029a9e24c4c3d9e6244bffb29"
    private val jvmLinuxAndroidX86: String = "31f744365ecc6840659e99cd017581440e0cc6ac56d85e32131c8ff2e89c6a77"
    private val jvmLinuxAndroidX86_64: String = "af85ce36c2e5325f3f9a5ebea9cd56f501a5232feee9d8da1b4af1fd18b74cc8"

    private val jvmLinuxLibcAarch64: String = "7b72fb5c051dda245369fd9c2a5788ca972082f3cdcc93f97089bceeabc54219"
    private val jvmLinuxLibcArmv7: String = "020efc71844ce0619e1dc96d3b6de8fb5db954a27ede2b63a737408049ce3137"
    private val jvmLinuxLibcPpc64: String = "c4844cba3582811c5a1f80d8bc35c69351d7e881edbbb86a57460a9cabfc493e"
    private val jvmLinuxLibcX86: String = "ecc80d15a9e7354f9c854ff8e7c834c1d997189f29809869f64c70a206e9dd3c"
    private val jvmLinuxLibcX86_64: String = "fd57f5d527847b6351853a5801c08c9e431c1ef3de71298ab756d05dde9d7779"

    protected open val jvmMacosAarch64: String = "33a13709bb23be52118b5fed934625810f220a74eab607fdfb2ab7f2ad38d865"
    protected open val jvmMacosX86_64: String = "03ddb8881a00b3ffd909ed8f783bb2007ee14bef62db9ccfb679f076169964a8"

    protected open val jvmMingwX86: String = "d1d689b74373c205851fa0a9a525992360c02e01c1c7360ddca1d82ff846a907"
    protected open val jvmMingwX86_64: String = "e6b027575ca521bcd6e22bffa3e2d8998183d8036aa12f233065c5e005e8eef2"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "6d4429283178c82a85b058a7ccd982f7fa57c4e9c09edfaeebe2c60201cae6ef"
        override val jvmMacosX86_64: String = "954ca3b25828ccc20bcf285d2d0cde1b4e968b6e47ed12827a41e412414e5432"

        override val jvmMingwX86: String = "e23c942f9fb329213dafa4cda3effd0e8f8d2e6af256b745bf548595ade6dded"
        override val jvmMingwX86_64: String = "681324c9dae0b65e9474e74447b632ed1ccfa25c69890434db52d8c5ea941224"

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
