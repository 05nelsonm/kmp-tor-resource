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
import resource.validation.extensions.internal.ValidationHash
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-compilation-lib-tor`
 *
 * @see [GPL]
 * */
open class CompilationLibTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-compilation-lib-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.compilation.lib.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val androidAarch64: String = "ba05948a26e30814db0f85f1477a8310b36173907cb50df541d413e582a3aa31"
    protected open val androidArmv7: String = "50f11a25894341ec4fff71c4f23b3b3a2c68cb2b4f9cabc9c466178e6ffd27d4"
    protected open val androidX86: String = "da0243134b5d7b3d3b71260fd385eeb3cb0e40f8d285e6dbe4760ee45d013a65"
    protected open val androidX86_64: String = "eadec1da31534e5f8b8954eaa7eee39559cfa3713a2779eea07ef0c6c73e896b"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "3b969ebec80007e1e81101c16aeeda3aaf7135167e95a2238a2ab94cb30e2201"
        override val androidArmv7: String = "e308fb15ae6b89537ccfef6d01c2f939b9a12046e6e6b0bfce21584775f5058f"
        override val androidX86: String = "e04007947c4d4a449189e4ee1bdd360664f5027c937690930cd74abc806d17a5"
        override val androidX86_64: String = "aa0aa1c0853b21afb5d6403c121773470a035df8a17dcdb6b77b0265e6ff0fbd"

        internal companion object {
            internal const val NAME = "compilationLibTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtor.so",
            hashArm64 = androidAarch64,
            hashArmv7 = androidArmv7,
            hashX86 = androidX86,
            hashX86_64 = androidX86_64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "compilationLibTorResourceValidation"
    }
}
