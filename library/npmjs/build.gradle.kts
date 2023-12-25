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
 **/
import dev.petuska.npm.publish.extension.domain.NpmPackage
import dev.petuska.npm.publish.extension.domain.NpmPackages

plugins {
    id("base")
    alias(libs.plugins.publish.npm)
    id("resource-validation")
}

// Have to have as a standalone gradle module so that the publication
// task is not created for the :library:binary module's js target. All
// that is being done here is publication of resources so that they can
// be consumed as a npm dependency from :library:binary module.
npmPublish {
    val npmjsAuthToken = rootProject.findProperty("NPMJS_AUTH_TOKEN") as? String
    if (npmjsAuthToken.isNullOrBlank()) return@npmPublish

    registries {
        npmjs {
            authToken.set(npmjsAuthToken)
        }
    }

    val dryRun = properties["NPMJS_DRY_RUN"] != null

    if (dryRun) {
        dry.set(true)
    }

    val jvmGeoipSrcDir = torResourceValidation.jvmGeoipResourcesSrcDir
    val jvmTorLibsSrcDir = torResourceValidation.jvmTorLibResourcesSrcDir
    val jvmGPLGeoipSrcDir = torResourceGPLValidation.jvmGeoipGPLResourcesSrcDir
    val jvmGPLTorLibsSrcDir = torResourceGPLValidation.jvmTorGPLLibResourcesSrcDir

    packages {
        val snapshotVersion = properties["NPMJS_SNAPSHOT_VERSION"]!!
            .toString()
            .toInt()

        check(snapshotVersion >= 0) {
            "NPMJS_SNAPSHOT_VERSION cannot be negative"
        }

        val vProject = "${project.version}"
        if (vProject.endsWith("-SNAPSHOT")) {

            // Only register snapshot task when project version is -SNAPSHOT
            registerTorResources(
                isGPL = false,
                releaseVersion = "$vProject.$snapshotVersion",
                geoipResourcesDir = jvmGeoipSrcDir,
                torResourcesDir = jvmTorLibsSrcDir,
            )
            registerTorResources(
                isGPL = true,
                releaseVersion = "$vProject.$snapshotVersion",
                geoipResourcesDir = jvmGPLGeoipSrcDir,
                torResourcesDir = jvmGPLTorLibsSrcDir,
            )
        } else {
            check(snapshotVersion == 0) {
                "NPMJS_SNAPSHOT_VERSION must be 0 for releases"
            }

            // Release will be XXXX.XX.##
            // Increment the # for the next SNAPSHOT version
            val increment = vProject.substringAfterLast('.').toInt() + 1
            val nextVersion = vProject.substringBeforeLast('.') + ".$increment"

            // Register both snapshot and release tasks when project
            // version indicates a release so after maven publication
            // and git tagging, updating VERSION_NAME with -SNAPSHOT
            // there will be a "next release" waiting
            registerTorResources(
                isGPL = false,
                releaseVersion = vProject,
                geoipResourcesDir = jvmGeoipSrcDir,
                torResourcesDir = jvmTorLibsSrcDir,
            )
            registerTorResources(
                isGPL = false,
                releaseVersion = "$nextVersion-SNAPSHOT.$snapshotVersion",
                geoipResourcesDir = jvmGeoipSrcDir,
                torResourcesDir = jvmTorLibsSrcDir,
            )
            registerTorResources(
                isGPL = true,
                releaseVersion = vProject,
                geoipResourcesDir = jvmGPLGeoipSrcDir,
                torResourcesDir = jvmGPLTorLibsSrcDir,
            )
            registerTorResources(
                isGPL = true,
                releaseVersion = "$nextVersion-SNAPSHOT.$snapshotVersion",
                geoipResourcesDir = jvmGPLGeoipSrcDir,
                torResourcesDir = jvmGPLTorLibsSrcDir,
            )
        }
    }
}

fun NpmPackages.registerTorResources(
    isGPL: Boolean,
    releaseVersion: String,
    geoipResourcesDir: File,
    torResourcesDir: File
) {
    val suffix = if (isGPL) "-gpl" else ""

    val name = if (releaseVersion.contains("SNAPSHOT")) {
        "tor${suffix}-resources-snapshot"
    } else {
        "tor${suffix}-resources-release"
    }

    register(name) {
        packageName.set("kmp-tor-resource-tor$suffix")
        version.set(releaseVersion)

        main.set("index.js")
        readme.set(projectDir.resolve("resource-tor$suffix").resolve("README.md"))

        files {
            from("index.js")
            from(geoipResourcesDir)
            from(torResourcesDir)
        }

        packageInfoJson()
    }
}

fun NpmPackage.packageInfoJson() {
    packageJson {
        homepage.set("https://github.com/05nelsonm/kmp-tor-resource")
        license.set("Apache 2.0")

        repository {
            type.set("git")
            url.set("git+https://github.com/05nelsonm/kmp-tor-resource.git")
        }
        author {
            name.set("Matthew Nelson")
        }
        bugs {
            url.set("https://github.com/05nelsonm/kmp-tor-resource/issues")
        }

        keywords.add("tor")
        keywords.add("kmp-tor")
    }
}

tasks.getByName("clean") {
    projectDir.resolve("build").delete()
}
