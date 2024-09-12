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

    protected open val androidAarch64: String = "fe536c871b6aad544998e76c9d5480534409f0cfbff45e09433e0e535371f0de"
    protected open val androidArmv7: String = "d67a0b31c4bafc794a54e78875f0413f7eb536f8f93c0f706bd1ebf897c77626"
    protected open val androidX86: String = "8f5363dd458fbdb76f75fa2f6962c1d2dd60ca1df7de94c4e94100fb1af09e5f"
    protected open val androidX86_64: String = "9edacc0c341a30d1a80470521251e9eeacd4fe10d7e151b907650de2776d59f9"

    protected open val jvmLinuxAndroidAarch64: String = "aa652260b30c48f888c193dba89f3590e0d3ffc905f42245d43d2cd1680aac5e"
    protected open val jvmLinuxAndroidArmv7: String = "19b9c651dd6460d295ec5f2b89fb94dd527a5b928a22d1426ae319f06de31c85"
    protected open val jvmLinuxAndroidX86: String = "36a546dc35f84139d89b88fb5a23d5e7b4a3dbd6d184148cf57810627169f3ad"
    protected open val jvmLinuxAndroidX86_64: String = "5de6d694428e93b5d5c81ee700eef4abba6de5a89cdf983620ac800ec5a96a8c"

    protected open val jvmLinuxLibcAarch64: String = "3bdaa80e8f91bdbcd01fc12264c62a7609eb72fcf6e75aecadf77db9c102ba0b"
    protected open val jvmLinuxLibcArmv7: String = "3684e175bf73c07c04decd2b6f1016e6e643f94bfe0b0cdd56cbc26539a97b15"
    protected open val jvmLinuxLibcPpc64: String = "475841537362e00ac1293d6ffbc768abacc382fca6fa5d9c5eb4af53d0dd5bb6"
    protected open val jvmLinuxLibcX86: String = "31c37da4cd2cb0cdbd757e2252afdfcf8edaa7d83da418288ced8f54d4445a0d"
    protected open val jvmLinuxLibcX86_64: String = "a37fcf1105416f391d71fdbc919119967a6216002fc341415ccb895f72f13a75"

    protected open val jvmMacosAarch64: String = "bbd7ec542d8dea7830b6717ed55b1bf1dbe67e142e9ef74f40decdf8f5d02017"
    protected open val jvmMacosX86_64: String = "8dcc06411566e637473f1735558ee54abe678f0a3ef3f57a80db96f08d70a9a6"

    protected open val jvmMingwX86: String = "900a35e6f93dbe2c219a4dd40d42777458618c9efa422204cc947e32c04b1507"
    protected open val jvmMingwX86_64: String = "caf6911f6375ba9aa54962282d0132d2cb405beaeb4d1486187e1d77835bf4d0"

    /**
     * Resoure validation and configuration for module `:library:resource-shared-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): SharedTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "c26a4d972f3973e6a03105d62a7fd3f5029adf7e6493753d5ebb60092c66393b"
        override val androidArmv7: String = "daa5877f0aec67565838ded30d66fe9733e1b3be1a0e21a6729114fce8ec8535"
        override val androidX86: String = "2ba17d3b4b7279d188d338a16e63d2cbe03278537c2242784e687a30e61f4ded"
        override val androidX86_64: String = "241f35f12848a1a7709b53fd436a3f68bf0d0e3e8dd2144ab74bc62ff229d827"

        override val jvmLinuxAndroidAarch64: String = "a56d53899eedf7808ccd49bdd449585c19f648400cc05f366d797ec71a378206"
        override val jvmLinuxAndroidArmv7: String = "0204d179b4dcdc038d14cbb3af25cb930940a39c473d67f4f1fee6968a84f2ff"
        override val jvmLinuxAndroidX86: String = "6c12d0a3bae2d0358a1687b75cd6166513030bcc49da286dd2b985f0c9303208"
        override val jvmLinuxAndroidX86_64: String = "48529da0a880830c79340daa14f6dacc0db9e827547e94e88140387947717782"

        override val jvmLinuxLibcAarch64: String = "6534b86ca6c7d5bb229397e89893c2ac185bc363b1ff9326d2f6aa796199d6af"
        override val jvmLinuxLibcArmv7: String = "aa5e3f2c97e1bd1d5ca62896d54b1affc606894ea1d420e5e5fb895abb06054e"
        override val jvmLinuxLibcPpc64: String = "e9e3b7062b2d48ab638fab6419a885c2f432ec2fbc1c3554e7342561675d033e"
        override val jvmLinuxLibcX86: String = "28ee4b671b2f8c56662622b4fe5708ddb43259c3b9f421c98a3e54e509e42219"
        override val jvmLinuxLibcX86_64: String = "d25a91a081fbc87195c9d79a1221c3891fc489c40f617d0a5eb4b30f09c7463c"

        override val jvmMacosAarch64: String = "37bb644b957854adab14246821aa633f477c230a0d6e71c98cd84db5f7a5c7fa"
        override val jvmMacosX86_64: String = "4eabf2f001798fb1f1b537e8d9f4f6ffecadc57c08be089dc88b5d7e81a9f5cd"

        override val jvmMingwX86: String = "9f6b059da64e8e2c4aad7a445753ed4b8c02c7aaeefa0f1f08b5db35ee33ce00"
        override val jvmMingwX86_64: String = "3434411f3043326c9b5d69e389a900d0b17016f33436c8bfa4f823c24d5fa038"

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
