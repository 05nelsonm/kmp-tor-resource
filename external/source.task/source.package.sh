#!/usr/bin/env bash
# Copyright (c) 2024 Matthew Nelson
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

# Sourced by external/task.sh

# Dependency
. "$DIR_TASK/source.task/source.docker.sh"

function __package:libs:shared {
  __util:require:var_set "$libname" "libname"
  __util:require:var_set "$dirname_final" "dirname_final"
  __util:require:var_set "$dirname_out" "dirname_out"

  __package:android "arm64-v8a"
  __package:android "armeabi-v7a"
  __package:android "x86"
  __package:android "x86_64"

  __package:jvm "linux-android/aarch64" "$libname"
  __package:jvm "linux-android/armv7" "$libname"
  __package:jvm "linux-android/x86" "$libname"
  __package:jvm "linux-android/x86_64" "$libname"
  __package:jvm "linux-libc/aarch64" "$libname"
  __package:jvm "linux-libc/armv7" "$libname"
  __package:jvm "linux-libc/ppc64" "$libname"
  __package:jvm "linux-libc/x86" "$libname"
  __package:jvm "linux-libc/x86_64" "$libname"

  __package:jvm:codesigned "macos-lts/aarch64" "$libname"
  __package:jvm:codesigned "macos-lts/x86_64" "$libname"

  # Jvm/Js utilize the LTS builds. Need to move them into
  # their final resting place 'macos-lts' -> 'macos'
  local dir_native="build/package/$dirname_final/src/jvmMain/resources/io/matthewnelson/kmp/tor/resource/shared/tor/native"
  if [ -d "$dir_native/macos-lts" ]; then
    rm -rf "$dir_native/macos"
    mv -v "$dir_native/macos-lts" "$dir_native/macos"
  fi
  unset dir_native

  __package:jvm:codesigned "mingw/x86" "$libname.exe"
  __package:jvm:codesigned "mingw/x86_64" "$libname.exe"
}

function __package:libs:native:exec {
  __util:require:var_set "$libname" "libname"
  __util:require:var_set "$dirname_final" "dirname_final"
  __util:require:var_set "$dirname_out" "dirname_out"

  __package:native:exec "linux-libc/aarch64" "$libname" "linuxArm64Main"
  __package:native:exec "linux-libc/x86_64" "$libname" "linuxX64Main"
  __package:native:exec:codesigned "macos/aarch64" "$libname" "macosArm64Main"
  __package:native:exec:codesigned "macos/x86_64" "$libname" "macosX64Main"
  __package:native:exec:codesigned "mingw/x86_64" "$libname.exe" "mingwX64Main"
}

function __package:geoip {
  local permissions="664"
  local gzip="yes"

  __package "tor/src/config" "jvmMain/resources/io/matthewnelson/kmp/tor/resource/shared/geoip" "$1"

  local native_resource="io.matthewnelson.kmp.tor.resource.shared.geoip.internal"
  __package "tor/src/config" "nativeMain" "$1"
}

function __package:android {
  local permissions="755"
  # no gzip
  __package "build/out/$dirname_out/android/$1" "androidMain/jniLibs/$1" "lib$libname.so"
}

function __package:jvm {
  local permissions="755"
  local gzip="yes"
  __package "build/out/$dirname_out/$1" "jvmMain/resources/io/matthewnelson/kmp/tor/resource/shared/tor/native/$1" "$2"
}

function __package:jvm:codesigned {
  local detached_sig="$1"
  __package:jvm "$@"
}

function __package:native:exec {
  local permissions="755"
  local gzip="yes"
  local native_resource="io.matthewnelson.kmp.tor.resource.exec.tor.internal"
  __package "build/out/$dirname_out/$1" "$3" "$2"
}

function __package:native:exec:codesigned {
  local detached_sig="$1"
  __package:native:exec "$@"
}

function __package {
  __util:require:var_set "$1" "Packaging target dir (relative to dir external/build/)"
  __util:require:var_set "$2" "Binary module src path (e.g. external/package/resource-shared-tor/src)"
  __util:require:var_set "$3" "File name"
  __util:require:var_set "$dirname_final" "dirname_final"

  __util:require:var_set "$permissions" "permissions"
  __util:require:var_set "$DIR_STAGING" "DIR_STAGING"

  if $DRY_RUN; then
    echo "
    Packaging Target:     $1/$3
    Detached Signature:   $detached_sig
    gzip:                 $gzip
    permissions:          $permissions
    NativeResource:       $native_resource
    Module Src Dir:       build/package/$dirname_final/src/$2
    "
    return 0
  fi

  if [ ! -f "$DIR_TASK/$1/$3" ]; then
    echo "FileNotFound. SKIPPING >> ARGS - 1[$1] - 2[$2] - 3[$3]"
    return 0
  fi

  cp -a "$DIR_TASK/$1/$3" "$DIR_STAGING"

  if [ -n "$detached_sig" ]; then
    ../tooling diff-cli apply \
      "$DIR_TASK/codesign/$dirname_out/$detached_sig/$3.signature" \
      "$DIR_STAGING/$3"
  fi

  local file_ext=""
  if [ -n "$gzip" ]; then
    # Must utilize docker gzip for reproducible results
    __docker:run "--silent" \
      "$DIR_STAGING" \
      "05nelsonm/build-env.linux-libc.base" \
      gzip --no-name --best --verbose "/work/$3"

    file_ext=".gz"
  fi

  # Need to apply permissions after tooling
  # because it strips that as the file is atomically
  # moved instead of being modified in place (see Issue #77).
  chmod "$permissions" "$DIR_STAGING/$3$file_ext"

  local dir_module="$DIR_TASK/build/package/$dirname_final/src/$2"

  if [ -z "$native_resource" ]; then
    mkdir -p "$dir_module"
    mv -v "$DIR_STAGING/$3$file_ext" "$dir_module"
  else
    ../tooling resource-cli \
      "$native_resource" \
      "$dir_module" \
      "$DIR_STAGING/$3$file_ext"

    rm -rf "$DIR_STAGING/$3$file_ext"
  fi
}
