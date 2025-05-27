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

            sourceSetMain {
                dependencies {

                }
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
                // -Wno-return-type
                compilerOptions.suppressWarnings.set(true)
            }
        }

        androidNativeArm32 { setup() }
        androidNativeArm64 { setup() }
        androidNativeX64 { setup() }
        androidNativeX86 { setup() }

        common {
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
            try {
                project.evaluationDependsOn(":library:resource-compilation-lib-tor")
            } catch (_: Throwable) {}

            project.afterEvaluate {
                val isUsingMocks = project(":library:resource-compilation-lib-tor")
                    .layout
                    .buildDirectory
                    .asFile.get()
                    .resolve("reports")
                    .resolve("resource-validation")
                    .resolve("resource-compilation-lib-tor")
                    .resolve("android.err")
                    .readText()
                    .isNotBlank()

                val srcDir = project
                    .layout
                    .buildDirectory
                    .asFile.get()
                    .resolve("generated")
                    .resolve("sources")
                    .resolve("buildConfig")
                    .resolve("androidInstrumentedTest")
                    .resolve("kotlin")

                val packageName = "io.matthewnelson.kmp.tor.resource.test.android.internal"
                var configDir = srcDir
                packageName.split('.').forEach { segment ->
                    configDir = configDir.resolve(segment)
                }
                configDir.mkdirs()
                configDir.resolve("TestBuildConfig.kt").writeText("""
                    package $packageName

                    internal const val IS_USING_MOCK_RESOURCES: Boolean = $isUsingMocks

                """.trimIndent())

                sourceSets.findByName("androidInstrumentedTest")?.kotlin?.srcDir(srcDir)
            }
        }

        kotlin { explicitApi() }
    }
}
