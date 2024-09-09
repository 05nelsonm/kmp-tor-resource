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
import resource.validation.extensions.SharedTorResourceValidationExtension

fun KmpConfigurationExtension.configureSharedTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-shared-tor")) { "Invalid project." }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val resourceValidation by lazy {
        if (isGpl) {
            SharedTorResourceValidationExtension.GPL::class.java
        } else {
            SharedTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configureShared(
        androidNamespace = "io.matthewnelson.kmp.tor.resource.shared.tor",
        java9ModuleName = "io.matthewnelson.kmp.tor.resource.shared.tor",
        excludeNative = true,
        publish = true,
    ) {
        androidLibrary {
            android {
                resourceValidation.configureAndroidJniResources()
            }
        }

        jvm {
            sourceSetMain {
                resources.srcDir(resourceValidation.jvmNativeLibResourcesSrcDir())
            }
        }

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                }
            }
        }

        action.execute(this)
    }
}
