# UPDATING

1) Update git submodules in `external` to desired tags

2) Update version strings in `README.md`

3) Copy `tor` man pages
   ```shell
   cp external/tor/doc/man/tor.1.txt docs/tor-man.adoc
   ```

4) Clean & build
   ```shell
   ./external/task.sh clean
   ./external/task.sh build:all
   ```

5) Generate code signatures
   ```shell
   ./external/task.sh sign:macos
   ./external/task.sh sign:mingw
   ```

6) Package binaries
   ```shell
   ./external/task.sh package
   ```

7) Run verification to output new expected hash values
   ```shell
   ./external/task.sh verify:all
   ```

8) Update expected hash values in `build-logic/src/main/kotlin/io/matthewnelson/kmp/tor/resource/validation`

9) Re-run verification to ensure updated values are correct
   ```shell
   ./external/task.sh verify:all
   ```
