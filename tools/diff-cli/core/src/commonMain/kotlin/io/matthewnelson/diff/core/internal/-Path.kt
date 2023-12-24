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

import okio.FileSystem
import okio.Path

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal inline fun Path.checkExistsAndIsFile(fs: FileSystem) {
    check(fs.exists(this)) { "File $name does not exist" }
    check(fs.metadata(this).isRegularFile) { "$name exists, but is not a regular file" }
}

/* Returns t/f for mustCreate */
@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalArgumentException::class)
internal inline fun Path.checkIsDirOrNull(fs: FileSystem): Boolean {
    val isDir = fs.metadataOrNull(this)?.isDirectory
    check(isDir != false) { "$this is not a directory" }
    return isDir == null
}
