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
import io.matthewnelson.diff.cli.internal.DiffCLICommand
import io.matthewnelson.diff.core.Diff

internal class PrintHeader: DiffCLICommand(
    name = "print-header",
    subcommandDescription = """
        Prints a prettily formatted diff file's header contents.
    """.trimIndent(),
) {

    private val diffFile by argument(name = "diff-file")

    override fun run() {
        echo(Diff.readHeader(diffFile))
    }
}
