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
import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val libs = the<LibrariesForLibs>()

plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
    id("publication")
}

private val jVersion = properties["kmp.tor.resource.plugin.java_version"]
    ?.toString()
    ?: "11"

java {
    sourceCompatibility = JavaVersion.toVersion(jVersion)
    targetCompatibility = JavaVersion.toVersion(jVersion)
}

tasks.withType(KotlinCompile::class.java) {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(jVersion))
        explicitApiMode.set(ExplicitApiMode.Strict)
        freeCompilerArgs.add("-Xsuppress-version-warnings")
        @Suppress("DEPRECATION")
        languageVersion.set(KotlinVersion.KOTLIN_1_9)
    }
}

configurations.all {
    // Pin the kotlin version
    resolutionStrategy {
        force(libs.kotlin.stdlib)
        force(libs.kotlin.stdlib.jdk8)
        force(libs.kotlin.reflect)
    }
}
