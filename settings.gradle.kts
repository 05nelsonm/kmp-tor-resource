rootProject.name = "kmp-tor-resource"

pluginManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

includeBuild("build-logic")

@Suppress("PrivatePropertyName")
private val CHECK_PUBLICATION: String? by settings

if (CHECK_PUBLICATION != null) {
    include(":tools:check-publication")
} else {
    listOf(
        "resource-android-unit-test",
        "resource-tor",
        "npmjs",
    ).forEach { module ->
        include(":library:$module")
    }

    listOf(
        "cli-core",
        "diff-cli",
        "diff-cli:core",
        "resource-cli",
    ).forEach { module ->
        include(":tools:$module")
    }
}
