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
package io.matthewnelson.kmp.tor.resource.compilation.lib.tor

import android.content.Context
import android.system.Os
import androidx.startup.AppInitializer
import dalvik.system.BaseDexClassLoader
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.ENV_KEY_LIBTOR
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.ENV_KEY_LIBTOREXEC
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.ENV_KEY_LIBTORJNI

internal class LibTorInitializer internal constructor(): androidx.startup.Initializer<LibTorInitializer.Init> {

    internal sealed class Init
    private object RealInit: Init()

    override fun create(context: Context): Init {
        val appInitializer = AppInitializer.getInstance(context)
        check(appInitializer.isEagerlyInitialized(javaClass)) {
            """
                LibTorInitializer cannot be initialized lazily.
                Please ensure that you have:
                <meta-data
                    android:name='${LibTorInitializer::class.qualifiedName}'
                    android:value='androidx.startup' />
                under InitializationProvider in your AndroidManifest.xml
            """.trimIndent()
        }

        var loader = context.classLoader
        var libtor: String? = null
        var libtorexec: String? = null
        var libtorjni: String? = null

        while (loader != null) {
            if (loader is BaseDexClassLoader) {
                if (libtor == null) {
                    libtor = loader.findLibrary("tor")
                }
                if (libtorexec == null) {
                    libtorexec = loader.findLibrary("torexec")
                }
                if (libtorjni == null) {
                    libtorjni = loader.findLibrary("torjni")
                }
            }
            if (libtor != null && libtorexec != null && libtorjni != null) break
            loader = loader.parent
        }

        if (libtor != null) {
            Os.setenv(/* name = */ ENV_KEY_LIBTOR, /* value = */ libtor, /* overwrite = */ true)
        }
        if (libtorexec != null) {
            Os.setenv(/* name = */ ENV_KEY_LIBTOREXEC, /* value = */ libtorexec, /* overwrite = */ true)
        }
        if (libtorjni != null) {
            Os.setenv(/* name = */ ENV_KEY_LIBTORJNI, /* value = */ libtorjni, /* overwrite = */ true)
        }

        return RealInit
    }

    override fun dependencies(): List<Class<out androidx.startup.Initializer<*>>> = emptyList()
}
