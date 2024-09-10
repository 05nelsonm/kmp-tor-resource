# Releasing

### Linux

- Ensure java version is greater than or equal to 17
```bash
java --version
```

- Set version variable in terminal shell
```
VERSION_NAME="<version name>"
```

- Create a release branch
```bash
git checkout master
git pull
git checkout -b release_"$VERSION_NAME"
```

- Update `VERSION_NAME` (remove `-SNAPSHOT`) and `VERSION_CODE` in root project's `gradle.properties` file

- Update `version` in project's `README.md` documentation

- Update `tag` in project's `docs/BUILD.md` documentation (step 2)

- Update `CHANGELOG.md`

- Ensure that credentials for `Npmjs` are set in `~/.gradle/gradle.properties`
```
NPMJS_AUTH_TOKEN=<auth token>
```

- Clean
```bash
./gradlew clean
```

- Perform a dry run of the publication
```bash
NPMJS_PUBLISH_TASKS=$(./gradlew tasks | grep "ToNpmjsRegistry" | cut -d ' ' -f 1)
./gradlew $NPMJS_PUBLISH_TASKS --no-daemon --no-parallel -PNPMJS_DRY_RUN
```

- Inspect the terminal output to ensure:
    - `README.md` is present
    - `index.js` is present
    - geoip files should be about `1MB` each
    - tor files should be about `3MB` each

- Check for resource validation errors (tasks would not have been present otherwise)
```bash
cat library/npmjs/build/reports/resource-validation/resource-shared-geoip/jvm-geoip.err
cat library/npmjs/build/reports/resource-validation/resource-shared-tor/jvm.err
cat library/npmjs/build/reports/resource-validation/resource-shared-tor-gpl/jvm.err
```

- Publish to `Npmjs`
```bash
./gradlew $NPMJS_PUBLISH_TASKS --no-daemon --no-parallel
```

- Update `.kotlin-js-store/yarn.lock`
```bash
./gradlew kotlinUpgradeYarnLock
```

- Verify that `.kotlin-js-store/yarn.lock` is using the release 
  publication dependency (should not be using `SNAPSHOT` version).
```bash
cat .kotlin-js-store/yarn.lock | grep "kmp-tor.resource" | grep "@"
```

- Commit Changes
```bash
git add --all
git commit -S -m "Prepare $VERSION_NAME release"
git tag -s "$VERSION_NAME" -m "Release v$VERSION_NAME"
```

- Make sure you have valid credentials in `~/.gradle/gradle.properties`
```
mavenCentralUsername=MyUserName
mavenCentralPassword=MyPassword
```

- Make sure you have GPG gradle config setup in `~/.gradle/gradle.properties` for signing
```
signing.gnupg.executable=gpg
signing.gnupg.useLegacyGpg=true
signing.gnupg.homeDir=/path/to/.gnupg/
signing.gnupg.optionsFile=/path/to/.gnupg/gpg.conf
signing.gnupg.keyName=0x61471B8AB3890961
```

- Make sure GPG is picking up YubiKey to sign releases
```bash
gpg --card-status
```

- Disable YubiKey touch for signing
```bash
ykman openpgp keys set-touch sig off
```

- Perform a clean build
```bash
./gradlew clean -DKMP_TARGETS_ALL
./gradlew build -DKMP_TARGETS_ALL
```

