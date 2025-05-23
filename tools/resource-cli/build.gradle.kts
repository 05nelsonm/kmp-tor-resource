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
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("configuration")
}

kmpConfiguration {
    configure {
        jvm {
            target {
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                mainRun {
                    mainClass.set("io.matthewnelson.resource.cli.MainKt")
                }
            }

            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8
        }

        common {
            sourceSetMain {
                dependencies {
                    implementation(libs.clikt)
                    implementation(libs.encoding.base16)
                    implementation(libs.encoding.base64)
                    implementation(kotlincrypto.hash.sha2)
                }
            }
            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        kotlin { explicitApi() }
    }
}
