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

    protected open val androidAarch64: String = "5be7a3c6e8b10e624d35edebfb1a58a41b5691c6757098dffe8f9d98f2096231"
    protected open val androidArmv7: String = "f929a54d59b698cda734706dca0d2a907f45e9bf5c5dee5131c6d4db1f717cdb"
    protected open val androidX86: String = "a130dbd10ff0aa302d3897a683088fa97551b5a985e03adbf8f3656b48d6c7b2"
    protected open val androidX86_64: String = "816cf530095b4b76865369def2432b546761da0cfeb0ea85c60fbf51311984ef"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "6a27093551cf42bf6912d878d0db9d8d9964a2dbb0c250db7ef5c9b62dddf7e4"
        override val androidArmv7: String = "33c17957de8a0e9dfa04c3061afc14eeb7d8a6a6d997c452a34ea493515fa236"
        override val androidX86: String = "322c0b2d8095d8e3f67ed0c251b2649fe0b170a020511e90d881f229dcb55a03"
        override val androidX86_64: String = "83fd7184b62677c5d9f337cb2d594ae93d2c5588fb7af9776055cd2c2b031006"

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
