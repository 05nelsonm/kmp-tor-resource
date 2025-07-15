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
@file:Suppress("PropertyName", "SpellCheckingInspection")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-lib-tor`
 *
 * @see [GPL]
 * */
open class LibTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-lib-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.lib.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val jvmLinuxAndroidAarch64: String = "f5f7fb60a4d9a4ed06bbe5ce0cba7c94e19cd6e21aa8863cc5c2ef784985530e"
    protected open val jvmLinuxAndroidArmv7: String = "1d8308f09841b953deb64aaa9ee7e1dd3298eeaff473fff7b63ddc1d0079501b"
    protected open val jvmLinuxAndroidX86: String = "7f8a531a72dc744ae5907948ce0708f280be22ef8939f518146d71e783f1bb5b"
    protected open val jvmLinuxAndroidX86_64: String = "a6ad5c7e56353d7502331e608fca4a3f1b61d29d51c272ade6a2aeb5c70a2801"

    protected open val jvmLinuxLibcAarch64: String = "93d3ef539154f171cc2919297672404c83115b8d75087e189580c3870c605023"
    protected open val jvmLinuxLibcArmv7: String = "500f9da8bd26d76a0e1bc568c95f36e0e7fbfe568d995bc75ea38ec5a20ceb90"
    protected open val jvmLinuxLibcPpc64: String = "097b916301b6f3a6e0ac81aaaf5664d136ae17c208b1ac3c88a43ce120974c98"
    protected open val jvmLinuxLibcX86: String = "7712fba18fd38b87e621ec61191b74b16fed3fe0365173a9fde477be37f09e1b"
    protected open val jvmLinuxLibcX86_64: String = "5b0015c44a81182adc9b042dfab2c80cbccaac0153f5b8c70f7dbaa741dd1dd9"

    protected open val jvmMacosAarch64: String = "b6824f813231591ff1e4d72874a0cf4ec20b7b47cb116a5184ee46435a18c7aa"
    protected open val jvmMacosX86_64: String = "f03b82d4e091ab37c676cc09dcf415d39ac0183b22a27084cd80655b57bb004f"

    protected open val jvmMingwX86: String = "015d800e9676a0a057fd16376ae6ec499717dde89ad73a4ee1464dde381f4e5b"
    protected open val jvmMingwX86_64: String = "9b88b600f6599ba78eec4dacf07c398eb7cfacba778dd6c660701dd944b0a113"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "62524a32ef1999ad79ee36eb2bc97cfd377ab35546e5af302741d7fe9d5b99e1"
    protected open val nativeIosX64: String = "4dd6749ca4ad534f93307dfd55bcb16a8dc9615777ebb27b34df7729fc878a38"

    protected open val nativeMacosArm64: String = "a63797663ed04e2def1d4bd5571700710c88db1a178e41f5e14af468c3bbe2fc"
    protected open val nativeMacosX64: String = "3f2fadc0d44c6938665006974e51bb37ef32eef4367ef5bb64ba0abf8d0269b4"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "93e920c224b5416ad35b670d056d8cf28920bc7b142e82864697afdb72f4ba9a"
        override val jvmLinuxAndroidArmv7: String = "b64e1758b801f8ed5af3a54c71b77f44f225e1a4721a1502a8ab0ccd3fc3183d"
        override val jvmLinuxAndroidX86: String = "743a56c7bbc84d43cd38dd1a0cdbd9428187beda6f401774fb619015e2545129"
        override val jvmLinuxAndroidX86_64: String = "9f2641cf6246c97b8ae0a03c594fcfc3c9ffddf657ac66dc4047da598ead1b81"

        override val jvmLinuxLibcAarch64: String = "c4693b7df1c4e42ea283f6828aaa876b083da4c7c2484b2a34074c5a0be3174c"
        override val jvmLinuxLibcArmv7: String = "44d2a872f301fdc2e9abb167c9784a6ce22932c324de59e9f9bd10225124469c"
        override val jvmLinuxLibcPpc64: String = "6d8567c018b003ac949169a7e46ac09b40fe7a8ca29e94298fba8a0678205a2c"
        override val jvmLinuxLibcX86: String = "e72fb999678c604308af55e06a6abc79a9fe34e4b363e5be24dfc2b08816e16e"
        override val jvmLinuxLibcX86_64: String = "22d7cdb340f32a8a2d6d27aa69855547f4e1e9d6255e539d2a5032b20a2e4661"

        override val jvmMacosAarch64: String = "6e5a1d724e869bea130d69f6fc3f2215fb497084ecb2ca0f31f08add6fd02c61"
        override val jvmMacosX86_64: String = "44feb4ac427d9e9174434f6439673ec6a003dac9aaa170837effd0cd009e2856"

        override val jvmMingwX86: String = "3d5205a25f0d13646ae7bc83644faa5967a971ec3c4e92fc63a5cd81d28329a7"
        override val jvmMingwX86_64: String = "8c460f8c4488dcc56d168e0606e2c76a691b121f51dc223b27686468d0985ac3"

        override val nativeIosSimulatorArm64: String = "1cf17df68daf73f36de2001aeb9e27d10cc956a82f4c0285722b3380371349d3"
        override val nativeIosX64: String = "f8f065b0eaf05448a1400c82a68b28cf20e930ebb2b50d4276c8d22b48c8aaee"

        override val nativeMacosArm64: String = "aad4467b00cfb4e3b4e7ae25bf48cafc1afac8545cacb5a83791c79bdb7940e1"
        override val nativeMacosX64: String = "ca9e627db8011bc4609dcec1bf73cb3cb0dcc93e93f4ffcbe7a075992ddef9c8"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun errorReportJvmNativeLibResources(): String = errorReportJvmNativeLibsProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }
    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    fun errorReportNativeResource(sourceSet: String): String = errorReportNativeResourceProtected(sourceSet)

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // jvm linux-android
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // jvm linux-libc
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // jvm macos
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "aarch64",
            libName = "libtor.dylib.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "macos",
            arch = "x86_64",
            libName = "libtor.dylib.gz",
            hash = jvmMacosX86_64,
        ),

        // jvm mingw
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86",
            libName = "tor.dll.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibJvm(
            osName = "mingw",
            arch = "x86_64",
            libName = "tor.dll.gz",
            hash = jvmMingwX86_64,
        ),

        // native linux
        ValidationHash.ResourceNative(
            sourceSetName = "linuxArm64".toSourceSetName(),
            ktFileName = "resource_libtor_so_gz.kt",
            hash = nativeLinuxArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "linuxX64".toSourceSetName(),
            ktFileName = "resource_libtor_so_gz.kt",
            hash = nativeLinuxX64,
        ),

        // native ios-simulator
        ValidationHash.ResourceNative(
            sourceSetName = "iosSimulatorArm64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeIosSimulatorArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "iosX64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeIosX64,
        ),

        // native macos
        ValidationHash.ResourceNative(
            sourceSetName = "macosArm64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeMacosArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "macosX64".toSourceSetName(),
            ktFileName = "resource_libtor_dylib_gz.kt",
            hash = nativeMacosX64,
        ),

        // native mingw
        ValidationHash.ResourceNative(
            sourceSetName = "mingwX64".toSourceSetName(),
            ktFileName = "resource_tor_dll_gz.kt",
            hash = nativeMingwX64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "libTorResourceValidation"
    }
}
