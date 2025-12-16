# DETERMINISTIC BUILDS

Tor and it's dependencies are compiled deterministically such that anyone who wishes to verify
the output can do so. Compilation, packaging and validation are had by the `external/task.sh`
script. The expected Sha256 hashes are provided by gradle extensions located in directory
`build-logic/src/main/kotlin/resource/validation/extensions`.

### TL;DR

<!-- TAG_VERSION -->
```
git clone --branch 408.21.0 --depth 1 https://github.com/05nelsonm/kmp-tor-resource.git \
  && cd kmp-tor-resource \
  && ./external/task.sh build:all \
  && ./external/task.sh package:all \
  && ./external/task.sh validate
```

### Validate Compilations

- What you will need:
    - An `x86_64` machine
        - If your machine is not `x86_64`, but your docker installation supports virtualization 
          of `linux/amd64` containers, things will still work but there will be significant overhead. 
          For example, `macOS` with `M` series chipsets build fine using rosetta virtualization, but 
          may take upwards of 6x longer.
    - Bash
    - Git
    - Docker
    - Java 11+
    - Approximately 20GB of available disk space

1) Clone the repository:
   ```shell
   git clone https://github.com/05nelsonm/kmp-tor-resource.git
   cd kmp-tor-resource
   ```

<!-- TAG_VERSION -->
2) Checkout the tag you wish to validate (replace with desired tag name):
   ```shell
   git checkout 408.21.0
   ```

3) Compile code (go touch grass for a bit):
   ```shell
   ./external/task.sh build:all
   ```
   - NOTE: If you are not building for the first time here, you can add flag `--rebuild` 
     or perform `./external/task.sh clean` before `build:all` to start completely fresh.

4) Package all compilations:
   ```shell
   ./external/task.sh package:all
   ```

5) Validate hashes of the packaged compilations with their expected hashes:
   ```shell
   ./external/task.sh validate
   ```
   - NOTE: Java `linux-android` resources are the exact same compilations as the `build:all:android` 
     task output, just gzipped. So, there is implicit validation of the android compilations.
   - If you have Java 17+ and Android Studio installed, you can run task `validate:all` instead 
     which will include android in its checks.

If the output of the error report files are empty, all built/packaged resources matched the 
expected Sha256 hash values.

Any error output is pretty self-explanatory; either the file didn't exist or hashes didn't 
match what was expected for the given platform/architecture.

<details>
    <summary>Example error output</summary>

```
resource-compilation-exec-tor/android.err:

resource-compilation-exec-tor-gpl/android.err:

resource-compilation-lib-tor/android.err:

resource-compilation-lib-tor-gpl/android.err:

resource-exec-tor/jvm.err:
resource-exec-tor/linuxArm64.err:
resource-exec-tor/linuxX64.err:
resource-exec-tor/macosArm64.err:
resource-exec-tor/macosX64.err:
resource-exec-tor/mingwX64.err:

resource-exec-tor-gpl/jvm.err:
resource-exec-tor-gpl/linuxArm64.err:
resource-exec-tor-gpl/linuxX64.err:
resource-exec-tor-gpl/macosArm64.err:
resource-exec-tor-gpl/macosX64.err:
resource-exec-tor-gpl/mingwX64.err:

resource-frameworks-gradle-plugin/jvm.err:

resource-geoip/jvm-geoip.err:
ERROR[ Resource does not exist: external/build/package/resource-geoip/src/jvmMain/resources/io/matthewnelson/kmp/tor/resource/geoip/geoip.gz ]
resource-geoip/native.err:

resource-lib-tor/iosSimulatorArm64.err:
resource-lib-tor/iosX64.err:
resource-lib-tor/jvm.err:
ERROR[ Lib hash[1f0415656954b34d1e8eebbaec447b239572b5f9168caff2e46e353e19c58260] did not match expected[testing56954b34d1e8eebbaec447b239572b5f9168caff2e46e353e19c58260]: external/build/package/resource-lib-tor/src/jvmMain/resources/io/matthewnelson/kmp/tor/resource/lib/tor/native/macos/aarch64/libtor.dylib.gz ]
resource-lib-tor/linuxArm64.err:
resource-lib-tor/linuxX64.err:
resource-lib-tor/macosArm64.err:
resource-lib-tor/macosX64.err:
resource-lib-tor/mingwX64.err:

resource-lib-tor-gpl/iosSimulatorArm64.err:
resource-lib-tor-gpl/iosX64.err:
resource-lib-tor-gpl/jvm.err:
resource-lib-tor-gpl/linuxArm64.err:
resource-lib-tor-gpl/linuxX64.err:
resource-lib-tor-gpl/macosArm64.err:
resource-lib-tor-gpl/macosX64.err:
resource-lib-tor-gpl/mingwX64.err:

resource-noexec-tor/jvm.err:

resource-noexec-tor-gpl/jvm.err:


    Task 'validate' completed in 0m38.531s

```

</details>

That's it.
