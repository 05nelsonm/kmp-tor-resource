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
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager
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
    val buildDir = project.layout
        .buildDirectory
        .get()
        .asFile
    val generatedSourcesDir = buildDir
        .resolve("generated")
        .resolve("sources")

    // TODO: CKLIB 0.3.3 remove
    //  Current version of cklib being utilized cannot be
    //  used on Windows, and cannot currently update to
    //  latest version (0.3.3) because it uses Kotlin 2.0.0.
    val useCKLib = !HostManager.hostIsMingw

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

            // TODO: CKLIB 0.3.3 remove if statement
            if (useCKLib) {
                pluginIds(libs.plugins.cklib.get().pluginId)
            }

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
            val externalNativeDir = project.rootDir
                .resolve("external")
                .resolve("native")

            val generatedNativeDir = generatedSourcesDir.resolve("native")
            generatedNativeDir.mkdirs()

            val files = listOf("lib_load", "win32_sockets", "kmp_tor").map { name ->
                val sb = StringBuilder()
                sb.appendLine("package = $packageName.internal")

                // TODO: CKLIB 0.3.3 remove
                if (!useCKLib) {
                    val h = externalNativeDir.resolve("$name.h")
                    val c = externalNativeDir.resolve("$name.c")

                    generatedNativeDir.resolve("$name.h").writeBytes(h.readBytes())

                    sb.apply {
                        appendLine("---")
                        append(c.readText())
                    }

                    val defFile = generatedNativeDir.resolve("$name.def")
                    defFile.writeText(sb.toString())
                    return@map defFile
                }

                sb.appendLine("headers = $name.h")
                sb.appendLine("headerFilter = $name.h")
                generatedNativeDir.resolve("$name.def").writeText(sb.toString())

                return@map File("$name.c")
            }

            // TODO: CKLIB 0.3.3 remove if statement
            if (useCKLib) {
                project.extensions.configure<CompileToBitcodeExtension> {
                    config.kotlinVersion = libs.versions.gradle.kotlin.get()

                    create("kmp_tor") {
                        language = CompileToBitcode.Language.C
                        srcDirs = project.files(externalNativeDir)

                        val kt = KonanTarget.predefinedTargets[target]!!

                        files.mapNotNull { cFile ->
                            val name = cFile.name
                            if (name == "win32_sockets.c" && kt.family != Family.MINGW) {
                                return@mapNotNull null
                            }
                            name
                        }.let { includeFiles = it }
                    }
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

                val compilationMain = target.compilations["main"]

                // TODO: CKLIB 0.3.3 remove if statement
                if (useCKLib) {
                    compilationMain.cinterops.create("kmp_tor") {
                        defFile(generatedNativeDir.resolve("$name.def"))
                        includeDirs(externalNativeDir)
                    }

                    if (target.konanTarget.family == Family.MINGW) {
                        target.compilations["test"].cinterops.create("win32_sockets") {
                            defFile(generatedNativeDir.resolve("$name.def"))
                            includeDirs(externalNativeDir)
                        }
                    }
                } else {
                    // TODO: CKLIB 0.3.3 remove
                    files.forEach interop@{ defFile ->
                        if (defFile.name == "win32_sockets.def") {
                            if (target.konanTarget.family != Family.MINGW) {
                                return@interop
                            }
                        }

                        compilationMain.cinterops.create(defFile.nameWithoutExtension) {
                            defFile(defFile)
                            includeDirs(generatedNativeDir)
                        }
                    }
                }

                if (linkerOpts.isBlank()) return@target

                target.compilerOptions.freeCompilerArgs.addAll("-linker-options", linkerOpts)
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

                        @Suppress("DEPRECATION")
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

        action.execute(this)
    }
}
