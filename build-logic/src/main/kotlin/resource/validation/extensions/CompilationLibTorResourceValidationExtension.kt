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

    protected open val androidAarch64: String = "d828508bab8324f4758d59f772b338d0a1342c1772150298f7d317229a3e60b2"
    protected open val androidArmv7: String = "4a4fcf362b9770e240f93a66fe5ea14cdb4b0f1cb996d3434a925037c1e7ea1d"
    protected open val androidX86: String = "7b89fcee6523a6a1055b15073980cd70220f878ae573985073255ef3c9c398ce"
    protected open val androidX86_64: String = "943741536c5b57e4386f56a5041528689ec9e385c0740bb7f5bb1fef97a2b290"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "8db91e0f191ba80af8cfc14ce92073d674ec28b08f463e9538d4dfef56bea3f6"
        override val androidArmv7: String = "9a8cfd62c5aa56343ea324a03ed784401b9fc7a0bcc3be5d4a58766414bc724f"
        override val androidX86: String = "82019eef8d4d54b399308b8d5622bc4b886a70ca7ce769b1dab81920cd009176"
        override val androidX86_64: String = "966ac756bf6998ba3f075309b4d772c8c2b8f2e993c179c2468a14a21a1e3300"

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
