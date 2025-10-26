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

    protected open val androidAarch64: String = "21ab1a8e8f9306add9fc2f505184f4d07a321b77cdc8edd14a623305411dd9ae"
    protected open val androidArmv7: String = "0f3bbf9efeddc6743761607a9c638ee8d37878db7cbcaa6dbeb58c7dd9541c76"
    protected open val androidX86: String = "0836451571e186581c89c6fc0f07a0d87f3cefdd8faaa3751ebf04427514009e"
    protected open val androidX86_64: String = "078a7b5376a78cae340ff2e9802c2a88b00c9127f389e7e769cf87588246b2bf"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "854da603529c3424d94e7716c8735cd3003afd2b93b02c564d3f8cd818f1c7ef"
        override val androidArmv7: String = "f3a661d5fecde5f49a3acc98f9ac2ec405d2cef2b94111552699e3f350278ae6"
        override val androidX86: String = "73ad1b20723cab116812471da1d424002d246ca3cad32eeb6238aa522117885e"
        override val androidX86_64: String = "59580a0809e28fed16b83aeb924bf9368530d3308bf4dd020c369a6ed5ef36b4"

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
