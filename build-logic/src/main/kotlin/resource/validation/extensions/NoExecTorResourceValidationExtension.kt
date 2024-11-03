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

    private val androidAarch64: String = "3bb3a239147327b275236d68a20fe159f0efe37833a7da23a13a07732d6b39d4"
    private val androidArmv7: String = "8772a8182611142377b10300969f5f0baa076367c1c4381dd3f5c1a9832df7eb"
    private val androidX86: String = "7c4230783d0ee38634c8c4d571b0d3a10e9cc18e3ddfe3a3e76394bb32e67f06"
    private val androidX86_64: String = "2c6a3e571f349e1b23a3dc211d961143c796795bf278dddbf89f1004fad98ded"

    private val jvmLinuxAndroidAarch64: String = "9f48626dabfa22be701820430230eba128ff838e63368460d8c00f04243ba731"
    private val jvmLinuxAndroidArmv7: String = "6a228f3e61bdcc33e8fca054bd9477da11c6f19187939db615ce12300b07772e"
    private val jvmLinuxAndroidX86: String = "937360dc53a5002558bc32b16bf858d3154782a0266379880cee0baa9032fe50"
    private val jvmLinuxAndroidX86_64: String = "64f2a80c65c452f6e17068e40b2950aa8c322f3d5a00d8e31f1e2cd6c8321cd5"

    private val jvmLinuxLibcAarch64: String = "8d998265324489409f3246bea95a6e88f4be52c1412c4269520048ac033d054d"
    private val jvmLinuxLibcArmv7: String = "242d1a568955b495fc318dd206b5d113bf8ff07ef99d5917f40e2affd74dcd59"
    private val jvmLinuxLibcPpc64: String = "14280c1c4b5801cfe7cbdd8d5a9eb79e332799aab4ca23460ef4d85772f55151"
    private val jvmLinuxLibcX86: String = "e07643afae6b606a99ff4e085b628cbc38e4aec3e4caa4429a6fff8b3279b772"
    private val jvmLinuxLibcX86_64: String = "4b5078bb016424be9c0322c0dfb33384352c50d85f5388352b52818d8637f6bd"

    protected open val jvmMacosAarch64: String = "df3d439bb855a72a8c757e55be657482a967cf7d882924bc4319a671b18c010e"
    protected open val jvmMacosX86_64: String = "ba2a4274c45e4e0d97149fd1f935be07fc5117aa0ae5a6a3d08b57fb453c4db7"

    protected open val jvmMingwX86: String = "39f608ffb7d5108dc7aebc290aa78bb59647fce24a5e7690ff97a41714954dbe"
    protected open val jvmMingwX86_64: String = "fc1429b553c59ff91e6590e5e067d84823beb6cdde87b890a9896f3be3063422"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "d7e850877cb081b2d4b5a63580913ba229bbbd3378499d59de16c5e78ec6e883"
        override val jvmMacosX86_64: String = "ac6a114610e941271806334e6882178b3443710dfcf70380573acd822ddbb46c"

        override val jvmMingwX86: String = "b31487633a43aab7aa9699d5f1c359df83b507ba7103dbbe3938350c51b36205"
        override val jvmMingwX86_64: String = "bc9216413ee230e3a36f8a389473a5c415022159c66924a782608b82f43523c6"

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
