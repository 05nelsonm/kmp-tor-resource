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
package io.matthewnelson.diff.core.internal.apply

import io.matthewnelson.diff.core.Diff
import io.matthewnelson.diff.core.Header.Companion.readDiffHeader
import io.matthewnelson.diff.core.Options
import io.matthewnelson.diff.core.internal.*
import io.matthewnelson.diff.core.internal.BASE_16
import io.matthewnelson.diff.core.internal.BASE_64
import io.matthewnelson.diff.core.internal.checkExistsAndIsFile
import io.matthewnelson.diff.core.internal.create.Create.Companion.EOF_HASH
import io.matthewnelson.diff.core.internal.create.Create.Companion.INDEX
import io.matthewnelson.diff.core.internal.hashLengthOf
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.encoding.core.ExperimentalEncodingApi
import okio.*
import okio.Path.Companion.toPath
import org.kotlincrypto.hash.sha2.SHA256

internal sealed class Apply private constructor() {

    internal companion object {

        internal fun diff(fs: FileSystem, diffFile: Path, applyTo: Path, options: Options.Apply) {
            check(diffFile != applyTo) { "files cannot be the same" }
            applyTo.checkExistsAndIsFile(fs)
            diffFile.checkExistsAndIsFile(fs)

            // The very last line of the diff is the sha256 hash of all that
            // was written to it, prior to writing said sha256 hash (i.e. the
            // "contents hash"). This is a validation check of the diff file
            // contents such that if the diff file was modified, the hash of its
            // contents will not match the recorded hash value.
            //
            // Of course there are ways around this, but it will work 99% of the
            // time if someone modifies the diff file.
            val (hashContents, hashEOF) = fs.read(diffFile) {
                val digest = SHA256()
                var hashEOF = ""
                val newLineByte = '\n'.code.toByte()

                while (true) {
                    val line = readUtf8Line()
                    when {
                        line == null -> break
                        line.startsWith(EOF_HASH) -> {
                            hashEOF = line.substringAfter(EOF_HASH)
                            break
                        }
                        else -> {
                            digest.update(line.encodeToByteArray())
                            digest.update(newLineByte)
                        }
                    }
                }

                val hashContent = digest.digest().encodeToString(BASE_16)
                Pair(hashContent, hashEOF)
            }

            if (hashContents != hashEOF) {
                throw IllegalStateException("""
                    Validation check failed. Was the diff file modified?
                    Expected: sha256[$hashEOF]
                    Actual:   sha256[$hashContents]
                """.trimIndent())
            }

            fs.read(diffFile) bsDiff@ {
                val header = readDiffHeader()
                val (hash, applyToLength) = fs.hashLengthOf(applyTo)

                if (header.createdForHash != hash) {
                    throw IllegalStateException("""
                        Validation check failed. The diff was not created for this file.
                        Expected: sha256[${header.createdForHash}]
                        Actual:   sha256[$hash]
                    """.trimIndent())
                }

                val applyToCanonical = fs.canonicalize(applyTo)
                val bak = "$applyToCanonical.bak".toPath()
                fs.delete(bak, mustExist = false)

                try {
                    HashingSink.sha256(fs.sink(bak, mustCreate = true)).use { hsBak ->
                        hsBak.buffer().use { bsBak ->
                            fs.read(applyTo) bsApplyTo@ {

                                when (header.schema) {
                                    Diff.Schema.v1 -> V1(this@bsDiff, this@bsApplyTo, applyToLength, bsBak)
                                }

                            }
                        }

                        val bakHash = hsBak.hash.hex()
                        if (bakHash != header.createdFromHash) {
                            throw IllegalStateException("""
                                Failed to apply diff to $applyTo.
                                Expected: sha256[${header.createdFromHash}]
                                Actual:   sha256[$bakHash]
                            """.trimIndent())
                        }
                    }
                } catch (t: Throwable) {
                    try {
                        fs.delete(bak)
                    } catch (_: Throwable) {}

                    throw IOException("Failed to apply diff to ${applyTo.name}", t)
                }

                if (!options.dryRun) {
                    fs.atomicMove(bak, applyToCanonical)
                }
            }
        }
    }

    private object V1: Apply() {

        @OptIn(ExperimentalEncodingApi::class)
        @Throws(IllegalStateException::class, IOException::class)
        operator fun invoke(
            diffFile: BufferedSource,
            applyTo: BufferedSource,
            applyToLength: Long,
            bak: BufferedSink
        ) {
            var i = 0L
            var iBak = 0L

            fun newFeed() = BASE_64.newDecoderFeed { decodedByte ->
                bak.writeByte(decodedByte.toInt())
                iBak++
                if (iBak < applyToLength) applyTo.skip( byteCount = 1L)
            }

            var feed = newFeed()

            while (true) {
                val line = diffFile.readUtf8Line()

                when {
                    line == null ||
                    line.startsWith(EOF_HASH) -> break
                    line.startsWith(INDEX) -> {
                        feed.doFinal()
                        feed = newFeed()
                        i = line.substringAfter(INDEX).toLong()
                    }
                    else -> {
                        if (iBak < i && iBak < applyToLength) {
                            bak.write(applyTo, byteCount = i - iBak)
                            iBak = i
                        }

                        line.forEach { c ->
                            feed.consume(c)
                        }
                    }
                }
            }

            if (!feed.isClosed()) feed.doFinal()
        }
    }
}
