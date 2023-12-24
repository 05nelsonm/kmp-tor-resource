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
package io.matthewnelson.diff.cli.internal.create

import io.matthewnelson.diff.cli.internal.*
import io.matthewnelson.diff.cli.internal.ArgDiffDir
import io.matthewnelson.diff.cli.internal.ArgDiffDir.Companion.diffDirArgument
import io.matthewnelson.diff.cli.internal.OptDiffFileExtName
import io.matthewnelson.diff.cli.internal.OptDiffFileExtName.Companion.diffFileExtNameOption
import io.matthewnelson.cli.core.OptQuiet.Companion.quietOption
import io.matthewnelson.diff.cli.internal.OptStaticTime.Companion.staticTimeOption
import io.matthewnelson.cli.core.Subcommand
import kotlinx.cli.ArgType

internal class DirCreate: Subcommand(
    name = NAME_CMD,
    description = """
        Creates diff files from 2 identically structured
        directories. Walks the entire file tree of both
        directories and outputs diff files to the specified
        ${ArgDiffDir.NAME_ARG} when encoutering differences.
        Both directories MUST have an identical file structure.
    """,
),  ArgDiffDir,
    OptDiffFileExtName,
    OptStaticTime
{

    private val dir1Arg: String by argument(
        type = ArgType.String,
        fullName = NAME_DIR_1,
        description = "The first directory (e.g. /path/to/unsigned/program)",
    )

    private val dir2Arg: String by argument(
        type = ArgType.String,
        fullName = NAME_DIR_2,
        description = "The second directory (identical structure to $NAME_DIR_1) to diff against the first (e.g. /path/to/signed/program)",
    )

    override val diffDirArg: String by diffDirArgument(
        description = "The directory to output the generated diff files (e.g. /path/to/diffs/program)",
    )

    override val diffFileExtNameOpt: String by diffFileExtNameOption(
        description = "The file extension name to use when diff files are created"
    )

    override val quietOpt: Boolean by quietOption()

    override val staticTimeOpt: Boolean by staticTimeOption()

    override fun execute() {
        // TODO
    }

    internal companion object {
        internal const val NAME_CMD = "dir-create"

        internal const val NAME_DIR_1 = "dir1"
        internal const val NAME_DIR_2 = "dir2"
    }
}
