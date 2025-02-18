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
import io.matthewnelson.diff.core.Options

internal class Apply: DiffCLICommand(
    name = "apply",
    subcommandDescription = """
        Applies a diff to its associated file.
        
            NOTE: The file is modified in place.
    """.trimIndent(),
) {

    private val diffFile by argument(name = "diff-file")
        .help {
            theme.info("The previously created diff file to be applied (e.g. /path/to/diffs/file.diff)")
        }

    private val applyTo by argument(name = "file")
        .help {
            theme.info("The file to apply the diff to (e.g. /path/to/unsigned/file)")
        }

    private val dryRunOpt by option("--dry-run")
        .boolean()
        .default(false)
        .help {
            theme.info("Will apply the diff, but leaves the '.bak' file in place instead of atomically moving it")
        }

    private val quiet by option()
        .boolean()
        .default(false)
        .help {
            theme.info("Silences the terminal output")
        }

    override fun run() {
        Diff.apply(diffFile, applyTo, Options.Apply { dryRun = dryRunOpt })
        if (quiet) return
        echo("applied diff '$diffFile' to '$applyTo" + if (dryRunOpt) ".bak'" else "'")
    }
}
