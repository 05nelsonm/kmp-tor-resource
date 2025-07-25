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
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.konan.target.HostManager

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

            // windows always throws a fit if not using Java 11. This disables
            // compilations of module-info.java. Nobody deploys from Windows
            // anyway...
            if (!HostManager.hostIsMingw) {
                java9ModuleInfoName = java9ModuleName
            }
        }

        @Suppress("RedundantSamConstructor")
        js {
            target {
                browser {
                    testTask(Action {
                        isEnabled = false
                    })
                }
                nodejs {
                    testTask(Action {
                        useMocha { timeout = "30s" }
                    })
                }
            }
        }

        // TODO: Update Kotlin to 2.2.20
        //  See https://youtrack.jetbrains.com/issue/KT-77443
//        @Suppress("RedundantSamConstructor")
//        @OptIn(ExperimentalWasmDsl::class)
//        wasmJs {
//            target {
//                browser {
//                    testTask(Action {
//                        isEnabled = false
//                    })
//                }
//                nodejs()
//            }
//        }

        if (!excludeNative) {
            androidNativeAll()
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
