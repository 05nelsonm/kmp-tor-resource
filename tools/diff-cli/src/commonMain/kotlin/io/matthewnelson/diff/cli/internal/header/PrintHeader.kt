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
package io.matthewnelson.diff.cli.internal.header

import io.matthewnelson.diff.cli.internal.ArgDiffFile
import io.matthewnelson.diff.cli.internal.ArgDiffFile.Companion.diffFileArgument
import io.matthewnelson.cli.core.Subcommand
import io.matthewnelson.diff.core.Diff

internal class PrintHeader: Subcommand(
    name = NAME_CMD,
    description = "Prints a prettily formatted diff file's header",
),  ArgDiffFile
{

    override val diffFileArg: String by diffFileArgument(
        description = "A diff file (e.g. /path/to/diffs/file.diff)"
    )

    override val quietOpt: Boolean = false

    override fun execute() {
        println(Diff.readHeader(diffFileArg))
    }

    internal companion object {
        internal const val NAME_CMD: String = "print-header"
    }
}
