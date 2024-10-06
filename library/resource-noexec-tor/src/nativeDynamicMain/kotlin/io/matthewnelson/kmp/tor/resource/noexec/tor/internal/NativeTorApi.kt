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
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.SysTempDir
import io.matthewnelson.kmp.file.resolve
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.TorApi
import io.matthewnelson.kmp.tor.resource.noexec.tor.internal.DlOpenHandle.Companion.dlOpen
import kotlinx.cinterop.*
import kotlin.random.Random
import kotlin.system.getTimeNanos

// nativeDynamicMain
@OptIn(ExperimentalForeignApi::class, InternalKmpTorApi::class)
internal actual sealed class NativeTorApi
@Throws(IllegalStateException::class, IOException::class)
protected actual constructor(): TorApi() {

    protected actual fun getProviderVersion(): CPointer<ByteVar>? = _ptrGetProviderVersion.invoke()
    protected actual fun configurationNew(): CPointer<*>? = _ptrConfigurationNew.invoke()
    protected actual fun configurationFree(cfg: CPointer<*>) { _ptrConfigurationFree.invoke(cfg) }
    protected actual fun configurationSetCmdLine(
        cfg: CPointer<*>,
        argc: Int,
        argv: CArrayPointer<CPointerVar<ByteVar>>,
    ): Int = _ptrConfigurationSetCmdLine.invoke(cfg, argc, argv)
    protected actual fun run(cfg: CPointer<*>): Int = _ptrRun.invoke(cfg)

    private val _ptrGetProviderVersion: CPointer<CFunction<() -> CPointer<ByteVar>?>>
    private val _ptrConfigurationNew: CPointer<CFunction<() -> CPointer<*>?>>
    private val _ptrConfigurationFree: CPointer<CFunction<(CPointer<*>?) -> Unit>>
    private val _ptrConfigurationSetCmdLine: CPointer<CFunction<(CPointer<*>?, Int, CArrayPointer<CPointerVar<ByteVar>>?) -> Int>>
    private val _ptrRun: CPointer<CFunction<(CPointer<*>?) -> Int>>

    init {
        // TODO: Use UUID (Kotlin 2.0.0)
        @Suppress("DEPRECATION")
        val tempDir = Random(getTimeNanos()).nextBytes(16).let { bytes ->
            @OptIn(ExperimentalStdlibApi::class)
            SysTempDir.resolve("kmp-tor_${bytes.toHexString(HexFormat.UpperCase)}")
        }

        var libTor: File? = null
        var handle: DlOpenHandle? = null

        try {
            libTor = RESOURCE_CONFIG_LIB_TOR
                .extractTo(tempDir, onlyIfDoesNotExist = false)
                .getValue(ALIAS_LIB_TOR)

            handle = libTor.dlOpen()
            _ptrGetProviderVersion = handle.fDlSym("tor_api_get_provider_version")
            _ptrConfigurationNew = handle.fDlSym("tor_main_configuration_new")
            _ptrConfigurationFree = handle.fDlSym("tor_main_configuration_free")
            _ptrConfigurationSetCmdLine = handle.fDlSym("tor_main_configuration_set_command_line")
            _ptrRun = handle.fDlSym("tor_run_main")

            tempDir.deleteOnExit()
            libTor.deleteOnExit()
        } catch (t: Throwable) {
            try {
                handle?.dlClose()
            } catch (e: IllegalStateException) {
                t.addSuppressed(e)
            }

            libTor?.delete()
            tempDir.delete()

            if (t is IOException) throw t
            throw IllegalStateException("Failed to dynamically load tor library", t)
        }
    }
}
