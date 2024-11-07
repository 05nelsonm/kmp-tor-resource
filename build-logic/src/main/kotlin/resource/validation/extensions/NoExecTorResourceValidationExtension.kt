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

    private val androidAarch64: String = "a33b595dd8cd78d00b1ae51f7e6953144d990bb2b74521f2d2157d0fc811b5a0"
    private val androidArmv7: String = "f46e884ac43b94983e5858bdf90af8879bc563fe33266c276355d9a3a42ea6ce"
    private val androidX86: String = "7f183570b8be1332ed16ac63f6d40b91ebde7cbfc283ec89d7f659b32706c147"
    private val androidX86_64: String = "7a7d8fa1f3e89530d1cf0ff80ad970a6d7602e9e44b988956460641b91d8cd3b"

    private val jvmLinuxAndroidAarch64: String = "95dd519518a455c22cbdaa49539d8423bead168b93890a3d24aea5aa5f363a53"
    private val jvmLinuxAndroidArmv7: String = "d7017e508172089ef67386a0a01be10fe8e6518e539ec11d135acb607df9c78d"
    private val jvmLinuxAndroidX86: String = "21f62db8a4587aa2387a13bb18930cdbae57568c325c4d59e20082ab2ac33b4f"
    private val jvmLinuxAndroidX86_64: String = "487b9dc3b8961e2398ca78cb816fe4df39185c88a5e3268441333e2a717dffb4"

    private val jvmLinuxLibcAarch64: String = "620f004966065c24c68c850c2c90ffe8a2750b65ed76c9a51adebc51109891bc"
    private val jvmLinuxLibcArmv7: String = "f32655d05bf54731b7b67396881bec19f3563a04d3bf8c13530726b41bed5d4f"
    private val jvmLinuxLibcPpc64: String = "f31aaf9b6dededaaf84d0496b08cf3bdbc2044f909076a78cb05b2ab437df24c"
    private val jvmLinuxLibcX86: String = "9ddc6d223fa88d0a323c6954f2a0b77551fd90936dc5df9f8f253759995f6255"
    private val jvmLinuxLibcX86_64: String = "e92cff0e0b5c896f0f56f2bc8e9392dfbf6a2f9191529a14e7c57eaf1a74d1f4"

    protected open val jvmMacosAarch64: String = "664575b904b3265d8cf886038eb24ed2a58ebac4eade5ee8ffce353a200359c7"
    protected open val jvmMacosX86_64: String = "787361757e20aca209f1f506f699586c0dbfa1261d720369e271867be183f549"

    protected open val jvmMingwX86: String = "16f2ce029c62a9229ce50b011d99ef4c5db7cba236e56d004b453016532b2f16"
    protected open val jvmMingwX86_64: String = "5136b10a242dbf889f0cbbaadbb4a576198e67b6c141c048dcd23b340d2988cd"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "f526216a290afd4383d79cc245ffda089efa4e9c8cbcb215b7c13384ac0febe4"
        override val jvmMacosX86_64: String = "e7813e101fc0eed3afc2e129994fa0403a074e840ba31ececd97dc1d927bccac"

        override val jvmMingwX86: String = "e5800216bb6dff678c1cbce468c256f43c6018a0d7d2626da57bfd0f6c5c4b97"
        override val jvmMingwX86_64: String = "b92f0ca17e9db83ea182687e777556fa3fadd7bd988cf40afac4a5496f234c6e"

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
