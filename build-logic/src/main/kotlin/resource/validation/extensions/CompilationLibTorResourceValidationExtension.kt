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

    protected open val androidAarch64: String = "30508f94c4e3f0977e94d75673a59db20d20c10b2eee9de01a0985f64c904724"
    protected open val androidArmv7: String = "796a3c64cc3b1ad2cc40d1dfdd0e64661704e63bfae5df11dc9329c595c72b05"
    protected open val androidX86: String = "826cd2783a01a20bf162ba911cd3cf281250a88ede0f54bb4276808a4e887166"
    protected open val androidX86_64: String = "8da3208f6c1efeb5c9d838cb41ce9fc09d8a3a769d8e6d28902a43d127b6faae"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "52077ebe59cbaa9607a1acc032e3f70b6c1d59c35ae6ea75cb96be189252f8ef"
        override val androidArmv7: String = "bcb8cc88c51d864b34548cfa81d85413f7f9de1a9597908bd105e2921b654bec"
        override val androidX86: String = "eec3b73f9610b6bb357a96dc2e14b056305f9b3fe33ab670e58a069ffddefc89"
        override val androidX86_64: String = "3dced05aa1deb5b2544944701370f23078bc81e9a7154b678b2ff1bd5e338148"

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
