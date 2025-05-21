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
import io.matthewnelson.kmp.configuration.extension.KmpConfigurationExtension
import io.matthewnelson.kmp.configuration.extension.container.target.KmpConfigurationContainerDsl
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import resource.validation.extensions.CompilationExecTorResourceValidationExtension

fun KmpConfigurationExtension.configureCompilationExecTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-compilation-exec-tor")) { "Invalid project." }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val suffix = if (isGpl) "-gpl" else ""
    val execResourceValidation by lazy {
        if (isGpl) {
            CompilationExecTorResourceValidationExtension.GPL::class.java
        } else {
            CompilationExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configure {
        options {
            useUniqueModuleNames = true
        }

        androidLibrary(namespace = "io.matthewnelson.kmp.tor.resource.compilation.exec.tor") {
            target { publishLibraryVariants("release") }

            android { execResourceValidation.configureAndroidJniResources() }

            sourceSetMain {
                dependencies {
                    // So that Android Native KmpTorLibLocator gets initialized with
                    // its environment variable for ApplicationInfo.nativeLibraryDir
                    implementation(libs.kmp.tor.common.lib.locator)
                    implementation(project(":library:resource-compilation-lib-tor$suffix"))
                }
            }
        }

        common { pluginIds("publication", "resource-validation") }

        action.execute(this)
    }
}
