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
package io.matthewnelson.diff.cli.internal

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context

internal abstract class DiffCLICommand(
    name: String,
    private val subcommandDescription: String = "",
): CliktCommand(name) {

    final override val printHelpOnEmptyArgs: Boolean = true

    final override fun help(context: Context): String = """
        v$VERSION

        Copyright (C) 2023 Matthew Nelson

        Compares files byte for byte and creates diffs
        which can be applied at a later date and time.
        Was created primarily for applying code signatures
        to reproducibly built software.

        Project: $URL
    """.trimIndent().let {
        if (subcommandDescription.isNotBlank()) {
            subcommandDescription + "\n\n" + it
        } else {
            it
        }
    }

    private companion object {
        private const val VERSION = "0.1.0"
        private const val URL = "https://github.com/05nelsonm/kmp-tor-resource/tree/master/tools/diff-cli"
    }
}
