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
@file:Suppress("UNUSED")

package resource.validation

import org.gradle.api.Plugin
import org.gradle.api.Project
import resource.validation.extensions.*

open class ResourceValidationPlugin internal constructor(): Plugin<Project> {
    override fun apply(target: Project) {
        listOf(
            CompilationExecTorResourceValidationExtension.NAME to CompilationExecTorResourceValidationExtension::class.java,
            CompilationExecTorResourceValidationExtension.GPL.NAME to CompilationExecTorResourceValidationExtension.GPL::class.java,
            CompilationLibTorResourceValidationExtension.NAME to CompilationLibTorResourceValidationExtension::class.java,
            CompilationLibTorResourceValidationExtension.GPL.NAME to CompilationLibTorResourceValidationExtension.GPL::class.java,
            ExecTorResourceValidationExtension.NAME to ExecTorResourceValidationExtension::class.java,
            ExecTorResourceValidationExtension.GPL.NAME to ExecTorResourceValidationExtension.GPL::class.java,
            FrameworksResourceValidationExtension.NAME to FrameworksResourceValidationExtension::class.java,
            GeoipResourceValidationExtension.NAME to GeoipResourceValidationExtension::class.java,
            LibTorResourceValidationExtension.NAME to LibTorResourceValidationExtension::class.java,
            LibTorResourceValidationExtension.GPL.NAME to LibTorResourceValidationExtension.GPL::class.java,
            NoExecTorResourceValidationExtension.NAME to NoExecTorResourceValidationExtension::class.java,
            NoExecTorResourceValidationExtension.GPL.NAME to NoExecTorResourceValidationExtension.GPL::class.java,
        ).forEach { (name, clazz) ->
            target.extensions.create(name, clazz, target)
        }
    }
}
