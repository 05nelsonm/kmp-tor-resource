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

5) Clean:
   ```shell
   ./gradlew clean -PKMP_TARGETS="JVM,LINUX_ARM64,LINUX_X64,MACOS_ARM64,MACOS_X64,MINGW_X64,IOS_ARM64,IOS_SIMULATOR_ARM64,IOS_X64,TVOS_ARM64,TVOS_SIMULATOR_ARM64,TVOS_X64,WATCHOS_ARM32,WATCHOS_ARM64,WATCHOS_DEVICE_ARM64,WATCHOS_SIMULATOR_ARM64,WATCHOS_X64"
   ```

6) Sync the project:
   ```shell
   ./gradlew prepareKotlinBuildScriptModel -PKMP_TARGETS="JVM,LINUX_ARM64,LINUX_X64,MACOS_ARM64,MACOS_X64,MINGW_X64,IOS_ARM64,IOS_SIMULATOR_ARM64,IOS_X64,TVOS_ARM64,TVOS_SIMULATOR_ARM64,TVOS_X64,WATCHOS_ARM32,WATCHOS_ARM64,WATCHOS_DEVICE_ARM64,WATCHOS_SIMULATOR_ARM64,WATCHOS_X64"
   ```

7) Check the generated reports for any errors:
   ```shell
   (
     set +e
     ERRS=$(ls library/resource-tor/build/reports/resource-validation/resource-tor | grep ".err")
     echo ""
     for file_err in $ERRS; do
       echo "$file_err:"
       cat "library/resource-tor/build/reports/resource-validation/resource-tor/$file_err"
     done
     echo ""
   )
   ```

If the output is blank, all built/packaged resources matched the expected sha256 hash values

Any error output is pretty self-explanatory (either the file didn't exist or hashes did not 
match what was expected for the given platform/architecture).

That's it.
