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
package io.matthewnelson.resource.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.transform.theme
import com.github.ajalt.clikt.parameters.types.boolean
import io.matthewnelson.resource.cli.internal.ResourceWriter
import io.matthewnelson.resource.cli.internal.write

public fun main(args: Array<String>): Unit = ResourceCLI().main(args)

private class ResourceCLI: CliktCommand(name = "resource-cli") {

    override val printHelpOnEmptyArgs: Boolean = true

    private val packageName by argument(name = "package-name")
        .help {
            theme.info("The package name for the resource (e.g. io.matthewnelson.kmp.tor.resource.geoip)")
        }

    private val pathSourceSet by argument(name = "path-source-set")
        .help {
            theme.info("The absolute path to the target source set to place the resource_{name}.kt file (e.g. /some/path/project/src/nativeMain)")
        }

    private val pathFile by argument(name = "path-file")
        .help {
            theme.info("The absolute path of the file to transform into a resource_{name}.kt file")
        }

    private val quiet by option()
        .boolean()
        .default(false)
        .help {
            theme.info("Silences the terminal output")
        }

    override fun run() {
        val resourcePath = ResourceWriter(
            packageName = packageName,
            pathSourceSet = pathSourceSet,
            pathFile = pathFile,
        ).write()

        if (quiet) return
        echo("transformed '$pathFile' -> '$resourcePath'")
    }

    override fun help(context: Context): String = """
        v$VERSION

        Copyright (C) 2023 Matthew Nelson

        Utility for converting files to resource_{name}.kt files since
        non-jvm Kotlin Multiplatform source sets do not have a way to
        package and distribute resources.

        Project: $URL
    """.trimIndent()

    private companion object {
        private const val VERSION = "0.1.0"
        private const val URL = "https://github.com/05nelsonm/kmp-tor-resource/tree/master/tools/resource-cli"
    }
}
