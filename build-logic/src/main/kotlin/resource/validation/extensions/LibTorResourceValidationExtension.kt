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

    protected open val jvmMacosAarch64: String = "619974a7fefbc42ce2c80c8c59ecca4b3a7b0d162af3ea313188f7617cec2ada"
    protected open val jvmMacosX86_64: String = "a00db68622e91b0e81a4a044b84faa42c29232f6ddb1e2c9699a01e57ece19c6"

    protected open val jvmMingwX86: String = "1038769d2d4a8cfa627cd867b81b641703269beb83c4e4afc4ea38889a85756e"
    protected open val jvmMingwX86_64: String = "631d1d0b651eeaec1db196435db75b06bab39946094959d03ed6c8bb6d1e8a9e"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "f5a053a0742cb7ec7d14cd5fbbffb25741f0c6fa86119440f653450cc821e5f2"
    protected open val nativeIosX64: String = "e29ca70dd0a745d5d5a52b149618eac1e62c00b5ddba55c85766aa8a09696502"

    protected open val nativeMacosArm64: String = "43b6f04a87a1a70e122e2da1786719da1eedacbcd53e97e1ac0bc0903cdf64de"
    protected open val nativeMacosX64: String = "c8f8063ccbf0225daf948d47604dc2a79e2368d5287026bd335b09385bb18687"

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

        override val jvmMacosAarch64: String = "eae6b8c8885a0d98da2108cc48b8225230530bc31d7d1323286d1880ef460119"
        override val jvmMacosX86_64: String = "5036e39bd8479a6d01f88d968ee32d631432cdf0c2e57a87da80381f89a61684"

        override val jvmMingwX86: String = "4eb84552844be4fd145494ef18e1bf3290b606e301821b83a2ab4c2a776bde19"
        override val jvmMingwX86_64: String = "ed7e8fdfd9718e4784362fa1285875ea8b29083f77c33f9514cd2f513a09b465"

        override val nativeIosSimulatorArm64: String = "cc0c545d70f0905909b3cf8f0d6a8ea17d473bba4fb366ee82739e840ad7a618"
        override val nativeIosX64: String = "ff3f40dd33a5c590d0623e6a0ef6e06084753e033b1ad6b0cb4e8453054ccd44"

        override val nativeMacosArm64: String = "5a09edd84bbd0028c141e30db8aca252ff5bc66006027a4274ed72dd67b7bd7a"
        override val nativeMacosX64: String = "696420b1d95ea8699ed6fb8f56fad78561b72dc00bdea27dd7cad98a0842a8b2"

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
