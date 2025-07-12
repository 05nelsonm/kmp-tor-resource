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

    protected open val androidAarch64: String = "581c48d59e73e4cce899e03a8d25c36c7498b745558c76ae1b6e49a17c71c766"
    protected open val androidArmv7: String = "684828a52c99395ea4e51f1160364f93329c14940f6f7524a59fb837e3637377"
    protected open val androidX86: String = "aa3ada9ca91ec12c7b922be645945a8607927d2918f11f0b95c9bacc6a3466da"
    protected open val androidX86_64: String = "969100d0bb5e50151a1ca9579357df7418b00c4afc4b4f1f5a5c9b5ce98491f9"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "d87e6cb967eb6f6a95d7daee642d91ba1912d319d48536eb64e53a5a36e2fc7c"
        override val androidArmv7: String = "38e31542744a48b9bac99c9d9ec825c61f43894471f8afcac011d90198858655"
        override val androidX86: String = "1f1ff92d734be2eae35db5e6fbc4ad1b6063a29a5e9a12844976f2796b672a9e"
        override val androidX86_64: String = "75e0b2bfda0eaf9cee5aae3a4f8f8e2b4af690072d990c074732fd17b7c3e20f"

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
