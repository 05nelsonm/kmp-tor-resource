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
import org.gradle.kotlin.dsl.the
import resource.validation.extensions.CompilationLibTorResourceValidationExtension

fun KmpConfigurationExtension.configureCompilationLibTor(
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-compilation-lib-tor")) { "Invalid project." }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val libResourceValidation by lazy {
        if (isGpl) {
            CompilationLibTorResourceValidationExtension.GPL::class.java
        } else {
            CompilationLibTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configure {
        options {
            useUniqueModuleNames = true
        }

        androidLibrary(namespace = "io.matthewnelson.kmp.tor.resource.compilation.lib.tor") {
            target { publishLibraryVariants("release") }

            android { libResourceValidation.configureAndroidJniResources() }

            sourceSetMain {
                dependencies {
                    implementation(libs.androidx.startup.runtime)
                }
            }

            sourceSetTestInstrumented {
                dependencies {
                    implementation(libs.androidx.test.runner)
                }
            }
        }

        common {
            pluginIds("publication", "resource-validation")

            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        kotlin { explicitApi() }

        configureAndroidEnvironmentKeysConfig(project)

        action.execute(this)
    }
}
