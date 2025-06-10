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

    protected open val androidAarch64: String = "f7912e5eae05d4c2fd1fe7195208eb889ab1837dd88adf9b0b9d00d7cd70ad8f"
    protected open val androidArmv7: String = "a1c5f90feb8fce0876b669004134992ef96967756f3bf7ebea29977beed74d18"
    protected open val androidX86: String = "64ded25b97c7336def453ebd3b14225315d384d6ce9a6ab21e7ead01e16a06ca"
    protected open val androidX86_64: String = "a7091bccdb21c01c085c9cb32ade4c43e13d06ebf6e79c1170bbda290ee54c93"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "1b075c968ea9821cb16cdc4ecae2cca3e4dd5b24e4ce6e331b06a4b50abf722e"
        override val androidArmv7: String = "29f7333ade6d8e0c8b3e7bf22e6b1e1be6bcbd6327a8bb0184ab38f831f2e79f"
        override val androidX86: String = "0834c118cdf987f7f739466ecf4a016d4d7a16f36ae8b617be803087966a30d6"
        override val androidX86_64: String = "cc054f6938c033cff89a9373700ea3ad235cb562bd57419c4c95c9135016ead3"

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
