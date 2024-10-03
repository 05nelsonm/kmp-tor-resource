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
import resource.validation.extensions.NoExecTorResourceValidationExtension
import java.io.File

fun KmpConfigurationExtension.configureNoExecTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-noexec-tor")) { "Invalid project" }

    val libs = project.the<LibrariesForLibs>()
    val isGpl = project.name.endsWith("gpl")
    val suffix = if (isGpl) "-gpl" else ""
    val packageName = "io.matthewnelson.kmp.tor.resource.noexec.tor"
    val noExecResourceValidation by lazy {
        if (isGpl) {
            NoExecTorResourceValidationExtension.GPL::class.java
        } else {
            NoExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configureShared(
        androidNamespace = packageName,
        java9ModuleName = packageName,
        publish = true,
    ) {
        androidLibrary {
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

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    api(libs.kmp.tor.common.api)
                }
            }
        }

        kotlin { noExecResourceValidation.configureNativeInterop() }

        sourceSetConnect(
            "loadable",
            listOf("jvmAndroid", "native"),
            sourceSetMain = {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                    implementation(project(":library:resource-geoip"))
                }
            },
            sourceSetTest = {
                dependencies {
                    implementation(libs.encoding.base16)
                }
            },
        )
        sourceSetConnect("nonLoadable", listOf("js"))
        sourceSetConnect(
            "nativeDesktop",
            listOf(
                "linux",
                "macos",
                "mingw",
            ),
            "loadable",
            sourceSetMain = {
                dependencies {
                    implementation(project(":library:resource-lib-tor$suffix"))
                }
            },
        )
        kotlin {
            sourceSets.findByName("jvmAndroidMain")?.dependencies {
                implementation(project(":library:resource-lib-tor$suffix"))
            }
        }

        kotlin {
            with(sourceSets) {
                val loadableTest = findByName("loadableTest") ?: return@with

                val buildDir = project.layout
                    .buildDirectory
                    .get()
                    .asFile

                val buildConfigDir = buildDir
                    .resolve("generated")
                    .resolve("sources")
                    .resolve("buildConfig")

                fun KotlinSourceSet.generateBuildConfig(isErrReportEmpty: Boolean?) {
                    val kotlinSrcDir = buildConfigDir
                        .resolve(this.name)
                        .resolve("kotlin")

                    val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))

                    dir.mkdirs()

                    val textRunFullTests = if (isErrReportEmpty == null) {
                        "internal expect val CAN_RUN_FULL_TESTS: Boolean"
                    } else {
                        "internal actual val CAN_RUN_FULL_TESTS: Boolean = $isErrReportEmpty"
                    }

                    dir.resolve("BuildConfig${this.name.capitalized()}.kt").writeText("""
                        package $packageName

                        $textRunFullTests

                    """.trimIndent())

                    this.kotlin.srcDir(kotlinSrcDir)
                }

                loadableTest.generateBuildConfig(isErrReportEmpty = null)

                val reportDirLibTor = project.rootDir
                    .resolve("library")
                    .resolve("resource-lib-tor$suffix")
                    .resolve("build")
                    .resolve("reports")
                    .resolve("resource-validation")
                    .resolve("resource-lib-tor$suffix")

                val reportDirNoExec = buildDir
                    .resolve("reports")
                    .resolve("resource-validation")
                    .resolve(project.name)

                listOf(
                    Triple("android", "androidInstrumented", reportDirLibTor),

                    // If no errors for JVM resources, then android-unit-test project
                    // dependency is not utilizing mock resources and can run tests.
                    Triple("jvm", "androidUnit", reportDirLibTor),

                    Triple("jvm", null, reportDirLibTor),
                    Triple("linuxArm64", null, reportDirLibTor),
                    Triple("linuxX64", null, reportDirLibTor),
                    Triple("macosArm64", null, reportDirLibTor),
                    Triple("macosX64", null, reportDirLibTor),
                    Triple("mingwX64", null, reportDirLibTor),

                    Triple("iosArm64", null, reportDirNoExec),
                    Triple("iosSimulatorArm64", null, reportDirNoExec),
                    Triple("iosX64", null, reportDirNoExec),
                ).forEach { (reportName, srcSetName, reportDir) ->
                    val srcSetTest = findByName("${srcSetName ?: reportName}Test") ?: return@forEach

                    val isErrReportEmpty = reportDir
                        .resolve("${reportName}.err")
                        .readText()
                        .indexOfFirst { !it.isWhitespace() } == -1

                    srcSetTest.generateBuildConfig(isErrReportEmpty = isErrReportEmpty)
                }
            }
        }

        action.execute(this)
    }
}
