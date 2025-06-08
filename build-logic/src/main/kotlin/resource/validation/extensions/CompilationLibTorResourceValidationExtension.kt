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

    protected open val androidAarch64: String = "089b4b765d3283aa0498a189e966323ab0ba36284b65589dd4bdbde3f532bdd5"
    protected open val androidArmv7: String = "3021416933aa967b19fed14facf440189a012abd165121c5e017dc439319d331"
    protected open val androidX86: String = "5bb5db37856e968e9dc9a58d0bc25c3961444d8c00070282e1d8226a41e393e6"
    protected open val androidX86_64: String = "4c1b4586aef92f542cb5da8d0688fe9b7347847d0c837196de820c6e949dc004"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "f0ba53ec1e897235f426910fe4779f3f8137c4b8d49c6831f81a46d092e9f39e"
        override val androidArmv7: String = "37c6a10f23ffac1f13eaaad214e91a864a26f8a6778b116113d348b864e5206b"
        override val androidX86: String = "302d1edf12dd19c8bb452c7eaada104249b29404d2ea10066c99e66e3696f561"
        override val androidX86_64: String = "8ac9822251e1ab9fa452ed35f4b78fffbbf7d63b2d4138e30d984b6c2e4b1dc9"

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
