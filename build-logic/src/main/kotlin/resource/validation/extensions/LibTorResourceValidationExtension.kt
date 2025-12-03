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

    protected open val jvmLinuxAndroidAarch64: String = "69ea5ffe54bafff8f9cfc415620a5b8c7e0a9ad0ff133d8472169993e21a9263"
    protected open val jvmLinuxAndroidArmv7: String = "c5e0fc0d7abe50cc2ac704cd3c258f70539c842af3fc7fb5ed308ee7969b0339"
    protected open val jvmLinuxAndroidX86: String = "c77259c717f767b264c9c4020d059bba5315aeee6a435a1a29d3199a12f4e94a"
    protected open val jvmLinuxAndroidX86_64: String = "ba442b7a984799f1bf6751685ea82ae20321fb773a584eaa5828d9ab5b11ddb5"

    protected open val jvmLinuxLibcAarch64: String = "8814c942bbe0377f08ecf9394882ee4e676e94cabcee16ff136bfe58bb519840"
    protected open val jvmLinuxLibcArmv7: String = "e380d3dac3e6f81dda9a67595f98eb537fad683a4aed52af2ddb1b15fe60c74e"
    protected open val jvmLinuxLibcPpc64: String = "17145a7766499dc4a01abe4153473359aedec7b300638730c6b78a382d59fa2d"
    protected open val jvmLinuxLibcRiscv64: String = "120279c8715bb3fad56c06ed0612a35655b15bfbca80c679e28bf2e3274c0b2c"
    protected open val jvmLinuxLibcX86: String = "ff397a9a869186f3d129a434f666361dbc750c531435cf4729ebed0817a4150f"
    protected open val jvmLinuxLibcX86_64: String = "88082c777df331c388d106d285d9833c8c38a6965253ae871a4f173cf4c9a038"

    protected open val jvmLinuxMuslAarch64: String = "b225f89364256ad74b1ed1456e1cee65f3e3ab244109302ee84e755cf0302ee7"
    protected open val jvmLinuxMuslX86: String = "c89081121215784edfafb05d550ac9561d9a703161fd6244075fc443f531ae63"
    protected open val jvmLinuxMuslX86_64: String = "6b74a3bc3d0c48eb5787747a5e09a375d1175359d6c741847f7b0057547fb8e6"

    protected open val jvmMacosAarch64: String = "5033998bc335aac09330d5bf45f6f05f7cd87f00b1297c5ed598e3cc6e118807"
    protected open val jvmMacosX86_64: String = "93bc9f2a0f7eae4bd2f8fdfc183578359e21cb761129b8cb9cf9c9a3322ccf62"

    protected open val jvmMingwX86: String = "78ad8bea5b3ac3d553d8f605a1ba9b361ae4b66605aa13ec84927e0cb05fc980"
    protected open val jvmMingwX86_64: String = "09f9d7232c7b975ec15fa04f5765ce2d136064cc2503b03ea7d7dd00fa4254b0"

    private val nativeLinuxArm64: String by lazy { jvmLinuxLibcAarch64 }
    private val nativeLinuxX64: String by lazy { jvmLinuxLibcX86_64 }

    protected open val nativeIosSimulatorArm64: String = "67ea39d69aeeb311974289913f851f139319fd0b10e675033754a60389dfcffb"
    protected open val nativeIosX64: String = "6bae510d22e679c47b0232360b705a4e081d094208d23568fc59b3d54575141a"

    protected open val nativeMacosArm64: String = "5618c87cc50f1489b6fb23f44fada28057dea7694ef62a7271eb3ef26ff64b3d"
    protected open val nativeMacosX64: String = "27a96c0e372a23c16420ac24ed97e97799df26a9e7508d152c78bfd146ea3545"

    private val nativeMingwX64: String by lazy { jvmMingwX86_64 }

    /**
     * Resource validation and configuration for module `:library:resource-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): LibTorResourceValidationExtension(project, isGpl = true) {

        override val jvmLinuxAndroidAarch64: String = "daa938dffb1162fba0b2f8634efe0a170e79184f43727151cbe0aa78bf5abb9c"
        override val jvmLinuxAndroidArmv7: String = "2f555d71908bcd4a391f7a95631c17cc754e6e4ce0a45e311b3a0c2e1e4f6a0a"
        override val jvmLinuxAndroidX86: String = "3457362e78bb739eb4240e263b183a0e4e0d7e167ea128fd513e660c9051a2ff"
        override val jvmLinuxAndroidX86_64: String = "bc8cc29eefa8b75cce39c953a3009c2765f5d18177258d12d052f2f550595476"

        override val jvmLinuxLibcAarch64: String = "c99f91a1007779159c1c2ee8a2477ca05fa2f5ab3e9a0c7e0ab3a72ee71bff08"
        override val jvmLinuxLibcArmv7: String = "b5227c30067ea984e7ca1320fba527c224f6f432cdbd093688ace86e51ea7bbe"
        override val jvmLinuxLibcPpc64: String = "55093380f840c50878992c7dffeeb05fd330724930f80d7b2cf72c28a1e86807"
        override val jvmLinuxLibcRiscv64: String = "1d0331c3d5715939056f75a2a73a557f224ec948f7b35d0a3f8966d18ba8bfdb"
        override val jvmLinuxLibcX86: String = "f458886eaed8837ed51b5a86a83cbd9cdc6afdbe49405637993da13e343eb92a"
        override val jvmLinuxLibcX86_64: String = "0eeff0fa5577276c8348941fbce09070019fd90b26718f33a3ba45f55ba8d7c4"

        override val jvmLinuxMuslAarch64: String = "24faa7130276186f7fbf14ea63c6f17915b8c7f3fdb98af4efce1d3b5dce55d4"
        override val jvmLinuxMuslX86: String = "eb479b63624820ad50743aa0f3cee52aa8e9cba5d6541518e91f1e2ab46cd11e"
        override val jvmLinuxMuslX86_64: String = "ce12766fa79c2084def2c304c24a67cccd44547239c73b0fc86b3072111a7d8e"

        override val jvmMacosAarch64: String = "51e259bdf6d856687079409e840a8976a8a27648bc4d73ada30068c72b245d5e"
        override val jvmMacosX86_64: String = "f4f1cce3991d8ca27aff65701033104f91163e32ba37e4a67e5fe92862cd5a0b"

        override val jvmMingwX86: String = "7cdc6f643c802420c2f5cd70e4b8a998c5f9af4c622985c41b9b5e3175b7c08d"
        override val jvmMingwX86_64: String = "106ac5d18929c264f542d1b7c5b3938f7f1f7ce934912b439abb0d6c2ea881d6"

        override val nativeIosSimulatorArm64: String = "124c0b9511591924dcf32021123c851e59584106f4dcbec1f7ad825a04f1dcf6"
        override val nativeIosX64: String = "ba36f606f0a07018fc480d8c4fdcf0e8ca7dc1d56fe1d088fdd43aba71293f67"

        override val nativeMacosArm64: String = "4ff94587397408810250b20ed6a3fda6e7f25275d31eaed54c028e652824d01d"
        override val nativeMacosX64: String = "b64c1334cc94fd9248d808cac21a24e9f78f8c31f5533b60617c4385e1d7015f"

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
            arch = "riscv64",
            libName = "libtor.so.gz",
            hash = jvmLinuxLibcRiscv64,
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

        // jvm linux-musl
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "aarch64",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslAarch64,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslX86,
        ),
        ValidationHash.LibJvm(
            osName = "linux",
            osSubtype = "musl",
            arch = "x86_64",
            libName = "libtor.so.gz",
            hash = jvmLinuxMuslX86_64,
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
