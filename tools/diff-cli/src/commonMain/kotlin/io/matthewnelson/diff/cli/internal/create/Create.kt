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
import io.matthewnelson.diff.cli.internal.ArgDiffDir.Companion.diffDirArgument
import io.matthewnelson.diff.cli.internal.OptDiffFileExtName.Companion.diffFileExtNameOption
import io.matthewnelson.cli.core.OptQuiet.Companion.quietOption
import io.matthewnelson.diff.cli.internal.OptStaticTime.Companion.staticTimeOption
import io.matthewnelson.cli.core.Subcommand
import io.matthewnelson.diff.core.Diff
import io.matthewnelson.diff.core.NoDiffException
import io.matthewnelson.diff.core.Options
import kotlinx.cli.ArgType

internal class Create: Subcommand(
    name = NAME_CMD,
    description = """
        Creates a diff from 2 file inputs. The first file is
        compared to the second file whereby any differences
        that the second file has will be recorded.
    """,
    additionalIndent = 6,
),  ArgDiffDir,
    OptDiffFileExtName,
    OptStaticTime
{

    private val file1Arg: String by argument(
        type = ArgType.String,
        fullName = NAME_FILE_1,
        description = "The first file (e.g. /path/to/unsigned/file)"
    )

    private val file2Arg: String by argument(
        type = ArgType.String,
        fullName = NAME_FILE_2,
        description = "The second file to diff against the first file (e.g. /path/to/signed/file)"
    )

    override val diffDirArg: String by diffDirArgument(
        description = "The directory to output the generated diff file to (e.g. /path/to/diffs)"
    )

    override val diffFileExtNameOpt: String by diffFileExtNameOption(
        description = "The file extension name to use for the diff file"
    )

    override val quietOpt: Boolean by quietOption()

    override val staticTimeOpt: Boolean by staticTimeOption()

    override fun execute() {
        with(settings()) {
            val path = try {
                Diff.create(
                    file1Path = file1Arg,
                    file2Path = file2Arg,
                    diffDirPath = diffDirArg,
                    options = Options.Create {
                        diffFileExtensionName(value = diffFileExtNameOpt)
                        useStaticTime = staticTimeOpt
                    },
                )
            } catch (t: Throwable) {
                if (t is NoDiffException) {
                    println(t.message)
                    return
                } else {
                    throw t
                }
            }

            println("created diff '$path' for '$file1Arg'")
        }
    }

    internal companion object {
        internal const val NAME_CMD = "create"

        internal const val NAME_FILE_1 = "file1"
        internal const val NAME_FILE_2 = "file2"
    }
}
