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
@file:Suppress("ClassName", "ConstPropertyName")

package io.matthewnelson.kmp.tor.resource.exec.tor.internal

// This is an automatically generated file.
// DO NOT MODIFY

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.NativeResource

@OptIn(InternalKmpTorApi::class)
internal object resource_tor_exe_local: NativeResource(
    version = 1,
    name = "tor.exe.local",
    size = 130L,
    sha256 = "5c9a9e1bafa716bdd763e1add00ad10d1ceb7e207c64a770d1eb45be04a7a096",
    chunks = 1L,
) {

    @Throws(IllegalStateException::class, IndexOutOfBoundsException::class)
    override operator fun get(index: Long): Chunk = when (index) {
        0L -> _0
        else -> throw IndexOutOfBoundsException()
    }.toChunk()

    private const val _0 =
"""IyBodHRwczovL2xlYXJuLm1pY3Jvc29mdC5jb20vZW4tdXMvd2luZG93cy93aW4z
Mi9kbGxzL2R5bmFtaWMtbGluay1saWJyYXJ5LXJlZGlyZWN0aW9uI2hvdy10by1y
ZWRpcmVjdC1kbGxzLWZvci11bnBhY2thZ2VkLWFwcHMKCg=="""

}
