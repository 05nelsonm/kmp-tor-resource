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

    private val androidAarch64: String = "6b34d96f0162f37d33e3690d24b812a2bea7c4d0191fb37ab70a439fbf49a1d3"
    private val androidArmv7: String = "06946ab1275e4bf33567f1fd3b04be984c7787bfae59478f80899aa6a88f1014"
    private val androidX86: String = "50e751234e069162f0d88d2f225be0f0afb64c43314fafdb95a08e175f4c2a77"
    private val androidX86_64: String = "bb2ddfa83cf1980b5a5609c561e44b1533afa91be8e8a512107e26d499f8abfa"

    private val jvmLinuxAndroidAarch64: String = "3c0e90df023fc94eff546a3feb6c268fc47de401b0655f7a8a2d1fab6bfc3041"
    private val jvmLinuxAndroidArmv7: String = "3ee9f8405b37c473e72522dcbb17b965ee965c150956cdbcd3de169346605ba0"
    private val jvmLinuxAndroidX86: String = "f8462323df73ecda7d39e88cdf393b27190c2b575e6c02e8d2d0c920bd2086d0"
    private val jvmLinuxAndroidX86_64: String = "17709a2882455dd90bb70c42394fe237b39bbf01af4bf09464d7ee46fcc6807a"

    private val jvmLinuxLibcAarch64: String = "67d0c95450e440d16cc9f5d522302347556ad356a6ce53c7f0ff09c73ae0671e"
    private val jvmLinuxLibcArmv7: String = "5a35d406a803574baf37e250efbf751ebc0cbbdee308529543d940d6d7e9730e"
    private val jvmLinuxLibcPpc64: String = "eddc3811cc5279a9962cbb05c487e6b6a2fdae78ae4691ccc3548055ad71d28b"
    private val jvmLinuxLibcX86: String = "685d62cf81154dcac5b732a51a94f055c941b2ca246a862defeb533f27df06a2"
    private val jvmLinuxLibcX86_64: String = "10f0d6b1bb81e933accda3a12b691d9985b64b7e86cefc3867c832f7c5edbdf9"

    protected open val jvmMacosAarch64: String = "db848c74b7e7607d16fdcf733428bd65530dca53173aff2242e056ba4805f9b5"
    protected open val jvmMacosX86_64: String = "e3d48f4d929867ba183ef5635ecddd86a76b9c1c4840a95f5e4488f95fe340d0"

    protected open val jvmMingwX86: String = "18e7ce4236715e5e1171266632737f7bfa1bf0ccf6834b98eb6d91b4d110fe73"
    protected open val jvmMingwX86_64: String = "3da7b42154ef2117e2e03b8799a3a1af58399a2f5d4e1afe46cca48060ab9a66"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "4f5ee99518d4f76a5de99908ea1055e799759ecc9d6923a1c21b7ab420f39a8b"
        override val jvmMacosX86_64: String = "3efc125be0b9cc8336ab9348dd38ebfe83e5a2b7194a05d2d3ea7a9e58c709c9"

        override val jvmMingwX86: String = "f385e876c449493089084367445302f8cb933258f599b57f2a98d42753605c1c"
        override val jvmMingwX86_64: String = "987c77e991997ba02a6950892d87f8263c920f134bdf88178fed1f2c6da68eea"

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
