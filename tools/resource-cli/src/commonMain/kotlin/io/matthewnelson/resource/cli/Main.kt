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

import io.matthewnelson.cli.core.CLIRuntime
import io.matthewnelson.cli.core.OptQuiet
import io.matthewnelson.cli.core.OptQuiet.Companion.quietOption
import io.matthewnelson.resource.cli.internal.ResourceWriter
import io.matthewnelson.resource.cli.internal.write
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType

public fun main(args: Array<String>) {
    val runtime = KMPResourceCLIRuntime()
    runtime.run(args)

    val resourcePath = ResourceWriter(
        packageName = runtime.packageName,
        pathSourceSet = runtime.pathSourceSet,
        pathFile = runtime.pathFile
    ).write()

    if (runtime.quietOpt) return
    println("transformed '${runtime.pathFile}' -> '$resourcePath'")
}

private class KMPResourceCLIRuntime: CLIRuntime(parser = ArgParser(PROGRAM_NAME.lowercase())), OptQuiet {

    private companion object {
        private const val PROGRAM_NAME = "Resource-CLI"
    }

    val packageName by parser.argument(
        type = ArgType.String,
        fullName = "package-name",
        description = "The package name for the resource (e.g. io.matthewnelson.kmp.tor.binary)"
    )

    val pathSourceSet by parser.argument(
        type = ArgType.String,
        fullName = "path-source-set",
        description = "The absolute path to the target source set to place the resource_{name}.kt file"
    )

    val pathFile by parser.argument(
        type = ArgType.String,
        fullName = "path-file",
        description = "The absolute path of the file to transform into a resource_{name}.kt file"
    )

    override val quietOpt: Boolean by parser.quietOption()

    override fun printHeader() {
        val versionName = "0.1.0"
        val url = "https://github.com/05nelsonm/kmp-tor-binary/tree/master/tools/kmp-resource-cli"

        println("""
            $PROGRAM_NAME v$versionName
            Copyright (C) 2023 Matthew Nelson
            Apache License, Version 2.0

            Utility for converting files to resource_{name}.kt files since
            non-jvm Kotlin Multiplatform source sets do not have a way to
            package and distribute resources.
            
            Project: $url
    
        """.trimIndent())
    }
}
