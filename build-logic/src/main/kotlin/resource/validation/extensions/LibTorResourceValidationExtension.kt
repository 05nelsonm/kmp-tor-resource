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

    protected open val androidAarch64: String = "3f54f949010264221050171290bbea8fceaeb2480fb13b33492c5a2ae7c8d710"
    protected open val androidArmv7: String = "745b3db4655f403a1b78a7cb1698069be703535ac1426633d1a5679b996d003f"
    protected open val androidX86: String = "3e5fafcb788e17ec4af3f260f38835a81c70cedaa563b7013d794bd8d03b241c"
    protected open val androidX86_64: String = "71b593435b142e98e88a4a8203c4cbe915668f95edc3c89c80300508524c996c"

    protected open val jvmLinuxAndroidAarch64: String = "4754c7878d3181375135239f03dbbf570a41bcac8d15aff3c5475a6f250815f6"
    protected open val jvmLinuxAndroidArmv7: String = "19fb0c826e8313669338a4baba8b9bb00d586610f16665af40df5d1e92e08d53"
    protected open val jvmLinuxAndroidX86: String = "178231b09f9fcf9766f1c204d292d1cef5eaed04a64d7a3a0a225f1e66734088"
    protected open val jvmLinuxAndroidX86_64: String = "235b80144a2ca81bf7a007b7bdbdfaab487d03e6b596a6d8adf5cd8ea8a4dfd1"

    protected open val jvmLinuxLibcAarch64: String = "7309775c1053f17211013d4c741cac69a8c8f45954f62409e6accb27d3102970"
    protected open val jvmLinuxLibcArmv7: String = "41434033dcdce3831406cbede455cfb612d8419d4f094cf8b7d99aa7089e1d24"
    protected open val jvmLinuxLibcPpc64: String = "94d78a39f3008776fdc0f73bb7b8929dabb4fc76d9bc0525cb46eae13d25649b"
    protected open val jvmLinuxLibcX86: String = "23e3fa0c1db93e4e572b3de3e09a3baca655c2bb25a5dbf2dd4383a32c7b68d3"
    protected open val jvmLinuxLibcX86_64: String = "d3affb4539cc8019b61c7b54bcf6cf8243dc99720c1e2c2716359dfd38f042a0"

    protected open val jvmMacosAarch64: String = "552ac3107eb3e1dc0aacb5fa8f9780295631c9199a3b5ebb6656868f1d93256c"
    protected open val jvmMacosX86_64: String = "537f68f831861602dc2d8cf1278dd4cc0440ce703032d77ea43875fc21857833"

    protected open val jvmMingwX86: String = "b28f897c1bdb1aecd301a1ef8a7ff7cb6ec47979e8f7091ffab914f60200de78"
    protected open val jvmMingwX86_64: String = "431155c35c9d9e35db2a04ef758714d63ce93ae173c46c41542359edf102a209"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeMacosArm64: String = "17c97e503e3c8034c844a78ef9ff7ce0bc8357424ca26b9d40113a69b1513c2f"
    protected open val nativeMacosX64: String = "d406a6e1ccd8abbe1ab0e79f372a12aca087f82950c1ddc49712b172409f756e"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "961bf2dea79152b5f7be33b9dcc03a2177eef4605ff5786b9197900e88a44498"
        override val androidArmv7: String = "ed442ebd5ce60ef65f65a342b1b7a6495bf0b918ebbeb980aa79471defe057b7"
        override val androidX86: String = "9a898aee8801989b3abcbbde6576143f44bcbfb41a89d8807329b91dcfe7e322"
        override val androidX86_64: String = "8e7ced5ef059198678702a97fb5e26288bcfed210c8b5ecf19e733ce451990e1"

        override val jvmLinuxAndroidAarch64: String = "3e0f207c4d47b4b479c4e87c9d449cf029df69a747a49587fd46ee8740af3d18"
        override val jvmLinuxAndroidArmv7: String = "284f5853c7b9b07d5bdc6ffa30e78935d6d7a1dd0aacd777598d70637f44b9bb"
        override val jvmLinuxAndroidX86: String = "85960d8c9c848302fce383136f633a2366a92aa54b1612ce24581280a9ef64c1"
        override val jvmLinuxAndroidX86_64: String = "4501ff6bca64de44c058ea4a5cf7e9ff44b298b0ffa9445ed8af18c767917f3c"

        override val jvmLinuxLibcAarch64: String = "57af64739c135d379835c65b41f3ffbb12b0d350f0c253ea8575f3206e1e9e4b"
        override val jvmLinuxLibcArmv7: String = "0f84e5bb9d05c4a0867f8b70d0982c19d2034166b0216e0fad7dab44465e21bc"
        override val jvmLinuxLibcPpc64: String = "7d899f2c899f11a18c7514d537a91b71c8e0e1fdfd4f4112748d4bde3a592899"
        override val jvmLinuxLibcX86: String = "f72b70926fc3d6ea3ded13829262fb3af8d2bd7cf9f3de25173f6327b617878e"
        override val jvmLinuxLibcX86_64: String = "4205b3d9d45b5089a834275a1591395fd2bf12b3587d7d44d953fdc61090a0f8"

        override val jvmMacosAarch64: String = "23d59e5a7ae36df424a4a32eae3f866aac5d58a1f08b1e8ea079e2c7758150f6"
        override val jvmMacosX86_64: String = "861684696d4ad2d67b237663dfdf066d782bd8ec59d114040a30be3a3cab059c"

        override val jvmMingwX86: String = "96a1238dc462ab3846b467fb06feab1ca855cb42cf6d1bb89c4ffb31904d3da4"
        override val jvmMingwX86_64: String = "2e722b1fb9379eff4cb5f123a2ca5353bed777613403309234cf1e889756d51c"

        override val nativeMacosArm64: String = "4a047c011f9754812d54018f2f415142cfa4b3f6ac13d5ef499495724de4ea43"
        override val nativeMacosX64: String = "bab88c16ab628b82393ac6ac8a684ba2daff95f615eae751876c2bce0b5cfc98"

        internal companion object {
            internal const val NAME = "libTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtor.so",
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
