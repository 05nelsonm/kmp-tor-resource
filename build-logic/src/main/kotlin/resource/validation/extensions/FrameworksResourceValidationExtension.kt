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

        const val HASH_IOS_LIBTOR: String = "31775d26efc33fb5824256cca01da6634ebbf8f4cf0fe0fc91c0bfe3cfedee3c"
        const val HASH_IOS_LIBTOR_GPL: String = "26695599125db9f23a0701b7a35f1b719c638a3fb1b19502d45647f7cd2a7882"
    }
}
