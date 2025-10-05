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

    protected open val androidAarch64: String = "493028924181038a11ebb2afaa3ba7d9675c08cb8ac3da635da4e190ab4f8ed8"
    protected open val androidArmv7: String = "dae382754cc1f95c583f45b839fe193025a8acc9e34ea5018410f9c7835c79f1"
    protected open val androidX86: String = "0f09e99120a7fd5080394f5a20fc44e94bb252f3f1685a12eeba560c64ad53e1"
    protected open val androidX86_64: String = "96c47d6f3004628ca06b3688c846a3c3f03657e6ba86cfb3143713d71741fc6d"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "af0e8011ea496963b0c60a5cd8af9e8e96fd102ae8687dbadfcc06718a8d82b1"
        override val androidArmv7: String = "db949952493910c5b4de502d6085ca4f7e55ac403537f266e74b68d06e4bb586"
        override val androidX86: String = "b28fd74d964cf242527d6b6775725a9b274ed98d359b12493f4237bdea4fcde8"
        override val androidX86_64: String = "0097ceca97cb6856feba34450f99891fd0cfd60b2d4b0db7df4f0436ffb48449"

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
