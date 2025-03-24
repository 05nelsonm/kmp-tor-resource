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
import resource.validation.extensions.internal.ValidationHash
import java.io.File

open class FrameworksResourceValidationExtension internal constructor(
    project: Project,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-frameworks-gradle-plugin",
    packageName = "io.matthewnelson.kmp.tor.resource.frameworks"
) {

    fun jvmNativeLibResourcesSrcDir(): File = jvmNativeLibsResourcesSrcDirProtected()

    override val hashes: Set<ValidationHash> = setOf(
        ValidationHash.LibJvm(
            osName = "tor",
            arch = "ios",
            libName = "LibTor",
            hash = HASH_IOS_LIBTOR,
        ),
        ValidationHash.LibJvm(
            osName = "tor",
            osSubtype = "gpl",
            arch = "ios",
            libName = "LibTor",
            hash = HASH_IOS_LIBTOR_GPL,
        ),
    )

    companion object {
        internal const val NAME = "frameworksResourceValidation"

        const val HASH_IOS_LIBTOR: String = "298b908878b3329440e1c6ca474acfac01761387b5103ffb4a21ba9de4d953dd"
        const val HASH_IOS_LIBTOR_GPL: String = "d06d852c7b02c0d53bb2522922ec95f43b086479a838d79ef6280023c3c4a81f"
    }
}
