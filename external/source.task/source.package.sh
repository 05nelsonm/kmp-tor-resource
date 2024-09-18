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

function __package:geoip {
  __util:require:var_set "$1" "geoip file name"

  local permissions="664"
  local gzip="yes"

  __package:file "tor/src/config" "jvmMain/resources/io/matthewnelson/kmp/tor/resource/geoip" "$1"

  local native_resource="io.matthewnelson.kmp.tor.resource.geoip.internal"
  __package:file "tor/src/config" "nativeMain" "$1"
}

function __package:android {
  __util:require:var_set "$dirname_out" "dirname_out"
  __util:require:var_set "$1" "[1] ndk abi"
  __util:require:var_set "$2" "[2] Lib name"

  local permissions="755"
  unset gzip

  __package:file "build/out/$dirname_out/android/$1" "androidMain/jniLibs/$1" "$2"
}

function __package:jvm {
  __util:require:var_set "$dirname_out" "dirname_out"
  __util:require:var_set "$rpath_native" "rpath_native (e.g. jvmMain/resources/io/matthewnelson/kmp/tor/{rpath_native}/native/{target}/{arch})"
  __util:require:var_set "$target" "target (e.g. linux-android)"
  __util:require:var_set "$1" "[1] arch"
  __util:require:var_set "$2" "[2] File name"

  if [ -z "$permissions" ]; then
    local permissions="775"
  fi
  if [ -z "$gzip" ]; then
    local gzip="yes"
  fi

  __package:file "build/out/$dirname_out/$target/$1" "jvmMain/resources/io/matthewnelson/kmp/tor/$rpath_native/native/$target/$1" "$2"
}

function __package:jvm:codesign {
  __util:require:var_set "$target" "target (e.g. linux-android)"
  __util:require:var_set "$1" "[1] arch"

  local detached_sig="$target/$1"
  __package:jvm "$@"
}

function __package:jvm:move:macos-lts {
  if [ "$target" != "macos-lts" ]; then
    __util:error "target must be set to macos-lts"
  fi

  __util:require:var_set "$dirname_final" "dirname_final"
  __util:require:var_set "$rpath_native" "rpath_native"

  local dir_native="$DIR_TASK/build/package/$dirname_final/src/jvmMain/resources/io/matthewnelson/kmp/tor/$rpath_native/native"
  if [ -d "$dir_native/$target" ]; then
    rm -rf "$dir_native/macos"
    mv -v "$dir_native/$target" "$dir_native/macos"
  fi
}

function __package:native {
  __util:require:var_set "$dirname_out" "dirname_out"
  __util:require:var_set "$target" "target (e.g. linux-libc)"
  __util:require:var_set "$native_resource" "native_resource (package name)"
  __util:require:var_set "$1" "[1] arch"
  __util:require:var_set "$2" "[2] File name"
  __util:require:var_set "$3" "[3] Source set name (e.g. linuxArm64Main)"

  if [ -z "$permissions" ]; then
    local permissions="775"
  fi
  if [ -z "$gzip" ]; then
    local gzip="yes"
  fi

  __package:file "build/out/$dirname_out/$target/$1" "$3" "$2"
}

function __package:native:codesign {
  __util:require:var_set "$target" "target (e.g. linux-libc)"
  __util:require:var_set "$1" "[1] arch"

  local detached_sig="$target/$1"
  __package:native "$@"
}

function __package:file {
  __util:require:var_set "$1" "Packaging target dir (relative to dir external/build/)"
  __util:require:var_set "$2" "Module src path (e.g. external/package/resource-lib-tor/src)"
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
    echo "FileNotFound. SKIPPING >> ARGS - Target[$1] - Path[$2] - Name[$3]"
    return 0
  fi

  cp -a "$DIR_TASK/$1/$3" "$DIR_STAGING"

  if [ -n "$detached_sig" ]; then
    ../tooling diff-cli apply \
      "$DIR_TASK/codesign/$dirname_out/$detached_sig/$3.signature" \
      "$DIR_STAGING/$3"
  fi

  local file_ext=""
  if [ "$gzip" = "yes" ]; then
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
