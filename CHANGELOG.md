# CHANGELOG

## Version 408.18.0 (2025-10-07)
 - Updates `tor` to `tor-0.4.8.18` [[#183]][183]
 - Updates `openssl` to `3.4.3` [[#183]][183]

## Version 408.17.1 (2025-09-19)
 - Updates `kotlin` to `2.2.20` [[#171]][171]
 - Updates `kmp-tor-common` to `2.4.0` [[#171]][171]
 - Updates `build-env` to `0.4.1` [[#163]][163] [[#170]][170]
 - Updates `gradle-filterjar-plugin` to `0.1.1` [[#173]][173]
 - Adds support for `linux-libc` `riscv64` [[#165]][165]
 - Adds support for `linux-musl` `aarch64`, `x86`, `x86_64` [[#170]][170]
 - Adds support for `wasmJs` [[#174]][174]
 - Fixes `resource-filterjar-gradle-plugin` and `resource-frameworks-gradle-plugin` metadata 
   incompatibility issues related to Kotlin language version [[#173]][173] [[#177]][177]
 - Lower supported `KotlinVersion` to `1.9` [[#177]][177]
     - Source sets `js`, `wasmJs`, & `jsWasmJs` require a minimum `KotlinVersion` of `2.0`

## Version 408.17.0 (2025-08-20)
 - Updates `kmp-tor-common` to `2.3.1` [[#155]][155]
 - Updates `tor` to `tor-0.4.8.17` [[#155]][155]
 - Updates `openssl` to `3.4.2` [[#155]][155]
 - Refactors `kmp_tor-jni.c` to use `JNI_OnLoad` and `JNI_OnUnload`, instead
   of exposing individual JNI function symbols [[#155]][155]
 - Deprecates `ResourceLoaderTorNoExec.getOrCreate` for Jvm [[#157]][157]
     - The `resource-noexec-tor{-gpl}` dependencies for Android were **NOT** deprecated, only 
       the Jvm dependency.
     - Jvm consumers should switch to using the `resource-exec-tor{-gpl}` dependency instead.
 - Use UTF-8 encoded ByteArray when passing tor arguments to JNI layer [[#160]][160]

## Version 408.16.4 (2025-06-11)
 - Updates `kotlin` to `2.1.21` [[#130]][130]
 - Updates `android-gradle-plugin` to `8.9.3` [[#130]][130]
 - Updates `kmp-tor-common` to `2.3.0` [[#131]][131] [[#154]][154]
 - Updates `tor` to `maint-0.4.8` commit `f84d461b5560d5675d2a4ce86a040c301b814b51` [[#153]][153]
     - This is `tor` version `0.4.8.16-dev` which includes a critical bug fix for tor's thread pool 
       shutdown sequence which is being incorporated in this release of `kmp-tor-resource` while 
       awaiting `tor` version `0.4.8.17` to come out sometime in July.
     - Consumers of the `resource-exec-tor{-gpl}` dependency are not affected as all resources are 
       relinquished back to the system on process exit.
     - See [[tor-#844]][tor-844]
 - Adds support for the following targets [[#131]][131] [[#135]][135] [[#137]][137]
     - `androidNativeArm32`
     - `androidNativeArm64`
     - `androidNativeX64`
     - `androidNativeX86`
 - Adds publications `resource-compilation-exec-tor`, `resource-compilation-exec-tor-gpl`, 
   `resource-compilation-lib-tor` and `resource-compilation-lib-tor-gpl` to distribute `libtor.so` 
   and `libtorexec.so` compilations for Android such that they can be consumed by Android Native 
   users' applications independently [[#131]][131].
     - Android consumers are unaffected by this modification as these publications are now transitive 
       dependencies that are automatically pulled in when using the `resource-exec-tor{-gpl}` or 
       `resource-noexec-tor{-gpl}` dependencies.
 - Adds `proguard` rules for Kotlin/Jvm `resource-noexec-tor{-gpl}` [[#149]][149]
 - Deprecates `resource-noexec-tor{-gpl}` shutdownhook registration for Jvm/Android [[#150]][150]
     - The default `ResourceLoaderTorNoExec.getOrCreate` behavior has been changed to **not** 
       automatically add a shutdownhook.
 - Fixes a file descriptor double closure when using `__OwningControllerFD` which was triggering 
   `SIGABRT` crashes on Android API 30+ related to `fdsan` [[#151]][151]
     - Tor no longer "takes ownership" over, or closes the descriptor passed to it by the option 
       `__OwningControllerFD`. The responsibility lies with whomever opened it to ensure proper 
       descriptor closure after tor stops, be it via `tor_api.h/tor_main_configuration_free` if 
       `tor_api.h/tor_main_configuration_setup_control_socket` was used to open it, or otherwise.
 - Refactors `kmp_tor-jni.c` [[#143]][143]
     - `CharArray` is now used instead of `String` for JNI function arguments to mitigate any native 
       memory allocation by `libjvm` or `libandroid` from use of `GetStringUTFChars`.
     - Removes unnecessary use of `DeleteLocalRef`.
 - `lib_load.c` now uses `RTLD_LAZY` instead of `RTLD_NOW` when loading libtor via `dlopen` [[#143]][143]
 - Refactors `kmp_tor.c` [[#143]][143]
     - `OPENSSL_cleanup` is now properly called from the thread which `tor_api.h/tor_run_main` is called 
       from, after `tor_api.h/tor_run_main` returns.
     - `tor_api.h/tor_main_configuration_free` is now called from the thread which `tor_api.h/tor_run_main` 
       is called from, after `tor_api.h/tor_run_main` returns.
     - Simplifies the `kmp_tor_handle_t` structure and interactions between `kmp_tor_run_main`, the thread 
       for which tor runs in, and `kmp_tor_terminate_and_await_result`.
     - Control Socket descriptors for `__OwningControllerFD` for Unix-like systems are now configured with 
       the appropriate `CLOEXEC` flag, be it via `SOCK_CLOEXEC` if available, or `fcntl` with `FD_CLOEXEC`.
 - Adds a patch to `tor` to ensure `OPENSSL_stop_thread` is called upon thread shutdown for all threads that 
   tor starts [[#144]][144]
     - This is necessary because `openssl` is compiled statically, inhibiting automatic thread local cleanup 
       of any resources that may have been allocated.
 - Updates the following conifiguration flags for `openssl` compilation [[#143]][143] [[#152]][152]
     - Adds `no-autoload-config`
     - Adds `no-docs`
     - Adds `no-dynamic-engine`
     - Adds `no-ec2m`
     - Adds `no-engine`
     - Adds `no-hw`
     - Adds `no-mdc2`
     - Adds `no-module`
     - Adds `no-pinshared`
     - Adds `no-static-engine`
     - Adds `no-zlib`
     - Adds `no-zlib-dynamic`
     - Adds `no-zstd`
     - Adds `no-zstd-dynamic`
     - Removes `no-err`
     - Removes `no-md2` (a default setting)
     - Removes `no-rc5` (a default setting)
     - Removes `--with-zlib-lib`
     - Removes `--with-zlib-include`

## Version 408.16.3 (2025-05-20)
 - Fix `macOS`, `iOS` and `iOS-simulator` compilations by removing linker flag `-no_uuid`. [[#129]][129]
     - `macOS` version `15.4.1+` now requires binaries running on the machine to contain a `LC_UUID` 
       block; this was not the case on prior versions.

## Version 408.16.2 (2025-05-14)
 - Fix [resource-filterjar-gradle-plugin][url-resource-filterjar-gradle-plugin] filtering out `tor.exe.local` 
   from `resource-exec-tor` dependency when on Windows [[#124]][124]

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
 - Updates `kmp-tor-common` to `2.2.0` [[#108]][108]
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
[124]: https://github.com/05nelsonm/kmp-tor-resource/pull/124
[129]: https://github.com/05nelsonm/kmp-tor-resource/pull/129
[130]: https://github.com/05nelsonm/kmp-tor-resource/pull/130
[131]: https://github.com/05nelsonm/kmp-tor-resource/pull/131
[135]: https://github.com/05nelsonm/kmp-tor-resource/pull/135
[137]: https://github.com/05nelsonm/kmp-tor-resource/pull/137
[143]: https://github.com/05nelsonm/kmp-tor-resource/pull/143
[144]: https://github.com/05nelsonm/kmp-tor-resource/pull/144
[149]: https://github.com/05nelsonm/kmp-tor-resource/pull/149
[150]: https://github.com/05nelsonm/kmp-tor-resource/pull/150
[151]: https://github.com/05nelsonm/kmp-tor-resource/pull/151
[152]: https://github.com/05nelsonm/kmp-tor-resource/pull/152
[153]: https://github.com/05nelsonm/kmp-tor-resource/pull/153
[154]: https://github.com/05nelsonm/kmp-tor-resource/pull/154
[155]: https://github.com/05nelsonm/kmp-tor-resource/pull/155
[157]: https://github.com/05nelsonm/kmp-tor-resource/pull/157
[160]: https://github.com/05nelsonm/kmp-tor-resource/pull/160
[163]: https://github.com/05nelsonm/kmp-tor-resource/pull/163
[165]: https://github.com/05nelsonm/kmp-tor-resource/pull/165
[170]: https://github.com/05nelsonm/kmp-tor-resource/pull/170
[171]: https://github.com/05nelsonm/kmp-tor-resource/pull/171
[173]: https://github.com/05nelsonm/kmp-tor-resource/pull/173
[174]: https://github.com/05nelsonm/kmp-tor-resource/pull/174
[177]: https://github.com/05nelsonm/kmp-tor-resource/pull/177
[183]: https://github.com/05nelsonm/kmp-tor-resource/pull/183

[tor-844]: https://gitlab.torproject.org/tpo/core/tor/-/merge_requests/844
[url-build-env]: https://github.com/05nelsonm/build-env
[url-cklib]: https://github.com/touchlab/cklib
[url-resource-filterjar-gradle-plugin]: https://github.com/05nelsonm/kmp-tor-resource/tree/master/library/resource-filterjar-gradle-plugin
