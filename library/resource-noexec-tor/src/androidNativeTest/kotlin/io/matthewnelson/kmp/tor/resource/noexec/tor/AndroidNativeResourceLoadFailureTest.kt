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

import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.ENV_KEY_LIBTOR
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv
import platform.posix.setenv

@OptIn(ExperimentalForeignApi::class)
class AndroidNativeResourceLoadFailureTest: AndroidResourceLoadFailureBaseTest() {
    override fun environmentLibTorGet(): String? {
        return getenv(ENV_KEY_LIBTOR)?.toKString()
    }
    override fun environmentLibTorSet(value: String) {
        setenv(ENV_KEY_LIBTOR, value, 1)
    }
}
