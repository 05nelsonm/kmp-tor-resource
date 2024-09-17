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
import dev.petuska.npm.publish.extension.domain.NpmDependency
import dev.petuska.npm.publish.extension.domain.NpmPackage
import dev.petuska.npm.publish.extension.domain.NpmPackages
import java.io.FileNotFoundException

plugins {
    id("base")
    alias(libs.plugins.publish.npm)
    id("resource-validation")
}

// Have to have as a standalone gradle module so that the publication
// task is not created for the :library:resource-* module's js target.
// All that is being done here is publication of resources so that they
// can be consumed as a npm dependency from :library:resource-* module.
npmPublish {
    val npmjsAuthToken = rootProject.findProperty("NPMJS_AUTH_TOKEN") as? String
    if (npmjsAuthToken.isNullOrBlank()) return@npmPublish

    val srcResDirGeoip = geoipResourceValidation.jvmResourcesSrcDir()
    val srcResDirLibTor = libTorResourceValidation.jvmNativeLibResourcesSrcDir()
    val srcResDirLibTorGPL = libTorGPLResourceValidation.jvmNativeLibResourcesSrcDir()
    val srcResDirExecTor = execTorResourceValidation.jvmNativeLibResourcesSrcDir()
    val srcResDirExecTorGPL = execTorGPLResourceValidation.jvmNativeLibResourcesSrcDir()

    project.rootProject.rootDir.resolve("mock-resources").let { mockResources ->
        check(mockResources.exists()) { "mock-resources does not exist... dir name change?" }

        listOf(
            srcResDirGeoip,
            srcResDirLibTor,
            srcResDirLibTorGPL,
            srcResDirExecTor,
            srcResDirExecTorGPL,
        ).forEach { srcDir ->
            // Configure NOTHING if mock resources are being utilized.
            if (srcDir.path.startsWith(mockResources.path)) {
                println("Skipping NPM publication configuration (mock resources are being used).")
                return@npmPublish
            }
        }
    }

    val vPublications = createPublicationVersions()

    registries {
        npmjs {
            authToken.set(npmjsAuthToken)
        }
    }

    if (properties["NPMJS_DRY_RUN"] != null) { dry.set(true) }

    val targets = NativeTargets(
        "linux-android",
        "linux-libc",
        "macos",
        "mingw",
    )

    packages {
        vPublications.forEach { version ->
            register(
                version = version,
                dirName = NpmjsDirName("resource-geoip"),
                configureFiles = {
                    from(srcResDirGeoip.resolve("io/matthewnelson/kmp/tor/resource/geoip"))
                 },
            )

            registerAll(
                targets = targets,
                version = version,
                srcResDirLibTor = srcResDirLibTor,
                srcResDirLibTorGPL = srcResDirLibTorGPL,
                srcResDirExecTor = srcResDirExecTor,
                srcResDirExecTorGPL = srcResDirExecTorGPL,
            )
        }
    }
}

private fun Project.createPublicationVersions(): List<PublicationVersion> {
    val key = "NPMJS_SNAPSHOT_VERSION"
    val vSnapshot = properties[key]?.toString()?.toIntOrNull()

    check(vSnapshot != null && vSnapshot >= 0) {
        "$key must be set to an integer greater than or equal to 0 (see gradle.properties)"
    }

    val vProject = project.version.toString()

    return if (vProject.endsWith("-SNAPSHOT")) {
        // SNAPSHOTs always get a positively incrementing
        // number appended to them, starting from 0.
        listOf("$vProject.$vSnapshot")
    } else {
        check(vSnapshot == 0) {
            "$key must be set to 0 for release publications"
        }

        // Release will be XXX.YY.ZZ
        //
        // Increment ZZ for the "next" SNAPSHOT version so that
        // upon updating gradle.properties after tagging & publication,
        // there is a "zeroith" SNAPSHOT available for that next
        // development iteration.
        val vNext = vProject.indexOfLast { it == '.' }.let { i ->
            val increment = vProject.substring(i + 1).toInt() + 1
            vProject.substring(0, i + 1) + increment
        }

        // Always put release publication first
        listOf(vProject, "$vNext-SNAPSHOT.$vSnapshot")
    }.map { PublicationVersion(it) }
}

@JvmInline
private value class NpmjsDirName(private val value: String) {
    fun toPackageName(): String = "kmp-tor.$value"
    override fun toString(): String = value
}

