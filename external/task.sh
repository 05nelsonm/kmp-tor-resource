#!/usr/bin/env bash
# Copyright (c) 2023 Matthew Nelson
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
export LC_ALL=C
set -e

readonly CMD_TASK="$1"
readonly DIR_TASK="$( cd "$( dirname "$0" )" >/dev/null && pwd )"
if [ -n "$CMD_TASK" ]; then shift; fi

function build:all { ## Builds all targets
  build:all:desktop
  build:all:mobile
}

function build:all:android { ## Builds all Android targets
  build:android:aarch64
  build:android:armv7
  build:android:x86
  build:android:x86_64
}

function build:all:desktop { ## Builds all Linux, macOS, Windows targets
#  build:all:freebsd
  build:all:linux-libc
#  build:all:linux-musl
  build:all:macos
  build:all:mingw
}

#function build:all:freebsd { ## Builds all FreeBSD targets
#  build:freebsd:aarch64
#  build:freebsd:x86
#  build:freebsd:x86_64
#}

function build:all:ios { ## Builds all iOS targets
  build:ios-simulator:aarch64
  build:ios-simulator:x86_64
  build:ios:aarch64
}

function build:all:linux-libc { ## Builds all Linux Libc targets
  build:linux-libc:aarch64
  build:linux-libc:armv7
  build:linux-libc:ppc64
  build:linux-libc:x86
  build:linux-libc:x86_64
}

#function build:all:linux-musl { ## Builds all Linux Musl targets
#  build:linux-musl:aarch64
#  build:linux-musl:x86
#  build:linux-musl:x86_64
#}

function build:all:macos { ## Builds all macOS and macOS LTS targets
  build:macos-lts:aarch64
  build:macos-lts:x86_64
  build:macos:aarch64
  build:macos:x86_64
}

function build:all:mobile { ## Builds all Android and iOS targets
  build:all:android
  build:all:ios
}

function build:all:mingw { ## Builds all Windows targets
  build:mingw:x86
  build:mingw:x86_64
}

