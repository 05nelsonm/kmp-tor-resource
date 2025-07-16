/*
 * Copyright (c) 2025 Matthew Nelson
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
import com.android.build.gradle.tasks.MergeSourceSetFolders
import io.matthewnelson.kmp.configuration.extension.container.target.TargetAndroidNativeContainer
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import resource.validation.extensions.CompilationLibTorResourceValidationExtension
import resource.validation.extensions.NoExecTorResourceValidationExtension

plugins {
    id("configuration")
}

kmpConfiguration {
    configure {
        val jniLibsDir = projectDir
            .resolve("src")
            .resolve("androidInstrumentedTest")
            .resolve("jniLibs")

        project.tasks.all {
            if (name != "clean") return@all
            jniLibsDir.deleteRecursively()
        }

        androidLibrary(namespace = "io.matthewnelson.kmp.tor.resource.test.android") {
            android {
                packaging.jniLibs.useLegacyPackaging = false
                sourceSets["androidTest"].jniLibs.srcDir(jniLibsDir)
            }

            sourceSetTestInstrumented {
                dependencies {
                    implementation(libs.androidx.test.runner)
                    implementation(kotlin("test"))
                }
            }
        }

        fun <T: KotlinNativeTarget> TargetAndroidNativeContainer<T>.setup() {
            target {
                binaries {
                    sharedLib {
                        baseName = "testjni"
                    }
                }
            }
        }

        androidNativeArm32 { setup() }
        androidNativeArm64 { setup() }
        androidNativeX64 { setup() }
        androidNativeX86 { setup() }

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    implementation(project(":library:resource-noexec-tor"))
                }
            }
        }

        kotlin {
            if (!project.plugins.hasPlugin("com.android.base")) return@kotlin

            val buildDir = project
                .layout
                .buildDirectory
                .asFile.get()

            val nativeBinaryTasks = listOf(
                "Arm32" to "armeabi-v7a",
                "Arm64" to "arm64-v8a",
                "X64" to "x86_64",
                "X86" to "x86",
            ).mapNotNull { (arch, abi) ->
                val nativeBinaryTask = project
                    .tasks
                    .findByName("androidNative${arch}MainBinaries")
                    ?: return@mapNotNull null

                val abiDir = jniLibsDir.resolve(abi)
                if (!abiDir.exists() && !abiDir.mkdirs()) throw RuntimeException("mkdirs[$abiDir]")

                val lib = buildDir
                    .resolve("bin")
                    .resolve("androidNative${arch}")
                    .resolve("releaseShared")
                    .resolve("libtestjni.so")

                nativeBinaryTask.doLast {
                    lib.copyTo(abiDir.resolve(lib.name), overwrite = true)
                }

                nativeBinaryTask
            }

            project.tasks.withType(MergeSourceSetFolders::class.java).all {
                if (name != "mergeDebugAndroidTestJniLibFolders") return@all
                nativeBinaryTasks.forEach { task -> this.dependsOn(task) }
            }
        }

        kotlin {
            val androidInstrumentedTest = sourceSets.findByName("androidInstrumentedTest") ?: return@kotlin

            val resourceValidationCompilationLibTor = project
                .extensions
                .getByType(CompilationLibTorResourceValidationExtension::class.java)
            val resourceValidationNoExec = project
                .extensions
                .getByType(NoExecTorResourceValidationExtension::class.java)

            val reportAndroid by lazy {
                val lib = resourceValidationCompilationLibTor.errorReportAndroidJniResources()
                val noexec = resourceValidationNoExec.errorReportAndroidJniResources()
                lib + noexec
            }

            val kotlinSrcDir = project
                .layout
                .buildDirectory
                .asFile.get()
                .resolve("generated")
                .resolve("sources")
                .resolve("buildConfig")
                .resolve("androidInstrumentedTest")
                .resolve("kotlin")

            val packageName = "io.matthewnelson.kmp.tor.resource.test.android.internal"
            val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))
            dir.mkdirs()

            project.afterEvaluate {
                val areErrorReportsEmpty = reportAndroid.indexOfFirst { !it.isWhitespace() } == -1

                dir.resolve("TestBuildConfig.kt").writeText("""
                    package $packageName

                    internal const val IS_USING_MOCK_RESOURCES: Boolean = ${!areErrorReportsEmpty}

                """.trimIndent())
            }

            androidInstrumentedTest.kotlin.srcDir(kotlinSrcDir)
        }

        kotlin { explicitApi() }
    }
}
