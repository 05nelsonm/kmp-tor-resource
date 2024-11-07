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
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager
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
    val buildDir = project.layout
        .buildDirectory
        .get()
        .asFile
    val generatedSourcesDir = buildDir
        .resolve("generated")
        .resolve("sources")

    configureShared(
        androidNamespace = packageName,
        java9ModuleName = packageName,
        publish = true,
    ) {
        androidLibrary {
            android {
                noExecResourceValidation.configureAndroidJniResources()

                sourceSets["androidTest"].manifest.srcFile(
                    project.projectDir
                        .resolve("src")
                        .resolve("androidInstrumentedTest")
                        .resolve("AndroidManifest.xml")
                )
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
                resources.srcDir(noExecResourceValidation.jvmNativeLibResourcesSrcDir())
            }
        }

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    api(libs.kmp.tor.common.api)
                }
            }

            sourceSetTest {
                dependencies {
                    implementation(libs.kotlinx.coroutines.test)
                }
            }
        }

        sourceSetConnect(
            newName = "noExec",
            existingNames = listOf(
                "jvmAndroid",
                "native",
            ),
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
        sourceSetConnect(
            newName = "nonNoExec",
            existingNames = listOf(
                "js",
            ),
        )
        sourceSetConnect(
            newName = "appleFramework",
            existingNames = listOf(
                "iosArm64",
            ),
            dependencyName = "native",
        )
        sourceSetConnect(
            newName = "nonAppleFramework",
            existingNames = listOf(
                "iosSimulatorArm64",
                "iosX64",
                "linux",
                "macos",
                "mingw",
            ),
            dependencyName = "native",
        )
        kotlin {
            with(sourceSets) {
                listOf("jvmAndroid", "nonAppleFramework").forEach { name ->
                    findByName(name + "Main")?.dependencies {
                        implementation(project(":library:resource-lib-tor$suffix"))
                    }
                }
            }
        }

        kotlin {
            with(sourceSets) {
                val noExecTest = findByName("noExecTest") ?: return@with

                try {
                    project.evaluationDependsOn(":library:resource-lib-tor$suffix")
                } catch (_: Throwable) {}

                val buildConfigDir = generatedSourcesDir.resolve("buildConfig")

                fun KotlinSourceSet.generateBuildConfig(isErrReportEmpty: () -> Boolean?) {
                    val kotlinSrcDir = buildConfigDir
                        .resolve(this.name)
                        .resolve("kotlin")

                    this.kotlin.srcDir(kotlinSrcDir)

                    val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))

                    dir.mkdirs()

                    val writeReport = {
                        val isErrReportEmptyResult = isErrReportEmpty.invoke()

                        val textRunFullTests = if (isErrReportEmptyResult == null) {
                            "internal expect val CAN_RUN_FULL_TESTS: Boolean"
                        } else {
                            "internal actual val CAN_RUN_FULL_TESTS: Boolean = $isErrReportEmptyResult"
                        }

                        dir.resolve("BuildConfig${this.name.capitalized()}.kt").writeText("""
                            package $packageName
    
                            $textRunFullTests
    
                        """.trimIndent())
                    }

                    project.afterEvaluate { writeReport.invoke() }
                }

                noExecTest.generateBuildConfig(isErrReportEmpty = { null })

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
                    Triple("android", "androidInstrumented", listOf(reportDirLibTor, reportDirNoExec)),
                    Triple("jvm", "androidUnit", listOf(reportDirLibTor, reportDirNoExec)),
                    Triple("jvm", null, listOf(reportDirLibTor, reportDirNoExec)),
                    Triple("linuxArm64", null, listOf(reportDirLibTor)),
                    Triple("linuxX64", null, listOf(reportDirLibTor)),
                    Triple("macosArm64", null, listOf(reportDirLibTor)),
                    Triple("macosX64", null, listOf(reportDirLibTor)),
                    Triple("mingwX64", null, listOf(reportDirLibTor)),

                    Triple("iosArm64", null, emptyList()),
                    Triple("iosSimulatorArm64", null, listOf(reportDirLibTor)),
                    Triple("iosX64", null, listOf(reportDirLibTor)),
                ).forEach { (reportName, srcSetName, reportDirs) ->
                    val srcSetTest = findByName("${srcSetName ?: reportName}Test") ?: return@forEach

                    val isErrReportEmpty = report@ {
                        if (reportName == "jvm" && !HostManager.hostIsMingw) {
                            if (JavaVersion.current() < JavaVersion.VERSION_16) {
                                // Java 15- tests for JVM on UNIX are very problematic due
                                // to Process and how the tests get run... test runner will
                                // exit exceptionally crying about memory issues, but it's
                                // actually OK.
                                return@report false
                            }
                        }

                        var hasError = reportDirs.isEmpty()
                        reportDirs.forEach readReport@{ dir ->
                            if (hasError) return@readReport
                            hasError = dir
                                .resolve("${reportName}.err")
                                .readText()
                                .indexOfFirst { !it.isWhitespace() } == -1
                        }
                        hasError
                    }

                    srcSetTest.generateBuildConfig(isErrReportEmpty = isErrReportEmpty)
                }
            }
        }

        kotlin {
            val externalNativeDir = project.rootDir
                .resolve("external")
                .resolve("native")

            val nativeDir = generatedSourcesDir.resolve("native")
            nativeDir.mkdirs()

            val defFiles = listOf("lib_load", "win32_sockets", "kmp_tor").map { name ->
                val h = externalNativeDir.resolve("$name.h")
                val c = externalNativeDir.resolve("$name.c")

                nativeDir.resolve("$name.h").writeBytes(h.readBytes())

                val sb = StringBuilder().apply {
                    appendLine("package = io.matthewnelson.kmp.tor.resource.noexec.tor.internal")
                    appendLine("headers = $name.h")
                    appendLine("headerFilter = $name.h")
                    appendLine("---")
                    append(c.readText())
                }

                val defFile = nativeDir.resolve("$name.def")
                defFile.writeText(sb.toString())
                defFile
            }

            targets.filterIsInstance<KotlinNativeTarget>().forEach target@ { target ->
                val linkerOpts = when (target.konanTarget.family) {
                    Family.LINUX,
                    Family.IOS,
                    Family.OSX -> "-lpthread -ldl"
                    Family.MINGW -> ""
                    else -> null
                }

                check(linkerOpts != null) { "Configuration needed for $target" }

                defFiles.forEach interop@ { defFile ->
                    if (defFile.name == "win32_sockets.def") {
                        if (target.konanTarget.family != Family.MINGW) {
                            return@interop
                        }
                    }

                    target.compilations["main"].cinterops.create(defFile.nameWithoutExtension) {
                        definitionFile.set(defFile)
                        includeDirs(nativeDir)
                    }
                }

                if (linkerOpts.isEmpty()) return@target

                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                target.compilerOptions.freeCompilerArgs.addAll("-linker-options", linkerOpts)
            }
        }

        action.execute(this)
    }
}
