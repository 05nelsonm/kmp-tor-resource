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

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgParserResult
import kotlin.jvm.JvmField

public abstract class CLIRuntime(
    @JvmField
    public val parser: ArgParser
) {

    protected abstract fun printHeader()

    public fun run(args: Array<String>): ArgParserResult {
        val helpOrArgs = when {
            args.isEmpty() -> {
                printHeader()
                arrayOf("-h")
            }
            else -> {
                when (args.first()) {
                    "-h", "--help" -> printHeader()
                }
                args
            }
        }

        return parser.parse(helpOrArgs)
    }
}
