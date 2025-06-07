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

    private val androidAarch64: String = "9f2ed4eaca5afdacafcd2b99ae91b32248b19dbae8f86edf891af4165418df37"
    private val androidArmv7: String = "2af14a825b4f13a93c0661e500dd53ef8e731937ef3338ca3c1ea0fee9ee2f5b"
    private val androidX86: String = "72353bfd536fa8f987ea01b5c16b468f43d19e9668715d6c89270d4615061ccf"
    private val androidX86_64: String = "46deba4f4f14c20df440efc5278bcae8392ca859742f2a87a905ec0fefe9467a"

    private val jvmLinuxAndroidAarch64: String = "7581688caa3851ad17baff5bf68289b170d4aa5d68a0ecfe56b60c0d90f8230d"
    private val jvmLinuxAndroidArmv7: String = "358283adf21567b642164f42ff73f898b47211a72c2b7490866af7367292ef48"
    private val jvmLinuxAndroidX86: String = "3965b9405ee399090bf0c773f68e1eca2bd5e2d6cc2c2beb4ed3f5738ecc3a6b"
    private val jvmLinuxAndroidX86_64: String = "3e5dc0d15a7086b82509a82bf79161672d4bb5303c4025f73cfc34f2e975f22e"

    private val jvmLinuxLibcAarch64: String = "76889f7a74b8435e34fab33ddf79d2d7ea7b77be34d92be1a06ee1ff06d50b41"
    private val jvmLinuxLibcArmv7: String = "02ef87f769b564fc7a91399882baa40499e77b37f90a48ae1c6fc4abf04403cf"
    private val jvmLinuxLibcPpc64: String = "1fab0c53013d3de6c315bd7ac9b828e47b8e501eadedb86d443e7254735cd72d"
    private val jvmLinuxLibcX86: String = "cd5dba184d458aa7c66a181faff483b7c380954203fd1f1227697fe028f6fec8"
    private val jvmLinuxLibcX86_64: String = "47900876bd5662b8d3cd69dd8a62af432065ddc2f887ec1184855697033ae5b0"

    protected open val jvmMacosAarch64: String = "17c5e805ad04d7e11c186c2ec27b2ae14386ca7130fbed87708475a67b0ee0a2"
    protected open val jvmMacosX86_64: String = "22291e02333881dc15fdf2b02fa985751306d86d3158d7bbf928dae91c049354"

    protected open val jvmMingwX86: String = "2be5fcdd3278331ecc07230702ba0a5f297ca37496fc6a7c0e4c27af9daf9202"
    protected open val jvmMingwX86_64: String = "9797c0d14c93cc455ec537575d8a7ba79e4049a2594f196754a94a094602846a"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "ca255fd182b4c8cadcdc1c5885c5356b227b596fe9d892334dc546e8d2877be1"
        override val jvmMacosX86_64: String = "9e61143a69f87cc427205d8b3c0947e3d2dd21ac585bc456a560be4bec40a45d"

        override val jvmMingwX86: String = "ebe2360f9ddaefc56cb51bf2b870cdfa1ba1cb52d883595f2534c12d85a530e7"
        override val jvmMingwX86_64: String = "ad16edd240297bd258ba2d5e48e8c070a6c7a6fb6f7e2917c1d0ba1a3fbcd776"

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
