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
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import resource.validation.extensions.CompilationExecTorResourceValidationExtension
import resource.validation.extensions.CompilationLibTorResourceValidationExtension
import resource.validation.extensions.ExecTorResourceValidationExtension
import resource.validation.extensions.LibTorResourceValidationExtension
import java.io.File

fun KmpConfigurationExtension.configureExecTor(
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

    val buildDir = project.layout
        .buildDirectory
        .get().asFile

    configureShared(
        androidNamespace = packageName,
        java9ModuleName = packageName,
        publish = true,
    ) {
        androidLibrary {
            android {
                packaging.jniLibs.useLegacyPackaging = true

                sourceSets["androidTest"].manifest.srcFile(
                    project.projectDir
                        .resolve("src")
                        .resolve("androidInstrumentedTest")
                        .resolve("AndroidManifest.xml")
                )
            }

            sourceSetMain {
                dependencies {
                    implementation(project(":library:resource-compilation-exec-tor$suffix"))
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
                "androidNative",
                "jvmAndroid",
                "js",
                "wasmJs",
                "linux",
                "macos",
                "mingw",
            ),
            sourceSetMain = {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                    implementation(project(":library:resource-geoip"))
                }
            },
        )
        sourceSetConnect(
            newName = "nonExec",
            existingNames = listOf(
                "ios",
            ),
        )
        sourceSetConnect(
            newName = "jvmJs",
            existingNames = listOf(
                "jvm",
                "js",
                "wasmJs",
            ),
            dependencyName = "exec",
            sourceSetMain = {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                }
            },
        )

        kotlin {
            with(sourceSets) {
                listOf(
                    "jvmAndroid",
                    "js",
                    "wasmJs",
                    "linux",
                    "macos",
                    "mingw",
                ).forEach { name ->
                    findByName(name + "Main")?.apply {
                        dependencies {
                            implementation(project(":library:resource-lib-tor$suffix"))
                        }
                    }
                }
            }
        }

        kotlin {
            with(sourceSets) {
                listOf(
                    "js",
                    "wasmJs",
                ).forEach { name ->
                    findByName(name + "Test")?.apply {
                        // Only add as test dependency. Consumers need to declare the npm dependency
                        // themselves when using `Node.js` because there is not currently a way to
                        // exclude a npm dependency for kotlin when packaging things on a per-platform
                        // basis.
                        dependencies {
                            implementation(npm("kmp-tor.resource-exec-tor${suffix}.all", project.npmVersion))
                        }
                    }
                }
            }
        }

        kotlin {
            with(sourceSets) {
                val execTest = findByName("execTest") ?: return@kotlin

                val buildConfigDir = buildDir
                    .resolve("generated")
                    .resolve("sources")
                    .resolve("buildConfig")

                fun KotlinSourceSet.generateBuildConfig(areErrorReportsEmpty: () -> Boolean?) {
                    val kotlinSrcDir = buildConfigDir
                        .resolve(this.name)
                        .resolve("kotlin")

                    this.kotlin.srcDir(kotlinSrcDir)

                    val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))

                    dir.mkdirs()

                    val testResourcesDir = buildDir
                        .resolve("test-resources")
                        .resolve(this.name)
                        .path
                        .replace("\\", "\\\\")

                    val sourceSet = this
                    project.afterEvaluate {
                        val areErrorReportsEmptyResult = areErrorReportsEmpty.invoke()

                        var lineIsGpl = ""
                        var lineRunFullTests = "internal actual val CAN_RUN_FULL_TESTS: Boolean = $areErrorReportsEmptyResult"
                        var lineTestDir = "internal actual val TEST_DIR: String = \"$testResourcesDir\""

                        if (areErrorReportsEmptyResult == null) {
                            lineIsGpl = "internal const val IS_GPL: Boolean = $isGpl"
                            lineRunFullTests = "internal expect val CAN_RUN_FULL_TESTS: Boolean"
                            lineTestDir = "internal expect val TEST_DIR: String"
                        }

                        if (sourceSet.name.startsWith("android")) {
                            lineTestDir = "internal actual val TEST_DIR: String = \"\""
                        }

                        @Suppress("DEPRECATION")
                        dir.resolve("BuildConfig${sourceSet.name.capitalized()}.kt").writeText("""
                            package $packageName
    
                            $lineIsGpl
                            $lineRunFullTests
                            $lineTestDir
    
                        """.trimIndent())
                    }
                }

                // root
                execTest.generateBuildConfig(areErrorReportsEmpty = { null })

                val resourceValidationExecTor = execResourceValidation

                val resourceValidationLibTor = if (isGpl) {
                    LibTorResourceValidationExtension.GPL::class.java
                } else {
                    LibTorResourceValidationExtension::class.java
                }.let { project.extensions.getByType(it) }

                val resourceValidationCompilationExecTor = if (isGpl) {
                    CompilationExecTorResourceValidationExtension.GPL::class.java
                } else {
                    CompilationExecTorResourceValidationExtension::class.java
                }.let { project.extensions.getByType(it) }

                val resourceValidationCompilationLibTor = if (isGpl) {
                    CompilationLibTorResourceValidationExtension.GPL::class.java
                } else {
                    CompilationLibTorResourceValidationExtension::class.java
                }.let { project.extensions.getByType(it) }

                val reportAndroid by lazy {
                    val exec = resourceValidationCompilationExecTor.errorReportAndroidJniResources()
                    val lib = resourceValidationCompilationLibTor.errorReportAndroidJniResources()
                    exec + lib
                }
                val reportJvm by lazy {
                    val exec = resourceValidationExecTor.errorReportJvmNativeLibResources()
                    val lib = resourceValidationLibTor.errorReportJvmNativeLibResources()
                    exec + lib
                }

                listOf(
                    "androidInstrumented" to { reportAndroid },
                    "androidNative" to { reportAndroid },

                    // If no errors for JVM resources, then android-unit-test project
                    // dependency and js is not utilizing mock resources and can run tests.
                    "androidUnit" to { reportJvm },
                    "js" to { reportJvm },
                    "wasmJs" to { reportJvm },

                    "jvm" to { reportJvm },
                    "linuxArm64" to null,
                    "linuxX64" to null,
                    "macosArm64" to null,
                    "macosX64" to null,
                    "mingwX64" to null,
                ).forEach { (sourceSetName, reports) ->
                    val srcSetTest = findByName(sourceSetName + "Test") ?: return@forEach

                    srcSetTest.generateBuildConfig {
                        val errors = reports?.invoke() ?: listOf(
                            resourceValidationExecTor.errorReportNativeResource(sourceSetName),
                            resourceValidationLibTor.errorReportNativeResource(sourceSetName),
                        ).joinToString("")

                        errors.indexOfFirst { !it.isWhitespace() } == -1
                    }
                }
            }
        }

        configureAndroidEnvironmentKeysConfig(project)
        configureAndroidNativeEmulatorTests(project)

        action.execute(this)
    }
}
