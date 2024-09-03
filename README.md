# kmp-tor-resource
[![badge-license]][url-license]
[![badge-latest-release]][url-latest-release]

[![badge-kotlin]][url-kotlin]
[![badge-build-env]][url-build-env]
[![badge-kmp-tor-core]][url-kmp-tor-core]

![badge-platform-android]
![badge-platform-jvm]
![badge-platform-js-node]
![badge-platform-linux]
![badge-platform-ios]
![badge-platform-macos]
![badge-platform-windows]
![badge-support-apple-silicon]
![badge-support-js-ir]
![badge-support-linux-arm]

This project is focused on the compilation, packaging, distribution and installation of `tor`
resources for Kotlin Multiplatform, primarily to be consumed as a dependency for [kmp-tor][url-kmp-tor].

2 versions of `tor` are built; 1 **with** `--enable-gpl`, and 1 with**out**.

 - `resource-tor` (no `--enable-gpl`)
 - `resource-tor-gpl` (yes `--enable-gpl`)

They are positionally identical with the same package names, classes, resource names/locations, 
etc. The only difference between them are the compiled `tor` binaries provided.

Only **1** of the dependencies can be had for a project, as a conflict will occur if both are 
present. This is to respect the `GPL` licensed code `tor` is utilizing such that projects who 
have a `GPL` license are able to take advantage of the new functionality, and projects who do 
**not** have a `GPL` license can still utilize `tor` without infringing on the `GPL` license.

### Build Verification

See [BUILD.md](docs/BUILD.md)

### Jvm/Node.js Supported Operating Systems & Architectures

**NOTE:** `macOS` and `Windows` binaries are code signed, so they work out of the box.

|                 | x86 | x86_64 | armv7 | aarch64 | ppc64 |
|-----------------|-----|--------|-------|---------|-------|
| Windows         | ✔   | ✔      |       |         |       |
| macOS           |     | ✔      |       | ✔       |       |
| Linux (android) | ✔   | ✔      | ✔     | ✔       |       |
| Linux (libc)    | ✔   | ✔      | ✔     | ✔       | ✔     |
| Linux (musl)    |     |        |       |         |       |
| FreeBSD         |     |        |       |         |       |

### Compilation

Tor and its dependencies are compiled from source using the following versions 

<!-- TAG_VERSION -->
<!-- TAG_DEPENDENCIES -->

|          | git tag                 |
|----------|-------------------------|
| libevent | `release-2.1.12-stable` |
| openssl  | `openssl-3.2.2`         |
| tor      | `tor-0.4.8.12`          |
| xz       | `v5.6.2`                |
| zlib     | `v1.3.1`                |

`tor` is compiled via the `external/task.sh` script using `Docker` in order to maintain 
reproducability.

Detached code signatures are generated for Apple/Windows builds which are checked into 
`git`; this is so others wishing to verify reproducability of the `tor` binaries they 
are running (or providing to their users) can do so.

You can view the `help` output of `task.sh` by running `./external/task.sh` from the project's 
root directory.

```
$ git clone https://github.com/05nelsonm/kmp-tor-resource.git
$ cd kmp-tor-resource
$ ./external/task.sh
```

<details>
    <summary>Example Image</summary>

![image][url-task-image]

</details>

### Packaging

The compiled output from `task.sh`'s `build` tasks are "packaged" for the given platforms and 
moved to their designated package module's resource directories 
(e.g. `external/build/package/resource-tor/src/jvmMain/resources`).

Running `./external/task.sh package` after a `build` task will do the following.

**Android/Jvm/Node.js:**
 - Android `tor` binaries (`libtor.so` files) are moved to the `src/androidMain/jniLibs/{ABI}` directory
 - `geoip` & `geoip6` files are `gzipped` and moved to the `src/jvmAndroidMain/resources` directory
 - Detached code signatures for macOS and Windows are applied to the compilied `tor` binaries (if needed)
 - `tor` binaries are `gzipped` and moved to the `src/jvmMain/resources` directory for their respective 
   hosts and architectures.

**Native:**
 - The same process occurs as above, but after being `gzipped` each resource is transformed into 
   a `NativeResource` (e.g. `resource_tor_gz.kt`).

After "packaging" all resources, an additional step for Node.js is performed.
 - `geoip`, `geoip6`, and all `tor` files are published to `Npmjs` via the
   `library/npmjs` module.
     - See https://www.npmjs.com/package/kmp-tor-resource-tor
     - See https://www.npmjs.com/package/kmp-tor-resource-tor-gpl
 - The `library/resource-tor` and `library/resource-tor-gpl` modules then uses that `npm` dependency 
   in order to provide the resources via Kotlin Multiplatform.

### Distribution

New releases will be published to Maven Central and can be consumed as a Kotlin Multiplatform 
dependency.

Currently, there is only a `SNAPSHOT` publication in order to work on [kmp-tor][url-kmp-tor] `2.0.0`. 
Once that work is complete a Release will be made for `kmp-tor-resource`.

### Installation

The [kmp-tor][url-kmp-tor] project will handle all of this behind the scenes. 
If you are not using that, simply call:

```kotlin
val paths = TorResources(installationDir = "/path/to/my/tor/dir".toFile()).install()
println(paths.toString())

// Paths.Tor: [
//     geoip: /path/to/my/tor/dir/geoip
//     geoip6: /path/to/my/tor/dir/geoip6
//     tor: /path/to/my/tor/dir/tor
// ]
```

