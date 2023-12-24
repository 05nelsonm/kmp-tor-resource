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
package io.matthewnelson.diff.core

import io.matthewnelson.diff.core.internal.LINE_BREAK
import io.matthewnelson.diff.core.internal.writeNewLine
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import okio.BufferedSink
import okio.BufferedSource
import kotlin.jvm.JvmField
import kotlin.jvm.JvmSynthetic

public class Header
@Throws(IllegalStateException::class)
internal constructor(
    @JvmField public val schema: Diff.Schema,
    internal val createdAtInstant: Instant,
    @JvmField public val createdForFile: String,
    @JvmField public val createdForHash: String,
    @JvmField public val createdFromHash: String,
) {

    init {
        check(createdForHash.matches(REGEX)) { "createdForHash invalid sha256[$createdForHash]" }
        check(createdFromHash.matches(REGEX)) { "createdFromHash invalid sha256[$createdFromHash]" }
    }

    public fun createdAt(): Long = createdAtInstant.toEpochMilliseconds()

    internal fun writeTo(sink: BufferedSink) {
        with(sink) {
            writeUtf8(PREFIX_SCHEMA_VERSION)
            writeUtf8(schema.name)
            writeNewLine()

            writeUtf8(PREFIX_CREATED_AT)
            writeUtf8(createdAtInstant.toString())
            writeNewLine()

            writeUtf8(PREFIX_CREATED_FOR_FILE)
            writeUtf8(createdForFile)
            writeNewLine()

            writeUtf8(PREFIX_CREATED_FOR_HASH)
            writeUtf8(createdForHash)
            writeNewLine()

            writeUtf8(PREFIX_CREATED_FROM_HASH)
            writeUtf8(createdFromHash)
            writeNewLine()
        }
    }

    override fun equals(other: Any?): Boolean {
        return  other is Header
                && other.schema == schema
                && other.createdAtInstant == createdAtInstant
                && other.createdForFile == createdForFile
                && other.createdForHash == createdForHash
                && other.createdFromHash == createdFromHash
    }

    override fun hashCode(): Int {
        var result = 17
        result = result * 31 + schema.hashCode()
        result = result * 31 + createdAtInstant.hashCode()
        result = result * 31 + createdForFile.hashCode()
        result = result * 31 + createdForHash.hashCode()
        result = result * 31 + createdFromHash.hashCode()
        return result
    }

    override fun toString(): String {
        return """
            Header [
                schema: $schema
                createdAt: $createdAtInstant
                createdForFile: $createdForFile
                createdForHash: $createdForHash
                createdFromHash: $createdFromHash
            ]
        """.trimIndent()
    }

    internal companion object {
        private const val PREFIX_SCHEMA_VERSION: String = "$LINE_BREAK Diff Schema: " /* +Version */
        private const val PREFIX_CREATED_AT: String = "$LINE_BREAK Created At: "
        private const val PREFIX_CREATED_FOR_FILE: String = "$LINE_BREAK Created For File: "
        private const val PREFIX_CREATED_FOR_HASH: String = "$LINE_BREAK Created For Hash: "
        private const val PREFIX_CREATED_FROM_HASH: String = "$LINE_BREAK Created From Hash: "

        private val REGEX = "[a-f0-9]{64}".toRegex()

        @JvmSynthetic
        @Throws(IllegalStateException::class)
        internal fun BufferedSource.readDiffHeader(): Header {
            val versionString = readUtf8Line()
                ?.substringAfter(PREFIX_SCHEMA_VERSION)
                ?: throw IllegalStateException("Failed to read Diff schema version")

            val schema = try {
                Diff.Schema.valueOf(versionString)
            } catch (e: IllegalArgumentException) {
                throw IllegalStateException("Failed to read Diff schema version")
            }

            val createdAtString = readUtf8Line()
                ?.substringAfter(PREFIX_CREATED_AT)
                ?: throw IllegalStateException("Failed to read Diff createdAt")

            val createdAt = try {
                createdAtString.toInstant()
            } catch (e: IllegalArgumentException) {
                throw IllegalStateException("Failed to read Diff createdAt", e)
            }

            val forFileName = readUtf8Line()
                ?.substringAfter(PREFIX_CREATED_FOR_FILE)
                ?: throw IllegalStateException("Failed to read Diff createdForFile")

            val forFileHash = readUtf8Line()
                ?.substringAfter(PREFIX_CREATED_FOR_HASH)
                ?: throw IllegalStateException("Failed to read Diff createdForHash")

            val postApplyHash = readUtf8Line()
                ?.substringAfter(PREFIX_CREATED_FROM_HASH)
                ?: throw IllegalStateException("Failed to read Diff createdFromHash")

            return Header(schema, createdAt, forFileName, forFileHash, postApplyHash)
        }
    }
}
