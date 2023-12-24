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

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.base64.Base64
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.encoding.core.util.LineBreakOutFeed
import org.kotlincrypto.hash.sha2.SHA256
import java.io.File
import java.io.IOException

@Throws(Exception::class)
internal actual fun ResourceWriter.write(): String {
    val fileInput = File(pathFile).canonicalFile
    check(fileInput.exists()) { "file[$pathFile] does not exist" }

    var fileOutput = File(pathSourceSet)
        .resolve("kotlin")

    packageName.split('.').forEach { split ->
        fileOutput = fileOutput.resolve(split)
    }

    if (!fileOutput.exists() && !fileOutput.mkdirs()) {
        throw IllegalStateException("Failed to create directory[$fileOutput]")
    }

    fileOutput = fileOutput.resolve(fileNameToObjectName(fileInput.name) + ".kt")

    val buf = ByteArray(4096)

    // First pass to obtain header information
    val (size, sha256, chunks) = fileInput.inputStream().use { iStream ->
        var chunks = 0L
        var size = 0L
        val digest = SHA256()

        while (true) {
            val read = iStream.read(buf)
            if (read == -1) break
            digest.update(buf, 0, read)
            size += read
            chunks++
        }

        // reset the buffer
        buf.fill(0)

        Triple(
            size,
            digest.digest().encodeToString(Base16 { encodeToLowercase = true }),
            chunks,
        )
    }

    check(size > 0) { "file[$fileInput] cannot be empty" }

    if (fileOutput.exists() && !fileOutput.delete()) {
        throw IOException("Failed to delete file[$fileOutput]")
    }

    fileInput.inputStream().use { iStream ->
        fileOutput.outputStream().writer().use { oStream ->
            oStream.write(header(
                fileName = fileInput.name,
                size = size,
                sha256 = sha256,
                chunks = chunks
            ))

            val sb = StringBuilder()

            val out = LineBreakOutFeed(interval = 64, out = { char -> sb.append(char) })
            val feed = Base64.Default.newEncoderFeed(out)

            try {
                var i = 0L
                while (true) {
                    val read = iStream.read(buf)
                    if (read == -1) break

                    sb.append("    private const val _")
                    sb.append(i++)
                    sb.appendLine(" =")
                    sb.append("\"\"\"")

                    for (j in 0 until read) {
                        feed.consume(buf[j])
                    }
                    feed.flush()
                    out.reset()

                    sb.appendLine("\"\"\"")
                    sb.appendLine()

                    oStream.write(sb.toString())
                    sb.clear()
                }

                sb.appendLine('}')
                oStream.write(sb.toString())
            } finally {
                feed.close()
            }
        }
    }

    return fileOutput.path
}
