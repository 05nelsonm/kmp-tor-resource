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
import resource.validation.extensions.CompilationLibTorResourceValidationExtension
import resource.validation.extensions.LibTorResourceValidationExtension
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
                "wasmJs",
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
            with(sourceSets) {
                listOf(
                    "noExec" to libs.ktor.client.core,
                    "androidInstrumented" to libs.ktor.client.okhttp,
//                    "jvm" to libs.ktor.client.okhttp,
                    "linux" to libs.ktor.client.curl,
                    "macos" to libs.ktor.client.curl,
                    "mingw" to libs.ktor.client.winhttp,
                ).forEach { (name, ktorDependency) ->
                    findByName(name + "Test")?.dependencies {
                        implementation(ktorDependency)
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
                val noExecTest = findByName("noExecTest") ?: return@kotlin

                val buildConfigDir = generatedSourcesDir.resolve("buildConfig")

                fun KotlinSourceSet.generateBuildConfig(areErrorReportsEmpty: () -> Boolean?) {
                    val kotlinSrcDir = buildConfigDir
                        .resolve(this.name)
                        .resolve("kotlin")

                    this.kotlin.srcDir(kotlinSrcDir)

                    val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))

                    dir.mkdirs()

                    val sourceSet = this
                    project.afterEvaluate {
                        val areErrorReportsEmptyResult = areErrorReportsEmpty.invoke()

                        val lineRunFullTests = if (areErrorReportsEmptyResult == null) {
                            "internal expect val CAN_RUN_FULL_TESTS: Boolean"
                        } else {
                            "internal actual val CAN_RUN_FULL_TESTS: Boolean = $areErrorReportsEmptyResult"
                        }

                        @Suppress("DEPRECATION")
                        dir.resolve("BuildConfig${sourceSet.name.capitalized()}.kt").writeText("""
                            package $packageName
    
                            $lineRunFullTests
    
                        """.trimIndent())
                    }
                }

                // root
                noExecTest.generateBuildConfig(areErrorReportsEmpty = { null })

                val resourceValidationNoExec = noExecResourceValidation

                val resourceValidationLibTor = if (isGpl) {
                    LibTorResourceValidationExtension.GPL::class.java
                } else {
                    LibTorResourceValidationExtension::class.java
                }.let { project.extensions.getByType(it) }

                val resourceValidationCompilationLibTor = if (isGpl) {
                    CompilationLibTorResourceValidationExtension.GPL::class.java
                } else {
                    CompilationLibTorResourceValidationExtension::class.java
                }.let { project.extensions.getByType(it) }

                val reportAndroid by lazy {
                    val lib = resourceValidationCompilationLibTor.errorReportAndroidJniResources()
                    val noexec = resourceValidationNoExec.errorReportAndroidJniResources()
                    lib + noexec
                }

                val reportJvm by lazy {
                    val lib = resourceValidationLibTor.errorReportJvmNativeLibResources()
                    val noexec = resourceValidationNoExec.errorReportJvmNativeLibResources()
                    lib + noexec
                }

                listOf(
                    "androidInstrumented" to listOf { reportAndroid },
                    "androidNative" to listOf { reportAndroid },
                    // If no errors with JVM resources, then android-unit-test project
                    // dependency is not utilizing mock resources and can run tests.
                    "androidUnit" to listOf { reportJvm },
                    "jvm" to listOf { reportJvm },
                    "linuxArm64" to listOf { resourceValidationLibTor.errorReportNativeResource("linuxArm64") },
                    "linuxX64" to listOf { resourceValidationLibTor.errorReportNativeResource("linuxX64") },
                    "macosArm64" to listOf { resourceValidationLibTor.errorReportNativeResource("macosArm64") },
                    "macosX64" to listOf { resourceValidationLibTor.errorReportNativeResource("macosX64") },
                    "mingwX64" to listOf { resourceValidationLibTor.errorReportNativeResource("mingwX64") },
                    "iosArm64" to listOf { "false" /* No tests */ },
                    "iosSimulatorArm64" to listOf { resourceValidationLibTor.errorReportNativeResource("iosSimulatorArm64") },
                    "iosX64" to listOf { resourceValidationLibTor.errorReportNativeResource("iosX64") },
                ).forEach { (sourceSetName, reports) ->
                    val srcSetTest = findByName(sourceSetName + "Test") ?: return@forEach

                    srcSetTest.generateBuildConfig {
                        if (sourceSetName == "androidUnit" || sourceSetName == "jvm") {
                            if (!HostManager.hostIsMingw && JavaVersion.current() < JavaVersion.VERSION_16) {
                                // Java 15- tests for JVM on UNIX are very problematic due
                                // to Process and how the tests get run... test runner will
                                // exit exceptionally crying about memory issues, but it's
                                // actually OK.
                                return@generateBuildConfig false
                            }
                        }

                        val errors = reports.map { it.invoke() }
                        errors.joinToString("").indexOfFirst { !it.isWhitespace() } == -1
                    }
                }
            }
        }

        configureAndroidEnvironmentKeysConfig(project)
        configureAndroidNativeEmulatorTests(project)

        action.execute(this)
    }
}

