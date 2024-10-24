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
import io.matthewnelson.kmp.configuration.extension.container.target.TargetAndroidContainer
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import resource.validation.extensions.ExecTorResourceValidationExtension
import resource.validation.extensions.LibTorResourceValidationExtension
import resource.validation.extensions.NoExecTorResourceValidationExtension

fun KmpConfigurationContainerDsl.androidLibrary(
    namespace: String,
    buildTools: String? = "34.0.0",
    compileSdk: Int = 34,
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
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }
        }

        kotlinJvmTarget = javaVersion
        compileSourceCompatibility = javaVersion
        compileTargetCompatibility = javaVersion

        action?.execute(this)
    }
}

fun KmpConfigurationExtension.configureAndroidUnitTestTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-android-unit-test-tor")) { "Invalid project" }

    val isGpl = project.name.endsWith("gpl")
    val libResourceValidation by lazy {
        if (isGpl) {
            LibTorResourceValidationExtension.GPL::class.java
        } else {
            LibTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }
    val execResourceValidation by lazy {
        if (isGpl) {
            ExecTorResourceValidationExtension.GPL::class.java
        } else {
            ExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }
    val noExecResourceValidation by lazy {
        if (isGpl) {
            NoExecTorResourceValidationExtension.GPL::class.java
        } else {
            NoExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configure {
        options {
            useUniqueModuleNames = true
        }

        androidLibrary(namespace = "io.matthewnelson.kmp.tor.resource.android.unit.test.tor") {
            target { publishLibraryVariants("release") }

            android {
                sourceSets.getByName("main").resources {
                    srcDir(libResourceValidation.jvmNativeLibResourcesSrcDir())
                    srcDir(execResourceValidation.jvmNativeLibResourcesSrcDir())
                    srcDir(noExecResourceValidation.jvmNativeLibResourcesSrcDir())
                }
            }
        }

        common { pluginIds("publication", "resource-validation") }

        action.execute(this)
    }
}
