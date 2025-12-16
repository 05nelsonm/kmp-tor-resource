# Releasing

### Linux

- Ensure java version is greater than or equal to 17
```
java --version
```

- Validate compilations (there should be no errors)
```
./external/task.sh validate:all
```

- Set ssh variables in terminal shell
```
SSH_MAC_PROFILE="<profile name>"
SSH_MAC_PATH="/Users/path/on/mac/to/kmp-tor-resource"
```

- Copy compilations to `macOS` box via ssh for publication
```
cd external && tar c build | ssh $SSH_MAC_PROFILE "cd $SSH_MAC_PATH/external; rm -rf build; tar x -" && cd ..
```

### macOS

- Ensure java version is greater than or equal to 17
```
java --version
```

- Sign a random `.txt` file (gpg tty for YubiKey PIN + gradle build don't mix)
```
gpg --sign --armor --detach ~/Documents/hello.txt
```

- Ensure you are in a `bash` shell
```
bash
```

- Set version variable in terminal shell
```
VERSION_NAME="<version name>"
```

- Create a release branch
```
git checkout master
git pull
git checkout -b release_"$VERSION_NAME"
```

- Validate compilations (there should be no errors)
```
./external/task.sh validate:all
```

- Ensure that credentials for `Npmjs` are set in `~/.gradle/gradle.properties`
```
NPMJS_AUTH_TOKEN=<auth token>
```

- Ensure that credentials for `Maven Central` are set in `~/.gradle/gradle.properties`
```
mavenCentralUsername=MyUserName
mavenCentralPassword=MyPassword
```

- Ensure that the proper GPG gradle config are set in `~/.gradle/gradle.properties` for signing
```
signing.keyId=MyGPGKeyId
signing.password=MyGPGKeyPassword
signing.secretKeyRingFile=/path/to/.gnupg/MyGPGKey.gpg
```

- Update `VERSION_NAME` (remove `-SNAPSHOT`) and `VERSION_CODE` in root project's `gradle.properties` file

- Set `NPMJS_SNAPSHOT_VERSION` to `0` in root project's `gradle.properties` file

- Update `version` in project's `README.md` documentation

- Update `version` in project's `docs/DETERMINISTIC_BUILDS.md` documentation

- Update `version` in project's `library/resource-filterjar-gradle-plugin/README.md` documentation

- Update `version` in project's `library/resource-frameworks-gradle-plugin/README.md` documentation

- Update `CHANGELOG.md`

- Clean
```
./gradlew clean
```

- Perform a dry run of the publication
```
NPMJS_PUBLISH_TASKS=$(./gradlew tasks | grep "ToNpmjsRegistry" | cut -d ' ' -f 1)
./gradlew $NPMJS_PUBLISH_TASKS --no-daemon --no-parallel -PNPMJS_DRY_RUN
```

- Inspect the terminal output to ensure:
    - `README.md` is present
    - `index.js` is present
    - geoip files should be about `2MB` each
    - tor files should be about `3MB` each

- Publish to `Npmjs`
```
./gradlew $NPMJS_PUBLISH_TASKS --no-daemon --no-parallel
```

- Update `.kotlin-js-store/{js/wasm}/yarn.lock` files
```
./gradlew kotlinUpgradeYarnLock
./gradlew kotlinWasmUpgradeYarnLock
```

- Verify that `.kotlin-js-store/yarn.lock` is using the release 
  publication dependency (should not be using `SNAPSHOT` version).
```
cat .kotlin-js-store/js/yarn.lock | grep "kmp-tor.resource" | grep "@"
cat .kotlin-js-store/wasm/yarn.lock | grep "kmp-tor.resource" | grep "@"
```

- Commit Changes
```
git add --all
git commit -S -m "Prepare $VERSION_NAME release"
git tag -s "$VERSION_NAME" -m "Release v$VERSION_NAME"
```

- Perform a clean build
```
./gradlew clean -DKMP_TARGETS_ALL
./gradlew build --no-build-cache -DKMP_TARGETS_ALL
```

- Publish
```
./gradlew publishAllPublicationsToMavenCentralRepository --no-daemon --no-parallel -DKMP_TARGETS_ALL
```

- The [gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) should have automatically
  closed the staged repositories, but if it did not:
    - Close publications (Don't release yet)
        - Login to Central Portal: [central.sonatype.com](https://central.sonatype.com/publishing/deployments)
        - Click on **Staging Repositories**
        - Select all Publications
        - Click **Close** then **Confirm**
        - Wait a bit, hit **Refresh** until the *Status* changes to *Closed*

- Check Publication
```
./gradlew clean -PCHECK_PUBLICATION -DKMP_TARGETS_ALL
./gradlew :tools:check-publication:build --refresh-dependencies -PCHECK_PUBLICATION -DKMP_TARGETS_ALL
```

- **Release** publications from Central Portal UI at [central.sonatype.com](https://central.sonatype.com/publishing/deployments)

- Merge release branch to `master`
```
git checkout master
git pull
git merge --no-ff -S release_"$VERSION_NAME"
```

- Update `VERSION_NAME` (add `-SNAPSHOT`) and `VERSION_CODE` in root project's `gradle.properties` file

- Update `.kotlin-js-store/{js/wasm}/yarn.lock`
```
./gradlew kotlinUpgradeYarnLock
./gradlew kotlinWasmUpgradeYarnLock
```

- Verify that `.kotlin-js-store/yarn.lock` is using the `SNAPSHOT` publications
```
cat .kotlin-js-store/js/yarn.lock | grep "kmp-tor.resource" | grep "@"
cat .kotlin-js-store/wasm/yarn.lock | grep "kmp-tor.resource" | grep "@"
```

- Commit changes
```
git add --all
git commit -S -m "Prepare for next development iteration"
```

- Push Changes
```
git push
```

- Push Tag
```
git push origin "$VERSION_NAME"
```

- Delete local release branch
```bash
git branch -D release_"$VERSION_NAME"
git fetch origin --prune
```

- Wait for releases to become available on [MavenCentral](https://repo1.maven.org/maven2/io/matthewnelson/kotlin-components/)
- Draft new release on GitHub
    - Enter the release name <VersionName> as tag and title
    - Have the description point to the changelog
