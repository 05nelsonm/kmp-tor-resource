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
import okio.Path.Companion.toPath
import okio.use
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(InternalDiffApi::class)
class DiffIntegrationTest: DiffCoreTestHelper() {

    @Test
    fun givenDiff_whenDiffingFromLargerFile_thenApplicationSucceeds() {
        val f1Text = """
            Short
            File1
            Diff
        """.trimIndent()
        val f2Text = """
            Larger
            File2
            Diff
            With more data
        """.trimIndent()

        assertNotEquals(f1Text, f2Text)

        file1.writeText(f1Text)
        file2.writeText(f2Text)

        val diffFile = Diff.create(fs, file1, file2, diffDir)
        Diff.apply(fs, diffFile, file1)
        val f1ApplyText = fs.read(file1) { readUtf8() }

        assertEquals(f2Text, f1ApplyText)
    }

    @Test
    fun givenDiff_whenDiffingFromSmallerFile_thenApplicationSucceeds() {
        val f1Text = """
            FILE1
            Diff
        """.trimIndent()
        val f2Text = """
            FILE2
        """.trimIndent()

        assertNotEquals(f1Text, f2Text)

        file1.writeText(f1Text)
        file2.writeText(f2Text)

        val diffFile = Diff.create(fs, file1, file2, diffDir)
        Diff.apply(fs, diffFile, file1)
        val f1ApplyText = fs.read(file1) { readUtf8() }

        assertEquals(f2Text, f1ApplyText)
    }

    @Test
    fun givenDiff_whenDiffingEqualSizeFiles_thenApplicationSucceeds() {
        val f1Text = """
            FILE1
            Diff
        """.trimIndent()
        val f2Text = """
            FILE2
            Diff
        """.trimIndent()

        assertNotEquals(f1Text, f2Text)

        file1.writeText(f1Text)
        file2.writeText(f2Text)

        val diffFile = Diff.create(fs, file1, file2, diffDir)
        Diff.apply(fs, diffFile, file1)
        val f1ApplyText = fs.read(file1) { readUtf8() }

        assertEquals(f2Text, f1ApplyText)
    }

    @Test
    fun givenDiff_whenDiffingToEmptyFile_thenApplicationSucceeds() {
        val f2Text = """
            FILE2
            Diff
        """.trimIndent()

        fs.createDirectories(file1.parent!!, mustCreate = true)
        fs.sink(file1, mustCreate = true).use {  }
        file2.writeText(f2Text)

        val diffFile = Diff.create(fs, file1, file2, diffDir)
        Diff.apply(fs, diffFile, file1)
        val f1ApplyText = fs.read(file1) { readUtf8() }

        assertEquals(f2Text, f1ApplyText)
    }

    @Test
    fun givenDiff_whenDiffingFromEmptyFile_thenApplicationSucceeds() {
        val f1Text = """
            FILE1
            Diff
        """.trimIndent()

        file1.writeText(f1Text)
        fs.createDirectories(file2.parent!!, mustCreate = true)
        fs.sink(file2, mustCreate = true).use {  }

        val diffFile = Diff.create(fs, file1, file2, diffDir)
        Diff.apply(fs, diffFile, file1)
        val f1ApplyText = fs.read(file1) { readUtf8() }

        assertEquals("", f1ApplyText)
    }

}
