/*
 * Copyright (c) 2023 Matthew Nelson
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
import io.matthewnelson.kmp.configuration.extension.container.target.TargetAndroidContainer
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

fun KmpConfigurationExtension.configureShared(
    androidNamespace: String? = null,
    publish: Boolean = false,
    action: Action<KmpConfigurationContainerDsl>
) {
    configure {
        if (androidNamespace != null) {
            androidLibrary(namespace = androidNamespace) {
                if (publish) target { publishLibraryVariants("release") }
            }
        }

        jvm {
            if (androidNamespace == null) target { withJava() }

            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8
        }

        js {
            target {
                nodejs {
                    @Suppress("RedundantSamConstructor")
                    testTask(Action {
                        useMocha { timeout = "30s" }
                    })
                }
            }
        }

//        iosAll()
        linuxAll()
        macosAll()
        mingwAll()
//        tvosAll()
//        watchosAll()

        common {
            if (publish) pluginIds("publication")

            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        kotlin { explicitApi() }

        action.execute(this)
    }
}

fun KmpConfigurationContainerDsl.androidLibrary(
    namespace: String,
    buildTools: String? = "33.0.2",
    compileSdk: Int = 33,
    minSdk: Int = 21,
    javaVersion: JavaVersion = JavaVersion.VERSION_1_8,
    action: (Action<TargetAndroidContainer.Library>)? = null,
) {
    androidLibrary {
        android {
            buildTools?.let { buildToolsVersion = it }
            this.compileSdk = compileSdk
            this.namespace = namespace

            defaultConfig {
                this.minSdk = minSdk

                testInstrumentationRunnerArguments["disableAnalytics"] = true.toString()
            }
        }

        kotlinJvmTarget = javaVersion
        compileSourceCompatibility = javaVersion
        compileTargetCompatibility = javaVersion

        action?.execute(this)
    }
}

fun KmpConfigurationExtension.configureTool(
    project: Project,
    mainKtPath: String,
    enableNative: Boolean = true,
    action: Action<KmpConfigurationContainerDsl>
) {
    val (entryJvm, entryNative) = if (mainKtPath.isEmpty()) {
        Pair("MainKt", "main")
    } else {
        Pair("$mainKtPath.MainKt", "$mainKtPath.main")
    }

    configure {
        jvm {
            target {
                withJava()

                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                mainRun {
                    mainClass.set(entryJvm)
                }
            }

            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8
        }

        if (enableNative) {
            fun KotlinNativeTarget.setup() { binaries { executable { entryPoint = entryNative } } }

            val X86 = "x86"
            val X64 = "x64"
            val ARM64 = "arm64"

            val arch = when (System.getProperty("os.arch")) {
                X86 -> X86
                "i386" -> X86
                "i486" -> X86
                "i586" -> X86
                "i686" -> X86
                "pentium" -> X86

                X64 -> X64
                "x86_64" -> X64
                "amd64" -> X64
                "em64t" -> X64
                "universal" -> X64

                ARM64 -> ARM64
                "aarch64" -> ARM64
                else -> null
            }

            val os = org.gradle.internal.os.OperatingSystem.current()
            val targetName = project.name

            when {
                os.isLinux -> {
                    when (arch) {
                        ARM64 -> linuxArm64(targetName) { target { setup() } }
                        X64 -> linuxX64(targetName) { target { setup() } }
                    }
                }
                os.isMacOsX -> {
                    when (arch) {
                        ARM64 -> macosArm64(targetName) { target { setup() } }
                        X64 -> macosX64(targetName) { target { setup() } }
                    }
                }
                os.isWindows -> {
                    when (arch) {
                        X64 -> mingwX64(targetName) { target { setup() } }
                    }
                }
            }
        }

        common {
            sourceSetMain {
                dependencies {
                    implementation(project(":tools:cli-core"))
                }
            }

            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        kotlin { explicitApi() }

        action.execute(this)
    }
}
