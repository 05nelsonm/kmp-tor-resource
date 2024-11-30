# npmjs

Module specifically for `Node.js` module publications to `Npmjs` registry available at https://www.npmjs.com/search?q=kmp-tor.resource-exec

When utilizing the `resource-exec-tor` dependency (or its `-gpl` variant) for Kotlin Multiplatform `Node.js`, 
an additional dependency for the `npm` module must be added to the `js` source set. The Kotlin implementation 
expects the node module to be present at runtime.

- `Non-GPL` variant
  ```kotlin
  // build.gradle.kts

  kotlin {
      sourceSets {
          findByName("jsMain")?.dependencies {
              implementation("io.matthewnelson.kmp-tor:resource-exec-tor:$vKmpTorResource")

              // All platforms
              implementation(npm("kmp-tor.resource-exec-tor.all", vKmpTorResource))

              // Alternatively can express platform specific dependencies if you know
              // which your code will be running on.
              // implementation(npm("kmp-tor.resource-exec-tor.linux-android", vKmpTorResource))
              // implementation(npm("kmp-tor.resource-exec-tor.linux-libc", vKmpTorResource))
              // implementation(npm("kmp-tor.resource-exec-tor.macos", vKmpTorResource))
              // implementation(npm("kmp-tor.resource-exec-tor.mingw", vKmpTorResource))
          }
      }
  }
  ```

- `GPL` variant
  ```kotlin
  // build.gradle.kts

  kotlin {
      sourceSets {
          findByName("jsMain")?.dependencies {
              implementation("io.matthewnelson.kmp-tor:resource-exec-tor-gpl:$vKmpTorResource")

              // All platforms
              implementation(npm("kmp-tor.resource-exec-tor-gpl.all", vKmpTorResource))

              // Alternatively can express platform specific dependencies if you know
              // which your code will be running on.
              // implementation(npm("kmp-tor.resource-exec-tor-gpl.linux-android", vKmpTorResource))
              // implementation(npm("kmp-tor.resource-exec-tor-gpl.linux-libc", vKmpTorResource))
              // implementation(npm("kmp-tor.resource-exec-tor-gpl.macos", vKmpTorResource))
              // implementation(npm("kmp-tor.resource-exec-tor-gpl.mingw", vKmpTorResource))
          }
      }
  }
  ```