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

import io.matthewnelson.kmp.file.IOException
import kotlinx.cinterop.*

// appleMobileMain
@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
internal actual sealed class LibTor
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(): AutoCloseable {

    protected actual open fun getProviderVersion(): CPointer<ByteVar>? = tor_api_get_provider_version()
    protected actual open fun configurationNew(): CPointer<*>? = tor_main_configuration_new()
    protected actual open fun configurationFree(cfg: CPointer<*>) { tor_main_configuration_free(cfg.reinterpret()) }
    protected actual open fun configurationSetCmdLine(
        cfg: CPointer<*>,
        argc: Int,
        argv: CArrayPointer<CPointerVar<ByteVar>>,
    ): Int = tor_main_configuration_set_command_line(cfg.reinterpret(), argc, argv)
    protected actual open fun runMain(cfg: CPointer<*>): Int = tor_run_main(cfg.reinterpret())
    actual final override fun close() { /* TODO */ }
}
