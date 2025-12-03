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

    protected open val androidAarch64: String = "faa9ed2e005f2458fe9b65340da9107acfefb8de2623a8e2b447731d5348094d"
    protected open val androidArmv7: String = "1b8c8a4a5683664deddde1b1a856245711d615b5dea560ff0a5c235fe90cb775"
    protected open val androidX86: String = "86f500c9aae5acc2f94f1ed8d8f186366151a5b0dd2a3cb4592b50e18c8cea9c"
    protected open val androidX86_64: String = "452d228320a405680c48d3a75ff6dedf2efc7aa574fbbf112fa88d75bbe80a6e"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "129003f82b10c10fd4b8ccccc026b0e2a31badbf3a21b52b5d118226d0123206"
        override val androidArmv7: String = "cc3cf8582bfc22cd510a05f67605a7ad55eeb809cc938901b3bb9e99a2ecfddb"
        override val androidX86: String = "4edd4d6c3de7bfefac3d53bfce9d103801274bee3e6686a09f4ab89adf513e3d"
        override val androidX86_64: String = "d07f887b01312fd7d3dbba7fd8a5eecaaba2a8ea18c189e133583fc30b6a95b9"

        internal companion object {
            internal const val NAME = "compilationLibTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun errorReportAndroidJniResources(): String = errorReportLibAndroidProtected()

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
