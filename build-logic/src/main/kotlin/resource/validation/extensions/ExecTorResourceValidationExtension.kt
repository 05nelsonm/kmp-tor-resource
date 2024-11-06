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

    protected open val jvmMacosAarch64: String = "da0424a94c0597147df18cfdec20fb8394572ce798e8ecbcf0f77bba87ac6c07"
    protected open val jvmMacosX86_64: String = "b497f7256541648504c7f0df988954948633cf05927d2d2666d7f4bf930e1f97"

    protected open val jvmMingwX86: String = "fd70e7f3fae14dd7467e2b19b24204e1d794566d58a3e58a019155964e1b8d6d"
    protected open val jvmMingwX86_64: String = "7e2d408ad58fadb541c61f6c1a9e97b41283dae01a042280b0438f4278a93636"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "8601c8f6adc5b215d5135695587d2c956ea84bf92d8ad3e196a8dd0f8c04445a"
    protected open val nativeMacosX64: String = "89aff301e8fcf3fc941bcd5cf88f2b5212aba1ac52f6027a7a55660e124ebf70"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "d8933e9f7fb25e6aa78b4334c91f5b06a4f04b133c2134c77b42ce79daf9d1db"
        override val jvmMacosX86_64: String = "7773672600a2f0e58efc0af17ae5ca2616c6e5fd58f5736a115983d6bf98bc13"

        override val jvmMingwX86: String = "6bb0bd4070e7bc6a79780d7eeb9617ac15181537bcb4a45ba6aeedc09bb3039b"
        override val jvmMingwX86_64: String = "aa162e0ed6874ad47c7ca56441fc938e6a2572db5bcf5f63c64ee11e97a4f382"

        override val nativeMacosArm64: String = "ef62a11f6a26818220dc921b2e694f793fb3cdb3cd70587f8222a5923013ec8b"
        override val nativeMacosX64: String = "c46f9e55a34c7574c1f2b4e70be4040ca35602c150dfc2d28cb7fe4f37c797a2"

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
