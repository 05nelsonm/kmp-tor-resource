/*
 * Copyright (c) 2023 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
import kotlinx.validation.ApiValidationExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnLockMismatchReport
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension

plugins {
    alias(libs.plugins.android.library) apply(false)
    alias(libs.plugins.binary.compat)
    alias(libs.plugins.kotlin.multiplatform) apply(false)
    alias(libs.plugins.publish.npm) apply(false)
}

allprojects {
    findProperty("GROUP")?.let { group = it }
    findProperty("VERSION_NAME")?.let { version = it }
    findProperty("POM_DESCRIPTION")?.let { description = it.toString() }

    repositories {
        mavenCentral()
        google()

        if (version.toString().endsWith("-SNAPSHOT")) {
            // Only allow snapshot dependencies for non-release versions.
            // This would cause a build failure if attempting to make a release
            // while depending on a -SNAPSHOT version (such as core).
            maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        }
    }
}

@Suppress("PropertyName")
val CHECK_PUBLICATION = findProperty("CHECK_PUBLICATION") != null

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().lockFileDirectory = rootDir.resolve(".kotlin-js-store")
    if (CHECK_PUBLICATION) {
        the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.NONE
    }
}

@Suppress("LocalVariableName")
extensions.configure(ApiValidationExtension::class.java) {
    val KMP_TARGETS_ALL = System.getProperty("KMP_TARGETS_ALL") != null
    val KMP_TARGETS = (findProperty("KMP_TARGETS") as? String)?.split(',')

    // Only enable when selectively enabled targets are not being passed via cli.
    // See https://github.com/Kotlin/binary-compatibility-validator/issues/269
    @OptIn(kotlinx.validation.ExperimentalBCVApi::class)
    klib.enabled = KMP_TARGETS == null

    if (CHECK_PUBLICATION) {
        ignoredProjects.add("check-publication")
    } else {
        nonPublicMarkers.add("io.matthewnelson.diff.core.internal.InternalDiffApi")

        findProperty("TOOLING")?.let { return@configure }
        nonPublicMarkers.add("io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi")

        // Don't check these projects when building JVM only or Android only
        if (!KMP_TARGETS_ALL && KMP_TARGETS?.containsAll(setOf("ANDROID", "JVM")) == false) {
            ignoredProjects.add("resource-exec-tor")
            ignoredProjects.add("resource-exec-tor-gpl")
            ignoredProjects.add("resource-noexec-tor")
            ignoredProjects.add("resource-noexec-tor-gpl")
            ignoredProjects.add("resource-lib-tor")
            ignoredProjects.add("resource-lib-tor-gpl")
        }
    }
}
