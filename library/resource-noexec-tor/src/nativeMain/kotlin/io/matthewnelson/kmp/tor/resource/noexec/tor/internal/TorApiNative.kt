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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "UnnecessaryOptInAnnotation")

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.IOException
import kotlinx.cinterop.*
import platform.posix.usleep
import kotlin.time.Duration.Companion.milliseconds

@Throws(IllegalStateException::class, IOException::class)
internal actual fun loadTorApi(): AbstractTorApi = KmpTorApi()

private class KmpTorApi: AbstractTorApi() {

    //    @Throws(IllegalStateException::class, IOException::class)
    @OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
    override fun torRunMainProtected(args: Array<String>, log: Logger): Int {
        val rv = try {
            RealLibTor.open().use { libTor ->
                val cfg = libTor.configurationNew()
                    ?: throw IllegalStateException("Failed to acquire new tor_main_configuration_t")

                memScoped {
                    try {
                        val argv = args.toCStringArray(autofreeScope = this)
                        check(libTor.configurationSetCmdLine(cfg, args.size, argv) == 0) {
                            "Failed to set tor_main_configuration_t arguments"
                        }

                        libTor.runMain(cfg)
                    } finally {
                        libTor.configurationFree(cfg)
                    }
                }
            }
        } finally {
            usleep(5.milliseconds.inWholeMicroseconds.toUInt())
        }

        return if (rv < 0 || rv > 255) 1 else rv
    }
}

@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
internal expect sealed class LibTor
@Throws(IllegalStateException::class, IOException::class)
protected constructor(): AutoCloseable {

    protected open fun getProviderVersion(): CPointer<ByteVar>?
    protected open fun configurationNew(): CPointer<*>?
    protected open fun configurationFree(cfg: CPointer<*>)
    protected open fun configurationSetCmdLine(
        cfg: CPointer<*>,
        argc: Int,
        argv: CArrayPointer<CPointerVar<ByteVar>>,
    ): Int
    protected open fun runMain(cfg: CPointer<*>): Int

    final override fun close()
}

@Suppress("RedundantVisibilityModifier")
@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
private class RealLibTor
@Throws(IllegalStateException::class, IOException::class)
private constructor(): LibTor() {

    private val delegate = Any()

    public override fun getProviderVersion(): CPointer<ByteVar>? = super.getProviderVersion()
    public override fun configurationNew(): CPointer<*>? = super.configurationNew()
    public override fun configurationFree(cfg: CPointer<*>) { super.configurationFree(cfg) }
    public override fun configurationSetCmdLine(
        cfg: CPointer<*>,
        argc: Int,
        argv: CArrayPointer<CPointerVar<ByteVar>>,
    ): Int = super.configurationSetCmdLine(cfg, argc, argv)
    public override fun runMain(cfg: CPointer<*>): Int = super.runMain(cfg)

    public override fun equals(other: Any?): Boolean = delegate == other
    public override fun hashCode(): Int = delegate.hashCode()
    public override fun toString(): String = "RealLibTor@${delegate.hashCode()}"

    internal companion object {
        internal fun open(): RealLibTor = RealLibTor()
    }
}
