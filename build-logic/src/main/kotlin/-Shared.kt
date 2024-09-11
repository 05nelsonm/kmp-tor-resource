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
import io.matthewnelson.kmp.configuration.ExperimentalKmpConfigurationApi
import io.matthewnelson.kmp.configuration.extension.KmpConfigurationExtension
import io.matthewnelson.kmp.configuration.extension.container.target.KmpConfigurationContainerDsl
import org.gradle.api.Action
import org.gradle.api.JavaVersion

fun KmpConfigurationExtension.configureShared(
    androidNamespace: String? = null,
    java9ModuleName: String? = null,
    publish: Boolean = false,
    excludeNative: Boolean = false,
    action: Action<KmpConfigurationContainerDsl>
) {
    if (publish) {
        require(!java9ModuleName.isNullOrBlank()) { "publications must specify a module-info name" }
    }

    configure {
        options {
            useUniqueModuleNames = true
        }

        if (androidNamespace != null) {
            androidLibrary(namespace = androidNamespace) {
                if (publish) target { publishLibraryVariants("release") }
            }
        }

        jvm {
            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8

            @OptIn(ExperimentalKmpConfigurationApi::class)
            java9ModuleInfoName = java9ModuleName
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

        if (!excludeNative) {
//            androidNativeAll()
            iosAll()
            linuxAll()
            macosAll()
            mingwAll()
//            tvosAll()
//            watchosAll()
        }

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
