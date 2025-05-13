# CHANGELOG

## Version 408.16.1 (2025-05-13)
 - Updates `xz` to `5.8.1` [[#122]][122]
 - Adds [resource-filterjar-gradle-plugin][url-resource-filterjar-gradle-plugin] for minifying Java & 
   KotlinMultiplatform/Jvm dependencies by filtering out unneeded compilations of tor for a given application 
   distribution. [[#120]][120]

## Version 408.16.0 (2025-04-21)
 - Updates `tor` to `0.4.8.16` [[#117]][117]
 - Fixes `JPMS` module access error [[#115]][115]

## Version 408.15.0 (2025-03-25)
 - Updates `tor` to `0.4.8.15` [[#113]][113]
 - Adds `proguard` rules to `resource-noexec-tor` and `resource-noexec-tor-gpl` android publications [[#111]][111]

## Version 408.14.0 (2025-02-26)
 - Updates `kotin` to `2.1.10` [[#108]][108]
 - Updates `kmp-tor-common` to `0.2.0` [[#108]][108]
 - Updates `android-gradle-plugin` to `8.7.3` [[#108]][108]
 - Updates `tor` to `0.4.8.14` [[#110]][110]
 - Updates [build-env][url-build-env] to `0.3.0` [[#110]][110]
 - Migrates `external/docker` files for `iOS` to [build-env][url-build-env] repository [[#110]][110]
 - Migrates `CLI` tooling from `kotlinx.cli` to `clikt` library [[#106]][106]

## Version 408.13.2 (2025-02-13)
 - Updates `openssl` to `3.4.1` [[#103]][103]
 - Updates `xz` to `5.6.4` [[#103]][103]
 - Updates `kmp-tor-common` to `2.1.2`

## Version 408.13.1 (2025-01-15)
 - Updates `kmp-tor-common` to `2.1.1` [[#99]][99]

## Version 408.13.0 (2024-12-02)
 - Use [cklib][url-cklib] when compiling `external/native` C code for Kotlin/Native targets [[#97]][97]
 - Updates `tor` to `0.4.8.13` [[#98]][98]
 - Updates `openssl` to `3.4.0` [[#98]][98]
 - Updates `xz` to `5.6.3` [[#98]][98]

## Version 408.12.0 (2024-11-30)
 - Initial Release

[97]: https://github.com/05nelsonm/kmp-tor-resource/pull/97
[98]: https://github.com/05nelsonm/kmp-tor-resource/pull/98
[99]: https://github.com/05nelsonm/kmp-tor-resource/pull/99
[103]: https://github.com/05nelsonm/kmp-tor-resource/pull/103
[106]: https://github.com/05nelsonm/kmp-tor-resource/pull/106
[108]: https://github.com/05nelsonm/kmp-tor-resource/pull/106
[110]: https://github.com/05nelsonm/kmp-tor-resource/pull/110
[111]: https://github.com/05nelsonm/kmp-tor-resource/pull/111
[113]: https://github.com/05nelsonm/kmp-tor-resource/pull/113
[115]: https://github.com/05nelsonm/kmp-tor-resource/pull/115
[117]: https://github.com/05nelsonm/kmp-tor-resource/pull/117
[120]: https://github.com/05nelsonm/kmp-tor-resource/pull/120
[122]: https://github.com/05nelsonm/kmp-tor-resource/pull/122

[url-build-env]: https://github.com/05nelsonm/build-env
[url-cklib]: https://github.com/touchlab/cklib
[url-resource-filterjar-gradle-plugin]: https://github.com/05nelsonm/kmp-tor-resource/tree/master/library/resource-filterjar-gradle-plugin
