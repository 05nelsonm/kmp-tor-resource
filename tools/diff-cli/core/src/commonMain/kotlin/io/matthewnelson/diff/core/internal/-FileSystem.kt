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

package io.matthewnelson.diff.core.internal

import okio.*

@Suppress("NOTHING_TO_INLINE")
internal inline fun FileSystem.hashLengthOf(file: Path): Pair<String, Long> {
    return HashingSource.sha256(source(file)).use { hs ->
        var length = 0L

        hs.buffer().use { bs ->
            val buf = ByteArray(4096)
            while (true) {
                val read = bs.read(buf)
                if (read == -1) break
                length += read
            }
        }

        Pair(hs.hash.hex(), length)
    }
}
