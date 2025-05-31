/*
 * Copyright (c) 2025 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.TestRuntimeBinder.LOADER
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

/**
 * On Android emulator and Android Native we are able to modify the environment
 * variables that point to libtor.so. This allows us to test kmp_tor.c implementation
 * when a failure occurs
 * */
abstract class AndroidResourceLoadFailureBaseTest {

    protected abstract fun environmentLibTorGet(): String?
    protected abstract fun environmentLibTorSet(value: String)

    @Test
    fun givenKmpTorC_whenFailureToLoadLibTor_thenThrowsException() {
        if (!CAN_RUN_FULL_TESTS) {
            println("Skipping...")
            return
        }

        LOADER.withApi(TestRuntimeBinder) {
            torRunMain(listOf("--version"))
            assertEquals(0, terminateAndAwaitResult())

            val before = environmentLibTorGet()
            assertNotNull(before)
            environmentLibTorSet(before.replace("libtor.so", "libtor_fail.so"))

            try {
                torRunMain(listOf("--version"))
                fail("torRunMain did not throw exception")
            } catch (t: IllegalStateException) {
                assertEquals("Failed to load tor", t.message)
                assertEquals(TorApi.State.OFF, state())
                // pass
            } finally {
                environmentLibTorSet(before)
            }

            torRunMain(listOf("--version"))
            assertEquals(0, terminateAndAwaitResult())
        }
    }
}
