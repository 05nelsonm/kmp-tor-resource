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
package resource.validation.extensions

import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByName
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import resource.validation.extensions.internal.ERROR
import resource.validation.extensions.internal.SourceSetName
import resource.validation.extensions.internal.SourceSetName.Companion.toSourceSetName
import resource.validation.extensions.internal.ValidationHash
import java.io.File

/**
 * Base abstraction for validating reproducibility of packaged
 * resources located in the `external/build/package` directory.
 *
 * e.g. (terminal)
 *
 *     ./external/task.sh build:all
 *     ./external/task.sh package:all
 *     ./external/task.sh validate
 * */
sealed class AbstractResourceValidationExtension(
    protected val project: Project,
    protected val moduleName: String,
    protected val packageName: String,
) {

    protected val dirProjectRoot: File = project
        .rootProject
        .projectDir

    protected abstract val hashes: Set<ValidationHash>

    private val errLibAndroid = mutableSetOf<ERROR>()
    private val errLibJvm = mutableSetOf<ERROR>()
    private val errLibNativeInterop = mutableMapOf<String, MutableSet<ERROR>>()
    private val errResJvm = mutableSetOf<ERROR>()
    private val errResNative = mutableMapOf<SourceSetName, MutableSet<ERROR>>()

    private var isConfiguredLibAndroid = false
    private var isConfiguredLibJvm = false
    private var isConfiguredLibNativeInterop = false
    private var isConfiguredResJvm = false
    private var isConfiguredResNative = false

    private val dirModuleMock: File by lazy {
        dirProjectRoot
            .resolve("mock-resources")
            .resolve(moduleName)
    }
    private val dirModulePackage: File by lazy {
        dirProjectRoot
            .resolve("external")
            .resolve("build")
            .resolve("package")
            .resolve(moduleName)
    }
    private val dirReport: File by lazy {
        project.layout.buildDirectory.get().asFile
            .resolve("reports")
            .resolve("resource-validation")
            .resolve(moduleName)
    }

    @Throws(IllegalStateException::class)
    protected fun configureLibAndroidProtected() {
        check(project.plugins.hasPlugin("com.android.library")) {
            "The 'com.android.library' plugin is required to utilize this function"
        }

        project.extensions.getByName<LibraryExtension>("android").configureLibAndroid()
    }

    @Throws(IllegalStateException::class)
    private fun LibraryExtension.configureLibAndroid() {
        if (isConfiguredLibAndroid) return

        val hashes = hashes.filterIsInstance<ValidationHash.LibAndroid>()
        check(hashes.isNotEmpty()) { "No hashes to validate, no jni resources." }

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
            hashes.forEach { libHash ->
                val errors = libHash.validate(dirModulePackage)
                errLibAndroid.addAll(errors)
            }

            val jniSrcDir = if (errLibAndroid.isEmpty()) {
                dirModulePackage
            } else {
                dirModuleMock
            }.let { dir ->
                "android".toSourceSetName()
                    .sourceSetDir(dir)
                    .resolve("jniLibs")
            }

            jniLibs.srcDir(jniSrcDir)
        }

        isConfiguredLibAndroid = true
        generateReport("android", errLibAndroid)
    }

    protected fun jvmNativeLibsResourcesSrcDirProtected(): File {
        val hashes = hashes.filterIsInstance<ValidationHash.LibJvm>()
        check(hashes.isNotEmpty()) { "No hashes to validate, no jvm resources" }

        val name = "jvm".toSourceSetName()
        val srcResMock = name.sourceSetDir(dirModuleMock)
            .resolve("resources")
        val srcResPackage = name.sourceSetDir(dirModulePackage)
            .resolve("resources")

        if (!isConfiguredLibJvm) {
            val dirResNative = srcResPackage
                .resolve(packageName.replace('.', '/'))
                .resolve("native")

            hashes.forEach { libHash ->
                val error = libHash.validate(dirResNative) ?: return@forEach
                errLibJvm.add(error)
            }

            isConfiguredLibJvm = true
            generateReport("jvm", errLibJvm)
        }

        return if (errLibJvm.isEmpty()) srcResPackage else srcResMock
    }

    protected fun jvmResourcesSrcDirProtected(reportName: String): File {
        val hashes = hashes.filterIsInstance<ValidationHash.ResourceJvm>()
        check(hashes.isNotEmpty()) { "No hashes to validate, no jvm resources" }

        val name = "jvm".toSourceSetName()
        val srcResMock = name.sourceSetDir(dirModuleMock)
            .resolve("resources")
        val srcResPackage = name.sourceSetDir(dirModulePackage)
            .resolve("resources")

        if (!isConfiguredResJvm) {
            hashes.forEach { res ->
                val error = res.validate(dirModulePackage, packageName) ?: return@forEach
                errResJvm.add(error)
            }

            isConfiguredResJvm = true
            generateReport(reportName, errResJvm)
        }

        return if (errResJvm.isEmpty()) srcResPackage else srcResMock
    }

    @Throws(IllegalStateException::class)
    protected fun configureNativeResourcesProtected(kmp: KotlinMultiplatformExtension) { kmp.configureResourceNative() }

    private fun KotlinMultiplatformExtension.configureResourceNative() {
        if (isConfiguredResNative) return

        val hashes = hashes.filterIsInstance<ValidationHash.ResourceNative>()
        check(hashes.isNotEmpty()) { "No hashes to validate, no native resources" }

        // errResNative will be populated with all available
        // source set names which will be added after validating.
        hashes.forEach { res ->
            var errors = errResNative[res.sourceSetName]

            if (errors == null) {
                errors = mutableSetOf()
                errResNative[res.sourceSetName] = errors
            }

            val error = res.validate(dirModulePackage, packageName)
            if (error != null) {
                errors.add(error)
            }
        }

        with(sourceSets) {
            errResNative.forEach { (name, errors) ->
                val srcSet = findByName(name.main) ?: return@forEach

                val moduleDir = if (errors.isEmpty()) dirModulePackage else dirModuleMock
                val kotlinSrcDir = name.sourceSetDir(moduleDir).resolve("kotlin")
                srcSet.kotlin.srcDir(kotlinSrcDir)
            }
        }

        isConfiguredResNative = true

        errResNative.forEach { (name, errors) ->
            generateReport(name.value, errors)
        }
    }

    private fun generateReport(fileName: String, errors: Set<ERROR>) {
        if (!dirReport.exists()) dirReport.mkdirs()

        val report = buildString { errors.forEach { appendLine(it) } }
        dirReport.resolve("$fileName.err").writeText(report)
    }
}
