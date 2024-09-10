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
import resource.validation.extensions.ExecTorResourceValidationExtension

fun KmpConfigurationExtension.configureExecTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-exec-tor")) { "Invalid project" }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val suffix = if (isGpl) "-gpl" else ""
    val resourceValidation by lazy {
        if (isGpl) {
            ExecTorResourceValidationExtension.GPL::class.java
        } else {
            ExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configureShared(
        androidNamespace = "io.matthewnelson.kmp.tor.resource.exec.tor",
        java9ModuleName = "io.matthewnelson.kmp.tor.resource.exec.tor",
        publish = true,
    ) {
        androidLibrary {
            sourceSetMain {
                dependencies {
                    implementation(libs.kmp.tor.common.lib.locator)
                }
            }

            sourceSetTest {
                dependencies {
                    implementation(project(":library:resource-android-unit-test-tor$suffix"))
                }
            }

            sourceSetTestInstrumented {
                dependencies {
                    implementation(libs.androidx.test.core)
                    implementation(libs.androidx.test.runner)
                }
            }
        }

        js {
            // Only as test dependency. Consumers need to declare the npm dependency themselves
            // when using `Node.js` because there is not currently a way to express exclusions
            // for the individual platform publications (which `.all` provides).
            sourceSetTest {
                dependencies {
                    implementation(npm("kmp-tor.resource-exec-tor${suffix}.all", project.npmVersion))
                }
            }
        }

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    api(libs.kmp.tor.common.api)
                    implementation(libs.kmp.tor.common.core)
                    implementation(project(":library:resource-shared-geoip"))
                }
            }
        }

        nonNative {
            dependencies {
                implementation(project(":library:resource-shared-tor$suffix"))
            }
        }

        kotlin { resourceValidation.configureNativeResources() }

        action.execute(this)
    }
}
