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

    private val androidAarch64: String = "023813480e437b9353f42306eb69a790692951e28ab07ea93755326f1f4396b9"
    private val androidArmv7: String = "b9f531ce4ff79b87bfbf7e90a559fdfcd144b181597e81e82d4fbbc7c5e0944c"
    private val androidX86: String = "6470cc06aafd9dc0508034e179d239c5e378ef077ff443a1b0c8e3099553604f"
    private val androidX86_64: String = "91662a2e4b1d0db5a5da519fc19a94835ce3d1476f012791e98d18d10c46bdc2"

    private val jvmLinuxAndroidAarch64: String = "8e3e646b6a5ec89f26f6021ce30675d6cac57ac1592c182c7f75f5bba753d1ed"
    private val jvmLinuxAndroidArmv7: String = "79e21c5ff39c742252f9c26eed35dbc49bb6b2bf4f8c5f20913899cba1da51f4"
    private val jvmLinuxAndroidX86: String = "977443b55109d326dc28c2314ad04f67d53e3b5799193c7955eaba6c4699077c"
    private val jvmLinuxAndroidX86_64: String = "cd7d4acd9fbf4e3edfb5172b8c1563f9009f01bb95aebab647c4bc5eadb00e3d"

    private val jvmLinuxLibcAarch64: String = "7ab88fba151c22118416c60c73aa80e31e2f127aaeb6037d0f6a05ab47db320b"
    private val jvmLinuxLibcArmv7: String = "d7666dc1f9a9723c066152fc78fe7a27326b379dc338a1508faa1d0a728db577"
    private val jvmLinuxLibcPpc64: String = "8c19a31eae67564b07ceb79e57f3f43e6b0165faf1a02e104d675b40ae331fbe"
    private val jvmLinuxLibcX86: String = "fa544ccb8ac7636b84ef4cbb16d30c98031e9bbfa55491f7b2c78db12756b521"
    private val jvmLinuxLibcX86_64: String = "b36ceb05a177177858e9a909855abc5af2f18fb16b141ab31d62185db06cce6d"

    protected open val jvmMacosAarch64: String = "257800add2f02af0083b71da2d73e12e569affb1e29f3133ed57aee93c3340c4"
    protected open val jvmMacosX86_64: String = "27638c8c940dfed9c00dde5bc9442081999d7ebb2cdd29608f8bb67cf184d87d"

    protected open val jvmMingwX86: String = "e3cb8ef4604ae3830a1a97b501c8f16193273b4e0178e5c09bb9c733c02d9d73"
    protected open val jvmMingwX86_64: String = "d5df7c0ebccd17e61159873012bd59f694be168736aa69a34751c101a25a2004"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "477b7205212c4247170ed42290088262f726b7b2fd3515d2665e5893c01af740"
        override val jvmMacosX86_64: String = "2af40ea72c35f9089d73db98880ccca05fc3ff7130cf91bed8073e33b7345552"

        override val jvmMingwX86: String = "277442bc56452474e2a32c768ada88ee4a3c2f9bda563f0d953c20313312349d"
        override val jvmMingwX86_64: String = "011e920b54091ea8833924bae668644855ae5a408108b000e6471473a13dc066"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun errorReportAndroidJniResources(): String = errorReportLibAndroidProtected()
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun errorReportJvmNativeLibResources(): String = errorReportJvmNativeLibsProtected()

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
