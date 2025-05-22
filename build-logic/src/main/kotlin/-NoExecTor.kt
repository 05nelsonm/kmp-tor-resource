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
import co.touchlab.cklib.gradle.CKlibGradleExtension
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
import org.jetbrains.kotlin.konan.target.TargetSupportException
import org.jetbrains.kotlin.konan.util.ArchiveType
import org.jetbrains.kotlin.konan.util.DependencyProcessor
import org.jetbrains.kotlin.konan.util.DependencySource
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

                defaultConfig {
                    consumerProguardFiles(
                        project.projectDir
                            .resolve("src")
                            .resolve("androidMain")
                            .resolve("resource_noexec_tor.pro")
                    )
                }

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
                "androidNative",
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
                listOf(
                    "jvmAndroid",
                    "iosSimulatorArm64",
                    "iosX64",
                    "linux",
                    "macos",
                    "mingw",
                ).forEach { target ->
                    findByName(target + "Main")?.dependencies {
                        implementation(project(":library:resource-lib-tor$suffix"))
                    }
                }
            }
        }

        kotlin {
            val externalNativeDir = project.rootDir
                .resolve("external")
                .resolve("native")

            val generatedNativeDir = generatedSourcesDir
                .resolve("native")
                .apply { mkdirs() }

            val cFiles = listOf("lib_load", "win32_sockets", "kmp_tor").map { name ->
                val sb = StringBuilder()
                sb.appendLine("package = $packageName.internal")
                sb.appendLine("headers = $name.h")
                sb.appendLine("headerFilter = $name.h")
                generatedNativeDir.resolve("$name.def").writeText(sb.toString())

                File("$name.c")
            }

            val interopTaskInfo = targets.filterIsInstance<KotlinNativeTarget>().map { target ->
                val linkerOpts = when (target.konanTarget.family) {
                    Family.IOS,
                    Family.LINUX,
                    Family.OSX -> "-lpthread -ldl"
                    Family.ANDROID -> "-pthread -ldl -llog"
                    Family.MINGW -> ""
                    else -> null
                }

                check(linkerOpts != null) { "Configuration needed for $target" }

                val interopTaskName = target.compilations["main"].cinterops.create("kmp_tor") {
                    defFile(generatedNativeDir.resolve("$name.def"))
                    includeDirs(externalNativeDir)
                }.interopProcessingTaskName

                if (target.konanTarget.family == Family.MINGW) {
                    target.compilations["test"].cinterops.create("win32_sockets") {
                        defFile(generatedNativeDir.resolve("$name.def"))
                        includeDirs(externalNativeDir)
                    }
                }

                if (linkerOpts.isNotBlank()) {
                    target.compilerOptions.freeCompilerArgs.addAll("-linker-options", linkerOpts)
                }

                interopTaskName to target.konanTarget
            }

            project.extensions.configure<CompileToBitcodeExtension> {
                config.configure(libs)

                create("kmp_tor") {
                    language = CompileToBitcode.Language.C
                    srcDirs = project.files(externalNativeDir)

                    val kt = KonanTarget.predefinedTargets[target]!!

                    cFiles.mapNotNull { cFile ->
                        val name = cFile.name
                        if (name == "win32_sockets.c" && kt.family != Family.MINGW) {
                            return@mapNotNull null
                        }
                        name
                    }.let { includeFiles = it }

                    if (kt.family.isAppleFamily) {
                        listOf(
                            "-Wno-unused-command-line-argument",
                        ).let { compilerArgs.addAll(it) }
                    }

                    // Ensure the CompileToBitcode task comes after cinterop task such
                    // that whatever sysroot dependencies are needed get downloaded
                    // and are available at time of execution.
                    interopTaskInfo.forEach { (interopTaskName, interopTarget) ->
                        if (interopTarget != kt) return@forEach
                        this.dependsOn(interopTaskName)
                    }
                }
            }
        }

        kotlin {
            with(sourceSets) {
                val noExecTest = findByName("noExecTest") ?: return@with

                try {
                    project.evaluationDependsOn(":library:resource-compilation-lib-tor$suffix")
                } catch (_: Throwable) {}
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

                val reportDirCompilationLibTor = project.rootDir
                    .resolve("library")
                    .resolve("resource-compilation-lib-tor$suffix")
                    .resolve("build")
                    .resolve("reports")
                    .resolve("resource-validation")
                    .resolve("resource-compilation-lib-tor$suffix")

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
                    Triple("android", "androidInstrumented", listOf(reportDirCompilationLibTor, reportDirNoExec)),
                    Triple("android", "androidNative", listOf(reportDirCompilationLibTor)),
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

        configureAndroidNativeEmulatorTests(project)

        action.execute(this)
    }
}

// CKLib uses too old of a version of LLVM for current version of Kotlin which produces errors for android
// native due to unsupported link arguments. Below is a supplemental implementation to download and use
// the -dev llvm compiler for the current kotlin version.
//
// The following info can be found in ~/.konan/konan-native-prebuild-{os}-{arch}-{kotlin version}/konan/konan.properties
private const val LLVM_VERSION: String = "16.0.0"
private const val LLVM_URL: String = "https://download.jetbrains.com/kotlin/native/resources/llvm"

private fun CKlibGradleExtension.configure(libs: LibrariesForLibs) {
    kotlinVersion = libs.versions.gradle.kotlin.get()
    if (kotlinVersion != "2.1.21") {
        project.logger.error("Kotlin version out of date! Download URLs for LLVM need to be updated for ${project.path}.")
    }

    val host = HostManager.simpleOsName()
    val arch = HostManager.hostArch()
    val (id, archive) = when (host) {
        "linux" -> when (arch) {
            "x86_64" -> 80 to ArchiveType.TAR_GZ
            else -> null
        }
        "macos" -> when (arch) {
            "aarch64" -> 65 to ArchiveType.TAR_GZ
            "x86_64" -> 56 to ArchiveType.TAR_GZ
            else -> null
        }
        "windows" -> when (arch) {
            "x86_64" -> 56 to ArchiveType.ZIP
            else -> null
        }
        else -> null
    } ?: throw TargetSupportException("Unsupported host[$host] or arch[$arch]")

    val llvmDev = "llvm-${LLVM_VERSION}-${arch}-${host}-dev-${id}"
    val cklibDir = File(System.getProperty("user.home")).resolve(".cklib")
    llvmHome = cklibDir.resolve(llvmDev).path

    val source = DependencySource.Remote.Public(subDirectory = "${LLVM_VERSION}-${arch}-${host}")

    DependencyProcessor(
        dependenciesRoot = cklibDir,
        dependenciesUrl = LLVM_URL,
        dependencyToCandidates = mapOf(llvmDev to listOf(source)),
        homeDependencyCache = cklibDir.resolve("cache"),
        customProgressCallback = { _, currentBytes, totalBytes ->
            val total = totalBytes.toString()
            var current = currentBytes.toString()
            while (current.length < 15 && current.length < total.length) { current = " $current" }

            println("Downloading[$llvmDev] - $current / $total")
        },
        archiveType = archive,
    ).run()
}
