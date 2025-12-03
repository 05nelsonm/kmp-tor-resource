# kmp-tor-resource
[![badge-license]][url-license]
[![badge-latest-release]][url-latest-release]

[![badge-kotlin]][url-kotlin]
[![badge-build-env]][url-build-env]
[![badge-kmp-tor-common]][url-kmp-tor-common]
[![badge-androidx-startup]][url-androidx-startup]

![badge-platform-android]
![badge-platform-jvm]
![badge-platform-js-node]
![badge-platform-wasm]
![badge-platform-android-native]
![badge-platform-linux]
![badge-platform-macos]
![badge-platform-ios]
![badge-platform-windows]

This project is focused on the packaging and distribution of pre-compiled `tor` resources 
for Kotlin Multiplatform, primarily to be consumed as a dependency for [kmp-tor][url-kmp-tor].

### Build Reproducibility

See [DETERMINISTIC_BUILDS.md](docs/DETERMINISTIC_BUILDS.md)

### Compilations

Tor and its dependencies are compiled from source using the following versions

<!-- TAG_VERSION -->
<!-- TAG_DEPENDENCIES -->

|          | git tag                 |
|----------|-------------------------|
| libevent | `release-2.1.12-stable` |
| openssl  | `openssl-3.4.3`         |
| tor      | `tor-0.4.8.19`          |
| xz       | `v5.8.1`                |
| zlib     | `v1.3.1`                |

**NOTE:** All `macOS` and `Windows` compilations are code signed so they work out of the box.

More details about how things are compiled can be found [HERE](docs/COMPILATION_DETAILS.md)

### Supported Operating Systems & Architectures for Jvm, Kotlin/Js, & Kotlin/WasmJs

|                 | aarch64 | armv7a | ppc64le | riscv64 | x86 | x86_64 |
|-----------------|---------|--------|---------|---------|-----|--------|
| Windows         |         |        |         |         | ✔   | ✔      |
| macOS           | ✔       |        |         |         |     | ✔      |
| Linux (android) | ✔       | ✔      |         |         | ✔   | ✔      |
| Linux (libc)    | ✔       | ✔      | ✔       | ✔       | ✔   | ✔      |
| Linux (musl)    | ✔       |        |         |         | ✔   | ✔      |
| FreeBSD         |         |        |         |         |     |        |

### Types (`exec` & `noexec`)

2 types of resources are available; `exec` and `noexec`. This is to support platforms where process 
execution is not allowed (e.g. `iOS`).

- `resource-exec-tor` and its `-gpl` variant:
    - Provides an implementation of `ResourceLoader.Tor.Exec`
    - Support for all platforms **except** `iOS`

- `resource-noexec-tor` and its `-gpl` variant:
    - Provides an implementation of `ResourceLoader.Tor.NoExec`
    - Support for all platforms **except** `Node.js`

Even though tremendous work has gone into making the `noexec` dependencies as safe as possible by 
unloading `tor` after each invocation of `tor_run_main`, there is no safer way to run `tor` than in 
its own process (as it was designed). The `exec` dependency should be utilized whenever possible.

### Variants (`tor` & `tor-gpl`)

2 variants of `tor` are compiled; 1 **with** the flag `--enable-gpl`, and 1 with**out** it.

Publications with the `-gpl` suffix are indicative of the presence of the `--enable-gpl` compile 
time flag.

Both variants are positionally identical with the same package names, classes, resource 
names/locations, etc. The only difference between them are the compilations of `tor` being provided.

Only **1** variant can be had for a project, as a conflict will occur if both are present.

<details>
    <summary>Example</summary>