// CKLib uses too old of a version of LLVM for current version of Kotlin which produces errors for android
// native due to unsupported link arguments. Below is a supplemental implementation to download and use
// the -dev llvm compiler for the current kotlin version.
//
// The following info can be found in ~/.konan/kotlin-native-prebuild-{os}-{arch}-{kotlin version}/konan/konan.properties
private object LLVM {
    const val URL: String = "https://download.jetbrains.com/kotlin/native/resources/llvm"
    const val VERSION: String = "16.0.0"

    // llvm-{llvm version}-{arch}-{host}-dev-{id}
    object DevID {
        object Linux {
            const val x86_64: Int = 80
        }
        object MacOS {
            const val aarch64: Int = 65
            const val x86_64: Int = 56
        }
        object MinGW {
            const val x86_64: Int = 56
        }
    }
}

private fun CKlibGradleExtension.configure(libs: LibrariesForLibs) {
    kotlinVersion = libs.versions.gradle.kotlin.get()
    check(kotlinVersion == "2.1.21") {
        "Kotlin version out of date! Download URLs for LLVM need to be updated for ${project.path}"
    }

    val host = HostManager.simpleOsName()
    val arch = HostManager.hostArch()
    val (id, archive) = when (host) {
        "linux" -> when (arch) {
            "x86_64" -> LLVM.DevID.Linux.x86_64 to ArchiveType.TAR_GZ
            else -> null
        }
        "macos" -> when (arch) {
            "aarch64" -> LLVM.DevID.MacOS.aarch64 to ArchiveType.TAR_GZ
            "x86_64" -> LLVM.DevID.MacOS.x86_64 to ArchiveType.TAR_GZ
            else -> null
        }
        "windows" -> when (arch) {
            "x86_64" -> LLVM.DevID.MinGW.x86_64 to ArchiveType.ZIP
            else -> null
        }
        else -> null
    } ?: throw TargetSupportException("Unsupported host[$host] or arch[$arch]")

    val llvmDev = "llvm-${LLVM.VERSION}-${arch}-${host}-dev-${id}"
    val cklibDir = File(System.getProperty("user.home")).resolve(".cklib")
    llvmHome = cklibDir.resolve(llvmDev).path

    val source = DependencySource.Remote.Public(subDirectory = "${LLVM.VERSION}-${arch}-${host}")

    DependencyProcessor(
        dependenciesRoot = cklibDir,
        dependenciesUrl = LLVM.URL,
        dependencyToCandidates = mapOf(llvmDev to listOf(source)),
        homeDependencyCache = cklibDir.resolve("cache"),
        customProgressCallback = { _, currentBytes, totalBytes ->
            val total = totalBytes.toString()
            var current = currentBytes.toString()
            while (current.length < 15 && current.length < total.length) {
                current = " $current"
            }

            println("Downloading[$llvmDev] - $current / $total")
        },
        archiveType = archive,
    ).run()
}
