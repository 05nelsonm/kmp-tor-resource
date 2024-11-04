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
import co.touchlab.cklib.gradle.CompileToBitcode
import co.touchlab.cklib.gradle.CompileToBitcodeExtension
import io.matthewnelson.kmp.configuration.extension.KmpConfigurationExtension
import io.matthewnelson.kmp.configuration.extension.container.target.KmpConfigurationContainerDsl
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.support.kotlinCompilerOptions
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget
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

    // Needed so that memory space is separated
    // when jni libs are loaded for Jvm/Android.
    project.tasks.withType<Test> {
        forkEvery = 1
//        environment["DYLD_PRINT_APIS"] = "1"
    }

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
            pluginIds("resource-validation", libs.plugins.cklib.get().pluginId)

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

                val buildDir = project.layout
                    .buildDirectory
                    .get()
                    .asFile

                val buildConfigDir = buildDir
                    .resolve("generated")
                    .resolve("sources")
                    .resolve("buildConfig")

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

                    // If no errors for JVM resources, then android-unit-test project
                    // dependency is not utilizing mock resources and can run tests.
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
                        if (isGpl && JavaVersion.current() < JavaVersion.VERSION_17) {
                            return@report false
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

            project.extensions.configure<CompileToBitcodeExtension>("cklib") {
                config.kotlinVersion = libs.versions.gradle.kotlin.get()

                create("kmp_tor") {
                    language = CompileToBitcode.Language.C
                    srcDirs = project.files(externalNativeDir)
                    headersDirs = project.files(externalNativeDir)
                    includeFiles = listOf("lib_load.c", "kmp_tor.c")
                }
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

                target.compilations["main"].cinterops.create("kmp_tor") {
                    if (isGpl) {
                        // Windows does not like symbolic links. Always use the real path.
                        project.projectDir.resolveSibling(project.name.substringBeforeLast("-gpl"))
                    } else {
                        project.projectDir
                    }.resolve("src")
                        .resolve("nativeInterop")
                        .resolve("cinterop")
                        .resolve("kmp_tor.def")
                        .let { definitionFile.set(it) }

                    includeDirs(externalNativeDir.path)
                }

                if (linkerOpts.isEmpty()) return@target

                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                target.compilerOptions.freeCompilerArgs.addAll("-linker-options", linkerOpts)
            }
        }

        action.execute(this)
    }
}
