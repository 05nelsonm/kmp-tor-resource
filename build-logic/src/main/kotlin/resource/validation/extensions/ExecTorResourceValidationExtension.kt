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
package resource.validation.extensions

import org.gradle.api.Project
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
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

    protected open val nativeLinuxArm64: String = "3bdaa80e8f91bdbcd01fc12264c62a7609eb72fcf6e75aecadf77db9c102ba0b"
    protected open val nativeLinuxX64: String = "a37fcf1105416f391d71fdbc919119967a6216002fc341415ccb895f72f13a75"
    protected open val nativeMacosArm64: String = "c8fda1ab4f0152cb69dd99a556b1dc421c3d4e643264bebfc67c85c93dc8d701"
    protected open val nativeMacosX64: String = "d82bf88e8b11852aede6e05a037e6eed9de7f4c2070fbd6f9d6ce033fb60b9f4"
    protected open val nativeMingwX64: String = "caf6911f6375ba9aa54962282d0132d2cb405beaeb4d1486187e1d77835bf4d0"

    /**
     * Resource validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val nativeLinuxArm64: String = "6534b86ca6c7d5bb229397e89893c2ac185bc363b1ff9326d2f6aa796199d6af"
        override val nativeLinuxX64: String = "d25a91a081fbc87195c9d79a1221c3891fc489c40f617d0a5eb4b30f09c7463c"
        override val nativeMacosArm64: String = "db064f76042f16dac7a422f2e5ebeb4b6c9a2fbc2b9b6f854a20cc66bbc7f781"
        override val nativeMacosX64: String = "82cc56f489713b5fef35e4e67ff3657778f5b85b61791f37efc63297313c62ee"
        override val nativeMingwX64: String = "3434411f3043326c9b5d69e389a900d0b17016f33436c8bfa4f823c24d5fa038"

        internal companion object {
            internal const val NAME = "execTorGPLResourceValidation"
        }
    }

    fun configureNativeResources() { configureNativeResourcesProtected() }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // linux
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

        // macos
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

        // mingw
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
