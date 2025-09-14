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
import com.android.build.gradle.tasks.MergeSourceSetFolders
import io.matthewnelson.kmp.configuration.extension.KmpConfigurationExtension
import io.matthewnelson.kmp.configuration.extension.container.target.KmpConfigurationContainerDsl
import io.matthewnelson.kmp.configuration.extension.container.target.TargetAndroidContainer
import org.gradle.api.Action
import org.gradle.api.Project
import resource.validation.extensions.ExecTorResourceValidationExtension
import resource.validation.extensions.LibTorResourceValidationExtension
import resource.validation.extensions.NoExecTorResourceValidationExtension

fun KmpConfigurationContainerDsl.androidLibrary(
    namespace: String,
    buildTools: String? = "35.0.1",
    compileSdk: Int = 35,
    minSdk: Int = 21,
    action: (Action<TargetAndroidContainer.Library>)? = null,
) {
    androidLibrary {
        android {
            buildTools?.let { buildToolsVersion = it }
            this.compileSdk = compileSdk
            this.namespace = namespace

            defaultConfig {
                this.minSdk = minSdk

                testInstrumentationRunnerArguments["disableAnalytics"] = true.toString()
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            testOptions {
                targetSdk = compileSdk
            }
        }

        action?.execute(this)
    }
}

fun KmpConfigurationContainerDsl.configureAndroidEnvironmentKeysConfig(project: Project) {
    kotlin {
        val envKeyConfigDir = project
            .layout
            .buildDirectory
            .asFile.get()
            .resolve("generated")
            .resolve("sources")
            .resolve("envKeyConfig")

        val prefix = "io.matthewnelson.kmp.tor."
        val packageName = prefix + project.name
            .substringBefore("-gpl")
            .replace('-', '.') + ".internal"

        arrayOf("androidMain", "androidNativeMain").forEach { name ->
            val srcDir = envKeyConfigDir
                .resolve(name)
                .resolve("kotlin")
            srcDir.deleteRecursively()

            var configDir = srcDir

            packageName.split('.').forEach { segment ->
                configDir = configDir.resolve(segment)
            }
            configDir.mkdirs()

            val prefixPosix = prefix.replace('.', '_')

            @Suppress("SpellCheckingInspection")
            configDir.resolve("_EnvKeyConfig.kt").apply { delete() }.writeText("""
                @file:Suppress("SpellCheckingInspection")

                package $packageName

                internal const val ENV_KEY_LIBTOR: String = "${prefixPosix}resource_compilation_lib_tor_LIBTOR"
                internal const val ENV_KEY_LIBTOREXEC: String = "${prefixPosix}resource_compilation_exec_tor_LIBTOREXEC"
                internal const val ENV_KEY_LIBTORJNI: String = "${prefixPosix}resource_noexec_tor_LIBTORJNI"

                //@Throw(IllegalStateException::class)
                @Suppress("NOTHING_TO_INLINE")
                internal inline fun String.envKeyLibName(): String = when (this) {
                    ENV_KEY_LIBTOR, ENV_KEY_LIBTOREXEC, ENV_KEY_LIBTORJNI -> substringAfterLast('_').lowercase() + ".so"
                    else -> error("Unknown environment key >> ${"\$this"}")
                }

            """.trimIndent())
            sourceSets.findByName(name)?.apply {
                kotlin.srcDir(srcDir)
            }
        }
    }
}

fun KmpConfigurationContainerDsl.configureAndroidNativeEmulatorTests(project: Project) {
    val testJniLibs = project.projectDir.resolve("testJniLibs")
    project.tasks.all {
        if (name != "clean") return@all
        doLast { testJniLibs.deleteRecursively() }
    }

    androidLibrary {
        android {
            sourceSets
                .getByName("androidTest")
                .jniLibs
                .srcDir(testJniLibs)
        }
    }

    kotlin {
        if (!project.plugins.hasPlugin("com.android.base")) return@kotlin

        val nativeTestBinaryTasks = listOf(
            "Arm32" to "armeabi-v7a",
            "Arm64" to "arm64-v8a",
            "X64" to "x86_64",
            "X86" to "x86",
        ).mapNotNull { (arch, abi) ->
            val nativeTestBinariesTask = project
                .tasks
                .findByName("androidNative${arch}TestBinaries")
                ?: return@mapNotNull null

            val abiDir = testJniLibs.resolve(abi)
            if (!abiDir.exists() && !abiDir.mkdirs()) throw RuntimeException("mkdirs[$abiDir]")

            val testExecutable = project
                .layout
                .buildDirectory
                .get().asFile
                .resolve("bin")
                .resolve("androidNative$arch")
                .resolve("debugTest")
                .resolve("test.kexe")

            nativeTestBinariesTask.doLast {
                testExecutable.copyTo(abiDir.resolve("libTestExec.so"), overwrite = true)
            }

            nativeTestBinariesTask
        }

        project.tasks.withType(MergeSourceSetFolders::class.java).all {
            if (name != "mergeDebugAndroidTestJniLibFolders") return@all
            nativeTestBinaryTasks.forEach { task -> dependsOn(task) }
        }
    }
}

fun KmpConfigurationExtension.configureAndroidUnitTestTor(
    project: Project,
    action: Action<KmpConfigurationContainerDsl>,
) {
    require(project.name.startsWith("resource-android-unit-test-tor")) { "Invalid project" }

    val isGpl = project.name.endsWith("gpl")
    val libResourceValidation by lazy {
        if (isGpl) {
            LibTorResourceValidationExtension.GPL::class.java
        } else {
            LibTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }
    val execResourceValidation by lazy {
        if (isGpl) {
            ExecTorResourceValidationExtension.GPL::class.java
        } else {
            ExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }
    val noExecResourceValidation by lazy {
        if (isGpl) {
            NoExecTorResourceValidationExtension.GPL::class.java
        } else {
            NoExecTorResourceValidationExtension::class.java
        }.let { project.extensions.getByType(it) }
    }

    configure {
        options {
            useUniqueModuleNames = true
        }

        androidLibrary(namespace = "io.matthewnelson.kmp.tor.resource.android.unit.test.tor") {
            target { publishLibraryVariants("release") }

            android {
                sourceSets.getByName("main").resources {
                    srcDir(libResourceValidation.jvmNativeLibResourcesSrcDir())
                    srcDir(execResourceValidation.jvmNativeLibResourcesSrcDir())
                    srcDir(noExecResourceValidation.jvmNativeLibResourcesSrcDir())
                }
            }
        }

        common { pluginIds("publication", "resource-validation") }

        action.execute(this)
    }
}
