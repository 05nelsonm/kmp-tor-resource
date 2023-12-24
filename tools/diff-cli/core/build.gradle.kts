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
plugins {
    id("configuration")
}

kmpConfiguration {
    configure {
        jvm {
            target { withJava() }

            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8
        }

        js {
            target { nodejs {
                @Suppress("RedundantSamConstructor")
                testTask(Action { useMocha { timeout = "30s" } })
            } }

            sourceSetMain {
                dependencies {
                    implementation(libs.okio.node)
                }
            }
        }

        iosAll()
        linuxAll()
        macosAll()
        mingwAll()
        tvosAll()

        watchosArm32()
        watchosArm64()
        // Not supported by Okio
        // See https://github.com/square/okio/issues/1381
//        watchosDeviceArm64()
        watchosX64()
        watchosSimulatorArm64()

        common {
            sourceSetMain {
                dependencies {
                    implementation(libs.encoding.base16)
                    implementation(libs.encoding.base64)
                    implementation(libs.kotlincrypto.hash.sha2)
                    implementation(libs.kotlinx.datetime)
                    implementation(libs.okio.okio)
                }
            }

            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.okio.fakeFileSystem)
                }
            }
        }

        kotlin { explicitApi() }
    }
}
