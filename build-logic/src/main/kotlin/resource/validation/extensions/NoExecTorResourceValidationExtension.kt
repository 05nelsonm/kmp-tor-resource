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

    private val androidAarch64: String = "aaa386fd6ed017b4612a8c3c0e2f62ef851d8d2c7185b37d74669e5777a455fe"
    private val androidArmv7: String = "acb7b81bcfabcf0303726e60926b0b70a008c9ad25b18a14fc42585758e1dedd"
    private val androidX86: String = "11044f9c6933902a1852f4b7a978b9bc8ca2cdca1d06bd922f94e18ed392add3"
    private val androidX86_64: String = "7ae4ec239473bc207cda3ccea6278c7848175e69491302928788b2437588074c"

    private val jvmLinuxAndroidAarch64: String = "5a01f772e9afd764a80a9242c19e15c446bc0923d52892df7e74e16744793cdd"
    private val jvmLinuxAndroidArmv7: String = "658d33263c3fa05a892f7238c5893062fc7601ab52e915c30356ecbbdfb9d134"
    private val jvmLinuxAndroidX86: String = "f3d4855c23ef0bd4f91285cb274ce171fd9fb73fcaed19157e50270a2528df4e"
    private val jvmLinuxAndroidX86_64: String = "4cc209c6348158498a9fb17a07447950e46ef0efe78486c10f811701fd099f5a"

    private val jvmLinuxLibcAarch64: String = "55eb591b6876d7bed47829e043ba7355a8d74b6ffab8c78025eb971104ef2d66"
    private val jvmLinuxLibcArmv7: String = "e340d26867850bad130a444e8921ece06ecf924916a6559832dfffc162f85d5e"
    private val jvmLinuxLibcPpc64: String = "2bddf08fe763d884423991014c66739566b6f4960b93782ed4e09f04f7b08cd3"
    private val jvmLinuxLibcX86: String = "a763cb3f7f38f0654c751e781f69f47ad59d6f6ba5c251f552351b5f284f731c"
    private val jvmLinuxLibcX86_64: String = "8b1cb6bbce814983451d022ef423a728198acd6c3527c04e38d191a2c4a95315"

    protected open val jvmMacosAarch64: String = "3c8b0e48e0b466fe928fc08d3a314c9b713007d9c5a31fa1ec2794088f7d38c2"
    protected open val jvmMacosX86_64: String = "284890871f51339e9e54d822ac96893d852c3f27b04a6941cdab15baa90cffb0"

    protected open val jvmMingwX86: String = "11a79716aa005b51ffd88b0164b3030b15413be68a1d73f5b53f00a08e7fdf0b"
    protected open val jvmMingwX86_64: String = "0040007be66edd24b136e19469a182d4ac80c457a611a97373941c999a7db68b"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "1fad9e0c969a2ae1cbcc4afdf320b7493ea3b0308225d25fcf1d5546a12f2281"
        override val jvmMacosX86_64: String = "f745b0889c65dc286d74fe0066b96a7cce30aa46dcc3aba4233103a8695ca7e9"

        override val jvmMingwX86: String = "587a2c31bc626a728a73e880f06c61f8e809de3aec694e4f2ce496e3e01700ee"
        override val jvmMingwX86_64: String = "81de3fb57e998d60a0e76733a589147c53283624f9a649f41b0cb7b938ba9b54"

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
