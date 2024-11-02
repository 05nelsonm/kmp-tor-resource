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

    private val androidAarch64: String = "c120e2866e8b3241f74074210f22ce1abb1ff80cbb661092916a1c76150bde34"
    private val androidArmv7: String = "6bc700107742014f4787c262c037ff658a4546f75cfa0d06cc142ce4eb2620db"
    private val androidX86: String = "aaae1d7127c9a4a247db0478d533b50be97b287b99e142e8b78dae2f2324995f"
    private val androidX86_64: String = "9dab6ff5e8ce3b6075f31840415bc921d79cd1f39b0b346b39d990d0b39154a8"

    private val jvmLinuxAndroidAarch64: String = "b7a1f874c26d9de36b64db282405475db755303394c0be797552355b2408f316"
    private val jvmLinuxAndroidArmv7: String = "629c99fc729242e8e578360787ec71ffe512ebcd94215acbc5709d9c42c1fc79"
    private val jvmLinuxAndroidX86: String = "8cad0ddc7bf9a4f303f390ed6c758290476934faced944b8714ef91dd5769925"
    private val jvmLinuxAndroidX86_64: String = "942e6b9f5bc0f66485d48bbdc39e6d09e6e702cd989daa2516d046eff64d9418"

    private val jvmLinuxLibcAarch64: String = "bd4e368b05872ad40726a0035e1f05e28bd25b75ac7b30b71891e7868b87e8b7"
    private val jvmLinuxLibcArmv7: String = "79ef5c07f986d6d43a05ce88c3f3a320147a67f0178037e05b92de2288a241bf"
    private val jvmLinuxLibcPpc64: String = "02895b35beefddaa3544722d487b2ce6ca7fab01379df690770dc64f92f512ad"
    private val jvmLinuxLibcX86: String = "00950f63e4c89d6e6e2c81ba52b37256fde890850bf11c4d6f1f77e6c20459fb"
    private val jvmLinuxLibcX86_64: String = "bd2291df32ba8dcbe71ca09f6ef2df858d0a90f439f41fbaf41020774ce2ea63"

    protected open val jvmMacosAarch64: String = "ffa4a349d578b99c34e4233d3e03790692c541cc24077580cff7c07187fd0c67"
    protected open val jvmMacosX86_64: String = "127c7e7403f2f2e65b3c8575da1eda71a8d7ac21189976417117858b6629d47b"

    protected open val jvmMingwX86: String = "f57ff55d316add983f947d9585b6736f26adfd0b9a062efaeeab5363b23802b6"
    protected open val jvmMingwX86_64: String = "81686c9709deeb9d8c7201e70a273a627e0b21587581635a77d2eca7e89135da"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "d74df646dcba0130edd431b754bd81010e827d186b357c3e6b7be0d786f922a9"
        override val jvmMacosX86_64: String = "d001c9bb82a137b34b42626eeef4b92d5da3c6ed641084a55e2eb26013a455af"

        override val jvmMingwX86: String = "737a9b1dac58cb2391ece99b95ed498f4978f5ccef2443447efbac105dfd3b18"
        override val jvmMingwX86_64: String = "7cf7eac4d891913f3c3964630ce9cb83537a683b663d05c0f690cd6d623e98f7"

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
