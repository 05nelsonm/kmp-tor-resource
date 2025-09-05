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

    protected open val androidAarch64: String = "bce96c38ad516fd9258c8857ff3e04d3db9dd617de658ea94da5e4a196cc2454"
    protected open val androidArmv7: String = "c53ded659a883e466592bdd2318bffe3cfc334007839a3da262eebca67d8150c"
    protected open val androidX86: String = "d813d57c228d3f52be98ce5c887d81b7c3b7380c7dcf1ec73dccd5c4b3fb8926"
    protected open val androidX86_64: String = "d415468c0b260dd4e1558b93f72beddba7b7eb3aa505796a2c1a93f7efe64422"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "b32e4a69755d77ba2ee2a88172d7374e8f85da8156ad65e8d6290152716cb55a"
        override val androidArmv7: String = "2e6c8ed06da87ac7fec6bd7d1819462f771b21c4662919701ec254330d6f2a8d"
        override val androidX86: String = "90808358c3538fc6d12805d2a94c3fc3c32e4f7acf83681bf227d2b283477ea0"
        override val androidX86_64: String = "1b3a20a57632caaa13e391b0a2fc3e8984fb6494b67ec799393048811ce4715e"

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
