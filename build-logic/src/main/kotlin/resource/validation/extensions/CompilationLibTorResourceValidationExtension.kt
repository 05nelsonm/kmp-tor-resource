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

    protected open val androidAarch64: String = "4b3298ece200112f2b381fc2dc11cf0f6e611dd73bba91ec52fdf2924998a400"
    protected open val androidArmv7: String = "6f815ac306d2ce39e2017fce5786abf7a7a395c53b7e99395ace874a672fe8b3"
    protected open val androidX86: String = "5594843254d3d2b1b7358ba7c8186dce6f42ce62a42cf980dc6a8d3d1e103c91"
    protected open val androidX86_64: String = "0b6db241ac3d8ed276d203cf95119b34dfd92046a5456b9bab3fc9543d682def"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "9389fef08c53b1f1c6a9380bd876e8f8cce75decd47cd85d47358cc0bc90f203"
        override val androidArmv7: String = "4cbd9fe07a4c8f1cb7d94b94a070385070e020f260b9101dc0fff294b2bc117a"
        override val androidX86: String = "9de2c441d5f2a33168edbf8b9eb48ab395a378b65eda7854e9fd4aff181f3dc0"
        override val androidX86_64: String = "66b155b10c574853672d174c2f5a76860094c0a95cc8ae82e6c9b2df8e3d48c2"

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
