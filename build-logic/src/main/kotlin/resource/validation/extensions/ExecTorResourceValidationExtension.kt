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

    protected open val androidAarch64: String = ""
    protected open val androidArmv7: String = ""
    protected open val androidX86: String = ""
    protected open val androidX86_64: String = ""

    protected open val jvmLinuxAndroidAarch64: String = ""
    protected open val jvmLinuxAndroidArmv7: String = ""
    protected open val jvmLinuxAndroidX86: String = ""
    protected open val jvmLinuxAndroidX86_64: String = ""

    protected open val jvmLinuxLibcAarch64: String = ""
    protected open val jvmLinuxLibcArmv7: String = ""
    protected open val jvmLinuxLibcPpc64: String = ""
    protected open val jvmLinuxLibcX86: String = ""
    protected open val jvmLinuxLibcX86_64: String = ""

    protected open val jvmMacosAarch64: String = ""
    protected open val jvmMacosX86_64: String = ""

    protected open val jvmMingwX86: String = ""
    protected open val jvmMingwX86_64: String = ""

    protected open val nativeLinuxArm64: String = ""
    protected open val nativeLinuxX64: String = ""
    protected open val nativeMacosArm64: String = ""
    protected open val nativeMacosX64: String = ""
    protected open val nativeMingwX64: String = ""

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = ""
        override val androidArmv7: String = ""
        override val androidX86: String = ""
        override val androidX86_64: String = ""

        override val jvmLinuxAndroidAarch64: String = ""
        override val jvmLinuxAndroidArmv7: String = ""
        override val jvmLinuxAndroidX86: String = ""
        override val jvmLinuxAndroidX86_64: String = ""

        override val jvmLinuxLibcAarch64: String = ""
        override val jvmLinuxLibcArmv7: String = ""
        override val jvmLinuxLibcPpc64: String = ""
        override val jvmLinuxLibcX86: String = ""
        override val jvmLinuxLibcX86_64: String = ""

        override val jvmMacosAarch64: String = ""
        override val jvmMacosX86_64: String = ""

        override val jvmMingwX86: String = ""
        override val jvmMingwX86_64: String = ""

        override val nativeLinuxArm64: String = ""
        override val nativeLinuxX64: String = ""
        override val nativeMacosArm64: String = ""
        override val nativeMacosX64: String = ""
        override val nativeMingwX64: String = ""

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
