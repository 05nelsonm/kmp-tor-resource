# DETERMINISTIC BUILDS

Tor and it's dependencies are compiled deterministically such that anyone who wishes to verify 
the output can do so. Compilation, packaging and validation are had by the `external/task.sh` 
script. The expected Sha256 hashes are provided by gradle extensions located in directory 
`build-logic/src/main/kotlin/resource/validation/extensions`.

- What you will need:
    - `Linux` or `macOS` `x86_64`
        - Must be `x86_64` machine to compile apple targets. See [[#4]][issue-4]
    - Git
    - Docker
    - Java 11+

1) Clone the repository:
   ```shell
   git clone https://github.com/05nelsonm/kmp-tor-resource.git
   cd kmp-tor-resource
   ```

<!-- TAG_VERSION -->

2) Checkout the tag you wish to validate (replace with desired tag name):
   ```shell
   git checkout 408.13.1
   ```

3) Compile code (go touch grass for a bit):
   ```shell
   ./external/task.sh build:all
   ```
   - NOTE: If you are not building for the first time here, you can add flag `--rebuild` 
     or perform `clean` before `build:all` to start completely fresh.

4) Package all compilations:
   ```shell
   ./external/task.sh package:all
   ```

5) Validate hashes of the packaged compilations with their expected hashes:
   ```shell
   ./external/task.sh validate
   ```
   - NOTE: Java `linux-android` resources are the same compilations as the `build:all:android` 
     task, just gzipped (so, implicit validation of android compilations).
   - If you have Java 17+ and Android Studio installed, you can run task `validate:all` instead 
     which will include android in its checks.

If the output of the error report files is blank, all built/packaged resources matched the 
expected Sha256 hash values.

Any error output is pretty self-explanatory; either the file didn't exist or hashes didn't 
match what was expected for the given platform/architecture.

That's it.

[issue-4]: https://github.com/05nelsonm/kmp-tor-resource/issues/4
