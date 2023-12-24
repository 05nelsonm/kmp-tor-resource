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

import kotlin.test.Test
import kotlin.test.assertEquals

class DiffSchemaUnitTest {

    @Test
    fun givenSchema_whenLatest_isSetProperly() {
        var valuesCount = 0
        val codes = mutableSetOf<Int>()

        val latestSchemaVersionCode = Diff.Schema.values().apply {
            sortBy {
                valuesCount++
                codes.add(it.code)
                it.code
            }
        }.last()

        // No duplicate code values
        assertEquals(valuesCount, codes.size)

        // latest() is actually set to the latest version
        assertEquals(latestSchemaVersionCode, Diff.Schema.latest())
    }
}
