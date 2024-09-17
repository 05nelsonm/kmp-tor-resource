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
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val androidAarch64: String = "aa266faedf79b22b9c8e91f99ebb9f36b43ce098169d8a07590c73ed30fd1c62"
    protected open val androidArmv7: String = "c0958e9ffce00fe750b03ab6436bacd7267d74d0f3c4640fe85c9657f7d2ae94"
    protected open val androidX86: String = "a17d03b2ec666e9cf3da298a12145154ea6a9ad22c8b3186b60d6dbd7b7e6afb"
    protected open val androidX86_64: String = "6092becfa47fc3f169e2508947824904264c1ad00bba31233df57b7730dae428"

    protected open val jvmLinuxAndroidAarch64: String = "97a13132ab0df12a89ad30224631fdbb0287d22111f0449efde2cb0ae7a22ba3"
    protected open val jvmLinuxAndroidArmv7: String = "af4784ca100ba684699d24f2e82b6fef7e436678774b67e2d615b6918b278464"
    protected open val jvmLinuxAndroidX86: String = "40adcab2e30e184111d2692965dfbebf750f28e81d2588c89b8c5acae2320ea9"
    protected open val jvmLinuxAndroidX86_64: String = "b3c5a61e6b92c7b50afc8fc3c7ad4cf1dd0104f65c2b48cd2ecef7de2cc6dacf"

    protected open val jvmLinuxLibcAarch64: String = "f6c74665fddd77fd0fad795e382820928d70bfa0c6701e1cf1c4441b4c3af8aa"
    protected open val jvmLinuxLibcArmv7: String = "2f1acc434c1a6ef35dde4983399ec204f54278ce897f3b078c47095ec5df1be9"
    protected open val jvmLinuxLibcPpc64: String = "6f01fc657be84e680009e87d15c7c2a8b8cc5436631128a6245ae0e19cd41e7b"
    protected open val jvmLinuxLibcX86: String = "954b20e4e255ff8a8f1a3095fbf5d1cfaf9fd9e1efe880fc82820562b4347b39"
    protected open val jvmLinuxLibcX86_64: String = "77ce55660baf7eb2157ce820fbb061692343075bab1141a9413f6551b03cf7b6"

    protected open val jvmMacosAarch64: String = ""
    protected open val jvmMacosX86_64: String = ""

    protected open val jvmMingwX86: String = "3087e5f3585117c60489ad0687c857a4df1006732f5f8ee3ba4022a60f09d37a"
    protected open val jvmMingwX86_64: String = "377bd07d1ba0a693a5c661eb47f0b50198f0d7b6d7d6db631d47591a7ddc0380"

    protected open val nativeLinuxArm64: String = "f6c74665fddd77fd0fad795e382820928d70bfa0c6701e1cf1c4441b4c3af8aa"
    protected open val nativeLinuxX64: String = "77ce55660baf7eb2157ce820fbb061692343075bab1141a9413f6551b03cf7b6"

    protected open val nativeMacosArm64: String = ""
    protected open val nativeMacosX64: String = ""

    protected open val nativeMingwX64: String = "377bd07d1ba0a693a5c661eb47f0b50198f0d7b6d7d6db631d47591a7ddc0380"

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "aa266faedf79b22b9c8e91f99ebb9f36b43ce098169d8a07590c73ed30fd1c62"
        override val androidArmv7: String = "c0958e9ffce00fe750b03ab6436bacd7267d74d0f3c4640fe85c9657f7d2ae94"
        override val androidX86: String = "a17d03b2ec666e9cf3da298a12145154ea6a9ad22c8b3186b60d6dbd7b7e6afb"
        override val androidX86_64: String = "6092becfa47fc3f169e2508947824904264c1ad00bba31233df57b7730dae428"

        override val jvmLinuxAndroidAarch64: String = "97a13132ab0df12a89ad30224631fdbb0287d22111f0449efde2cb0ae7a22ba3"
        override val jvmLinuxAndroidArmv7: String = "af4784ca100ba684699d24f2e82b6fef7e436678774b67e2d615b6918b278464"
        override val jvmLinuxAndroidX86: String = "40adcab2e30e184111d2692965dfbebf750f28e81d2588c89b8c5acae2320ea9"
        override val jvmLinuxAndroidX86_64: String = "b3c5a61e6b92c7b50afc8fc3c7ad4cf1dd0104f65c2b48cd2ecef7de2cc6dacf"

        override val jvmLinuxLibcAarch64: String = "f6c74665fddd77fd0fad795e382820928d70bfa0c6701e1cf1c4441b4c3af8aa"
        override val jvmLinuxLibcArmv7: String = "2f1acc434c1a6ef35dde4983399ec204f54278ce897f3b078c47095ec5df1be9"
        override val jvmLinuxLibcPpc64: String = "6f01fc657be84e680009e87d15c7c2a8b8cc5436631128a6245ae0e19cd41e7b"
        override val jvmLinuxLibcX86: String = "954b20e4e255ff8a8f1a3095fbf5d1cfaf9fd9e1efe880fc82820562b4347b39"
        override val jvmLinuxLibcX86_64: String = "77ce55660baf7eb2157ce820fbb061692343075bab1141a9413f6551b03cf7b6"

        override val jvmMacosAarch64: String = ""
        override val jvmMacosX86_64: String = ""

        override val jvmMingwX86: String = "5b753a63431a3d47b9c251f4f52c9fe28cc5134e4401ffc3083218dda77c955d"
        override val jvmMingwX86_64: String = "4849aed4333ffcdc8e28b624848ce46c10a45f6b8bee62a2b73e6fb27c59d534"

        override val nativeLinuxArm64: String = "f6c74665fddd77fd0fad795e382820928d70bfa0c6701e1cf1c4441b4c3af8aa"
        override val nativeLinuxX64: String = "77ce55660baf7eb2157ce820fbb061692343075bab1141a9413f6551b03cf7b6"

        override val nativeMacosArm64: String = ""
        override val nativeMacosX64: String = ""

        override val nativeMingwX64: String = "4849aed4333ffcdc8e28b624848ce46c10a45f6b8bee62a2b73e6fb27c59d534"

        internal companion object {
            internal const val NAME = "execTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()
    fun configureNativeResources() { configureNativeResourcesProtected() }

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

        // jvm linux-libc
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

        // jvm macos
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

        // jvm mingw
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