`build.gradle.kts`
```kotlin
// BAD
dependencies {
    implementation("io.matthewnelson.kmp-tor:resource-exec-tor:$vKmpTorResource")
    implementation("io.matthewnelson.kmp-tor:resource-exec-tor-gpl:$vKmpTorResource")
}

// BAD
dependencies {
    implementation("io.matthewnelson.kmp-tor:resource-exec-tor:$vKmpTorResource")
    implementation("io.matthewnelson.kmp-tor:resource-noexec-tor-gpl:$vKmpTorResource")
}

// GOOD! (non-gpl)
dependencies {
    implementation("io.matthewnelson.kmp-tor:resource-exec-tor:$vKmpTorResource")
    implementation("io.matthewnelson.kmp-tor:resource-noexec-tor:$vKmpTorResource")
}

// GOOD! (gpl)
dependencies {
    implementation("io.matthewnelson.kmp-tor:resource-exec-tor-gpl:$vKmpTorResource")
    implementation("io.matthewnelson.kmp-tor:resource-noexec-tor-gpl:$vKmpTorResource")
}
```

</details>

This is to respect the `GPL` licensed code `tor` is utilizing such that projects who 
have a `GPL` license are able to take advantage of the new functionality, and projects who do 
**not** have a `GPL` license can still utilize `tor` without infringing on the license.

### Get Started

<!-- TAG_VERSION -->

```kotlin
// build.gradle.kts
dependencies {
    val vKmpTorResource = "408.19.0"
    implementation("io.matthewnelson.kmp-tor:resource-exec-tor:$vKmpTorResource")
    implementation("io.matthewnelson.kmp-tor:resource-noexec-tor:$vKmpTorResource")

    // Alternatively, if wanting tor compiled with `--enable-gpl` (GPL v3 licensed code)
//    implementation("io.matthewnelson.kmp-tor:resource-exec-tor-gpl:$vKmpTorResource")
//    implementation("io.matthewnelson.kmp-tor:resource-noexec-tor-gpl:$vKmpTorResource")
}
```

<details>
    <summary>Configure Android</summary>

Some additional configuration may be necessary for your Android application.

- If utilizing the `-exec` Android dependency, `tor` compilations must be extracted to the
  `ApplicationInfo.nativeLibraryDir` when the application is installed:
  ```kotlin
  // build.gradle.kts
  android {
      packaging {
          jniLibs.useLegacyPackaging = true
      }
  }
  ```
  ```kotlin
  // gradle.properties
  android.bundle.enableUncompressedNativeLibs=false
  ```

- If running unit tests for Android (not device/emulator), add the following dependency which 
  will provide the desktop compilations and use them in lieu of the `android` compilations.
  ```kotlin
  // build.gradle.kts
  dependencies {
      testImplementation("io.matthewnelson.kmp-tor:resource-android-unit-test-tor:$vKmpTorResource")
  
      // Alternatively, if using the `-gpl` variants
  //    testImplementation("io.matthewnelson.kmp-tor:resource-android-unit-test-tor-gpl:$vKmpTorResource")
  }
  ```

- Optionally, configure splits for each ABI:
  ```kotlin
  // build.gradle.kts
  android {
      splits {
          abi {
              isEnable = true
              reset()
              include("x86", "armeabi-v7a", "arm64-v8a", "x86_64")
              isUniversalApk = true
          }
      }
  }
  ```

</details>

<details>
    <summary>Configure Android Native</summary>

Some additional configuration is necessary for your **Android** application. `tor` compilations are not 
shipped with the `resource-exec-tor{-gpl}` or `resource-noexec-tor{-gpl}` Android **Native** dependencies, 
they are packaged separately in an `.aar` and expected to be present at runtime.

- If utilizing the `resource-exec-tor{-gpl}` Android **Native** dependency:
    - Add `tor` compilations to your **Android** application:
      ```kotlin
      // build.gradle.kts
      dependencies {
          implementation("io.matthewnelson.kmp-tor:resource-compilation-exec-tor:$vKmpTorResource")

          // Alternatively, if using the `-gpl` variants
      //    implementation("io.matthewnelson.kmp-tor:resource-compilation-exec-tor-gpl:$vKmpTorResource")
      }
      ```
    - The `tor` compilations must be extracted to the `ApplicationInfo.nativeLibraryDir` when the 
      application is installed:
      ```kotlin
      // build.gradle.kts
      android {
          packaging {
              jniLibs.useLegacyPackaging = true
          }
      }
      ```
      ```kotlin
      // gradle.properties
      android.bundle.enableUncompressedNativeLibs=false
      ```

