/*
 * Copyright (c) 2025 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.filterjar

import io.matthewnelson.filterjar.FilterJarExtension
import io.matthewnelson.filterjar.FilterJarPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Applies the [FilterJarPlugin]
 *
 * @see [KmpTorResourceFilterJarExtension]
 * */
public open class KmpTorResourceFilterJarPlugin internal constructor(): Plugin<Project> {

    public final override fun apply(target: Project) {
        target.plugins.apply("io.matthewnelson.filterjar")
        target.extensions.create(
            KmpTorResourceFilterJarExtension.NAME,
            KmpTorResourceFilterJarExtension::class.java,
            target.extensions.getByType(FilterJarExtension::class.java),
        )
    }
}
