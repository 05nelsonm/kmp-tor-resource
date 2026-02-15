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
@file:Suppress("PropertyName")

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

    protected open val androidAarch64: String = "05b8764a7f9b5f91f26f8ee9152d4e0c2c23d9eb09bee6719b54e3052f0e8b95"
    protected open val androidArmv7: String = "ef837d396e28f6dd1c7430f55eb69c53c597dce335ddfcad497ed53f0645d67b"
    protected open val androidX86: String = "e56897e78950b65c10e5bb6a8a860f514f727ac316aca54e8ed224693bc2549e"
    protected open val androidX86_64: String = "cdb7f7229bbb4493894915b5865f19aec1f6b56cc84acccc7f578103d96d8672"

    /**
     * Resource validation and configuration for module `:library:resource-compilation-lib-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): CompilationLibTorResourceValidationExtension(project, isGpl = true) {

        override val androidAarch64: String = "305cea732494a87afd2954ea8c38f29ecd02f4e2c55bf1d007eb022bb5bee7e4"
        override val androidArmv7: String = "752d8b6558f26769d600979e218ef3a611ff397ea514d5e83c15594785c5456d"
        override val androidX86: String = "a22ccc7a03c9f2e7160ff6b56156d90306513b68a3326f0ae765454b7b5b2d13"
        override val androidX86_64: String = "d4563f603277f3c8a5dbd70cf6f7162446940be55271176e8f9c1d87e089d246"

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
