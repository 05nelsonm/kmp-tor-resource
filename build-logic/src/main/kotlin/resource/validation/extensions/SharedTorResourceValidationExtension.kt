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
@file:Suppress("PropertyName")

package resource.validation.extensions

import org.gradle.api.Project
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resoure validation and configuration for module `:library:resource-shared-tor`
 *
 * @see [GPL]
 * */
open class SharedTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-shared-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.shared.tor",
) {

    @Inject
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val androidAarch64: String = "13daaaf147a098bcb035009f61b2f222be1a7962aae50d3a1d7e2a7ea98efe5a"
    protected open val androidArmv7: String = "b43c389df59039d5eeaf2d679f312144fa91a1651b755471751d9b6a81ad09d7"
    protected open val androidX86: String = "cb1d49f4fb68e430b277e81b784e980a9dbbcf88bf7e6f6bf8b393d651ce6ee9"
    protected open val androidX86_64: String = "9690a709c3b555d7a47092f023bf8bbd0bd133814e38dab30c395d56c5a8ec89"

    protected open val jvmLinuxAndroidAarch64: String = "57ad474d15946d9ec2709b28c162ac9da24ff0c55a4b1643ce72881883e9d8e6"
    protected open val jvmLinuxAndroidArmv7: String = "e545ec3553ba31fe04e9c620fc246024570d2c594026c12ee150c14b36b5976c"
    protected open val jvmLinuxAndroidX86: String = "2e2648f7cb8dc7459fd518228327e0d46f63fb30a3211ce2310c62798583aa94"
    protected open val jvmLinuxAndroidX86_64: String = "0064dcbaaff0c3d92d753fb9bd6017b829ed2b2d12da2b7cbdd72872b3a1bcbc"

    protected open val jvmLinuxLibcAarch64: String = "49ad1505c66ea17eccdc48761e406eef75a4cc5575cd19d48e7d183f1faf9d0d"
    protected open val jvmLinuxLibcArmv7: String = "a9afec260d5569278d38af8f689fd83140fc44789d2418418e8ee8e998606e96"
    protected open val jvmLinuxLibcPpc64: String = "64bea20250126dfef49db744f3b5444024353c5959615358c450aad5c6e74a74"
    protected open val jvmLinuxLibcX86: String = "d52463086a118cb9c72b0cf0dffa3c410f5cb321246480c4cc4e99a0f6e5b1b9"
    protected open val jvmLinuxLibcX86_64: String = "ccbcb36839f99f5dbcc4f0d3d5bc719e180b00b8bdc033c72c21aaaed09bf364"

    protected open val jvmMacosAarch64: String = "1a01ab2de1279f27e246cbbc3154e12d9dfe3f174351f685158b35ae0c3863bb"
    protected open val jvmMacosX86_64: String = "89aa82f9290924873adc4b7de21d82b54bc2e2fc34f8c03272b565e6a8da48ee"

    protected open val jvmMingwX86: String = "5d42022964c527c994c3b78b6d43d882bf726b5a3fff2292ea60c6988a277482"
    protected open val jvmMingwX86_64: String = "3b69341dc453057f3b8cb51e4957bc8d72e04ede543413123014ef0fd320f877"

    /**
     * Resoure validation and configuration for module `:library:resource-shared-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): SharedTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "c8d6e2aa51b90a3406364254e2af2fa507fc23f96a9afb9bc230a1fecc9b968f"
        override val androidArmv7: String = "c8c1e0b9a436fa293733a83e060015e6e5f16290d471fbe714b874e6e18e173a"
        override val androidX86: String = "c3a0da8a446c1c05b8baaeabef719408d2d226cb55c86f71ee4cfe005dc41b2a"
        override val androidX86_64: String = "8d4fdcd96849c929ae302bce9dc01e68c74212c677af5bd11de73ce65b8ced21"

        override val jvmLinuxAndroidAarch64: String = "b23c860fe832b6055eb24fb523e3568dee2305948034d0b957348882f8c47fb2"
        override val jvmLinuxAndroidArmv7: String = "c12d75d5ccab45ef2e44dd73c5bcd835d97e5e965eea4cfa97359ab21cc52921"
        override val jvmLinuxAndroidX86: String = "dfe0a718f14c8dbe320231238654518dd600828963ba40956f4f5b902ebee3cf"
        override val jvmLinuxAndroidX86_64: String = "b4da1567a272ef6f4674e22a6168bdee3d0482cebb1a13db7c10ff9d9c888434"

        override val jvmLinuxLibcAarch64: String = "c8492f1e2bdbbe25d8b9787fd571d17d557ff5c157dca7db5dc3b2bb181185ec"
        override val jvmLinuxLibcArmv7: String = "4c4d9fc5c55e5189773f690e5fa6bea23806370c9e90eadf5a7e2befb2f07908"
        override val jvmLinuxLibcPpc64: String = "4af1becaf64483ca55ab8605fbee93a7c54869e4af5726cef0f238786fd969f1"
        override val jvmLinuxLibcX86: String = "3caa33ac51195495163e0ffcc714185b638e4532ce4d807ff2525dcd6a5d4acb"
        override val jvmLinuxLibcX86_64: String = "ffdb6866ba86f7011f77459f4772ef6c3f857bd9362acec345a306141f9a4fe3"

        override val jvmMacosAarch64: String = "a81d824e305a12fd4cf4af1281ede74ca8ee5888a76dd3335a297494a0bc1e07"
        override val jvmMacosX86_64: String = "0a8f67c33e4b35010b86172a7474c8ffb0ff44ce5d32fd0db126fafb5bd9b9f3"

        override val jvmMingwX86: String = "402e1717f736ff4e7c4e9387a1d91fc98515129ec9c2360591d6c816beb547b7"
        override val jvmMingwX86_64: String = "7a75807309c18dddd081384f86dd4b70e777f6008387d21aa04e019b8afdf4c5"

        internal companion object {
            internal const val NAME = "sharedTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtor.so",
            hashArm64 = androidAarch64,
            hashArmv7 = androidArmv7,
            hashX86 = androidX86,
            hashX86_64 = androidX86_64,
        ),

        // linux-android
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxAndroidAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxAndroidArmv7,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "android",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxAndroidX86_64,
        ),

        // linux-libc
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmLinuxLibcAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "armv7",
            libName = "tor.gz",
            hash = jvmLinuxLibcArmv7,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "ppc64",
            libName = "tor.gz",
            hash = jvmLinuxLibcPpc64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "linux",
            osSubtype = "libc",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmLinuxLibcX86_64,
        ),

        // macos
        ValidationHash.LibNative.JVM(
            osName = "macos",
            arch = "aarch64",
            libName = "tor.gz",
            hash = jvmMacosAarch64,
        ),
        ValidationHash.LibNative.JVM(
            osName = "macos",
            arch = "x86_64",
            libName = "tor.gz",
            hash = jvmMacosX86_64,
        ),

        // mingw
        ValidationHash.LibNative.JVM(
            osName = "mingw",
            arch = "x86",
            libName = "tor.exe.gz",
            hash = jvmMingwX86,
        ),
        ValidationHash.LibNative.JVM(
            osName = "mingw",
            arch = "x86_64",
            libName = "tor.exe.gz",
            hash = jvmMingwX86_64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "sharedTorResourceValidation"
    }
}
