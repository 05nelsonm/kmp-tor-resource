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
package io.matthewnelson.diff.cli.internal.apply

import io.matthewnelson.diff.cli.internal.ArgDiffDir
import io.matthewnelson.diff.cli.internal.ArgDiffDir.Companion.diffDirArgument
import io.matthewnelson.diff.cli.internal.OptDiffFileExtName
import io.matthewnelson.diff.cli.internal.OptDiffFileExtName.Companion.diffFileExtNameOption
import io.matthewnelson.cli.core.OptQuiet.Companion.quietOption
import io.matthewnelson.cli.core.Subcommand
import io.matthewnelson.diff.cli.internal.create.DirCreate
import kotlinx.cli.ArgType

internal class DirApply: Subcommand(
    name = NAME_CMD,
    description = """
        Applies diff files from previously created
        ${DirCreate.NAME_CMD} to the specified $NAME_DIR.
        Files from $NAME_DIR are modified in place.
    """,
    additionalIndent = 1,
),  ArgDiffDir,
    OptDiffFileExtName
{

    private val dirArg: String by argument(
        type = ArgType.String,
        fullName = NAME_DIR,
        description = "The directory to apply the file diffs to (e.g. /path/to/unsigned/program)",
    )

    override val diffDirArg: String by diffDirArgument(
        description = "The directory of diff files to be applied to $NAME_DIR (e.g. /path/to/diffs/program)",
    )

    override val diffFileExtNameOpt: String by diffFileExtNameOption(
        description = "The file extension name used when diff files were created with ${DirCreate.NAME_CMD}"
    )

    override val quietOpt: Boolean by quietOption()

    override fun execute() {
        // TODO
    }

    internal companion object {
        internal const val NAME_CMD = "dir-apply"

        internal const val NAME_DIR = "dir"
    }
}
