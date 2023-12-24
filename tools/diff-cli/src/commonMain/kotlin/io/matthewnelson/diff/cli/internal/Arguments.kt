/*
 * Copyright (c) 2023 Matthew Nelson
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
package io.matthewnelson.diff.cli.internal

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.DefaultRequiredType
import kotlinx.cli.SingleArgument

internal interface ArgDiffDir {
    val diffDirArg: String

    companion object {
        internal const val NAME_ARG = "diff-dir"

        internal fun ArgParser.diffDirArgument(
            description: String
        ): SingleArgument<String, DefaultRequiredType.Required> {
            return argument(
                type = ArgType.String,
                fullName = NAME_ARG,
                description = description
            )
        }
    }
}

internal interface ArgDiffFile {
    val diffFileArg: String

    companion object {
        internal const val NAME_ARG = "diff-file"

        internal fun ArgParser.diffFileArgument(
            description: String
        ): SingleArgument<String, DefaultRequiredType.Required> {
            return argument(
                type = ArgType.String,
                fullName = NAME_ARG,
                description = description
            )
        }
    }
}
