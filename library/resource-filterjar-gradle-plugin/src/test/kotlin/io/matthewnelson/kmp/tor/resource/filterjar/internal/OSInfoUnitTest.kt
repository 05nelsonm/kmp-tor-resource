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
package io.matthewnelson.kmp.tor.resource.filterjar.internal

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSArch
import io.matthewnelson.kmp.tor.common.core.OSHost
import kotlin.test.*

@OptIn(InternalKmpTorApi::class)
internal class OSInfoUnitTest {

    @Test
    fun givenHost_whenCurrent_thenResolves() {
        "current".toOSHost()
    }

    @Test
    fun givenHost_whenLinuxMusl_thenThrowsException() {
        assertFailsWith<IllegalArgumentException>{ "linux-musl".toOSHost() }
    }

    @Test
    fun givenHost_whenFreeBSD_thenThrowsException() {
        assertFailsWith<IllegalArgumentException>{ "freebsd".toOSHost() }
    }

    @Test
    fun givenHost_whenWindows_thenResolves() {
        assertIs<OSHost.Windows>("windows".toOSHost())
    }

    @Test
    fun givenArch_whenAll_thenReturnsAllArches() {
        val arches = setOf("all").toOSArches()
        assertContains(arches, OSArch.Aarch64)
        assertContains(arches, OSArch.Armv7)
        assertContains(arches, OSArch.Ppc64)
        assertContains(arches, OSArch.X86)
        assertContains(arches, OSArch.X86_64)
        assertEquals(5, arches.size)
        assertEquals(0, arches.filterIsInstance<OSArch.Unsupported>().size)
    }

    @Test
    fun givenArch_whenCurrent_thenResolvesCurrent() {
        val arches = setOf("current").toOSArches()
        assertEquals(1, arches.size)
        assertEquals(0, arches.filterIsInstance<OSArch.Unsupported>().size)
    }
}
