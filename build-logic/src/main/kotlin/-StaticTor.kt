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
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action

fun KmpConfigurationExtension.configureStatikTor(
    libs: LibrariesForLibs,
    isGpl: Boolean,
    action: Action<KmpConfigurationContainerDsl>,
) {
    val suffix = if (isGpl) "-gpl" else ""

    configureShared(
        androidNamespace = "io.matthewnelson.kmp.tor.resource.statik.tor",
        java9ModuleName = "io.matthewnelson.kmp.tor.resource.statik.tor",
        publish = true,
    ) {
        androidLibrary {
            sourceSetTestInstrumented {
                dependencies {
                    implementation(libs.androidx.test.core)
                    implementation(libs.androidx.test.runner)
                }
            }
        }

        common {
            sourceSetMain {
                dependencies {
                    api(libs.kmp.tor.common.api)
                    implementation(libs.kmp.tor.common.core)
                    implementation(project(":library:resource-shared-geoip"))
                }
            }
        }

        kotlin {
            sourceSets.findByName("jvmAndroidMain")?.dependencies {
                implementation(project(":library:resource-shared-tor$suffix"))
            }
        }

        action.execute(this)
    }
}
