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

    protected open val jvmMacosAarch64: String = "6a304d7e100c5fa02ababec0cadcf45e606a64ef337047092e61be06472128b1"
    protected open val jvmMacosX86_64: String = "ad3acbf1cc5b1d4082f4884ba0fc4e4ab1937e1bd9f9dcd8825df3f1a10ba850"

    protected open val jvmMingwX86: String = "c1e7c908dce9dd532dbfc05fbf79bf13bd459506eaccacd8f40f1707b0e28a2f"
    protected open val jvmMingwX86_64: String = "6b32f8ea35cc48ebf93837429ad742db25e24c7dcbea19c4cf34cc2ca878bddd"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "8d1ac1a66a1ba40bd9c380ef0f8f00cd9de844edf471c0a6b7496fdd394ed7a2"
    protected open val nativeIosX64: String = "c072d26775ebdbd6b4e71011c4a44130c8d563b3cbf54d6e9f58a6c9bfed5b66"

    protected open val nativeMacosArm64: String = "cf5ca7e0ac96251902cd2eebb9211accddb9d2ab7383d71820c4544a273a7c06"
    protected open val nativeMacosX64: String = "a1fa45fde1534ca47169ada64405438ae971ae8a993ff55e9fbe4f88cb0917e7"

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

        override val jvmMacosAarch64: String = "0368faf545b324a6a089d1144f86537623f75cc14be59cb362bfe4c64a168014"
        override val jvmMacosX86_64: String = "84962134cc0152e93747be38a0e6597df0cd175143f12950ce2d881c1232c335"

        override val jvmMingwX86: String = "e6b5ba7f3cb8c44db6f6e628421f119d5c8b0b6d3f5ba6e8d5de278c769f3f92"
        override val jvmMingwX86_64: String = "dfcc5f7993a3a441270de7b299ff4fa12e128831845cd9449592c68212b9a795"

        override val nativeIosSimulatorArm64: String = "3a8e24e9a19cce0a4e2f0464b63ddeb806493a0caf0ec05e7615db6d7ebb23db"
        override val nativeIosX64: String = "b50919be732e37ede859bf98bad481730dd991fe54cbebc676535964bc6d149b"

        override val nativeMacosArm64: String = "466f4d76bed8694e690612f94dd06c046f0cff4c4d6044d5cbd9cbe5ed5984fa"
        override val nativeMacosX64: String = "486caae2c086a262641d13b37559ba75e766e2a543d659283e47af131564659a"

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
