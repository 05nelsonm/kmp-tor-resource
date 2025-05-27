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
package io.matthewnelson.kmp.tor.resource.compilation.lib.tor

import android.system.Os
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.ENV_KEY_LIBTOR
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.ENV_KEY_LIBTOREXEC
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.ENV_KEY_LIBTORJNI
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class LibTorInitializerTest {

    @Test
    fun givenEnvironmentVariable_whenKeyLibTor_thenIsPresent() {
        assertNotNull(Os.getenv(ENV_KEY_LIBTOR))
    }

    @Test
    fun givenEnvironmentVariable_whenKeyLibTorExec_thenIsNotPresent() {
        assertNull(Os.getenv(ENV_KEY_LIBTOREXEC))
    }

    @Test
    fun givenEnvironmentVariable_whenKeyLibTorJni_thenIsNotPresent() {
        assertNull(Os.getenv(ENV_KEY_LIBTORJNI))
    }
}
