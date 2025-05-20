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
@file:Suppress("KotlinRedundantDiagnosticSuppress")

package io.matthewnelson.diff.core.internal.create

import io.matthewnelson.diff.core.Diff
import io.matthewnelson.diff.core.Header
import io.matthewnelson.diff.core.NoDiffException
import io.matthewnelson.diff.core.Options
import io.matthewnelson.diff.core.internal.*
import io.matthewnelson.diff.core.internal.hashLengthOf
import io.matthewnelson.diff.core.internal.checkIsDirOrNull
import io.matthewnelson.diff.core.internal.checkExistsAndIsFile
import io.matthewnelson.encoding.core.ExperimentalEncodingApi
import kotlinx.datetime.Clock
import okio.*

internal sealed class Create private constructor() {

    internal companion object {
        internal const val EOF_HASH: String = "$LINE_BREAK END: " /* +sha256 */
        internal const val INDEX: String = "$LINE_BREAK i:"

        @Throws(IllegalArgumentException::class, IllegalStateException::class, IOException::class)
        internal fun diff(
            fs: FileSystem,
            file1: Path,
            file2: Path,
            diffDir: Path,
            options: Options.Create,
        ): Path {
            require(file1 != file2) { "files cannot be the same" }

            file1.checkExistsAndIsFile(fs)

            // Maybe write a diff file to delete file1 instead of failing???
            // Maybe as an option to enable this, and throw by default???
            file2.checkExistsAndIsFile(fs)

            val mustCreate = diffDir.checkIsDirOrNull(fs)

            val (f1Hash, f1Length) = fs.hashLengthOf(file1)
            val (f2Hash, f2Length) = fs.hashLengthOf(file2)

            if (f1Hash == f2Hash) {
                throw NoDiffException("No differences found between [$file1] and [$file2]")
            }

            // TODO: Maybe check length and throw exception if either is empty?

            fs.createDirectories(diffDir, mustCreate = mustCreate)
            val canonicalDiffDir = fs.canonicalize(diffDir)
            val diffFile = canonicalDiffDir.resolve(file1.name + options.diffFileExtensionName)

            try {
                val header = Header(options.schema, options.time(), file1.name, f1Hash, f2Hash)

                HashingSink.sha256(fs.sink(diffFile, mustCreate = true)).use { hsDiff ->
                    hsDiff.buffer().use { bsDiff ->
                        header.writeTo(bsDiff)

                        fs.read(file1) bsFile1@ {
                            fs.read(file2) bsFile2@ {

                                when (options.schema) {
                                    Diff.Schema.v1 -> V1(this@bsFile1, f1Length,this@bsFile2, f2Length, bsDiff)
                                }

                            }
                        }

                        bsDiff.writeNewLine()

                        // Write the diff file content's hash to the very last line
                        // of the file. This is used as a validation check when going
                        // to apply it to file1 later such that any modifications to
                        // the diff will fail its validation check.
                        bsDiff.flush()
                        val hash = hsDiff.hash.hex()

                        bsDiff.writeUtf8(EOF_HASH)
                        bsDiff.writeUtf8(hash)
                        bsDiff.writeNewLine()
                    }
                }
            } catch (t: Throwable) {
                if (mustCreate) {
                    fs.deleteRecursively(canonicalDiffDir)
                } else {
                    fs.delete(diffFile)
                }

                throw IOException("Failed to create diff for ${file1.name}", t)
            }

            return diffFile
        }
    }

    private object V1: Create() {

        @OptIn(ExperimentalEncodingApi::class)
        operator fun invoke(
            file1: BufferedSource,
            f1Length: Long,
            file2: BufferedSource,
            f2Length: Long,
            diff: BufferedSink,
        ) {
            val f1Buf = ByteArray(4096)
            val f2Buf = f1Buf.copyOf()

            fun newFeed() = BASE_64.newEncoderFeed { encodedChar ->
                diff.writeByte(encodedChar.code)
            }

            var feed = newFeed()
            var diffing = false
            var index = 0L
            while (true) {
                val f1Read = file1.read(f1Buf)
                val f2Read = file2.read(f2Buf)
                if (f1Read == -1 && f2Read == -1) break

                val f1Until = if (f1Read == -1) 0 else f1Read
                val f2Until = if (f2Read == -1) 0 else f2Read

                val (append, commonLength) = when {
                    f1Until == f2Until -> Pair(null, f1Until)
                    f1Until < f2Until -> Pair(true, f1Until)
                    else -> Pair(false, f2Until)
                }

                for (i in 0 until commonLength) {
                    val f1b = f1Buf[i]
                    val f2b = f2Buf[i]

                    if (f1b == f2b) {
                        if (diffing) {
                            feed.doFinal()
                            feed = newFeed()
                            diff.writeNewLine()
                            diffing = false
                        }
                    } else {
                        if (!diffing) {
                            diff.writeIndex(index)
                            diffing = true
                        }

                        feed.consume(f2b)
                    }

                    index++
                }

                when (append) {
                    null -> continue
                    true -> {
                        if (!diffing) {
                            diff.writeIndex(index)
                            diffing = true
                        }

                        for (i in commonLength until f2Until) {
                            feed.consume(f2Buf[i])
                        }
                    }
                    false -> {}
                }
            }

            if (!feed.isClosed()) feed.doFinal()

            // If same length, need to write en empty diff so that when
            // applying, it will read the remaining bytes from f1.
            if (f1Length == f2Length) diff.writeIndex(f1Length)
        }

        @Suppress("NOTHING_TO_INLINE")
        private inline fun BufferedSink.writeIndex(i: Long) {
            writeUtf8(INDEX)
            writeUtf8(i.toString())
            writeNewLine()
        }
    }
}
