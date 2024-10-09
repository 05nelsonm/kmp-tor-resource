# kmp-tor-resource
[![badge-license]][url-license]
[![badge-latest-release]][url-latest-release]

[![badge-kotlin]][url-kotlin]
[![badge-build-env]][url-build-env]
[![badge-kmp-tor-common]][url-kmp-tor-common]

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
| openssl  | `openssl-3.2.2`         |
| tor      | `tor-0.4.8.12`          |
| xz       | `v5.6.2`                |
| zlib     | `v1.3.1`                |

**NOTE:** All `macOS` and `Windows` compilations are code signed so they work out of the box.

More details about how things are compiled can be found [HERE](docs/COMPILATION_DETAILS.md)

### Jvm/Node.js Supported Operating Systems & Architectures

|                 | x86 | x86_64 | armv7 | aarch64 | ppc64 |
|-----------------|-----|--------|-------|---------|-------|
| Windows         | ✔   | ✔      |       |         |       |
| macOS           |     | ✔      |       | ✔       |       |
| Linux (android) | ✔   | ✔      | ✔     | ✔       |       |
| Linux (libc)    | ✔   | ✔      | ✔     | ✔       | ✔     |
| Linux (musl)    |     |        |       |         |       |
| FreeBSD         |     |        |       |         |       |

### Types (`exec` & `noexec`)

TODO

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

### Usage

<!-- TODO -->

Currently, there is only a `SNAPSHOT` publication in order to work on [kmp-tor][url-kmp-tor] `2.0.0`. 
Once that work is complete a Release will be made for `kmp-tor-resource`.

<!-- TAG_VERSION -->
[badge-latest-release]: https://img.shields.io/badge/latest--release-408.12.0--SNAPSHOT-5d2f68.svg?logo=torproject&style=flat&logoColor=5d2f68
[badge-license]: https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat

<!-- TAG_DEPENDENCIES -->
[badge-kotlin]: https://img.shields.io/badge/kotlin-1.9.24-blue.svg?logo=kotlin
[badge-build-env]: https://img.shields.io/badge/build--env-0.1.3-blue.svg?logo=docker
[badge-kmp-tor-common]: https://img.shields.io/badge/kmp--tor--common-2.1.0--SNAPSHOT-blue.svg?style=flat

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
[url-kmp-tor-common]: https://github.com/05nelsonm/kmp-tor-common
[url-core-lib-locator]: https://github.com/05nelsonm/kmp-tor-core/tree/master/library/core-lib-locator
