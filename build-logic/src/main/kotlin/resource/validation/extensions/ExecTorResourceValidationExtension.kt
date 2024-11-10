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

    protected open val jvmMacosAarch64: String = "e893d48176ec75fc4f7a03ec8d5af1bac65c50943ce00d975a635c2142918b9c"
    protected open val jvmMacosX86_64: String = "9f54a7fe0e82f8df7c388dd3cec32d2d7cb71dbdcab2514f4ce26d931d81757e"

    protected open val jvmMingwX86: String = "8ebc07c65f2ca7bc567fa028125a27ef8b28f545a73827650b13b2f809fb49d5"
    protected open val jvmMingwX86_64: String = "8d0a5d1b5d40849aa35c112f477155f628a8fab6aa07b0eca84ae6b395463ef4"

    private val nativeLinuxArm64: String = jvmLinuxLibcAarch64
    private val nativeLinuxX64: String = jvmLinuxLibcX86_64

    protected open val nativeMacosArm64: String = "6a3cfd84e5847717b3f26175aa6386e26e6309e627ed57583cf5a7846183f8f2"
    protected open val nativeMacosX64: String = "b1f80961b90fc2d5170828b406d4879d65b2c38216bc8932008be9a61f8c4b84"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val jvmMacosAarch64: String = "f3dd6476ff58e4cd40c8ecc2bd57d1f2be74b707b4a5e862ff5f0fd8f38eed80"
        override val jvmMacosX86_64: String = "c501fc091b5c83df6dc0ad8b2b86e31efc4290b30e92cd941c934300ccbab4e9"

        override val jvmMingwX86: String = "3d24fe67a72ef21bce54c815ba8d540ae11dfd49d2227047c780349e50b3e6e7"
        override val jvmMingwX86_64: String = "ac40b295067e9ade4560abe51153c4bdc98d9593813dde7a38b531459cd39ca5"

        override val nativeMacosArm64: String = "cec5c1e7cba2474694eb92f73a28f5659339a891540b418dfe1ad37e4b2717c6"
        override val nativeMacosX64: String = "c69d2abe2c4703329c19a529e737a5a0d946a554623af171d72e5e75faa9128c"

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
