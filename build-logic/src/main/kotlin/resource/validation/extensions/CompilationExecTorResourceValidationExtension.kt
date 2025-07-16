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
@file:Suppress("PropertyName", "PrivatePropertyName", "SpellCheckingInspection")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-compilation-exec-tor`
 *
 * @see [GPL]
 * */
open class CompilationExecTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-compilation-exec-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.compilation.exec.tor",
) {

    @Inject
    @Suppress("unused")
    internal constructor(project: Project): this(project, isGpl = false)

    private val androidAarch64: String = "8a4eb402a872f21fdb6de60b59cc783503a1f49555c381e48fcb32a11a8f73c9"
    private val androidArmv7: String = "3172cf145ed7a4060f7fc20c1ee9463224fb564b9f674df1fef9b4c3a5d72796"
    private val androidX86: String = "9c919fb4aa36c391d6aa64ac67ced5b220392597eb40d565456353edcc45c8bd"
    private val androidX86_64: String = "d556c33cb1d6ffeb951c6aaea441b80cf804c207e2728d3b3b47b99b3c78fd1c"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationExecTorResourceValidationExtension(project, isGpl = true) {

        internal companion object {
            internal const val NAME = "compilationExecTorGPLResourceValidation"
        }
    }

    fun configureAndroidJniResources() { configureLibAndroidProtected() }
    fun errorReportAndroidJniResources(): String = errorReportLibAndroidProtected()

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // android
        ValidationHash.LibAndroid(
            libname = "libtorexec.so",
            hashArm64 = androidAarch64,
            hashArmv7 = androidArmv7,
            hashX86 = androidX86,
            hashX86_64 = androidX86_64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "compilationExecTorResourceValidation"
    }
}
