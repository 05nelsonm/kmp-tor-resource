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
package io.matthewnelson.cli.core

import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
public abstract class Subcommand(
    name: String,
    description: String,
    additionalIndent: Int = 0,
): kotlinx.cli.Subcommand(
    name = name,
    actionDescription = description.indentDescription(name, additionalIndent)
), OptQuiet {

    protected fun settings(): Settings = Settings(quiet = quietOpt)

    public data class Settings(val quiet: Boolean) {
        public fun println(output: Any?) {
            if (quiet) return
            kotlin.io.println(output)
        }
    }

    private companion object {

        /**
         * Fixes multi-line descriptions so they are indented properly.
         * */
        private fun String.indentDescription(
            name: String,
            additionalIndent: Int,
        ): String {
            val lines = trimIndent().lines()
            if (isEmpty() || lines.size == 1) return this

            val plusIndent = if (additionalIndent > 0) additionalIndent else 0

            return StringBuilder().apply {
                repeat(plusIndent) { append(' ') }
                append(lines.first())

                for (i in 1..lines.lastIndex) {
                    appendLine()
                    repeat(name.length + 4 + 3 + plusIndent) { append(' ') }
                    append(lines[i])
                }
            }.toString()
        }
    }
}