It will either throw an exception or extract the resources to the specified directory
 - Note that for Android it will depend on a few things
     - **Android Runtime (Emulators and Devices):**
         - It will search for `libtor.so` within the application's `nativeLibraryDir` and return that path
         - All of this happens automatically and no configuration is needed.
         - See the [core-lib-locator][url-core-lib-locator]
     - **Android Unit Tests:**
         - You can add the `resource-android-unit-test` dependency and reflection will be used to
           source the correct `tor` binary resource for the given host/architecture that the tests 
           are running on (the exact same way Jvm does it).
         - Use `testImplementation` when adding the dependency!!! Do **NOT** ship your app with that

<!--

TODO: gradle configuration for android

 - Ensure `JavaVersion` is greater than or equal to 8:
   ```kotlin
   // build.gradle.kts

   android {
       // ...

       compileOptions {
           sourceCompatibility = JavaVersion.VERSION_1_8
           targetCompatibility = JavaVersion.VERSION_1_8
       }

       kotlinOptions {
           jvmTarget = JavaVersion.VERSION_1_8.toString()
       }
   }
   ```

 - Enable legacy packaging for `jniLibs` directory:
   ```kotlin
   // build.gradle.kts

   android {
       // ...

       packagingOptions {
           jniLibs.useLegacyPackaging = true
       }
   }
   ```

 - Configure splits for each architecture by adding the following to your 
   application module's `android` block:
   ```kotlin
   // build.gradle.kts

   android {
       // ...

       splits {

           // Configures multiple APKs based on ABI. This helps keep the size
           // down, since PT binaries can be large.
           abi {

               // Enables building multiple APKs per ABI.
               isEnable = true

               // By default, all ABIs are included, so use reset() and include to specify
               // that we only want APKs for x86 and x86_64, armeabi-v7a, and arm64-v8a.

               // Resets the list of ABIs that Gradle should create APKs for to none.
               reset()

               // Specifies a list of ABIs that Gradle should create APKs for.
               include("x86", "armeabi-v7a", "arm64-v8a", "x86_64")

               // Specify whether you wish to also generate a universal APK that
               // includes _all_ ABIs.
               isUniversalApk = true
           }
       }
   }
   ```

 - If you are publishing your application to Google Play using app bundling,
   add the following to your project's `gradle.properties` file:
   ```groovy
   android.bundle.enableUncompressedNativeLibs=false
   ```

     - You can also verify (prior to pushing your release to Google Play)
       if the bundled apk extracts binaries on install correctly by using
       the [bundletool][url-bundletool].

-->

<!-- TAG_VERSION -->
[badge-latest-release]: https://img.shields.io/badge/latest--release-408.12.0--SNAPSHOT-5d2f68.svg?logo=torproject&style=flat&logoColor=5d2f68
[badge-license]: https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat

<!-- TAG_DEPENDENCIES -->
[badge-kotlin]: https://img.shields.io/badge/kotlin-1.9.24-blue.svg?logo=kotlin
[badge-build-env]: https://img.shields.io/badge/build--env-0.1.3-blue.svg?logo=docker
[badge-kmp-tor-core]: https://img.shields.io/badge/kmp--tor--core-2.0.1-blue.svg?style=flat

<!-- TAG_PLATFORMS -->
[badge-platform-android]: http://img.shields.io/badge/-android-6EDB8D.svg?style=flat
[badge-platform-jvm]: http://img.shields.io/badge/-jvm-DB413D.svg?style=flat
[badge-platform-js]: http://img.shields.io/badge/-js-F8DB5D.svg?style=flat
[badge-platform-js-node]: https://img.shields.io/badge/-nodejs-68a063.svg?style=flat
[badge-platform-linux]: http://img.shields.io/badge/-linux-2D3F6C.svg?style=flat
[badge-platform-macos]: http://img.shields.io/badge/-macos-111111.svg?style=flat
[badge-platform-ios]: http://img.shields.io/badge/-ios-CDCDCD.svg?style=flat
[badge-platform-tvos]: http://img.shields.io/badge/-tvos-808080.svg?style=flat
[badge-platform-watchos]: http://img.shields.io/badge/-watchos-C0C0C0.svg?style=flat
[badge-platform-wasm]: https://img.shields.io/badge/-wasm-624FE8.svg?style=flat
[badge-platform-windows]: http://img.shields.io/badge/-windows-4D76CD.svg?style=flat
[badge-support-android-native]: http://img.shields.io/badge/support-[AndroidNative]-6EDB8D.svg?style=flat
[badge-support-apple-silicon]: http://img.shields.io/badge/support-[AppleSilicon]-43BBFF.svg?style=flat
[badge-support-js-ir]: https://img.shields.io/badge/support-[js--IR]-AAC4E0.svg?style=flat
[badge-support-linux-arm]: http://img.shields.io/badge/support-[LinuxArm]-2D3F6C.svg?style=flat

[url-bundletool]: https://github.com/google/bundletool
[url-build-env]: https://github.com/05nelsonm/build-env
[url-latest-release]: https://github.com/05nelsonm/kmp-tor-resource/releases/latest
[url-license]: https://www.apache.org/licenses/LICENSE-2.0
[url-kotlin]: https://kotlinlang.org
[url-kmp-tor]: https://github.com/05nelsonm/kmp-tor
[url-kmp-tor-core]: https://github.com/05nelsonm/kmp-tor-core
[url-core-lib-locator]: https://github.com/05nelsonm/kmp-tor-core/tree/master/library/core-lib-locator
[url-task-image]: https://github.com/05nelsonm/kmp-tor-resource/assets/44778092/8be9197e-4135-43ad-9629-e18ef0e90523
