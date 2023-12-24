# CHANGELOG

## Version 4.8.6-0 (2023-09-29)
 - Update dependencies
     - Kotlin -> `1.9.10`
     - Tor -> `0.4.8.6`

## Version 4.7.13-4 (2023-03-24)
 - *Actually* fixes an issue with Java 9 modularization not being able to find
   resources from different modules/jars during extraction. [[#76]][pr-76]
     - Adds a new parameter to `TorBinaryResource.from`; `loadPathPrefix`. This
       is in order for the `Extractor` to find the `Loader` class located in that
       module to retrieve declared resources using the correct `ClassLoader`.

## Version 4.7.13-3 (2023-03-23)
 - Fixes an issue with Java 9 modularization not being able to find resources
   attributed to `kmp-tor-binary-<os><arch>` modules not containing a class
   that can be exported. [[#75]][pr-75]

## Version 4.7.13-2 (2023-03-16)
 - Fixes macosarm64 binaries being unable to run on M1/2 because binary files
   were not developer signed. This is an intermediate "quick fix" solution. 
   More information can be found in [[#66]][issue-66].

## Version 4.7.13-1 (2023-03-14)
 - Adds ability for Jvm/JS consumers to package and provide their
   own binary resources via `TorBinaryResource` if a platform/architecture
   is not currently supported by `kmp-tor-binary`
 - Removes submodule `kotlin-components`
 - Refactors gradle build system to use composite builds
 - Updates Kotlin `1.8.0` -> `1.8.10`

## Version 4.7.13-0 (2023-01-31)
 - Updates `kotlin-components` submodule
     - Build & publishing improvements
 - Enables Kotlin's `explicitApi` setting for `kmp-tor-binary-extract`
 - Removes flaky tests

## Version 4.7.12-2 (2023-01-09)
 - Updates `kotlin-components` submodule
     - Kotlin `1.7.20` -> `1.8.0`

## Version 4.7.12-1 (2023-01-07)
 - Updates `kotlin-components` submodule
     - Kotlin `1.6.21` -> `1.7.20`
     - Android Gradle Plugin `7.0.4` -> `7.3.1`

## Version 4.7.12-0 (2023-01-01)
 - Update Tor to `0.4.7.12`

## Version 4.7.11-0 (2022-12-04)
 - Updates Tor to `0.4.7.11`
 - Removes the `binary-build-jni` project
 - Relocates `tor-browser-build` submodule
 - Cleans up the binary build script
 - Updates `kotlin-components`
     - Documentation + publication script improvements

## Version 4.7.10-1 (2022-10-22)
 - Changes the version naming scheme
     - Ex: `4.7.10-1`
         - `Tor` version `0.4.7.10`
         - `kmp-tor-binary-*` sub-release `1`
     - See [Issue 36](https://github.com/05nelsonm/kmp-tor-binary/issues/36#issuecomment-1284654389)
 - Refactors binary build script to handle upstream changes to `tor-browser-build` 
   packaging
 - Packages all binary/geoip files individually instead of using an archive
     - All files are gzipped
     - Mitigates need for external dependencies to extract from `.zip` or `.tar` archives
 - Refactors `kmp-tor-binary-extract` to handle extracting gzipped files
     - Removes the `ZipArchiveExtractor` class entirely, in favor of `TorResource` and 
       `Extractor` classes to handle gzipped files.
 - Adds support for `Node.js`
     - Assets are distributed via `npmjs`

## Version 0.4.7.10 (2022-09-24)
 - Updates `tor-browser-build` submodule source
     - FROM: `https://gitweb.torproject.org/builders/tor-browser-build.git/`
     - TO: `https://gitlab.torproject.org/tpo/applications/tor-browser-build.git`
 - Updates Tor to `0.4.7.10`

## Version 0.4.7.8 (2022-06-24)
 - Updates Tor to `0.4.7.8`
 - Updates geoip files
 - Increase Android minSdk from 16 -> 21
 - Re-enable compiler flag enableCompatibilityMetadataVariant=true to support 
   non-hierarchical projects. (sorry...)

## Version 0.4.7.7 (2022-05-08)
 - Updates Tor to `0.4.7.7`
 - Updates geoip files
 - Updates Kotlin-Components
     - Bumps Kotlin version 1.6.10 -> 1.6.21

## Version 0.4.6.10 (2022-02-20)
 - Fixes zip archival packaging reproducibility
     - Manifest file ordering was not deterministic
 - Updates Tor to `0.4.6.10`
 - Updates geoip files

## Version 0.4.6.9 (2022-02-06)
 - Initial release

[issue-66]: https://github.com/05nelsonm/kmp-tor-binary/issues/66
[pr-75]: https://github.com/05nelsonm/kmp-tor-binary/pull/75
[pr-76]: https://github.com/05nelsonm/kmp-tor-binary/pull/76
