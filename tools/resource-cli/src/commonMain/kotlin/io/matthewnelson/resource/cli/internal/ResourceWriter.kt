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
package io.matthewnelson.resource.cli.internal

internal data class ResourceWriter(
    public val packageName: String,
    public val pathSourceSet: String,
    public val pathFile: String,
) {

    internal fun fileNameToObjectName(name: String): String = "resource_" + name.replace('.', '_')

    internal fun header(
        fileName: String,
        size: Long,
        sha256: String,
        chunks: Long,
    ): String {
        val objName = fileNameToObjectName(fileName)

        val sb = StringBuilder()
        sb.appendLine("""
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
            @file:Suppress("ClassName", "ConstPropertyName")

            package $packageName

            // This is an automatically generated file.
            // DO NOT MODIFY

            import io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi
            import io.matthewnelson.kmp.tor.core.resource.NativeResource

            @InternalKmpTorApi
            internal object $objName: NativeResource(
                version = 1,
                name = "$fileName",
                size = ${size}L,
                sha256 = "$sha256",
                chunks = ${chunks}L,
            ) {

                @Throws(IllegalStateException::class, IndexOutOfBoundsException::class)
                override operator fun get(index: Long): Chunk = when (index) {
        """.trimIndent())

        var i = 0L
        while (i < chunks) {
            sb.append("        ")
            sb.append(i)
            sb.append("L -> _")
            sb.appendLine(i++)
        }

        sb.appendLine("        else -> throw IndexOutOfBoundsException()")
        sb.appendLine("    }.toChunk()")
        sb.appendLine()

        return sb.toString()
    }
}

@Throws(Exception::class)
internal expect fun ResourceWriter.write(): String
