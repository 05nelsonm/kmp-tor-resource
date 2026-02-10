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

    protected open val androidAarch64: String = "e6a8d10b876d1edfde0666b425e85943b9fba964e7049a0c922ad3fee8e019d9"
    protected open val androidArmv7: String = "fec4e162d69d625cc2e7f2983c78f87ba24e6bf59b13b7b7cc0adaa1a2396549"
    protected open val androidX86: String = "366475385afc83c4bb67a28a5325a929d3effdfc574ae1569bba2a64ebf16a85"
    protected open val androidX86_64: String = "228092c1c93f9b94aa74a168c3540dc8fe8582dcf89f4d8e026f52f4b5084309"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "6d0d3805c7c0f678fd5d2bcddc440251e773cca346701ed5d316840fe236dabb"
        override val androidArmv7: String = "3e508dd03b6f019710a0c7644bdcb01611b79110166ce0df610fdc4b99ce6a49"
        override val androidX86: String = "a47fb7bb972f3b8ad445213dea16dfba92f382f9997f49fef5a162e550c7a131"
        override val androidX86_64: String = "c54ff5db6c05a73a730830188716a0206ae0832541fa99a0b080250b882f8d0a"

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
