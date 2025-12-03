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

    private val geoip: String = "4546bac6df1c8f4ea7015d234c5c4a740d05e9bc4403cce93deae362f8accc68"
    private val geoip6: String = "61fba3c06f01fef036176794c69c0fddd82ed19559e02b1c8458566241e955ff"

    fun jvmResourcesSrcDir(): File = jvmResourcesSrcDirProtected(reportName = "jvm-geoip")
    fun errorReportJvmResources(): String = errorReportJvmResourcesProtected()
    fun configureNativeResources(kmp: KotlinMultiplatformExtension) { configureNativeResourcesProtected(kmp) }
    fun errorReportNativeResources(): String = errorReportNativeResourceProtected(sourceSet = "native")

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
