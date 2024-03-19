/*
 * Copyright (c) 2021 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
plugins {
    id("configuration")
    id("resource-validation")
}

kmpConfiguration {
    configureShared(
        androidNamespace = "io.matthewnelson.kmp.tor.resource.tor",
        java9ModuleName = "io.matthewnelson.kmp.tor.resource.tor",
        publish = true,
    ) {

        androidLibrary {
            android {

                torGPLResourceValidation.configureTorAndroidJniResources()

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                sourceSets.getByName("main") {
                    resources.srcDirs(torGPLResourceValidation.jvmGeoipResourcesSrcDir)
                }
            }

            sourceSetMain {
                dependencies {
                    implementation(libs.kmp.tor.core.lib.locator)
                }
            }

            sourceSetTest {
                dependencies {
                    implementation(project(":library:resource-android-unit-test"))
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
                resources.srcDir(torGPLResourceValidation.jvmGeoipResourcesSrcDir)
                resources.srcDir(torGPLResourceValidation.jvmTorLibResourcesSrcDir)
            }
        }

        js {
            configureTorNpmResources(project)
        }

        common {
            sourceSetMain {
                dependencies {
                    implementation(libs.kmp.tor.core.resource)
                    api(libs.kmp.tor.core.api)
                }
            }

            sourceSetTest {
                dependencies {
                    implementation(libs.encoding.base16)
                }
            }
        }

        kotlin {
            with(sourceSets) {
                val jsMain = findByName("jsMain")
                val jvmAndroidMain = findByName("jvmAndroidMain")

                if (jsMain != null || jvmAndroidMain != null) {
                    val nonNativeMain = maybeCreate("nonNativeMain")
                    nonNativeMain.dependsOn(getByName("commonMain"))
                    jvmAndroidMain?.apply { dependsOn(nonNativeMain) }
                    jsMain?.apply { dependsOn(nonNativeMain) }

                    val nonNativeTest = maybeCreate("nonNativeTest")
                    nonNativeTest.dependsOn(getByName("commonTest"))
                    findByName("jvmAndroidTest")?.apply { dependsOn(nonNativeTest) }
                    findByName("jsTest")?.apply { dependsOn(nonNativeTest) }
                }
            }

            torGPLResourceValidation.configureTorNativeResources()
        }
    }
}
