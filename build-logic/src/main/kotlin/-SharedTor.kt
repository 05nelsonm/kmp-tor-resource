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

fun KmpConfigurationExtension.configureSharedTor(
    libs: LibrariesForLibs,
    isGpl: Boolean,
    action: Action<KmpConfigurationContainerDsl>,
) {
    configureShared(
        androidNamespace = "io.matthewnelson.kmp.tor.resource.shared.tor",
        java9ModuleName = "io.matthewnelson.kmp.tor.resource.shared.tor",
        excludeNative = true,
        publish = true,
    ) {
        common {
            sourceSetMain {
                dependencies {
                    implementation(libs.kmp.tor.common.core)
                }
            }
        }

        action.execute(this)
    }
}