- If utilizing the `resource-noexec-tor{-gpl}` Android **Native** dependency:
    - Add `libtor` compilations to your **Android** application:
      ```kotlin
      // build.gradle.kts
      dependencies {
          implementation("io.matthewnelson.kmp-tor:resource-compilation-lib-tor:$vKmpTorResource")

          // Alternatively, if using the `-gpl` variants
      //    implementation("io.matthewnelson.kmp-tor:resource-compilation-lib-tor-gpl:$vKmpTorResource")
      }
      ```

- Optionally, configure splits for each ABI:
  ```kotlin
  // build.gradle.kts
  android {
      splits {
          abi {
              isEnable = true
              reset()
              include("x86", "armeabi-v7a", "arm64-v8a", "x86_64")
              isUniversalApk = true
          }
      }
  }
  ```

</details>

<details>
    <summary>Configure iOS</summary>

See the [frameworks gradle plugin README](library/resource-frameworks-gradle-plugin/README.md) for more details.

</details>

<details>
    <summary>Configure Jvm/Java</summary>

See the [filterjar gradle plugin README](library/resource-filterjar-gradle-plugin/README.md) for more details.

</details>

<details>
    <summary>Configure Kotlin/Js and/or Kotlin/WasmJs</summary>

See the [npmjs README](library/npmjs/README.md) for more details.

</details>

If utilizing with [kmp-tor][url-kmp-tor], simply pass into `TorRuntime.Environment.Builder`.

- E.g. `ResourceLoaderTorExec` via `resource-exec-tor` dependency (or its `-gpl` variant)
```kotlin
val env = TorRuntime.Environment.Builder(myWorkDir, myCacheDir, ResourceLoaderTorExec::getOrCreate)
```
- E.g. `ResourceLoaderTorNoExec` via `resource-noexec-tor` dependency (or its `-gpl` variant)
```kotlin
val env = TorRuntime.Environment.Builder(myWorkDir, myCacheDir, ResourceLoaderTorNoExec::getOrCreate)
```

See [kmp-tor-samples][url-kmp-tor-samples] for more details.

<!-- TAG_VERSION -->
[badge-latest-release]: https://img.shields.io/badge/latest--release-408.19.0-5d2f68.svg?logo=torproject&style=flat&logoColor=5d2f68
[badge-license]: https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat

<!-- TAG_DEPENDENCIES -->
[badge-androidx-startup]: https://img.shields.io/badge/androidx.startup-1.1.1-6EDB8D.svg?logo=android
[badge-kotlin]: https://img.shields.io/badge/kotlin-2.2.21-blue.svg?logo=kotlin
[badge-build-env]: https://img.shields.io/badge/build--env-0.4.1-blue.svg?logo=docker
[badge-kmp-tor-common]: https://img.shields.io/badge/kmp--tor--common-2.4.2--SNAPSHOT-blue.svg?style=flat

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
[badge-platform-wasi]: https://img.shields.io/badge/-wasi-18a033.svg?style=flat
[badge-platform-windows]: http://img.shields.io/badge/-windows-4D76CD.svg?style=flat
[badge-platform-android-native]: http://img.shields.io/badge/-android--native-6EDB8D.svg?style=flat

[url-androidx-startup]: https://developer.android.com/jetpack/androidx/releases/startup
[url-build-env]: https://github.com/05nelsonm/build-env
[url-latest-release]: https://github.com/05nelsonm/kmp-tor-resource/releases/latest
[url-license]: https://www.apache.org/licenses/LICENSE-2.0.html
[url-kotlin]: https://kotlinlang.org
[url-kmp-tor]: https://github.com/05nelsonm/kmp-tor
[url-kmp-tor-common]: https://github.com/05nelsonm/kmp-tor-common
[url-kmp-tor-samples]: https://github.com/05nelsonm/kmp-tor-samples
