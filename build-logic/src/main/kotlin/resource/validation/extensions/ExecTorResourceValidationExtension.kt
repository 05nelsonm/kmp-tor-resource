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
@file:Suppress("PropertyName", "PrivatePropertyName", "SpellCheckingInspection")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-exec-tor`
 *
 * @see [GPL]
 * */
open class ExecTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-exec-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.exec.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    private val androidAarch64: String = "e0c162b91ae590af8f4134d5840c073c8433d7a6f90d69fb907ee884c4989d99"
    private val androidArmv7: String = "99b767dad5231b6c01ae990ff225ea3551ef8145d042895e60f1eefb4191f14f"
    private val androidX86: String = "4ae78a431e1ba5b386d177ce40dff3f8cb55803558b392c32afd8701db1aaafe"
    private val androidX86_64: String = "7d586d4b74d846589442e646a41dc9c4b8744f5ddef2cc1ba4bba81c6474d66b"

    private val jvmLinuxAndroidAarch64: String = "66143498ba12b6022dee28dd55b6fcc8f73f5e7c55fa70350ee2dba699830667"
    private val jvmLinuxAndroidArmv7: String = "f3b314d2e32739143ee52a3110b7d198c92e28c7c04a58b939f7642eb10f93df"
    private val jvmLinuxAndroidX86: String = "9fdcc0b0a8357197241f7668f5847c8d2d56865391b89b948e25d90642068865"
    private val jvmLinuxAndroidX86_64: String = "b3cfef6b94fb4eb9d34ac5a478a230b9d91762cd8762c71560745ec01fdf86fe"

    private val jvmLinuxLibcAarch64: String = "8e611ab853d6f8ab70479ab4512ab6b0a14fb7091cb11df645059059440527c9"
    private val jvmLinuxLibcArmv7: String = "5e93c86b6e9b4158956312ec5a97fd4c89ccf944cf0fafa7d705485691864a36"
    private val jvmLinuxLibcPpc64: String = "3f6a9e2e3909f6fd23fa311516980bb8b324eed45e26bfbc3634457145562a49"
    private val jvmLinuxLibcX86: String = "a42765cf6cd35f8ca4ac619e288e57d0c7284d1fb32720a9cb9df72fd05eb070"
    private val jvmLinuxLibcX86_64: String = "355e61199f19b9ecd0c1fa3f65e7c18d173780a30f694f1fb031ec9e4cf1d900"

    protected open val jvmMacosAarch64: String = "223177d5fbbbddf53598add990365fe7b16476d750210286edd037ebbf876053"
    protected open val jvmMacosX86_64: String = "f88082c392503e6c8de4ec4f6de4ffb85c3f950472b7e5cd489cd41b57b358b3"

    protected open val jvmMingwX86: String = "8d860634608b81e3d48fe7b87334e2c7c1b37550db871436cb7203b2b29c4753"
    protected open val jvmMingwX86_64: String = "640bee2efba9d9178e39169f27274737647979064a3831e9671013bf472b7424"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "15b4fadd4cb6cf3c84076691379be31ca05f6143d1f42cee9e6cfc849b1fa7f6"
    protected open val nativeMacosX64: String = "aa4567febfb30adc6e075abb928f77c2766038c039a331f230bed0d28a5bacb9"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "dbfca9a7930b2f30a6a773e58087928d1a90fdf092796de77a19d92aca27c6b6"
        override val jvmMacosX86_64: String = "c17f37a60340fffc924e219bf62c88dfd6f1a2bad3b94478ad691c1553133768"

        override val jvmMingwX86: String = "a1a62cbbe38caa444ebbe2eb785a337636ead99df07254a0286ecaaebba617ee"
        override val jvmMingwX86_64: String = "ba6ee1d447279a395f2857c387153bc7018d16375d39e24d43dc1eb776fc6d83"

        override val nativeMacosArm64: String = "d6539de777a0a32746ec43c1a577c3a7ef54419ea68358abace358a14345d002"
        override val nativeMacosX64: String = "5244824f73c20678d5ceaead25d2e93cc4f36ca1fca250015f9adf82ebff81b9"

        internal companion object {
            internal const val NAME = "execTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtorexec.so",
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
            libName = "tor.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // jvm linux-libc
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "tor.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // jvm macos
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmMacosX86_64,
        ),

        // jvm mingw
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86",
            libName = "tor.exe.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86_64",
            libName = "tor.exe.gz",
            hash = jvmMingwX86_64,
        ),

        // native linux
        ValidationHash.ResourceNative(
            sourceSetName = "linuxArm64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeLinuxArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "linuxX64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeLinuxX64,
        ),

        // native macos
        ValidationHash.ResourceNative(
            sourceSetName = "macosArm64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeMacosArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "macosX64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeMacosX64,
        ),

        // native mingw
        ValidationHash.ResourceNative(
            sourceSetName = "mingwX64".toSourceSetName(),
            ktFileName = "resource_tor_exe_gz.kt",
            hash = nativeMingwX64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "execTorResourceValidation"
    }
}
