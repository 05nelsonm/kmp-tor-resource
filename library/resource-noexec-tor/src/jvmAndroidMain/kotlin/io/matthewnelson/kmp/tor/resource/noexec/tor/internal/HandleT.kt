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

@JvmInline
internal actual value class HandleT private actual constructor(private actual val _ptr: Any) {

    internal val ptr: Pointer get() = _ptr as Pointer

    internal class Pointer {

        @Volatile
        private var pointer: Long = -1L

        internal fun isSet(): Boolean = pointer != -1L
    }

    internal companion object {

        @JvmSynthetic
        internal fun Pointer.toHandleTOrNull(): HandleT? {
            if (!isSet()) return null
            return HandleT(this)
        }
    }
}
