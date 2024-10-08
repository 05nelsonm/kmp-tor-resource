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
package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import kotlinx.cinterop.*
import platform.windows.*

internal fun lastError(): String {
    @OptIn(ExperimentalForeignApi::class)
    return memScoped {
        val messageMaxSize = 2048
        val message = allocArray<ByteVar>(messageMaxSize)

        FormatMessageA(
            dwFlags = (FORMAT_MESSAGE_FROM_SYSTEM or FORMAT_MESSAGE_IGNORE_INSERTS).toUInt(),
            lpSource = null,
            dwMessageId = GetLastError(),
            dwLanguageId = (SUBLANG_DEFAULT * 1024 + LANG_NEUTRAL).toUInt(), // MAKELANGID macro.
            lpBuffer = message,
            nSize = messageMaxSize.toUInt(),
            Arguments = null
        )

        message.toKString().trim()
    }
}
