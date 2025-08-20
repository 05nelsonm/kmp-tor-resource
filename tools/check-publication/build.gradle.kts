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
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

plugins {
    id("configuration")
}

repositories {
    if (version.toString().endsWith("-SNAPSHOT")) {
        maven("https://central.sonatype.com/repository/maven-snapshots/")
    } else {
        maven("https://central.sonatype.com/api/v1/publisher/deployments/download/") {
            val p = rootProject.properties
            authentication.create("Authorization", HttpHeaderAuthentication::class.java)
            credentials(HttpHeaderCredentials::class.java) {
                val username = p["mavenCentralUsername"]?.toString() ?: throw NullPointerException()
                val password = p["mavenCentralPassword"]?.toString() ?: throw NullPointerException()
                name = "Authorization"
                @OptIn(ExperimentalEncodingApi::class)
                value = "Bearer " + Base64.Mime.encode("$username:$password".encodeToByteArray())
            }
        }
    }
}

kmpConfiguration {
    configureShared(androidNamespace = "tools.check.publication") {
        androidLibrary {
            sourceSetMain {
                dependencies {
                    implementation("$group:resource-compilation-exec-tor:$version")
                    implementation("$group:resource-compilation-exec-tor-gpl:$version")
                    implementation("$group:resource-compilation-lib-tor:$version")
                    implementation("$group:resource-compilation-lib-tor-gpl:$version")

                    // Should be a SEPARATE publication from -jvm
                    implementation("$group:resource-lib-tor-android:$version")
                    implementation("$group:resource-lib-tor-gpl-android:$version")
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
                    implementation("$group:resource-lib-tor-jvm:$version")
                    implementation("$group:resource-lib-tor-gpl-jvm:$version")
                }
            }
        }

        js {
            sourceSetMain {
                dependencies {
                    implementation("$group:resource-lib-tor-js:$version")
                    implementation("$group:resource-lib-tor-gpl-js:$version")
                    implementation(npm("kmp-tor.resource-exec-tor.all", project.npmVersion))
                    implementation(npm("kmp-tor.resource-exec-tor-gpl.all", project.npmVersion))
                }
            }
        }

        common {
            sourceSetMain {
                dependencies {
                    implementation("$group:resource-geoip:$version")
                    implementation("$group:resource-exec-tor:$version")
                    implementation("$group:resource-exec-tor-gpl:$version")
                    implementation("$group:resource-noexec-tor:$version")
                    implementation("$group:resource-noexec-tor-gpl:$version")
                }
            }
        }

        sourceSetConnect(
            "libTor",
            listOf(
                "jvmAndroid",
                "js",
                "iosSimulatorArm64",
                "iosX64",
                "linux",
                "macos",
                "mingw",
            ),
            sourceSetMain = {
                dependencies {
                    implementation("$group:resource-lib-tor:$version")
                    implementation("$group:resource-lib-tor-gpl:$version")
                }
            },
        )
    }
}
