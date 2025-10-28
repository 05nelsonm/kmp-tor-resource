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

    protected open val androidAarch64: String = "0b6e9ef2bf60d1041aa982de392a8f0c7e9fed82295eea42bdc4137e0b22d91f"
    protected open val androidArmv7: String = "e74a7315fcbeaced3c34980f197f7136cba22813c9a738891e5e217b65d011e4"
    protected open val androidX86: String = "2339e07da18e373ef4b02ec45ce426aa623fea3bb0a60f88ba897887de64d6eb"
    protected open val androidX86_64: String = "97ffbdfa2f09dd771739bfd5825fcf1e15b44b828741b92c2c67610afaf8ecbb"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "b2994fc6fe8276b8062424d3bf2c5a94b6df6ee0d7e1d7a1929fc632fad3383d"
        override val androidArmv7: String = "a3eeeedeadc3ff0797b517b334ec5cd5354acc7918e0928e8ce953fdec855de6"
        override val androidX86: String = "9f421a648fd02253c89cc01739197cfb2fe198e34dc3d352a15e3f37fd025dfe"
        override val androidX86_64: String = "55d8f9eed4fbb8c53ad53f3843785382ff9bc25851f0fb5f771117d55e27d22c"

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
