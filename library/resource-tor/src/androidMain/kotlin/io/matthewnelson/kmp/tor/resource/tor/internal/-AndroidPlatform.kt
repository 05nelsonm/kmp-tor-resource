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
package io.matthewnelson.kmp.tor.resource.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.resource.tor.TorResources
import io.matthewnelson.kmp.tor.core.resource.initializer.KmpTorResourceInitializer
import io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi
import io.matthewnelson.kmp.tor.core.resource.ImmutableMap
import io.matthewnelson.kmp.tor.core.resource.OSHost
import io.matthewnelson.kmp.tor.core.resource.OSInfo
import io.matthewnelson.kmp.tor.core.resource.Resource

// Android
@get:JvmSynthetic
@OptIn(InternalKmpTorApi::class)
internal actual val RESOURCE_CONFIG: Resource.Config by lazy {
    Resource.Config.create {
        val clazz = TorResources::class.java

        resource(ALIAS_GEOIP) {
            isExecutable = false

            platform {
                resourceClass = clazz
                resourcePath = PATH_RESOURCE_GEOIP
            }
        }

        resource(ALIAS_GEOIP6) {
            isExecutable = false

            platform {
                resourceClass = clazz
                resourcePath = PATH_RESOURCE_GEOIP6
            }
        }

        if (OSInfo.INSTANCE.isAndroidRuntime()) {
            // Is Android Runtime.
            //
            // Binaries are extracted on application install
            // to the nativeLib directory. This is required as
            // android does not allow execution from the app dir
            // (cannot download executables and run them).
            if (KmpTorResourceInitializer.Impl.INSTANCE.findLib("libtor.so") != null) {
                return@create
            }

            if (KmpTorResourceInitializer.Impl.INSTANCE.isInitialized) {
                error("""
                    Faild to find libtor.so within nativeLibraryDir
        
                    Ensure the following are set correctly:
                    build.gradle(.kts):  'android.packaging.jniLibs.useLegacyPackaging' is set to 'true'
                    AndroidManifest.xml: 'android:extractNativeLibs' is set to 'true'
                    gradle.properties:   'android.bundle.enableUncompressedNativeLibs' is set to 'false'
                """.trimIndent())
            } else {
                error(KmpTorResourceInitializer.errorMsg())
            }

            return@create
        }

        // Android Unit Test. Check for support via resource-android-unit-test
        val loader = "io.matthewnelson.kmp.tor.resource.android.unit.test.Loader"

        val loaderClass = try {
            Class.forName(loader)
        } catch (_: Throwable) {
            null
        }

        if (loaderClass == null) {
            error("""
                Failed to find class $loader
                Missing dependency for Android Unit Tests?
    
                Try adding the 'resource-android-unit-test' dependency
                as testImplementation
            """.trimIndent())
            return@create
        }

        val host = OSInfo.INSTANCE.osHost

        if (host is OSHost.Unknown) {
            error("Unknown host[$host]")
            return@create
        }

        val arch = OSInfo.INSTANCE.osArch

        val torResourcePath = host.toTorResourcePathOrNull(arch)

        if (torResourcePath == null) {
            error("Unsupported architecutre[$arch] for host[$host]")
            return@create
        }

        resource(ALIAS_TOR) {
            isExecutable = true

            platform {
                resourceClass = loaderClass
                resourcePath = torResourcePath
            }
        }
    }
}

@JvmSynthetic
@Throws(IllegalStateException::class)
@OptIn(InternalKmpTorApi::class)
internal actual fun ImmutableMap<String, File>.findLibTor(): Map<String, File> {
    if (contains(ALIAS_TOR)) return this

    val lib = KmpTorResourceInitializer.Impl.INSTANCE.requireLib("libtor.so")
    return toMutableMap().apply { put(ALIAS_TOR, lib) }
}
