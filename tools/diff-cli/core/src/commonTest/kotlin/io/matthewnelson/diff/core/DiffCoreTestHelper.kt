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

import okio.Path
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.test.AfterTest
import kotlin.test.fail

abstract class DiffCoreTestHelper {

    protected val file1 = "/home/user/unsigned/file1".toPath()
    protected val file2 = "/home/user/signed/file2".toPath()
    protected val diffDir = "/home/user/diffs/".toPath()
    protected val fs = FakeFileSystem()

    @AfterTest
    fun tearDownUnitTest() {
        fs.checkNoOpenFiles()
    }

    protected fun Path.writeText(text: String, mustCreate: Boolean = false) {
        parent?.let { p -> fs.createDirectories(p, mustCreate = mustCreate) }
        fs.write(this, mustCreate = mustCreate) { writeUtf8(text) }
    }

    protected inline fun <reified T: Throwable> assertThrew(print: Boolean = false, block: () -> Unit) {
        try {
            block.invoke()
            fail()
        } catch (t: Throwable) {
            if (t::class != T::class) throw t
            if (print) t.printStackTrace()
        }
    }
}
