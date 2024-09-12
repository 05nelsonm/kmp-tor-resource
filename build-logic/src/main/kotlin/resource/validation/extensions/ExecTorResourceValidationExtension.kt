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
package resource.validation.extensions

import org.gradle.api.Project
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import javax.inject.Inject

/**
 * Resoure validation and configuration for module `:library:resource-exec-tor`
 *
 * @see [GPL]
 * */
open class ExecTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-exec-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.exec.tor",
) {

    @Inject
    internal constructor(project: Project): this(project, isGpl = false)

    protected open val nativeLinuxArm64: String = "49ad1505c66ea17eccdc48761e406eef75a4cc5575cd19d48e7d183f1faf9d0d"
    protected open val nativeLinuxX64: String = "89fbc41597ec2e4ee57bf7239fdb6ade034aa5188322d0e61bd3dc67bf3a5ecf"
    protected open val nativeMacosArm64: String = "d0ccb9ce411d9a46d841f78da519580a676669743b7817f57255dea305eacc48"
    protected open val nativeMacosX64: String = "cc6edc2cfac738b1480235fb882fec009555e5edae048f515924586ca4d93412"
    protected open val nativeMingwX64: String = "6fb9d8e0637deb072c58b768a23720671bbff06aa3a6592206da80da401e3078"

    /**
     * Resoure validation and configuration for module `:library:resource-exec-tor-gpl`,
     * `tor` compiled with `--enable-gpl`.
     * */
    abstract class GPL @Inject internal constructor(
        project: Project,
    ): ExecTorResourceValidationExtension(project, isGpl = true) {

        override val nativeLinuxArm64: String = "c8492f1e2bdbbe25d8b9787fd571d17d557ff5c157dca7db5dc3b2bb181185ec"
        override val nativeLinuxX64: String = "fd3e615d9191df8fa453343752918e8c1243966bc2cff6f63f178dd76a0c5829"
        override val nativeMacosArm64: String = "08545a904e10f64123280f808b5a75c1f31134aa73117c3e9aceb92a7bac697b"
        override val nativeMacosX64: String = "afd8e39c12045c243352c49a39ee00b581a2b1626edcfdc5e2f9cd11cc50cadf"
        override val nativeMingwX64: String = "82334cc857d82913d464487e5c4071e8f647535254746aab70a8e2257853e34c"

        internal companion object {
            internal const val NAME = "execTorGPLResourceValidation"
        }
    }

    fun configureNativeResources() { configureNativeResourcesProtected() }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        // linux
        ValidationHash.ResourceNative(
            sourceSetName = "linuxArm64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeLinuxArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "linuxX64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeLinuxX64,
        ),

        // macos
        ValidationHash.ResourceNative(
            sourceSetName = "macosArm64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeMacosArm64,
        ),
        ValidationHash.ResourceNative(
            sourceSetName = "macosX64".toSourceSetName(),
            ktFileName = "resource_tor_gz.kt",
            hash = nativeMacosX64,
        ),

        // mingw
        ValidationHash.ResourceNative(
            sourceSetName = "mingwX64".toSourceSetName(),
            ktFileName = "resource_tor_exe_gz.kt",
            hash = nativeMingwX64,
        ),
    ) }

    internal companion object {
        internal const val NAME = "execTorResourceValidation"
    }
}
