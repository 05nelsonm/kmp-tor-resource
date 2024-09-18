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
package resource.validation.extensions.internal

import java.io.File

@JvmInline
internal value class SourceSetName private constructor(val value: String) {

    internal val main: String get() = "${value}Main"
    internal val test: String get() = "${value}Test"

    internal fun sourceSetDir(moduleDir: File, isMain: Boolean = true): File {
        val name = if (isMain) main else test
        return moduleDir.resolve("src").resolve(name)
    }

    internal companion object {

        @Throws(IllegalArgumentException::class)
        internal fun String.toSourceSetName(): SourceSetName {
            require(
                !endsWith("Main", true)
                && !endsWith("Test", true)
            ) {
                "$this cannot end with Main or Test"
            }

            return SourceSetName(this)
        }
    }
}

@JvmInline
internal value class ERROR private constructor(private val value: String) {

    internal companion object {

        internal fun String.toERROR(): ERROR = ERROR(this)
    }

    override fun toString(): String = "ERROR[ $value ]"
}
