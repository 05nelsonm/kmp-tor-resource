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
import resource.validation.extensions.LibTorResourceValidationExtension

fun KmpConfigurationExtension.configureLibTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-lib-tor")) { "Invalid project." }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val suffix = if (isGpl) "-gpl" else ""
    val libResourceValidation by lazy {
        if (isGpl) {
            LibTorResourceValidationExtension.GPL::class.java
        } else {
            LibTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configureShared(
        androidNamespace = "io.matthewnelson.kmp.tor.resource.lib.tor",
        java9ModuleName = "io.matthewnelson.kmp.tor.resource.lib.tor",
        excludeNative = true,
        publish = true,
    ) {
        androidLibrary {
            // TODO: Move to CompilationLibTor
            android { libResourceValidation.configureAndroidJniResources() }

            sourceSetMain {
                dependencies {
                    implementation(project(":library:resource-compilation-lib-tor$suffix"))
                }
            }
        }

        jvm {
            sourceSetMain {
                resources.srcDir(libResourceValidation.jvmNativeLibResourcesSrcDir())
            }
        }

        js {
            sourceSetMain {
                val srcDir = project.layout
                    .buildDirectory
                    .get()
                    .asFile
                    .resolve("generated")
                    .resolve("sources")
                    .resolve("buildConfig")
                    .resolve("jsMain")
                    .resolve("kotlin")

                val configDir = srcDir
                    .resolve("io")
                    .resolve("matthewnelson")
                    .resolve("kmp")
                    .resolve("tor")
                    .resolve("resource")
                    .resolve("lib")
                    .resolve("tor")
                    .resolve("internal")

                configDir.mkdirs()

                configDir.resolve("BuildConfigJS.kt").writeText("""
                    package io.matthewnelson.kmp.tor.resource.lib.tor.internal

                    internal const val IS_GPL: Boolean = $isGpl

                """.trimIndent())

                kotlin.srcDir(srcDir)
            }
        }

        // Simulator only
        iosSimulatorArm64()
        iosX64()

        linuxAll()
        macosAll()
        mingwAll()

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                }
            }
        }

        kotlin { libResourceValidation.configureNativeResources(this) }

        sourceSetConnect(
            newName = "nonNative",
            existingNames = listOf(
                "jvmAndroid",
                "js",
            ),
        )

        action.execute(this)
    }
}
