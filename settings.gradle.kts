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
        "npmjs",
        "resource-android-unit-test-tor",
        "resource-android-unit-test-tor-gpl",
        "resource-exec-tor",
        "resource-exec-tor-gpl",
        "resource-shared-geoip",
        "resource-shared-tor",
        "resource-shared-tor-gpl",
        "resource-statik-tor",
        "resource-statik-tor-gpl",
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
