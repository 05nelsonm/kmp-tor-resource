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
import resource.validation.extensions.ExecTorResourceValidationExtension
import resource.validation.extensions.GeoipResourceValidationExtension
import resource.validation.extensions.LibTorResourceValidationExtension

plugins {
    id("configuration")
}

kmpConfiguration {
    configure {
        jvm {
            kotlinJvmTarget = JavaVersion.VERSION_1_8
            compileSourceCompatibility = JavaVersion.VERSION_1_8
            compileTargetCompatibility = JavaVersion.VERSION_1_8
        }

        common {
            pluginIds("resource-validation")

            sourceSetMain {
                dependencies {
                    implementation(project(":library:resource-exec-tor"))
                    implementation(libs.kmp.tor.common.core)
                }
            }
            sourceSetTest {
                dependencies {
                    implementation(kotlin("test"))
                }
            }
        }

        kotlin {
            val configuration = targets
                .getByName("jvm")
                .compilations
                .getByName("main")
                .runtimeDependencyConfigurationName
                ?.let { project.configurations.getByName(it) }
                ?: return@kotlin

            project.tasks.register<Jar>("assembleFatJar") {
                manifest.attributes["Main-Class"] = "io.matthewnelson.kmp.tor.resource.test.linux.MainKt"
                duplicatesStrategy = DuplicatesStrategy.EXCLUDE

                archiveFileName.set("test-linux.jar")
                from(configuration.map { if (it.isDirectory) it else zipTree(it) })
                with(project.tasks.getByName("jvmJar") as CopySpec)
                dependsOn(":library:resource-geoip:jvmJar")
                dependsOn(":library:resource-lib-tor:jvmJar")
                dependsOn(":library:resource-exec-tor:jvmJar")
            }
        }

        kotlin {
            val jvmTest = sourceSets.findByName("jvmTest") ?: return@kotlin

            val resourceValidationGeoip = project
                .extensions
                .getByType(GeoipResourceValidationExtension::class.java)
            val resourceValidationLibTor = project
                .extensions
                .getByType(LibTorResourceValidationExtension::class.java)
            val resourceValidationExecTor = project
                .extensions
                .getByType(ExecTorResourceValidationExtension::class.java)

            val reportJvm by lazy {
                val geoip = resourceValidationGeoip.errorReportJvmResources()
                val lib = resourceValidationLibTor.errorReportJvmNativeLibResources()
                val exec = resourceValidationExecTor.errorReportJvmNativeLibResources()
                geoip + lib + exec
            }

            val kotlinSrcDir = project
                .layout
                .buildDirectory
                .asFile.get()
                .resolve("generated")
                .resolve("sources")
                .resolve("buildConfig")
                .resolve("jvmTest")
                .resolve("kotlin")

            val packageName = "io.matthewnelson.kmp.tor.resource.test.linux.internal"
            val dir = kotlinSrcDir.resolve(packageName.replace('.', File.separatorChar))
            dir.mkdirs()

            project.afterEvaluate {
                val areErrorReportsEmpty = reportJvm.indexOfFirst { !it.isWhitespace() } == -1

                dir.resolve("TestBuildConfig.kt").writeText("""
                    package $packageName

                    internal const val IS_USING_MOCK_RESOURCES: Boolean = ${!areErrorReportsEmpty}

                """.trimIndent())
            }

            jvmTest.kotlin.srcDir(kotlinSrcDir)
        }

        kotlin { explicitApi() }
    }
}
