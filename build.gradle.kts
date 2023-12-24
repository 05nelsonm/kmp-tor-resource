/*
 * Copyright (c) 2021 Matthew Nelson
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
        gradlePluginPortal()
    }

}

@Suppress("PropertyName")
val CHECK_PUBLICATION = findProperty("CHECK_PUBLICATION") as? String

plugins.withType<YarnPlugin> {
    the<YarnRootExtension>().lockFileDirectory = rootDir.resolve(".kotlin-js-store")
    if (CHECK_PUBLICATION != null) {
        the<YarnRootExtension>().yarnLockMismatchReport = YarnLockMismatchReport.NONE
    }
}

@Suppress("LocalVariableName")
apiValidation {
    val KMP_TARGETS_ALL = System.getProperty("KMP_TARGETS_ALL") != null
    val KMP_TARGETS = (findProperty("KMP_TARGETS") as? String)?.split(',')

    if (CHECK_PUBLICATION != null) {
        ignoredProjects.add("check-publication")
    } else {
        nonPublicMarkers.add("io.matthewnelson.diff.core.internal.InternalDiffApi")
        nonPublicMarkers.add("io.matthewnelson.kmp.tor.core.api.annotation.InternalKmpTorApi")

        // Don't check these projects when building JVM only or Android only
        if (!KMP_TARGETS_ALL && KMP_TARGETS?.containsAll(setOf("ANDROID", "JVM")) == false) {
            ignoredProjects.add("resource-tor")
        }
    }
}