- Check `resource-validation` error reports for any errors
```bash
cat library/resource-android-unit-test-tor/build/reports/resource-validation/resource-shared-tor/jvm.err
cat library/resource-android-unit-test-tor-gpl/build/reports/resource-validation/resource-shared-tor/jvm.err
cat library/resource-shared-geoip/build/reports/resource-validation/resource-shared-geoip/jvm-geoip.err
cat library/resource-shared-geoip/build/reports/resource-validation/resource-shared-geoip/native.err
cat library/resource-shared-tor/build/reports/resource-validation/resource-shared-tor/android.err
cat library/resource-shared-tor/build/reports/resource-validation/resource-shared-tor/jvm.err
cat library/resource-shared-tor-gpl/build/reports/resource-validation/resource-shared-tor-gpl/android.err
cat library/resource-shared-tor-gpl/build/reports/resource-validation/resource-shared-tor-gpl/jvm.err
cat library/resource-exec-tor/build/reports/resource-validation/resource-exec-tor/linuxArm64.err
cat library/resource-exec-tor/build/reports/resource-validation/resource-exec-tor/linuxX64.err
cat library/resource-exec-tor/build/reports/resource-validation/resource-exec-tor/mingwX64.err
cat library/resource-exec-tor-gpl/build/reports/resource-validation/resource-exec-tor-gpl/linuxArm64.err
cat library/resource-exec-tor-gpl/build/reports/resource-validation/resource-exec-tor-gpl/linuxX64.err
cat library/resource-exec-tor-gpl/build/reports/resource-validation/resource-exec-tor-gpl/mingwX64.err
```

- Publish
```bash
./gradlew publishAllPublicationsToMavenCentralRepository --no-daemon --no-parallel -DKMP_TARGETS_ALL
```

- Push release branch to repo (to publish from macOS)
```bash
git push -u origin release_"$VERSION_NAME"
```

- Push resources for `macOS` publications
```bash
git checkout -b release_RESOURCES
mkdir -p external/build-release
cp -R external/build/package external/build-release
git add --all
git commit -S -m "$VERSION_NAME release resources"
git push -u origin release_RESOURCES
```

### Macos

- Spin up VM of macOS and ensure USB pass through worked for the YubiKey
    - Should ask for PIN to log in

