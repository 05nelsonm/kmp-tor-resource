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
import resource.validation.extensions.FrameworksResourceValidationExtension

plugins {
    id("resource-validation")
    id("shared-plugin")
}

java {
    sourceSets.getByName("main") {
        resources.srcDir(frameworksResourceValidation.jvmNativeLibResourcesSrcDir())

        val kotlinSrcDir = layout.buildDirectory.get().asFile
            .resolve("generated")
            .resolve("sources")
            .resolve("buildConfig")
            .resolve("jvmMain")
            .resolve("kotlin")

        kotlin.srcDir(kotlinSrcDir)

        val internalDir = kotlinSrcDir
            .resolve("io")
            .resolve("matthewnelson")
            .resolve("kmp")
            .resolve("tor")
            .resolve("resource")
            .resolve("frameworks")
            .resolve("internal")

        internalDir.mkdirs()

        internalDir.resolve("BuildConfig.kt").writeText("""
            package io.matthewnelson.kmp.tor.resource.frameworks.internal

            internal const val HASH_IOS_LIBTOR: String = "${FrameworksResourceValidationExtension.HASH_IOS_LIBTOR}"
            internal const val HASH_IOS_LIBTOR_GPL: String = "${FrameworksResourceValidationExtension.HASH_IOS_LIBTOR_GPL}"

        """.trimIndent())
    }
}

gradlePlugin {
    plugins {
        create("resourceFrameworks") {
            id = "io.matthewnelson.kmp.tor.resource-frameworks"
            implementationClass = "io.matthewnelson.kmp.tor.resource.frameworks.KmpTorResourceFrameworksPlugin"
        }
    }
}

dependencies {
    implementation(libs.encoding.base16)
    testImplementation(kotlin("test"))
}
