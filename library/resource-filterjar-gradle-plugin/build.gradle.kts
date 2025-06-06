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
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("publication")
    id("java-gradle-plugin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        explicitApiMode.set(ExplicitApiMode.Strict)
    }
}

gradlePlugin {
    plugins {
        create("kmpTorResourceFilterJarPlugin") {
            id = "io.matthewnelson.kmp.tor.resource-filterjar"
            implementationClass = "io.matthewnelson.kmp.tor.resource.filterjar.KmpTorResourceFilterJarPlugin"
        }
    }
}

dependencies {
    api(libs.gradle.filterjar)
    implementation(libs.kmp.tor.common.core)
    testImplementation(kotlin("test"))
}
