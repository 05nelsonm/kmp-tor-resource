/*
 * Copyright (c) 2024 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.exec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibTorExec(): Map<String, File> = this

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun MutableMap<String, String>.configureProcessEnvironment(resourceDir: File) {
    if (OSInfo.INSTANCE.osHost !is OSHost.Linux.Android) return

    // Only necessary for Android API 23 and below, but set it nonetheless
    setLD_LIBRARY_PATH(resourceDir)
}
