# test-linux

For CI tests to check linux compilations for architectures that are not supported via GitHub Actions

`ARGS >> {timeout seconds} {expected host} {expected arch} {/path/to/runtime/dir}`

```sh
./gradlew clean
./gradlew :test-linux:assembleFatJar
java -jar test-linux/build/libs/test-linux.jar "10" "linux-libc" "x86_64" "test-linux/build"
```