- Sign a random `.txt` file (gpg tty for YubiKey PIN + gradle build don't mix)
```shell
gpg --sign --armor --detach ~/Documents/hello.txt
```

- Ensure java version is greater than or equal to 11
```shell
java --version
```

- Ensure you are in a `bash` shell
```shell
bash
```

- Set version variable in terminal shell
```
VERSION_NAME="<version name>"
```

- Pull the latest code
```bash
git checkout master
git pull
```

- Move resources to `external/build`
```bash
git checkout release_RESOURCES
./external/task.sh clean
cp -R external/build-release external/build
git checkout release_"$VERSION_NAME"
```

- Make sure you have valid credentials in `~/.gradle/gradle.properties`
```
mavenCentralUsername=MyUserName
mavenCentralPassword=MyPassword
```

- Make sure you have GPG gradle config setup in `~/.gradle/gradle.properties` for signing
```
signing.gnupg.executable=gpg
signing.gnupg.useLegacyGpg=true
signing.gnupg.homeDir=/path/to/.gnupg/
signing.gnupg.optionsFile=/path/to/.gnupg/gpg.conf
signing.gnupg.keyName=0x61471B8AB3890961
```

- Perform a clean build
```bash
MACOS_TARGETS="JVM,JS,IOS_ARM64,IOS_X64,IOS_SIMULATOR_ARM64,MACOS_ARM64,MACOS_X64,TVOS_ARM64,TVOS_X64,TVOS_SIMULATOR_ARM64,WATCHOS_ARM32,WATCHOS_ARM64,WATCHOS_DEVICE_ARM64,WATCHOS_X64,WATCHOS_SIMULATOR_ARM64,WASM_JS,WASM_WASI"
./gradlew clean -PKMP_TARGETS="$MACOS_TARGETS"
./gradlew build -PKMP_TARGETS="$MACOS_TARGETS"
```

- Check `resource-validation` error reports for any errors
```bash
cat library/resource-shared-geoip/build/reports/resource-validation/resource-shared-geoip/native.err
cat library/resource-exec-tor/build/reports/resource-validation/resource-exec-tor/macosArm64.err
cat library/resource-exec-tor/build/reports/resource-validation/resource-exec-tor/macosX64.err
cat library/resource-exec-tor-gpl/build/reports/resource-validation/resource-exec-tor-gpl/macosArm64.err
cat library/resource-exec-tor-gpl/build/reports/resource-validation/resource-exec-tor-gpl/macosX64.err
```

- Publish macOS build
```bash
PUBLISH_TASKS=$(./gradlew tasks -PKMP_TARGETS="$MACOS_TARGETS" |
  grep "ToMavenCentralRepository" |
  cut -d ' ' -f 1 |
  grep -e "publishIos" -e "publishMacos" -e "publishTvos" -e "publishWatchos"
)
./gradlew $PUBLISH_TASKS --no-daemon --no-parallel -PKMP_TARGETS="$MACOS_TARGETS"
```

### Linux

- The [gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) should have automatically
  closed the staged repositories, but if it did not:
    - Close publications (Don't release yet)
        - Login to Sonatype OSS Nexus: [oss.sonatype.org](https://s01.oss.sonatype.org/#stagingRepositories)
        - Click on **Staging Repositories**
        - Select all Publications
        - Click **Close** then **Confirm**
        - Wait a bit, hit **Refresh** until the *Status* changes to *Closed*

- Check Publication
```bash
./gradlew clean -PCHECK_PUBLICATION -DKMP_TARGETS_ALL
./gradlew :tools:check-publication:build --refresh-dependencies -PCHECK_PUBLICATION -DKMP_TARGETS_ALL
```

### macOS

- Check Publication
```bash
./gradlew clean -PCHECK_PUBLICATION -DKMP_TARGETS_ALL
./gradlew :tools:check-publication:build --refresh-dependencies -PCHECK_PUBLICATION -DKMP_TARGETS_ALL
```

### Linux

- Re-enable YubiKey touch for signing
```bash
ykman openpgp keys set-touch sig on
```

- **Release** publications from Sonatype OSS Nexus StagingRepositories manager
    - Alternatively, can use Curl with the given repository id's that were output
      to terminal when publishing, e.g. `iomatthewnelson-1018`
      ```shell
      curl -v -u "<USER NAME>" \
        -H "Content-Type: application/json" \
        -H "Accept: application/json" \
        https://s01.oss.sonatype.org/service/local/staging/bulk/promote --data '
        {
          "data": {
            "stagedRepositoryIds": [
              "<repository id>",
              "<repository id>"
            ],
            "autoDropAfterRelease": true
          }
        }'
      ```

- Merge release branch to `master`
```bash
git checkout master
git pull
git merge --no-ff -S release_"$VERSION_NAME"
```

- Update `VERSION_NAME` (add `-SNAPSHOT`) and `VERSION_CODE` in root project's `gradle.properties` file

- Update `.kotlin-js-store/yarn.lock`
```bash
./gradlew kotlinUpgradeYarnLock -DKMP_TARGETS_ALL
```

- Commit changes
```bash
git add --all
git commit -S -m "Prepare for next development iteration"
```

- Push Changes
```bash
git push
```

- Push Tag
```bash
git push origin "$VERSION_NAME"
```

- Delete release branch on GitHub
- Delete release resources branch on GitHub

- Delete local release branch
```bash
git branch -D release_"$VERSION_NAME"
git branch -D release_RESOURCES
git fetch origin --prune
```

### Macos

- Checkout master
```bash
git checkout master
git pull
```

- Delete local release branch
```bash
git branch -D release_"$VERSION_NAME"
git branch -D release_RESOURCES
git fetch origin --prune
```

- Shutdown VMs (if not needed anymore)

### Linux

- Wait for releases to become available on [MavenCentral](https://repo1.maven.org/maven2/io/matthewnelson/kotlin-components/)
- Draft new release on GitHub
    - Enter the release name <VersionName> as tag and title
    - Have the description point to the changelog