function build:android:aarch64 { ## Builds Android arm64-v8a
  local os_name="android"
  local os_arch="aarch64"
  local openssl_target="android-arm64"
  local ndk_abi="arm64-v8a"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:android:armv7 { ## Builds Android armeabi-v7a
  local os_name="android"
  local os_arch="armv7"
  local openssl_target="android-arm"
  local ndk_abi="armeabi-v7a"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:android:x86 { ## Builds Android x86
  local os_name="android"
  local os_arch="x86"
  local openssl_target="android-x86"
  local ndk_abi="x86"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:android:x86_64 { ## Builds Android x86_64
  local os_name="android"
  local os_arch="x86_64"
  local openssl_target="android-x86_64"
  local ndk_abi="x86_64"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:ios-simulator:aarch64 { ## Builds iOS Simulator arm64
  local os_name="ios"
  local os_subtype="-simulator"
  local os_arch="aarch64"
  local openssl_target="iossimulator-xcrun"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:ios-simulator:x86_64 { ## Builds iOS Simulator x86_64
  local os_name="ios"
  local os_subtype="-simulator"
  local os_arch="x86_64"
  local openssl_target="iossimulator-xcrun"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:ios:aarch64 { ## Builds iOS arm64
  local os_name="ios"
  local os_arch="aarch64"
  local openssl_target="ios64-xcrun"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

#function build:freebsd:aarch64 { ## Builds FreeBSD aarch64
#  local os_name="freebsd"
#  local os_arch="aarch64"
#  local openssl_target="BSD-aarch64"
#  __build:configure:target:init
#  # TODO __build:docker:execute
#}

#function build:freebsd:x86 { ## Builds FreeBSD x86
#  local os_name="freebsd"
#  local os_arch="x86"
#  local openssl_target="BSD-x86"
#  __build:configure:target:init
#  # TODO __build:docker:execute
#}

#function build:freebsd:x86_64 { ## Builds FreeBSD x86_64
#  local os_name="freebsd"
#  local os_arch="x86_64"
#  local openssl_target="BSD-x86_64"
#  __build:configure:target:init
#  # TODO __build:docker:execute
#}

function build:linux-libc:aarch64 { ## Builds Linux Libc aarch64
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="aarch64"
  local openssl_target="linux-aarch64"
  __build:configure:target:init
  __build:CFLAGS '-march=armv8-a'
  __build:docker:execute
}

function build:linux-libc:armv7 { ## Builds Linux Libc armv7
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="armv7"
  local openssl_target="linux-armv4"
  __build:configure:target:init
  __build:CFLAGS '-march=armv7-a'
  __build:CFLAGS '-mfloat-abi=hard'
  __build:CFLAGS '-mfpu=vfp'
  __build:docker:execute
}

function build:linux-libc:ppc64 { ## Builds Linux Libc powerpc64le
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="ppc64"
  local openssl_target="linux-ppc64le"
  __build:configure:target:init
  __build:docker:execute
}

function build:linux-libc:x86 { ## Builds Linux Libc x86
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="x86"
  local openssl_target="linux-x86"
  __build:configure:target:init
  __build:docker:execute
}

function build:linux-libc:x86_64 { ## Builds Linux Libc x86_64
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="x86_64"
  local openssl_target="linux-x86_64"
  __build:configure:target:init
  __build:docker:execute
}

#function build:linux-musl:aarch64 { ## Builds Linux Musl aarch64
#  local os_name="linux"
#  local os_subtype="-musl"
#  local os_arch="aarch64"
#  local openssl_target="linux-aarch64"
#  __build:configure:target:init
#  # TODO __build:docker:execute
#}

#function build:linux-musl:x86 { ## Builds Linux Musl x86
#  local os_name="linux"
#  local os_subtype="-musl"
#  local os_arch="x86"
#  local openssl_target="linux-x86"
#  __build:configure:target:init
#  __build:CFLAGS '-m32'
#  __build:LDFLAGS '-m32'
#  # TODO __build:docker:execute
#}

#function build:linux-musl:x86_64 { ## Builds Linux Musl x86_64
#  local os_name="linux"
#  local os_subtype="-musl"
#  local os_arch="x86_64"
#  local openssl_target="linux-x86_64"
#  __build:configure:target:init
#  # TODO __build:docker:execute
#}

function build:macos-lts:aarch64 { ## Builds macOS LTS (SDK 12.3 - Jvm/Js) aarch64
  local os_subtype="-lts"
  build:macos:aarch64
}

function build:macos-lts:x86_64 { ## Builds macOS LTS (SDK 12.3 - Jvm/Js) x86_64
  local os_subtype="-lts"
  build:macos:x86_64
}

function build:macos:aarch64 { ## Builds macOS     (SDK 14.0 - Native) aarch64
  local os_name="macos"
  local os_arch="aarch64"
  local openssl_target="darwin64-arm64-cc"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:macos:x86_64 { ## Builds macOS     (SDK 14.0 - Native) x86_64
  local os_name="macos"
  local os_arch="x86_64"
  local openssl_target="darwin64-x86_64-cc"
  local cc_clang="yes"
  __build:configure:target:init
  __build:docker:execute
}

function build:mingw:x86 { ## Builds Windows x86
  local os_name="mingw"
  local os_arch="x86"
  local openssl_target="mingw"
  __build:configure:target:init
  __build:LDFLAGS '-Wl,--no-seh'
  __build:docker:execute
}

function build:mingw:x86_64 { ## Builds Windows x86_64
  local os_name="mingw"
  local os_arch="x86_64"
  local openssl_target="mingw64"
  __build:configure:target:init
  __build:docker:execute
}

function clean { ## Deletes the build dir
  rm -rf "$DIR_TASK/build"
}

function help { ## THIS MENU
  echo "
    $0
    Copyright (C) 2023 Matthew Nelson

    Tasks for compiling, codesigning, and packaging tor

    Location: $DIR_TASK
    Syntax: $0 [task] [option] [args]

    Tasks:
$(
    # function names + comments & colorization
    grep -E '^function .* {.*?## .*$$' "$0" |
    grep -v "^function __" |
    sed -e 's/function //' |
    sort |
    awk 'BEGIN {FS = "{.*?## "}; {printf "        \033[93m%-30s\033[92m %s\033[0m\n", $1, $2}'
)

    Options:
        --dry-run                      Debugging output that does not execute.
        --rebuild                      Will cause a complete rebuild of the target.

    Example: $0 build:all --dry-run
  "
}

function package:all { ## Packages all build/out contents & geoip files
  package:geoip
  package:android
  package:ios
  package:linux-libc
  package:macos
  package:mingw
}

function package:geoip { ## Packages geoip & geoip6 files
  local dirname_final="resource-geoip"
  __package:geoip "geoip"
  __package:geoip "geoip6"
}

function package:android { ## Packages all Android build/out contents
  local dirname_out="tor"
  local dirname_final="resource-lib-tor"
  __package:android "arm64-v8a" "libtor.so"
  __package:android "armeabi-v7a" "libtor.so"
  __package:android "x86" "libtor.so"
  __package:android "x86_64" "libtor.so"

  dirname_final="resource-exec-tor"
  __package:android "arm64-v8a" "libtorexec.so"
  __package:android "armeabi-v7a" "libtorexec.so"
  __package:android "x86" "libtorexec.so"
  __package:android "x86_64" "libtorexec.so"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  __package:android "arm64-v8a" "libtor.so"
  __package:android "armeabi-v7a" "libtor.so"
  __package:android "x86" "libtor.so"
  __package:android "x86_64" "libtor.so"

  dirname_final="resource-exec-tor-gpl"
  __package:android "arm64-v8a" "libtorexec.so"
  __package:android "armeabi-v7a" "libtorexec.so"
  __package:android "x86" "libtorexec.so"
  __package:android "x86_64" "libtorexec.so"

  dirname_out="tor"
  dirname_final="resource-lib-tor"
  local rpath_native="resource/lib/tor"
  local target="linux-android"
  __package:jvm "aarch64" "libtor.so"
  __package:jvm "armv7" "libtor.so"
  __package:jvm "x86" "libtor.so"
  __package:jvm "x86_64" "libtor.so"

  dirname_final="resource-exec-tor"
  rpath_native="resource/exec/tor"
  __package:jvm "aarch64" "tor"
  __package:jvm "armv7" "tor"
  __package:jvm "x86" "tor"
  __package:jvm "x86_64" "tor"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  rpath_native="resource/lib/tor"
  __package:jvm "aarch64" "libtor.so"
  __package:jvm "armv7" "libtor.so"
  __package:jvm "x86" "libtor.so"
  __package:jvm "x86_64" "libtor.so"

  dirname_final="resource-exec-tor-gpl"
  rpath_native="resource/exec/tor"
  __package:jvm "aarch64" "tor"
  __package:jvm "armv7" "tor"
  __package:jvm "x86" "tor"
  __package:jvm "x86_64" "tor"
}

function package:ios { ## Packages all iOS & iOS Simulator build/out contents
  echo "TODO"
}

function package:linux-libc { ## Packages all Linux Libc build/out contents
  local dirname_out="tor"
  local dirname_final="resource-lib-tor"
  local rpath_native="resource/lib/tor"
  local target="linux-libc"
  __package:jvm "aarch64" "libtor.so"
  __package:jvm "armv7" "libtor.so"
  __package:jvm "ppc64" "libtor.so"
  __package:jvm "x86" "libtor.so"
  __package:jvm "x86_64" "libtor.so"

  dirname_final="resource-exec-tor"
  rpath_native="resource/exec/tor"
  __package:jvm "aarch64" "tor"
  __package:jvm "armv7" "tor"
  __package:jvm "ppc64" "tor"
  __package:jvm "x86" "tor"
  __package:jvm "x86_64" "tor"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  rpath_native="resource/lib/tor"
  __package:jvm "aarch64" "libtor.so"
  __package:jvm "armv7" "libtor.so"
  __package:jvm "ppc64" "libtor.so"
  __package:jvm "x86" "libtor.so"
  __package:jvm "x86_64" "libtor.so"

  dirname_final="resource-exec-tor-gpl"
  rpath_native="resource/exec/tor"
  __package:jvm "aarch64" "tor"
  __package:jvm "armv7" "tor"
  __package:jvm "ppc64" "tor"
  __package:jvm "x86" "tor"
  __package:jvm "x86_64" "tor"

  unset rpath_native

  dirname_out="tor"
  dirname_final="resource-lib-tor"
  local native_resource="io.matthewnelson.kmp.tor.resource.lib.tor.internal"
  __package:native "aarch64" "libtor.so" "linuxArm64Main"
  __package:native "x86_64" "libtor.so" "linuxX64Main"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  __package:native "aarch64" "libtor.so" "linuxArm64Main"
  __package:native "x86_64" "libtor.so" "linuxX64Main"

  dirname_out="tor"
  dirname_final="resource-exec-tor"
  native_resource="io.matthewnelson.kmp.tor.resource.exec.tor.internal"
  __package:native "aarch64" "tor" "linuxArm64Main"
  __package:native "x86_64" "tor" "linuxX64Main"

  dirname_out="tor-gpl"
  dirname_final="resource-exec-tor-gpl"
  __package:native "aarch64" "tor" "linuxArm64Main"
  __package:native "x86_64" "tor" "linuxX64Main"
}

function package:macos { ## Packages all macOS & macOS LTS build/out contents
  local dirname_out="tor"
  local dirname_final="resource-lib-tor"
  local rpath_native="resource/lib/tor"
  local target="macos-lts"
  __package:jvm:codesign "aarch64" "libtor.dylib"
  __package:jvm:codesign "x86_64" "libtor.dylib"
  __package:jvm:move:macos-lts

  dirname_final="resource-exec-tor"
  rpath_native="resource/exec/tor"
  __package:jvm:codesign "aarch64" "tor"
  __package:jvm:codesign "x86_64" "tor"
  __package:jvm:move:macos-lts

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  rpath_native="resource/lib/tor"
  __package:jvm:codesign "aarch64" "libtor.dylib"
  __package:jvm:codesign "x86_64" "libtor.dylib"
  __package:jvm:move:macos-lts

  dirname_final="resource-exec-tor-gpl"
  rpath_native="resource/exec/tor"
  __package:jvm:codesign "aarch64" "tor"
  __package:jvm:codesign "x86_64" "tor"
  __package:jvm:move:macos-lts

  unset rpath_native

  target="macos"
  dirname_out="tor"
  dirname_final="resource-lib-tor"
  local native_resource="io.matthewnelson.kmp.tor.resource.lib.tor.internal"
  __package:native:codesign "aarch64" "libtor.dylib" "macosArm64Main"
  __package:native:codesign "x86_64" "libtor.dylib" "macosX64Main"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  __package:native:codesign "aarch64" "libtor.dylib" "macosArm64Main"
  __package:native:codesign "x86_64" "libtor.dylib" "macosX64Main"

  dirname_out="tor"
  dirname_final="resource-exec-tor"
  native_resource="io.matthewnelson.kmp.tor.resource.exec.tor.internal"
  __package:native:codesign "aarch64" "tor" "macosArm64Main"
  __package:native:codesign "x86_64" "tor" "macosX64Main"

  dirname_out="tor-gpl"
  dirname_final="resource-exec-tor-gpl"
  __package:native:codesign "aarch64" "tor" "macosArm64Main"
  __package:native:codesign "x86_64" "tor" "macosX64Main"
}

function package:mingw { ## Packages all Windows build/out contents
  local dirname_out="tor"
  local dirname_final="resource-lib-tor"
  local rpath_native="resource/lib/tor"
  local target="mingw"
  __package:jvm:codesign "x86" "tor.dll"
  __package:jvm:codesign "x86_64" "tor.dll"

  dirname_final="resource-exec-tor"
  rpath_native="resource/exec/tor"
  __package:jvm:codesign "x86" "tor.exe"
  __package:jvm:codesign "x86_64" "tor.exe"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  rpath_native="resource/lib/tor"
  __package:jvm:codesign "x86" "tor.dll"
  __package:jvm:codesign "x86_64" "tor.dll"

  dirname_final="resource-exec-tor-gpl"
  rpath_native="resource/exec/tor"
  __package:jvm:codesign "x86" "tor.exe"
  __package:jvm:codesign "x86_64" "tor.exe"

  unset rpath_native

  dirname_out="tor"
  dirname_final="resource-lib-tor"
  local native_resource="io.matthewnelson.kmp.tor.resource.lib.tor.internal"
  __package:native:codesign "x86_64" "tor.dll" "mingwX64Main"

  dirname_out="tor-gpl"
  dirname_final="resource-lib-tor-gpl"
  __package:native:codesign "x86_64" "tor.dll" "mingwX64Main"

  dirname_out="tor"
  dirname_final="resource-exec-tor"
  native_resource="io.matthewnelson.kmp.tor.resource.exec.tor.internal"
  __package:native:codesign "x86_64" "tor.exe" "mingwX64Main"

  dirname_out="tor-gpl"
  dirname_final="resource-exec-tor-gpl"
  __package:native:codesign "x86_64" "tor.exe" "mingwX64Main"

  unset native_resource
  unset dirname_out

  echo "# https://learn.microsoft.com/en-us/windows/win32/dlls/dynamic-link-library-redirection#how-to-redirect-dlls-for-unpackaged-apps" > "$DIR_TASK/build/tor.exe.local"

  dirname_final="resource-exec-tor"
  local permissions="664"
  local gzip="no"
  __package:file "build" "jvmMain/resources/io/matthewnelson/kmp/tor/resource/exec/tor/native/mingw" "tor.exe.local"

  dirname_final="resource-exec-tor-gpl"
  __package:file "build" "jvmMain/resources/io/matthewnelson/kmp/tor/resource/exec/tor/native/mingw" "tor.exe.local"

  dirname_final="resource-exec-tor"
  local native_resource="io.matthewnelson.kmp.tor.resource.exec.tor.internal"
  __package:file "build" "mingwX64Main" "tor.exe.local"

  dirname_final="resource-exec-tor-gpl"
  __package:file "build" "mingwX64Main" "tor.exe.local"

  rm -rf "$DIR_TASK/build/tor.exe.local"
}

function sign:macos { ## 2 ARGS - [1]: smartcard-slot (e.g. 9c)  [2]: /path/to/app/store/connect/api_key.json
  if [ $# -ne 2 ]; then
    __util:error "Usage: $0 $CMD_TASK <smartcard-slot (e.g. 9c)> /path/to/app/store/connect/api_key.json"
  fi

  local hsm_pin=
  __sign:input:hsm_pin
  local smartcard_slot="$1"
  local path_apikey="$2"

  __sign:generate:detached:macos "aarch64"
  __sign:generate:detached:macos "x86_64"
}

function sign:mingw { ## Codesign mingw binaries (see codesign/windows.pkcs11.sample)
  __util:require:file_exists "codesign/windows.pkcs11" "codesign/windows.pkcs11"

  . "$DIR_TASK/codesign/windows.pkcs11"

  local hsm_pin=
  __sign:input:hsm_pin

  local file_names="tor.dll,tor.exe"
  local dirname_out="tor"

  __sign:generate:detached:mingw "x86"
  __sign:generate:detached:mingw "x86_64"

  dirname_out="tor-gpl"
  __sign:generate:detached:mingw "x86"
  __sign:generate:detached:mingw "x86_64"
}

function validate { ## Checks the build/package directory output against expected sha256 hashes
  local targets="JVM"
  targets="$targets,LINUX_ARM64,LINUX_X64"
  targets="$targets,MACOS_ARM64,MACOS_X64"
  targets="$targets,MINGW_X64"
  targets="$targets,IOS_ARM64,IOS_SIMULATOR_ARM64,IOS_X64"
  targets="$targets,TVOS_ARM64,TVOS_SIMULATOR_ARM64,TVOS_X64"
  targets="$targets,WATCHOS_ARM32,WATCHOS_ARM64,WATCHOS_DEVICE_ARM64,WATCHOS_SIMULATOR_ARM64,WATCHOS_X64"

  if [ -n "$include_android" ]; then
    targets="ANDROID,$targets"
  fi

  cd "$DIR_TASK/.."
  ./gradlew clean -PKMP_TARGETS="$targets"
  ./gradlew prepareKotlinBuildScriptModel -PKMP_TARGETS="$targets"

  __validate:report "resource-geoip"
  __validate:report "resource-lib-tor"
  __validate:report "resource-lib-tor-gpl"
  __validate:report "resource-exec-tor"
  __validate:report "resource-exec-tor-gpl"
}

# Does not show up in help output. The `validate` task does not
# include android, as the linux-android JVM binaries are the
# same.
#
# Including ANDROID in KMP_TARGETS requires Java 17+
function validate:all {
  local include_android="yes"
  validate
}

# Run
if [ -z "$CMD_TASK" ] || [ "$CMD_TASK" = "help" ]; then
  help
elif ! grep -qE "^function $CMD_TASK {" "$0"; then
  help
  . "$DIR_TASK/source.task/source.util.sh"
  __util:error "Unknown task '$CMD_TASK'"
else
  . "$DIR_TASK/source.task/source.util.sh"
  . "$DIR_TASK/source.task/source.options.sh" "$@"
  __util:require:no_build_lock

  # Ensure always starting in the external directory
  cd "$DIR_TASK"
  mkdir -p "build"

  _is_sign=false
  if echo "$CMD_TASK" | grep -q "^build"; then
    . "$DIR_TASK/source.task/source.build.sh"
    __util:require:cmd "$GIT" "git"

    ${GIT} submodule update --init

    __util:require:no_build_lock
    trap '__build:cleanup' EXIT
    echo "$CMD_TASK" > "$FILE_BUILD_LOCK"

    __util:git:clean "libevent"
    __util:git:apply_patches "libevent"
    __util:git:clean "openssl"
    __util:git:apply_patches "openssl"
    __util:git:clean "tor"
    __util:git:apply_patches "tor"
    __util:git:clean "xz"
    __util:git:apply_patches "xz"
    __util:git:clean "zlib"
    __util:git:apply_patches "zlib"
  elif echo "$CMD_TASK" | grep -q "^package"; then
    . "$DIR_TASK/source.task/source.package.sh"
    __util:require:cmd "$GIT" "git"

    ${GIT} submodule update --init "$DIR_TASK/tor"
    __util:git:clean "tor"

    __util:require:no_build_lock
    trap 'rm -rf "$FILE_BUILD_LOCK"; rm -rf "$DIR_STAGING"' EXIT
    echo "$CMD_TASK" > "$FILE_BUILD_LOCK"
    DIR_STAGING="$(mktemp -d)"
  elif echo "$CMD_TASK" | grep -q "^sign"; then
    . "$DIR_TASK/source.task/source.sign.sh"
    _is_sign=true
    trap 'rm -rf "$FILE_BUILD_LOCK"' EXIT
    echo "$CMD_TASK" > "$FILE_BUILD_LOCK"
  elif echo "$CMD_TASK" | grep -q "^validate"; then
    . "$DIR_TASK/source.task/source.validate.sh"
  fi

  TIMEFORMAT="
    Task '$CMD_TASK' completed in %3lR
  "
  if $_is_sign; then
    time "$CMD_TASK" "$@"
  else
    time "$CMD_TASK"
  fi
fi
