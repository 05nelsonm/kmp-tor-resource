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

    private val geoip: String = "14c6007083094f59e240013d3b836bbe0bf453fdd1912a0811e93e73941e1794"
    private val geoip6: String = "88f8b47212786bc6997432222776c5debeaf0298b83b3b75ec26b5ebbde7041f"

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
