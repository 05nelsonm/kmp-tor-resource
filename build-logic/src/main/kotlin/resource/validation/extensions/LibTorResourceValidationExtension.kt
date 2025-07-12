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

    protected open val jvmLinuxAndroidAarch64: String = "58e5d7c4416f7fde6019933c0138d9b2e05668ac48dc4211cbe2d7648b2d850f"
    protected open val jvmLinuxAndroidArmv7: String = "1162bd69d65e795f5c695053f08ea2ecb256e31f2954b5a8b51d22a7d88cb0ca"
    protected open val jvmLinuxAndroidX86: String = "18ebb82611d40edc3c79604cae027631e1fbf9a383f9b95e7384e1cfa5c714e3"
    protected open val jvmLinuxAndroidX86_64: String = "772d6f4ac8f2819583636d62207f969c03bb3a9b921537a829e25bd4424edf5c"

    protected open val jvmLinuxLibcAarch64: String = "4ddea215a371bd18e782ce8c695dc2407ff905c86c9170d704102747cb21563d"
    protected open val jvmLinuxLibcArmv7: String = "1c8b946c6af3665a3f77c034ec166c31cfd078dc0ac04dd11a5076531ee6c1d9"
    protected open val jvmLinuxLibcPpc64: String = "c282d107589542060736f813a9b14b3a6748cba44fc31889fd2d0a546357530c"
    protected open val jvmLinuxLibcX86: String = "ee1b18f141be9331d820e926d08fbdf8a6204e02426f53d360803a0d968a53fb"
    protected open val jvmLinuxLibcX86_64: String = "8a6d02a772f59c23871f119099a57ade632d8628f63a0512551cacfa9ee0aa52"

    protected open val jvmMacosAarch64: String = "f842d968756ac9c75c98e9b8dc9e8adf8a97999cb3406ade06eb2ec970d8d03e"
    protected open val jvmMacosX86_64: String = "ce38acd492d26f690b0a33a20668133e4527eee07dc3dd591c4ec7f76f76c594"

    protected open val jvmMingwX86: String = "536b2814fd75e82e405e44d54e0ad6113be4a51ffdb9720ee18622473764c1bf"
    protected open val jvmMingwX86_64: String = "fc90ac6f0ece082118fcc8fdce99ffa0b2557a20bb03f842160417f4da68f8a9"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "69f02a2483caa8c05dc132554df5903ae74cd82a807a78bda9be7f146e94a6a4"
    protected open val nativeIosX64: String = "877dd7aca3cd6eda7a4446f7af11acd6e29ed55428697475292872ce43b6c7db"

    protected open val nativeMacosArm64: String = "ec510b0e465b3c0d03ee6af6658f7d69f7da522a86eb436fad443a2bda60939c"
    protected open val nativeMacosX64: String = "3cd3e558ccd7244692c9034bb8c54fdde950385d2490608b99f215cafd099903"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "017bc5835c628527ce707b1576abd51beab34aff02dc01b1513cc07bc325df03"
        override val jvmLinuxAndroidArmv7: String = "ced73ea473b4ea0d66381aa12f29c3b0326d64f0857bdba9b8dad582b9688dff"
        override val jvmLinuxAndroidX86: String = "966b77517b28f80b36279583b00bb878a3c3c316fdcaeee7dee9d89992b3edb7"
        override val jvmLinuxAndroidX86_64: String = "1a702a188a177dfaee42032ea04876b849a036192bda24d9ae5c6be04ae2b6b8"

        override val jvmLinuxLibcAarch64: String = "ee2719e96af511aaed34a2f2ffca9852c951f7a484c03a45d38697096dbba2bb"
        override val jvmLinuxLibcArmv7: String = "e7207d50339e79fdcbaca6820e1426f1ce2fb40cd43570002f52c7d4a078ffbc"
        override val jvmLinuxLibcPpc64: String = "7cd0d4e2d73ea885e9dde248e006d8a3f8432e000ca210963aaa90fa17751a93"
        override val jvmLinuxLibcX86: String = "349940d80a762b267a63f0ad9ca0b42a51c818a1349b69d59976dbc41be79c24"
        override val jvmLinuxLibcX86_64: String = "93ad0c825102207c0c1c456229f09bdd582ecf13cfd128e2aec89786bcdcfce7"

        override val jvmMacosAarch64: String = "af10cca5052f5dbe979130f70b308668bfa0ce512400da36e9a2cb688def72e6"
        override val jvmMacosX86_64: String = "4ce412d83fa45ab7ac7d00887398253a2150bacdf4007ede4b4ee82a55c2db64"

        override val jvmMingwX86: String = "6a1f833cb4731f6359a3df08e266ad176ca51f7cc27f32d974d0cd73f27f326e"
        override val jvmMingwX86_64: String = "5ba308a070c117351b70140ff9d07fa6435e3da61959f2846d9c28dac2d9985c"

        override val nativeIosSimulatorArm64: String = "b6d36ec1f4b27644d6fec13914dbaa778aedd0dba0b830ee40be900e4954eab1"
        override val nativeIosX64: String = "42719c44dc315e9426b19c9658bb6598aa450a1bded883b37cb5e8f3df3e2b16"

        override val nativeMacosArm64: String = "6b0093dca5bef83843e82375674efabb2ee4fa19e5cc24e6d4c3b8b0fca1259b"
        override val nativeMacosX64: String = "dfccce3b00353c42538da779eae0fd113d50288a84a12fc65a2f463b2f33a499"

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
