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

import io.matthewnelson.diff.core.internal.InternalDiffApi
import kotlinx.datetime.toInstant
import okio.Path.Companion.toPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(InternalDiffApi::class)
class OptionsUnitTest: DiffCoreTestHelper() {

    @Test
    fun givenOptionsCreate_whenStaticTime_thenWritesStaticTime() {
        file1.writeText("asdfasdf")
        file2.writeText("aaaa")
        val diffFile = Diff.create(fs, file1, file2, diffDir, Options.Create {
            useStaticTime = true
        })
        val header = Diff.readHeader(fs, diffFile)
        assertEquals(Options.Create.STATIC_TIME.toInstant(), header.createdAtInstant)
    }

    @Test
    fun givenOptionsCreate_whenDifferentExtension_thenUses() {
        file1.writeText("asdfasdf")
        file2.writeText("aaaa")
        val expected = ".signature"
        val diffFile = Diff.create(fs, file1, file2, diffDir, Options.Create {
            diffFileExtensionName(expected)
        })
        assertTrue(diffFile.name.endsWith(expected))
    }

    @Test
    fun givenOptionsApply_whenDryRunTrue_thenLeavesFileUnmodified() {
        val t1 = "some text"
        val t2 = "emos text"
        file1.writeText(t1)
        file2.writeText(t2)

        val diffFile = Diff.create(fs, file1, file2, diffDir)

        Diff.apply(fs, diffFile, file1, Options.Apply { dryRun = true })

        // file1 was not modified
        assertEquals(t1, fs.read(file1) { readUtf8() })

        // bak still here and is the modified file
        val bak = "$file1.bak".toPath()
        assertTrue(fs.exists(bak))
        assertEquals(t2, fs.read(bak) { readUtf8() })
    }

}
