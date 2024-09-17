/*
 * Copyright (c) 2023 Matthew Nelson
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
}

repositories {
    val host = "https://s01.oss.sonatype.org"

    if (version.toString().endsWith("-SNAPSHOT")) {
        maven("$host/content/repositories/snapshots/")
    } else {
        maven("$host/content/groups/staging") {
            val p = rootProject.properties

            credentials {
                username = p["mavenCentralUsername"]?.toString()
                password = p["mavenCentralPassword"]?.toString()
            }
        }
    }
}

kmpConfiguration {
    configureShared(androidNamespace = "tools.check.publication") {
        androidLibrary {
            sourceSetMain {
                dependencies {
                    // Should be a SEPARATE publication from -jvm
                    implementation("$group:resource-shared-tor-android:$version")
                    implementation("$group:resource-shared-tor-gpl-android:$version")
                }
            }
            sourceSetTest {
                dependencies {
                    implementation("$group:resource-android-unit-test-tor:$version")
                    implementation("$group:resource-android-unit-test-tor-gpl:$version")
                }
            }
        }

        jvm {
            sourceSetMain {
                dependencies {
                    implementation("$group:resource-shared-tor-jvm:$version")
                    implementation("$group:resource-shared-tor-gpl-jvm:$version")
                }
            }
        }

        js {
            sourceSetMain {
                dependencies {
                    implementation("$group:resource-shared-tor-js:$version")
                    implementation("$group:resource-shared-tor-gpl-js:$version")
                    implementation(npm("kmp-tor.resource-exec-tor.all", project.npmVersion))
                    implementation(npm("kmp-tor.resource-exec-tor-gpl.all", project.npmVersion))
                }
            }
        }

        common {
            sourceSetMain {
                dependencies {
                    implementation("$group:resource-shared-geoip:$version")
                    implementation("$group:resource-exec-tor:$version")
                    implementation("$group:resource-exec-tor-gpl:$version")
                    implementation("$group:resource-noexec-tor:$version")
                    implementation("$group:resource-noexec-tor-gpl:$version")
                }
            }
        }

        sourceSetConnect("nonNative", listOf("jvmAndroid", "js")) {
            dependencies {
                implementation("$group:resource-shared-tor:$version")
                implementation("$group:resource-shared-tor-gpl:$version")
            }
        }
    }
}
