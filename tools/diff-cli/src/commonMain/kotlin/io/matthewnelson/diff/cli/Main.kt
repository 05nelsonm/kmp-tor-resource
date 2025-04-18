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
package io.matthewnelson.diff.cli

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import io.matthewnelson.diff.cli.internal.subcommand.Apply
import io.matthewnelson.diff.cli.internal.DiffCLICommand
import io.matthewnelson.diff.cli.internal.subcommand.Create
import io.matthewnelson.diff.cli.internal.subcommand.PrintHeader

public fun main(args: Array<String>): Unit = DiffCLI().main(args)

private class DiffCLI: DiffCLICommand(name = "diff-cli") {
    init { subcommands(Create(), Apply(), PrintHeader()) }
    override fun run() {}
}
