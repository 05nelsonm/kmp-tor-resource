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
@file:Suppress("SpellCheckingInspection")

package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File
import javax.inject.Inject

/**
 * Resource validation and configuration for module `:library:resource-geoip`
 * */
abstract class GeoipResourceValidationExtension @Inject internal constructor(
    project: Project,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-geoip",
    packageName = "io.matthewnelson.kmp.tor.resource.geoip",
) {

    private val geoip: String = "e3873cefd2810175e9cffbf5b3b236a809c198c564cde81110bfb940b6accc37"
    private val geoip6: String = "9ac7c1e81c7483f288be894ff245134c49b7a5e06439d728622c00057ba81be0"

    fun jvmResourcesSrcDir(): File = jvmResourcesSrcDirProtected(reportName = "jvm-geoip")
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        ValidationHash.ResourceJvm(
            fileName = "geoip.gz",
            hash = geoip,
        ),
        ValidationHash.ResourceJvm(
            fileName = "geoip6.gz",
            hash = geoip6,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "native".toSourceSetName(),
            ktFileName = "resource_geoip_gz.kt",
            hash = geoip,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "native".toSourceSetName(),
            ktFileName = "resource_geoip6_gz.kt",
            hash = geoip6,
        ),
    ) }

    internal companion object {
        internal const val NAME = "geoipResourceValidation"
    }
}
