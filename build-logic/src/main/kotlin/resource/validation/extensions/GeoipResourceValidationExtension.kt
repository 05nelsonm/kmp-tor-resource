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

    private val geoip: String = "bd1c82818f527ddff496bd6ccf9b2fd584315d2710f0e9ff0fdbafa1f4657d6f"
    private val geoip6: String = "933c7e3417cbc06db4b66e8d53d03f793c5bf762764e25322f2de473e83c16bf"

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
