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

    private val androidAarch64: String = "a553bc41f9b2eb36d5ff09789321fcf98f482c4bf8690a89c1c7280026eece7d"
    private val androidArmv7: String = "9894fe099d85111f3206676c9c1231d1760dc64cb6f3e5a8f4d34c57172a35e3"
    private val androidX86: String = "a74f5150d0c440d213f70372f8733b48889bfd0f09763c656a2b56548bb171e0"
    private val androidX86_64: String = "43bcf44db1eba53281a0a816e71f65b5b983ad7218d746cd5d25e7ad01357dfa"

    private val jvmLinuxAndroidAarch64: String = "04912afd28863d24143e9bddb5deff54296c531a8c92cced6cf4a498171cdc58"
    private val jvmLinuxAndroidArmv7: String = "0697a8d9998f917330a155d8608eb4b3dd272931c4ca6642dcac9b62d69abaeb"
    private val jvmLinuxAndroidX86: String = "66d59d8864936e13c72fd034051f041142b142448af848182a1982dd822e7e79"
    private val jvmLinuxAndroidX86_64: String = "43a9ee56bcea207e2b206dd88583c83b799885f317b149360a8a0ce1af65f8ab"

    private val jvmLinuxLibcAarch64: String = "596eb6526a094c2ccad17c921b635e7bb47e3bf38211d71d1f604e619da4a4d3"
    private val jvmLinuxLibcArmv7: String = "15d572b2728de9cbe16c7c3896cbac55aa5e07364ee3438a0ec163a99223a9a2"
    private val jvmLinuxLibcPpc64: String = "1bff2c238e21b139dcbe5ceafd665c71fe6032402a6f501fa93bb0bb7242cd7a"
    private val jvmLinuxLibcX86: String = "ad2ca46a516b6bd3d8f26992ba6257a17108746cec38ee1a81c761a4c4b51e3e"
    private val jvmLinuxLibcX86_64: String = "d199ad772b84248d58e0d63101d1908d6215e50081de7bf34c67d1439ef4a8a5"

    protected open val jvmMacosAarch64: String = "6ec97c7a113ba423b48059ab701b45f678807aa4e293f18bf549e9110933440f"
    protected open val jvmMacosX86_64: String = "dc81705e6a84543d1ca5673a7aa4824164d42a79e903d92844d9842f472b4682"

    protected open val jvmMingwX86: String = "d14bc36e81bc5d6adb46b0020a12ba203659ef1aa69354b4f078d5b9f9dd4934"
    protected open val jvmMingwX86_64: String = "520eab39232cc9ce45c7d55a254c6f61a82dc4b024ed903a78dd7e878747f182"

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "8e2954d4229e7179fb0f5e8e434fa4cd32f8ce636ca3ae4e2334c5c746d07c71"
        override val jvmMacosX86_64: String = "fb6c928c6a7519bde1142a995f7ed0ad60daff265d8b3eed737b8e5ee00e1d79"

        override val jvmMingwX86: String = "427093dd94056fea0fd89a9c38b303e546db8754e957348130e1ebaf4b90d733"
        override val jvmMingwX86_64: String = "87fe5eefa567d209a821951a89ebe49d036e8429eb7fd8237d03f0b1859e93c9"

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
