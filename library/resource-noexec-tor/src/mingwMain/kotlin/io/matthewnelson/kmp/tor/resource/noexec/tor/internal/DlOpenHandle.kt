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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.absolutePath
import kotlinx.cinterop.*
import platform.windows.*

@OptIn(ExperimentalForeignApi::class)
internal actual value class DlOpenHandle private actual constructor(private actual val ptr: CPointer<*>) {

    @Throws(IllegalStateException::class)
    internal actual fun dlSym(name: String): CPointer<out CPointed> = GetProcAddress(ptr.reinterpret(), name)
        ?: throw IllegalStateException("GetProcAddress failed for name[$name]. LastError >> ${lastError()}")

    @Throws(IllegalStateException::class)
    internal actual fun dlClose() {
        // https://learn.microsoft.com/en-us/windows/win32/api/libloaderapi/nf-libloaderapi-freelibrary
        check(FreeLibrary(ptr.reinterpret()) != 0) { "FreeLibrary failed. LastError >> ${lastError()}" }
    }

    internal actual companion object {

        @Throws(IllegalStateException::class)
        internal actual fun File.dlOpen(): DlOpenHandle {
            val ptr = LoadLibraryExW(absolutePath, NULL, DONT_RESOLVE_DLL_REFERENCES.convert())
                ?: LoadLibraryW(absolutePath)

            check(ptr != null) { "LoadLibrary failed for lib [$this]. LastError >> ${lastError()}" }

            return DlOpenHandle(ptr)
        }
    }
}
