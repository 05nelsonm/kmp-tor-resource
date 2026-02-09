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

    protected open val androidAarch64: String = "945845bb428c7f680de91aef97770f252729273247f3bf3a238207b09980360a"
    protected open val androidArmv7: String = "ad48bcb6dc97c8a2e68c00e34395819656c0b49734028fb719f94da8facdef6e"
    protected open val androidX86: String = "9f7a0f5577d5e376cfebe2c4f2bc07017780d46e9b0236cf690d031b60a8846a"
    protected open val androidX86_64: String = "58d7a67c8399d5fd29b1cd0cac33ec31172d76df66f0ff0262693bb5937dfbe6"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "a6254b832fea9306864e7c68231156309616f5609a5296280ffdaa9f46bde7dd"
        override val androidArmv7: String = "4c48f1f225f3725ead59086d6beef750a037b7e1bf3be6ffc8b0abf83c5cddee"
        override val androidX86: String = "a4a1e4b8872fc6bc0c15e202e84ae23e72155ff955de441651b1dd9bb9e95ecb"
        override val androidX86_64: String = "f5243dc9e2e39f2759d94b66bcf4720fd04dc1b700d5941938e995fed75c23c8"

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
