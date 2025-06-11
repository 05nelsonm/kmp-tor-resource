# resource-filterjar-gradle-plugin

This plugin is for Java & KotlinMultiplatform/Jvm projects to reduce the Jar 
artifact sizes of `resource-exec-tor`, `resource-lib-tor`, `resource-noexec-tor` 
and their `-gpl` variants by filtering out tor compilations they provide which are 
not needed for the given host & architecture the application is being compiled for. 
This can reduce the final application distribution size by upwards of 40 Mb, as opposed 
to doing no filtering and providing all tor compilations. 

This plugin provides the [gradle-filterjar-plugin][url-gradle-filterjar-plugin] and 
extends its filtering functionality with APIs specific to how `kmp-tor-resource` packages 
compilations of tor.

### Usage

<!-- TAG_VERSION -->

- Add the plugin to your project where the `kmp-tor-resource` dependencies are defined.
  ```kotlin
  plugins {
    id("io.matthewnelson.kmp.tor.resource-filterjar") version("408.16.4")
  }
  ```

- Configure the tor compilations you wish to keep
  ```kotlin
  // build.gradle.kts

  kmpTorResourceFilterJar {
      logging.set(true)

      // e.g. To filter out all tor compilations but the current host/architecture
      keepTorCompilation(host = "current", arch = "current")

      // e.g. To filter out all tor compilations but the current host
      keepTorCompilation(host = "current", arch = "all")

      // e.g. To filter out all tor compilations but the current host for arches aarch64 & x86_64
      keepTorCompilation(host = "current", "aarch64", "x86_64")

      // See extension documentation for further information on configurability
  }
  ```

[url-gradle-filterjar-plugin]: https://github.com/05nelsonm/gradle-filterjar-plugin
