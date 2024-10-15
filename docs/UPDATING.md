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
   ./external/task.sh sign:macos "<yubikey slot>" "</path/to/api_key.json>"
   ./external/task.sh sign:mingw
   ```

6) Package compilations
   ```shell
   ./external/task.sh package:all
   ```

7) Update `ResourceValidation` extension hash values
   ```shell
   ./external/task.sh validate:all:update_hashes
   ```
