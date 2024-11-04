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

    private val androidAarch64: String = "08f8d6c1fd89380804acc99915c10fe9d7c57eacf5143851e3e43c8e7c0a43a9"
    private val androidArmv7: String = "201621143ed37b0871df10d668c839c2aad64116483c466787a66f7d505f9ae5"
    private val androidX86: String = "95e6be703703f8f8dace569967989b21b53233ab31cc09f3a81900e5e9994bd6"
    private val androidX86_64: String = "24621f3b384fb5db1bf67c804de7531c84ff22fbd989c113a59bf824085bd0fa"

    private val jvmLinuxAndroidAarch64: String = "6aa6d74a2edc68a6232fe56881de3f896592125d4367986e58b18b70bc42ff89"
    private val jvmLinuxAndroidArmv7: String = "cc81f782e512a05a33dd988680968866b5782a8cb372d8d66ec6b87f779329a5"
    private val jvmLinuxAndroidX86: String = "3976c7e9c44ea5421953e52cd59efa6c59290a7c9036572e528f74f1ebd31796"
    private val jvmLinuxAndroidX86_64: String = "ca4bd67f34e0960de498f29ea4cdb81a85cc20cca60c20e4ed501e4391842d6c"

    private val jvmLinuxLibcAarch64: String = "31f054303d182f96410ce6bb497da7a08ec6cb420c7436a80d19791e31364ae1"
    private val jvmLinuxLibcArmv7: String = "f976563d9ac1e5c1803cee101150c10b6ca2f0c7df90dee560e1132c6452a353"
    private val jvmLinuxLibcPpc64: String = "b07d819aec6a9be7aa63d78f250cb048be0d57525068b8bc07ae83ad14e5f1eb"
    private val jvmLinuxLibcX86: String = "c9ec0934f29123baed57d86bec57c4774aa8b0f41bf659180eed34562d009e08"
    private val jvmLinuxLibcX86_64: String = "94d86549457562d2c9292c31b07462655feb85b8259c5206bd74a982be95980e"

    protected open val jvmMacosAarch64: String = "67f6f14fbdcce17677d98a78ebe6e33c0b432bd2a329b9f76c84cb737087b6d9"
    protected open val jvmMacosX86_64: String = "c7478508c48a768640b8e95e78dcca0d88808e4b75270626c935a4e07db65148"

    protected open val jvmMingwX86: String = "e7b644952ce7d550aaa2423d121f2d83c2f2504bb9e51d16e0a0442894bc697a"
    protected open val jvmMingwX86_64: String = "05a2f5faf25f8c85a92c2471e3a071791cc7c7ec3a16589eb832a6e745182258"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "6e5c1ff53dc69e08a9beb22b069581ded93903de1d003b83d85be5638be6b775"
        override val jvmMacosX86_64: String = "670b58555e945760b1e5a2de980f56feecec990f8d81dcc3de85145b56d3d1bd"

        override val jvmMingwX86: String = "0793474837aecb54ef8336be7b2b72a36675058dbfcafd7fb775b7cfed58963e"
        override val jvmMingwX86_64: String = "ca5afe08724fede976aaf589dbedcdd5132bd52ecba7bc9ecc5195bc40a38b55"

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
