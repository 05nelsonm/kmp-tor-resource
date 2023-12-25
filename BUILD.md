# BUILD

For those who wish to verify reproducibility of the binaries being distributed, 
you can do so by following along below.

- What you will need:
    - A `Linux x86_64` machine
    - Git
    - Docker
    - Java 11+


1) Clone the repository
   ```shell
   git clone https://github.com/05nelsonm/kmp-tor-resource.git
   cd kmp-tor-resource
   ```

2) Checkout the tag you wish to verify (replace with desired tag name)
   ```shell
   git checkout 4.8.10-0
   ```

3) Build `tor` binaries & go touch grass for a bit:
   ```shell
   ./external/task.sh build:all
   ```

4) Package them:
   ```shell
   ./external/task.sh package
   ```

5) Verify hashes:
   ```shell
   ./external/task.sh verify
   ```

If the output is blank, all built/packaged resources matched the expected sha256 hash values

Any error output is pretty self-explanatory (either the file didn't exist or hashes did not 
match what was expected for the given platform/architecture).

That's it.
