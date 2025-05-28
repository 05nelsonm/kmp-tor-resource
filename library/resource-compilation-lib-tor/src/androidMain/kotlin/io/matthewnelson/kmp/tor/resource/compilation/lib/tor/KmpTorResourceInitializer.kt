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
import io.matthewnelson.kmp.tor.resource.compilation.lib.tor.internal.envKeyLibName

internal class KmpTorResourceInitializer internal constructor(): androidx.startup.Initializer<KmpTorResourceInitializer.Companion> {

    internal companion object {}

    override fun create(context: Context): Companion {
        val appInitializer = AppInitializer.getInstance(context)
        check(appInitializer.isEagerlyInitialized(javaClass)) {
            """
                KmpTorResourceInitializer cannot be initialized lazily.
                Please ensure that you have:
                <meta-data
                    android:name='${KmpTorResourceInitializer::class.qualifiedName}'
                    android:value='androidx.startup' />
                under InitializationProvider in your AndroidManifest.xml
            """.trimIndent()
        }

        var loader = context.classLoader

        val keys = arrayOf(ENV_KEY_LIBTOR, ENV_KEY_LIBTOREXEC, ENV_KEY_LIBTORJNI)
        val libs = arrayOfNulls<String>(keys.size)

        while (loader != null) {
            if (loader is BaseDexClassLoader) {
                for (i in keys.indices) {
                    var lib = libs[i]
                    if (lib.isNullOrBlank()) {
                        val name = keys[i].envKeyLibName()
                            .substringAfter("lib")
                            .substringBeforeLast(".so")

                        lib = loader.findLibrary(name)

                        if (!lib.isNullOrBlank()) {
                            libs[i] = lib
                        }
                    }
                }
            }

            loader = loader.parent
        }

        for (i in keys.indices) {
            val lib = libs[i]
            if (lib.isNullOrBlank()) continue
            Os.setenv(/* name = */ keys[i], /* value = */ lib, /* overwrite = */ true)
        }

        return Companion
    }

    override fun dependencies(): List<Class<out androidx.startup.Initializer<*>>> = emptyList()
}