@JvmInline
private value class PublicationVersion(private val value: String) {

    val isSnapshot: Boolean get() = value.contains("SNAPSHOT")

    fun registryNameFor(dirName: NpmjsDirName): String {
        val stripped = dirName.toString()
            .substringAfter("resource-")
            .substringAfter("exec-")
            .substringAfter("lib-")

        return stripped + "." + if (isSnapshot) "snapshot" else "release"
    }

    override fun toString(): String = value
}

@JvmInline
private value class NativeTargets private constructor(
    private val targets: List<String>,
): Iterable<String> {

    constructor(vararg targets: String): this(targets.sorted())

    init { require(targets.isNotEmpty()) { "NativeTargets cannot be empty" } }

    override fun iterator(): Iterator<String> = targets.iterator()
}

private fun NpmPackages.registerAll(
    targets: NativeTargets,
    version: PublicationVersion,
    srcResDirLibTor: File,
    srcResDirLibTorGPL: File,
    srcResDirExecTor: File,
    srcResDirExecTorGPL: File,
) {
    listOf(
        Pair(
            srcResDirLibTor to "io/matthewnelson/kmp/tor/resource/lib/tor/native",
            srcResDirExecTor to "io/matthewnelson/kmp/tor/resource/exec/tor/native",
        ),
        Pair(
            srcResDirLibTorGPL to "io/matthewnelson/kmp/tor/resource/lib/tor/native",
            srcResDirExecTorGPL to "io/matthewnelson/kmp/tor/resource/exec/tor/native",
        ),
    ).forEach { (lib, exec) ->
        val (libSrcRes, libPathNative) = lib
        val (execSrcRes, execPathNative) = exec

        val isGpl = libSrcRes.parentFile.parentFile.parent.endsWith("-gpl")
        val npmjsDirBaseName = buildString {
            append("resource-exec-tor")
            if (isGpl) append("-gpl")
        }

        val dependenciesAll = targets.map { target ->
            val targetDirLib = libSrcRes.resolve(libPathNative).resolve(target)
            val targetDirExec = execSrcRes.resolve(execPathNative).resolve(target)
            check(targetDirLib.exists()) { "Directory missing: $targetDirLib" }
            check(targetDirExec.exists()) { "Directory missing: $targetDirExec" }

            val npmjsDirName = NpmjsDirName("$npmjsDirBaseName.$target")

            register(
                version = version,
                dirName = npmjsDirName,
                configureFiles = { from(targetDirLib, targetDirExec) },
            )

            npmjsDirName
        }

        register(
            version = version,
            dirName = NpmjsDirName("$npmjsDirBaseName.all"),
            configurePkg = {
                dependencies {
                    dependenciesAll.forEach { dependency ->
                        create(dependency.toPackageName()) {
                            this.version.set(version.toString())
                            type.set(NpmDependency.NORMAL)
                        }
                    }
                }
            }
        )
    }
}

private fun NpmPackages.register(
    version: PublicationVersion,
    dirName: NpmjsDirName,
    configureFiles: Action<ConfigurableFileCollection> = Action {},
    configurePkg: Action<NpmPackage> = Action {},
) {
    val readmeFile = projectDir.resolve(dirName.toString()).resolve("README.md")
    if (!readmeFile.exists()) {
        throw FileNotFoundException(readmeFile.path)
    }

    register(version.registryNameFor(dirName)) {
        packageName.set(dirName.toPackageName())
        this.version.set(version.toString())
        main.set("index.js")
        readme.set(readmeFile)

        files {
            from("index.js")
            configureFiles.execute(this)
        }

        configurePkg.execute(this)

        applyPackageInfoJson()
    }
}

private fun NpmPackage.applyPackageInfoJson() {
    packageJson {
        homepage.set("https://github.com/05nelsonm/${rootProject.name}")
        license.set("Apache 2.0")

        repository {
            type.set("git")
            url.set("git+https://github.com/05nelsonm/${rootProject.name}.git")
        }
        author {
            name.set("Matthew Nelson")
        }
        bugs {
            url.set("https://github.com/05nelsonm/${rootProject.name}/issues")
        }

        keywords.add("tor")
        keywords.add("kmp-tor")
    }
}

tasks.getByName("clean") {
    project.layout.buildDirectory.get().asFile.delete()
}
