# npmjs

Module specifically for `Node.js` publications to `Npmjs` registry, available from [Npmjs#search][url-npmjs-search]

When utilizing the `resource-exec-tor` dependency (or its `-gpl` variant) for Kotlin Multiplatform `Node.js`, 
an additional dependency for the `npm` module must be added to the `js` and/or `wasmJs` source set(s). The Kotlin 
implementation expects the node module to be present at runtime.

- `Non-GPL` variant
  ```kotlin
  // build.gradle.kts

  kotlin {
      sourceSets {
          arrayOf("jsMain", "wasmJsMain").forEach { name ->
              findByName(name)?.dependencies {
                  implementation("io.matthewnelson.kmp-tor:resource-exec-tor:$vKmpTorResource")

                  // All platforms
                  implementation(npm("kmp-tor.resource-exec-tor.all", vKmpTorResource))

                  // Alternatively can express platform specific dependencies if you know
                  // which your code will be running on.
                  // implementation(npm("kmp-tor.resource-exec-tor.linux-android", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor.linux-libc", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor.linux-musl", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor.macos", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor.mingw", vKmpTorResource))
              }
          }
      }
  }
  ```

- `GPL` variant
  ```kotlin
  // build.gradle.kts

  kotlin {
      sourceSets {
          arrayOf("jsMain", "wasmJsMain").forEach { name ->
              findByName(name)?.dependencies {
                  implementation("io.matthewnelson.kmp-tor:resource-exec-tor-gpl:$vKmpTorResource")

                  // All platforms
                  implementation(npm("kmp-tor.resource-exec-tor-gpl.all", vKmpTorResource))

                  // Alternatively can express platform specific dependencies if you know
                  // which your code will be running on.
                  // implementation(npm("kmp-tor.resource-exec-tor-gpl.linux-android", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor-gpl.linux-libc", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor-gpl.linux-musl", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor-gpl.macos", vKmpTorResource))
                  // implementation(npm("kmp-tor.resource-exec-tor-gpl.mingw", vKmpTorResource))
              }
          }
      }
  }
  ```

[url-npmjs-search]: https://www.npmjs.com/search?q=keywords%3Akmp-tor-resource
