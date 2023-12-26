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
 **/package io.matthewnelson.kmp.tor.resource.validation

import com.android.build.api.dsl.LibraryExtension
import io.matthewnelson.encoding.base16.Base16
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import java.io.File
import java.io.IOException
import java.security.MessageDigest

sealed class ResourceValidation(
    protected val project: Project,
    protected val moduleName: String,
    protected val modulePackageName: String,
) {

    protected abstract val androidLibHashes: Set<AndroidLibHash>
    protected abstract val jvmLibHashes: Set<JvmLibHash>
    // <SourceSet name, Set<hashes>>
    protected abstract val nativeResourceHashes: Map<String, Set<NativeResourceHash>>

    private val androidJniErrors = mutableSetOf<String>()
    private val jvmErrors = mutableSetOf<String>()
    // <SourceSet name, errors>
    private val nativeErrors = mutableMapOf<String, MutableSet<String>>()

    private var isAndroidConfigured = false
    private var isJvmConfigured = false
    private var isNativeConfigured = false

    protected val rootProjectDir: File = project.rootProject.projectDir
    private val reportDir: File get() = project.layout
        .buildDirectory
        .asFile
        .get()
        .resolve("reports")
        .resolve("resource-validation")
        .resolve(moduleName)

    protected fun configureAndroidJniResourcesProtected() {
        check(androidLibHashes.isNotEmpty()) { "androidLibHashes cannot be empty" }
        check(project.plugins.hasPlugin("com.android.library")) {
            "The 'com.android.library' plugin is required to utilize this function"
        }

        project.extensions.getByName<LibraryExtension>("android").apply {
            configureAndroidJniResources()
        }
    }

    private fun LibraryExtension.configureAndroidJniResources() {
        if (isAndroidConfigured) return

        packaging {
            jniLibs.useLegacyPackaging = true
        }

        defaultConfig {
            ndk {
                abiFilters.add("arm64-v8a")
                abiFilters.add("armeabi-v7a")
                abiFilters.add("x86")
                abiFilters.add("x86_64")
            }
        }

        sourceSets.getByName("main") {
            val packageDir = rootProjectDir
                .resolve("external")
                .resolve("build")
                .resolve("package")
                .resolve(moduleName)

            androidLibHashes.forEach { libHash ->
                val errors = libHash.validate(packageResourceDir = packageDir)

                if (errors.isNotEmpty()) {
                    this@ResourceValidation.androidJniErrors.addAll(errors)

                    // Use mock resources
                    jniLibs.srcDir(rootProjectDir
                        .resolve("library")
                        .resolve(moduleName)
                        .resolve("mock-resources")
                        .resolve("src")
                        .resolve("androidMain")
                        .resolve("jniLibs")
                    )

                    return@forEach
                }

                // Use external/build/package libs
                jniLibs.srcDir(packageDir
                    .resolve("src")
                    .resolve("androidMain")
                    .resolve("jniLibs")
                )
            }
        }

        isAndroidConfigured = true
        generateReport(reportFileName = "android", errors = androidJniErrors)
    }

    protected fun jvmLibResourcesSrcDirProtected(): File {
        check(jvmLibHashes.isNotEmpty()) { "jvmLibHashes cannot be empty" }

        val mockResourcesSrc = rootProjectDir
            .resolve("library")
            .resolve(moduleName)
            .resolve("mock-resources")
            .resolve("src")
            .resolve("jvmMain")
            .resolve("resources")

        val externalResourcesSrc = rootProjectDir
            .resolve("external")
            .resolve("build")
            .resolve("package")
            .resolve(moduleName)
            .resolve("src")
            .resolve("jvmMain")
            .resolve("resources")

        if (isJvmConfigured) {
            return if (jvmErrors.isEmpty()) {
                externalResourcesSrc
            } else {
                mockResourcesSrc
            }
        }

        val nativeResourcesDir = externalResourcesSrc
            .resolve(modulePackageName.replace('.', '/'))
            .resolve("native")

        jvmLibHashes.forEach { libHash ->
            val error = libHash.validate(nativeResourcesDir)

            if (error != null) {
                jvmErrors.add(error)
            }
        }

        isJvmConfigured = true
        generateReport(reportFileName = "jvm", errors = jvmErrors)

        return if (jvmErrors.isEmpty()) {
            // No errors found, return the external dir
            // resources to be configured
            externalResourcesSrc
        } else {
            mockResourcesSrc
        }
    }

    protected fun configureNativeResourcesProtected() {
        check(nativeResourceHashes.isNotEmpty()) { "nativeResourceHashes cannot be empty" }
        check(project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            "The 'org.jetbrains.kotlin.multiplatform' plugin is required to utilize this function"
        }

        project.extensions.getByName<KotlinMultiplatformExtension>("kotlin").apply {
            configureNativeResources()
        }
    }

    private fun KotlinMultiplatformExtension.configureNativeResources() {
        check(nativeResourceHashes.isNotEmpty()) { "nativeResourceHashes cannot be empty" }
        check(project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            "The 'org.jetbrains.kotlin.multiplatform' plugin is required to utilize this function"
        }

        if (isNativeConfigured) return

        val mockResourceModuleDir = rootProjectDir
            .resolve("library")
            .resolve(moduleName)
            .resolve("mock-resources")

        val packageModuleDir = rootProjectDir
            .resolve("external")
            .resolve("build")
            .resolve("package")
            .resolve(moduleName)

        with(sourceSets) {
            nativeResourceHashes.forEach { (sourceSet, hashes) ->
                var errors = nativeErrors[sourceSet]

                if (errors == null) {
                    errors = mutableSetOf()
                    nativeErrors[sourceSet] = errors
                }

                val srcSet = findByName("${sourceSet}Main") ?: return@forEach

                // check all entries for given source set
                for (hash in hashes) {
                    val error = hash.validate(sourceSet, modulePackageName, packageModuleDir) ?: continue
                    errors.add(error)
                }

                val moduleDir = if (errors.isEmpty()) packageModuleDir else mockResourceModuleDir

                srcSet.kotlin.srcDir(NativeResourceHash.kotlinSrcDir(sourceSet, moduleDir))
            }
        }

        isNativeConfigured = true
        nativeErrors.forEach { (sourceSet, errors) ->
            generateReport(reportFileName = sourceSet, errors = errors)
        }
    }

    protected fun generateReport(reportFileName: String, errors: Set<String>) {
        val dir = reportDir
        if (!dir.exists()) dir.mkdirs()

        dir.resolve("$reportFileName.err").writeText(buildString {
            errors.forEach { appendLine(it) }
        })
    }

    data class AndroidLibHash internal constructor(
        val libname: String,
        val hashArm64: String,
        val hashArmv7: String,
        val hashX86: String,
        @Suppress("PropertyName")
        val hashX86_64: String,
    ) {

        internal fun validate(
            packageResourceDir: File
        ): List<String> {
            val errors = mutableListOf<String>()

            listOf(
                "arm64-v8a" to hashArm64,
                "armeabi-v7a" to hashArmv7,
                "x86" to hashX86,
                "x86_64" to hashX86_64,
            ).forEach { (arch, hash) ->
                val lib = packageResourceDir
                    .resolve("src")
                    .resolve("androidMain")
                    .resolve("jniLibs")
                    .resolve(arch)
                    .resolve(libname)

                if (!lib.exists()) {
                    errors.add("Lib does not exist: $lib")
                    return@forEach
                }

                val actualHash = lib.sha256()
                if (hash != actualHash) {
                    errors.add("Lib hash[$actualHash] did not match expected[$hash]: $lib")
                    return@forEach
                }
            }

            return errors
        }
    }

    data class JvmLibHash internal constructor(
        val libname: String,
        val machine: String,
        val arch: String,
        val hash: String,
    ) {

        internal fun validate(
            nativeResourceDir: File
        ): String? {
            val lib = nativeResourceDir
                .resolve(machine)
                .resolve(arch)
                .resolve(libname)

            if (!lib.exists()) {
                return "Lib does not exist: $lib"
            }

            val actualHash = lib.sha256()
            if (hash != actualHash) {
                return "Lib hash[$actualHash] did not match expected[$hash]: $lib"
            }

            return null
        }
    }

    data class NativeResourceHash internal constructor(
        val ktFileName: String,
        val hash: String,
    ) {

        internal companion object {

            @JvmStatic
            internal fun kotlinSrcDir(
                sourceSetName: String,
                moduleDir: File,
            ): File {
                return moduleDir
                    .resolve("src")
                    .resolve("${sourceSetName}Main")
                    .resolve("kotlin")
            }
        }

        internal fun validate(
            sourceSetName: String,
            modulePackageName: String,
            packageModuleDir: File,
        ): String? {
            val file = kotlinSrcDir(sourceSetName, packageModuleDir)
                .resolve(modulePackageName.replace('.', '/'))
                .resolve("internal")
                .resolve(ktFileName)

            if (!file.exists()) {
                return "File does not exist: $file"
            }

            try {
                file.inputStream().bufferedReader().use { reader ->
                    while (true) {
                        val line = reader.readLine()

                        if (!line.startsWith("    sha256 = ")) continue

                        val actualHash = line.substringAfter('\"')
                            .substringBefore('\"')

                        return if (hash == actualHash) {
                            // No error
                            null
                        } else {
                            "File hash[$actualHash] did not match expected[$hash]: $file"
                        }
                    }
                }
            } catch (_: Throwable) {}

            return "Failed to find the sha256 value for NativeResource: $file"
        }
    }
}

@Throws(IOException::class)
internal fun File.sha256(): String {
    return MessageDigest.getInstance("SHA-256")
        .digest(readBytes())
        .encodeToString(Base16 { encodeToLowercase = true })
}
