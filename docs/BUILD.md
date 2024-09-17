# BUILD

For those who wish to validate reproducibility of the binaries being distributed, 
you can do so by following along below.

- What you will need:
    - `Linux` or `macOS` `x86_64`
        - Must be `x86_64` machine to compile apple targets. See [[#4]][issue-4]
    - Git
    - Docker
    - Java 11+

1) Clone the repository
   ```shell
   git clone https://github.com/05nelsonm/kmp-tor-resource.git
   cd kmp-tor-resource
   ```

2) Checkout the tag you wish to validate (replace with desired tag name)
   ```shell
   git checkout 408.10.0
   ```

3) Clean any prior builds
   ```shell
   ./external/task.sh clean
   ```

4) Compile code (go touch grass for a bit):
   ```shell
   ./external/task.sh build:all
   ```

5) Package everything:
   ```shell
   ./external/task.sh package:all
   ```

6) Validate hashes of packaged resources with their expected hashes:
   ```shell
   ./external/task.sh validate
   ```

If the output of the error report files is blank, all built/packaged resources matched the 
expected sha256 hash values.

Any error output is pretty self-explanatory; either the file didn't exist or hashes didn't 
match what was expected for the given platform/architecture.

That's it.

[issue-4]: https://github.com/05nelsonm/kmp-tor-resource/issues/4
