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

    private val androidAarch64: String = "7e704f1266b136ff60366429722ecfa44f792fe2a2a76cdf85eb1e752f76107e"
    private val androidArmv7: String = "19ad50292ef7eca7bf6e680daff7448a23e11382f3b216be28f5effcbc6dac65"
    private val androidX86: String = "155a731215adb5c8fbfff0834a2f37adb89d5cb4dc89b393e73f3639cfc305e9"
    private val androidX86_64: String = "b63fcba634db2b3dbdd06584a0251f4503559868803b55e23fc2c151c3041810"

    private val jvmLinuxAndroidAarch64: String = "ac4abbaab99b62fc2fd81ce01944885147cdbc80c2cac971c030163d133d6a1b"
    private val jvmLinuxAndroidArmv7: String = "73151caff0f18045bb3ed4c159a2ce3db5e628278f521f76863b1ec0b436bc5a"
    private val jvmLinuxAndroidX86: String = "21c3bb6ac145db8fad48a787d796b7257dcfe57cb38776ce360fc2759d26e435"
    private val jvmLinuxAndroidX86_64: String = "de5a92a3f31017a5fe271e4da23ed0d646063cbc671abf537292cccea9130b10"

    private val jvmLinuxLibcAarch64: String = "662fea8a56590b089a9ecbbb5cf8c24b59ce285c7e912e23bae67b0a9ec8a769"
    private val jvmLinuxLibcArmv7: String = "98c32517eda0d7cca79474661eb51065633c6ca7b2df64f317e2153aa2773ac2"
    private val jvmLinuxLibcPpc64: String = "1479f88b39148ef1e4948fb7cf17f2c8ef72a17602463aef655711f864c69082"
    private val jvmLinuxLibcX86: String = "206d69e068658644244c0dec7c2b2cc9ee9161cb212336983f3dee72bcfbac50"
    private val jvmLinuxLibcX86_64: String = "4e76717a7e3fcd7c87e6007b425243e771d2854e17d7c6dcea774b8fabb069e4"

    protected open val jvmMacosAarch64: String = "0be873899dba5b4a74f877ef64dcbc49f7c6566c7f266f30a995ba51df654d5a"
    protected open val jvmMacosX86_64: String = "67e6b4be4872119b1c1de499dc589a432cfb7390455c66da5683ceec07b34197"

    protected open val jvmMingwX86: String = "9cf91c850eb40dc88ffed84ad925fc4d6700e616201ef692d3fb17a1b22a1e4f"
    protected open val jvmMingwX86_64: String = "07e66b2b0840b501f866fbc9717f302f2a6d7019fa6e0b6682bdfdf1f5670c4b"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "51d139ceb93fead7f61a85f9a44bd8b9cd4d67f8fa3447fbf57fbf8f0fa24f18"
        override val jvmMacosX86_64: String = "310fae39e83b736d7d99595cd16d355250710bec3675b0823e7beb02db81938f"

        override val jvmMingwX86: String = "41268a24aa50d19188ac5437039c51f549d0cd5a6e1568d2d1388b3a5fc1360b"
        override val jvmMingwX86_64: String = "db82b9e09ab21efc4bb267c03aa54150144f08b3db9af32cacfea31a01ef2f97"

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
