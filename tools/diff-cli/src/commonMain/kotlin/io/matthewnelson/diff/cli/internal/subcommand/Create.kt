/*
 * Copyright (c) 2025 Matthew Nelson
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
package io.matthewnelson.diff.cli.internal.subcommand

import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.transform.theme
import com.github.ajalt.clikt.parameters.types.boolean
import io.matthewnelson.diff.cli.internal.DiffCLICommand
import io.matthewnelson.diff.core.Diff
import io.matthewnelson.diff.core.NoDiffException
import io.matthewnelson.diff.core.Options

internal class Create: DiffCLICommand(
    name = "create",
    subcommandDescription = """
        Creates a diff of 2 files.
        
            The first file is compared to the second file whereby any
            differences that the second file has will be recorded.
    """.trimIndent()
) {

    private val file1 by argument(name = "file1")
        .help {
            theme.info("The first file (e.g. /path/to/unsigned/file)")
        }

    private val file2 by argument(name = "file2")
        .help {
            theme.info("The second file to diff against the first file (e.g. /path/to/signed/file)")
        }

    private val diffDir by argument(name = "diff-dir")
        .help {
            theme.info("The directory to output the generated diff file to (e.g. /path/to/diffs)")
        }

    private val extension by option("--diff-ext-name")
        .default(Options.Create.DEFAULT_EXT_NAME)
        .help {
            theme.info("The file extension name to use for the diff file")
        }

    private val quiet by option()
        .boolean()
        .default(false)
        .help {
            theme.info("Silences the terminal output")
        }

    private val staticTime by option("--static-time")
        .boolean()
        .default(false)
        .help {
            theme.info("Uses a static time value of ${Options.Create.STATIC_TIME} instead of the current time value")
        }

    override fun run() {
        val path = try {
            Diff.create(
                file1Path = file1,
                file2Path = file2,
                diffDirPath = diffDir,
                options = Options.Create {
                    diffFileExtensionName(value = extension)
                    useStaticTime = staticTime
                }
            )
        } catch (t: Throwable) {
            if (t is NoDiffException) {
                if (!quiet) echo(t.message)
                return
            } else {
                throw t
            }
        }

        if (quiet) return
        echo("created diff '$path' for '$file1'")
    }
}
