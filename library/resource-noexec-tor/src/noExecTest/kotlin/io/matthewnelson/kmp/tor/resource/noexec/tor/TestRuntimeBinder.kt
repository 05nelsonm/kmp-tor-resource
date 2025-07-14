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
package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import kotlin.random.Random

object TestRuntimeBinder: ResourceLoader.RuntimeBinder {

    private val TEST_DIR: File by lazy {
        SysTempDir.resolve("kmp-tor_noexec")
    }

    val WORK_DIR: File by lazy {
        TEST_DIR.resolve(Random.Default.nextBytes(8).encodeToString(Base16))
    }

    val LOADER: ResourceLoader.Tor.NoExec by lazy {
        ResourceLoaderTorNoExec.getOrCreate(WORK_DIR) as ResourceLoader.Tor.NoExec
    }
}
