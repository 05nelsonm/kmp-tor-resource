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
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import resource.validation.extensions.ExecTorResourceValidationExtension
import java.io.File

fun KmpConfigurationExtension.configureExecTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-exec-tor")) { "Invalid project" }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val suffix = if (isGpl) "-gpl" else ""
    val packageName = "io.matthewnelson.kmp.tor.resource.exec.tor"
    val execResourceValidation by lazy {
        if (isGpl) {
            ExecTorResourceValidationExtension.GPL::class.java
        } else {
            ExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configureShared(
        androidNamespace = packageName,
        java9ModuleName = packageName,
        publish = true,
    ) {
        androidLibrary {
            android { execResourceValidation.configureAndroidJniResources() }

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

        jvm {
            sourceSetMain {
                resources.srcDir(execResourceValidation.jvmNativeLibResourcesSrcDir())
            }
        }

        js {
            // Only add as test dependency. Consumers need to declare the npm dependency
            // themselves when using `Node.js` because there is not currently a way to
            // exclude a npm dependency for kotlin when packaging things on a per-platform
            // basis.
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
                }
            }
        }

        kotlin { execResourceValidation.configureNativeResources(this) }

        sourceSetConnect(
            newName = "exec",
            existingNames = listOf(
                "jvmAndroid",
                "js",
                "linux",
                "macos",
                "mingw",
            ),
            sourceSetMain = {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                    implementation(project(":library:resource-geoip"))
                    implementation(project(":library:resource-lib-tor$suffix"))
                }
            },
        )
        sourceSetConnect(
            newName = "nonExec",
            existingNames = listOf(
                "ios",
            ),
        )

        kotlin {
            with(sourceSets) {
                val execTest = findByName("execTest") ?: return@with

                val buildDir = project.layout
                    .buildDirectory
                    .get()
                    .asFile

                val buildConfigDir = buildDir
                    .resolve("generated")
                    .resolve("sources")
                    .resolve("buildConfig")

                fun KotlinSourceSet.generateBuildConfig(areErrReportsEmpty: () -> Boolean?) {
                    val sourceSet = this
                    val kotlinSrcDir = buildConfigDir
                        .resolve(this.name)
                        .resolve("kotlin")

                    val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))

                    dir.mkdirs()

                    val testResourcesDir = buildDir
                        .resolve("test-resources")
                        .resolve(this.name)
                        .path
                        .replace("\\", "\\\\")

                    // Cannot read reports until after resource-lib-tor has been evaluated.
                    val writeReport = {
                        val areErrReportsEmptyResult = areErrReportsEmpty.invoke()

                        var lineRunFullTests =
                            "internal actual val CAN_RUN_FULL_TESTS: Boolean = $areErrReportsEmptyResult"
                        var lineTestDir = "internal actual val TEST_DIR: String = \"$testResourcesDir\""

                        if (areErrReportsEmptyResult == null) {
                            lineRunFullTests = "internal expect val CAN_RUN_FULL_TESTS: Boolean"
                            lineTestDir = "internal expect val TEST_DIR: String"
                        }

                        dir.resolve("BuildConfig${sourceSet.name.capitalized()}.kt").writeText("""
                            package $packageName
    
                            $lineRunFullTests
                            $lineTestDir
    
                        """.trimIndent())
                    }

                    this.kotlin.srcDir(kotlinSrcDir)

                    try {
                        writeReport.invoke()
                    } catch (_: Throwable) {
                        Thread {
                            writeReport.invoke()
                        }.start()
                    }
                }

                execTest.generateBuildConfig(areErrReportsEmpty = { null })

                val reportDirLibTor = project.rootDir
                    .resolve("library")
                    .resolve("resource-lib-tor$suffix")
                    .resolve("build")
                    .resolve("reports")
                    .resolve("resource-validation")
                    .resolve("resource-lib-tor$suffix")

                val reportDirExec = buildDir
                    .resolve("reports")
                    .resolve("resource-validation")
                    .resolve(project.name)

                listOf(
                    "android" to "androidInstrumented",

                    // If no errors for JVM resources, then android-unit-test project
                    // dependency and js is not utilizing mock resources and can run tests.
                    "jvm" to "androidUnit",
                    "jvm" to "js",

                    "jvm" to null,
                    "linuxArm64" to null,
                    "linuxX64" to null,
                    "macosArm64" to null,
                    "macosX64" to null,
                    "mingwX64" to null,
                ).forEach { (reportName, srcSetName) ->
                    val srcSetTest = findByName("${srcSetName ?: reportName}Test") ?: return@forEach

                    val areErrReportsEmpty = {
                        val reportLibTor = reportDirLibTor
                            .resolve("${reportName}.err")
                            .readText()
                        val reportExec = reportDirExec
                            .resolve("${reportName}.err")
                            .readText()

                        (reportLibTor + reportExec).indexOfFirst { !it.isWhitespace() } == -1
                    }

                    srcSetTest.generateBuildConfig(areErrReportsEmpty = areErrReportsEmpty)
                }
            }
        }

        action.execute(this)
    }
}
