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
package resource.validation

import org.gradle.api.Plugin
import org.gradle.api.Project
import resource.validation.extensions.ExecTorResourceValidationExtension
import resource.validation.extensions.GeoipResourceValidationExtension
import resource.validation.extensions.LibTorResourceValidationExtension

open class ResourceValidationPlugin internal constructor(): Plugin<Project> {
    override fun apply(target: Project) {
        listOf(
            GeoipResourceValidationExtension.NAME to GeoipResourceValidationExtension::class.java,
            LibTorResourceValidationExtension.NAME to LibTorResourceValidationExtension::class.java,
            LibTorResourceValidationExtension.GPL.NAME to LibTorResourceValidationExtension.GPL::class.java,
            ExecTorResourceValidationExtension.NAME to ExecTorResourceValidationExtension::class.java,
            ExecTorResourceValidationExtension.GPL.NAME to ExecTorResourceValidationExtension.GPL::class.java,
        ).forEach { (name, clazz) ->
            target.extensions.create(name, clazz, target)
        }
    }
}
