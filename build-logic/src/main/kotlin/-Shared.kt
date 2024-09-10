/*
 * Copyright (c) 2023 Matthew Nelson
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
import io.matthewnelson.kmp.configuration.ExperimentalKmpConfigurationApi
import io.matthewnelson.kmp.configuration.extension.KmpConfigurationExtension
import io.matthewnelson.kmp.configuration.extension.container.target.KmpConfigurationContainerDsl
import io.matthewnelson.kmp.configuration.extension.container.target.TargetJsContainer
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun KmpConfigurationExtension.configureShared(
    androidNamespace: String? = null,
    java9ModuleName: String? = null,
    publish: Boolean = false,
    excludeNative: Boolean = false,
    action: Action<KmpConfigurationContainerDsl>
) {
    if (publish) {
        require(!java9ModuleName.isNullOrBlank()) { "publications must specify a module-info name" }
    }

    configure {
        options {
            useUniqueModuleNames = true
        }

        if (androidNamespace != null) {
            androidLibrary(namespace = androidNamespace) {
                if (publish) target { publishLibraryVariants("release") }
            }
        }

        jvm {
            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8

            @OptIn(ExperimentalKmpConfigurationApi::class)
            java9ModuleInfoName = java9ModuleName
        }

        js {
            target {
                nodejs {
                    @Suppress("RedundantSamConstructor")
                    testTask(Action {
                        useMocha { timeout = "30s" }
                    })
                }
            }
        }

        if (!excludeNative) {
//            androidNativeAll()
            iosAll()
            linuxAll()
            macosAll()
            mingwAll()
//            tvosAll()
//            watchosAll()
        }

        common {
            if (publish) pluginIds("publication")

            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        kotlin { explicitApi() }

        action.execute(this)
    }
}

// TODO: Move to plugin
fun TargetJsContainer.configureTorNpmResources(project: Project) {
    // Will either be `kmp-tor-resource-tor` or `kmp-tor-resource-tor-gpl`
    val moduleName = "kmp-tor-${project.name}"

    sourceSetMain {
        val kotlinSrcDir = project.layout
            .buildDirectory
            .get()
            .asFile
            .resolve("generated")
            .resolve("sources")
            .resolve("buildConfig")
            .resolve("jsMain")
            .resolve("kotlin")

        val config = kotlinSrcDir
            .resolve("io")
            .resolve("matthewnelson")
            .resolve("kmp")
            .resolve("tor")
            .resolve("resource")
            .resolve("tor")
            .resolve("internal")

        config.mkdirs()

        config.resolve("BuildConfigJs.kt").writeText(
"""package io.matthewnelson.kmp.tor.resource.tor.internal

internal const val MODULE_NAME: String = "$moduleName"
"""
        )

        kotlin.srcDir(kotlinSrcDir)

        dependencies {
            val npmVersion = if ("${project.version}".endsWith("-SNAPSHOT")) {
                val snapshotVersion = project.properties["NPMJS_SNAPSHOT_VERSION"]
                    .toString()
                    .toInt()

                "${project.version}.$snapshotVersion"
            } else {
                // If project version is not SNAPSHOT, this
                // will inhibit releasing to MavenCentral w/o
                // first making a release publication to
                // Npmjs of the resources.
                "${project.version}"
            }

            implementation(npm(moduleName, npmVersion))
        }
    }
}
