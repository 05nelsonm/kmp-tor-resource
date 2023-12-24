/*
 * Copyright (c) 2023 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.initializer

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import java.io.File

public class KmpTorResourceInitializer: Initializer<KmpTorResourceInitializer.Impl> {

    public class Impl private constructor() {

        public companion object {
            @JvmField
            public val INSTANCE: Impl = Impl()
        }

        @get:JvmName("nativeLibraryDir")
        public var nativeLibraryDir: File? = null
            private set

        @get:JvmName("isInitialized")
        public val isInitialized: Boolean get() = nativeLibraryDir != null

        public fun findLib(name: String): File? {
            nativeLibraryDir?.walkTopDown()
                ?.iterator()
                ?.forEach { file ->
                    if (file.isFile && file.name == name) {
                        return file
                    }
                }

            return null
        }

        @Throws(IllegalStateException::class)
        public fun requireLib(name: String): File = findLib(name)
            ?: throw IllegalStateException("Failed to find lib[$name]")

        @JvmSynthetic
        internal fun init(context: Context) {
            nativeLibraryDir = File(context.applicationInfo.nativeLibraryDir)
        }
    }

    override fun create(context: Context): Impl {
        val appInitializer = AppInitializer.getInstance(context)
        check(appInitializer.isEagerlyInitialized(javaClass)) {
            errorMsg()
        }
        Impl.INSTANCE.init(context)
        return Impl.INSTANCE
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    public companion object {

        @JvmStatic
        public fun errorMsg(): String = ERROR_MSG.trimIndent()

        private const val ERROR_MSG: String = """
            KmpTorResourceInitializer cannot be initialized lazily.
            Please ensure that you have:
            <meta-data
                android:name='io.matthewnelson.kmp.tor.resource.initializer.KmpTorResourceInitializer'
                android:value='androidx.startup' />
            under InitializationProvider in your AndroidManifest.xml
        """
    }
}
