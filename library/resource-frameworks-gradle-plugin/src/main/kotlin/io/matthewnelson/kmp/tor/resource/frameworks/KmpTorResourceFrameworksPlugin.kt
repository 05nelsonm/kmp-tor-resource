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
package io.matthewnelson.kmp.tor.resource.frameworks

import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.kmp.tor.resource.frameworks.internal.HASH_IOS_LIBTOR
import io.matthewnelson.kmp.tor.resource.frameworks.internal.HASH_IOS_LIBTOR_GPL
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.FileNotFoundException
import java.security.MessageDigest

public abstract class KmpTorResourceFrameworksPlugin internal constructor(): Plugin<Project> {

    final override fun apply(project: Project) {
        project.rootProject.let { rootProject ->
            check(rootProject == project) {
                "${this::class.java} can only be applied to the $rootProject"
            }
        }

        val extension = project.extensions.create(
            KmpTorResourceFrameworksExtension.NAME,
            KmpTorResourceFrameworksExtension::class.java,
        ).apply { torGPL.convention(false) }

        project.afterEvaluate {
            project.extractFrameworks(extension)
        }
    }

    private fun Project.extractFrameworks(extension: KmpTorResourceFrameworksExtension) {
        val extractDir = layout.buildDirectory.get().asFile.resolve("kmp-tor-resource")
        val libTorXCFrameworkDir = extractDir.resolve("LibTor.xcframework")
        val libTorIosFrameworkDir = libTorXCFrameworkDir.resolve("ios").resolve("LibTor.framework")

        if (!libTorIosFrameworkDir.exists() && !libTorIosFrameworkDir.mkdirs()) {
            throw FileNotFoundException("Failed to create directory $libTorIosFrameworkDir")
        }

        listOf(
            libTorXCFrameworkDir to INFO_PLIST_XC,
            libTorIosFrameworkDir to INFO_PLIST_IOS,
        ).forEach plist@ { (frameworkDir, plistText) ->
            val infoPlist = frameworkDir.resolve("Info.plist")
            if (infoPlist.exists()) {
                if (infoPlist.readText() == plistText) {
                    return@plist
                }
            }

            infoPlist.writeText(plistText)
        }

        val (suffix, expectedHash) = if (extension.torGPL.get()) {
            "-gpl" to HASH_IOS_LIBTOR_GPL
        } else {
            "" to HASH_IOS_LIBTOR
        }

        val libTor = libTorIosFrameworkDir.resolve("LibTor")

        if (libTor.exists()) {
            val actualHash = MessageDigest
                .getInstance("SHA-256")
                .digest(libTor.readBytes())
                .encodeToString(Base16)

            if (actualHash == expectedHash) {
                return
            }

            libTor.delete()
        }

        "/io/matthewnelson/kmp/tor/resource/frameworks/native/tor$suffix/ios/LibTor".let { resourcePath ->
            extension::class.java.getResourceAsStream(resourcePath)
                ?: throw FileNotFoundException(resourcePath)
        }.use { iStream ->
            libTor.outputStream().use { oStream ->
                val buf = ByteArray(4096)
                while (true) {
                    val read = iStream.read(buf)
                    if (read == -1) break
                    oStream.write(buf, 0, read)
                }
            }
        }
    }

    private companion object {

        private const val INFO_PLIST_XC = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
	<key>AvailableLibraries</key>
	<array>
		<dict>
			<key>BinaryPath</key>
			<string>LibTor.framework/LibTor</string>
			<key>LibraryIdentifier</key>
			<string>ios</string>
			<key>LibraryPath</key>
			<string>LibTor.framework</string>
			<key>SupportedArchitectures</key>
			<array>
				<string>arm64</string>
			</array>
			<key>SupportedPlatform</key>
			<string>ios</string>
		</dict>
	</array>
	<key>CFBundlePackageType</key>
	<string>XFWK</string>
	<key>XCFrameworkFormatVersion</key>
	<string>1.0</string>
</dict>
</plist>
"""

        private const val INFO_PLIST_IOS = """<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleExecutable</key>
    <string>LibTor</string>
    <key>CFBundleIdentifier</key>
    <string>io.matthewnelson.kmp.tor.resource.lib.tor</string>
    <key>CFBundleInfoDictionaryVersion</key>
    <string>6.0</string>
    <key>CFBundleName</key>
    <string>LibTor</string>
    <key>CFBundlePackageType</key>
    <string>FMWK</string>
    <key>CFBundleShortVersionString</key>
    <string>1.0</string>
    <key>CFBundleSupportedPlatforms</key>
    <array>
        <string>iPhoneOS</string>
    </array>
    <key>CFBundleVersion</key>
    <string>1</string>
    <key>MinimumOSVersion</key>
    <string>12.0</string>
    <key>UIDeviceFamily</key>
    <array>
        <integer>1</integer>
        <integer>2</integer>       
    </array>
    <key>UIRequiredDeviceCapabilities</key>
    <array>
        <string>arm64</string>
    </array>
</dict>
</plist>
"""
    }
}
