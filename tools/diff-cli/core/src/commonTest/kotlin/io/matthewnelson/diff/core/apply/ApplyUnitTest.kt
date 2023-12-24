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
package io.matthewnelson.diff.core.apply

import io.matthewnelson.diff.core.Diff
import io.matthewnelson.diff.core.DiffCoreTestHelper
import io.matthewnelson.diff.core.internal.InternalDiffApi
import okio.Path.Companion.toPath
import kotlin.test.Test

@OptIn(InternalDiffApi::class)
class ApplyUnitTest: DiffCoreTestHelper() {

    @Test
    fun givenApply_whenApplyToFileDoesNotMatch_thenThrowsValidationException() {
        file1.writeText("some text")
        file2.writeText("some more text")
        val diffFile = Diff.create(fs, file1, file2, diffDir)
        fs.delete(file1)
        file1.writeText("some different text")
        assertThrew<IllegalStateException>(print = false) { Diff.apply(fs, diffFile, file1) }
    }

    @Test
    fun givenApply_whenDiffFileModified_thenThrowsValidationException() {
        file1.writeText("some text")
        file2.writeText("some more text")
        val diffFile = Diff.create(fs, file1, file2, diffDir)
        val bak = "$diffFile.bak".toPath()

        fs.copy(diffFile, bak)
        fs.read(bak) {
            fs.write(diffFile) {
                while (true) {
                    val line = readUtf8Line() ?: break
                    writeUtf8(line)
                    writeUtf8("\n\n")
                }
            }
        }

        assertThrew<IllegalStateException>(print = false) { Diff.apply(fs, diffFile, file1) }
    }

}
