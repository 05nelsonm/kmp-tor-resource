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

readonly OSSLSIGNCODE="$(which osslsigncode)"
readonly RCODESIGN="$(which rcodesign)"

# Use local hsm_pin to capture input
function __sign:input:hsm_pin {
  echo -n "
    Enter HSM PIN: "
  read -s hsm_pin
  echo ""
}

function __sign:generate:macos {
  __util:require:cmd "$RCODESIGN" "rcodesign"
  __util:require:var_set "$hsm_pin" "HSM PIN"
  __util:require:var_set "$1" "arch"
  __util:require:var_set "$smartcard_slot" "smartcard-slot"
  __util:require:file_exists "$path_apikey" "App Store Connect API key"

  DIR_TMP="$(mktemp -d)"
  export CODESIGN_HSM_PIN="$hsm_pin"
  trap 'rm -rf "$DIR_TMP"; unset DIR_TMP CODESIGN_HSM_PIN' SIGINT ERR

  echo "Creating detached signatures for macos arch[$1]"
  # TODO

  rm -rf "$DIR_TMP"
  unset DIR_TMP
  unset CODESIGN_HSM_PIN
  trap - SIGINT ERR
}

function __sign:generate:mingw {
  __util:require:cmd "$OSSLSIGNCODE" "osslsigncode"
  __util:require:var_set "$hsm_pin" "HSM PIN"
  __util:require:var_set "$1" "arch"
  __util:require:var_set "$dirname_out" "dirname_out"
  __util:require:var_set "$file_names" "file_names"

  __util:require:file_exists "$gen_pkcs11engine_path" "windows.pkcs11[gen_pkcs11engine_path]"
  __util:require:file_exists "$gen_pkcs11module_path" "windows.pkcs11[gen_pkcs11module_path]"
  __util:require:var_set "$gen_model" "windows.pkcs11[gen_model]"
  __util:require:var_set "$gen_manufacturer" "windows.pkcs11[gen_manufacturer]"
  __util:require:var_set "$gen_serial" "windows.pkcs11[gen_serial]"
  __util:require:var_set "$gen_ts" "windows.pkcs11[gen_ts]"
  __util:require:file_exists "$gen_cert_path" "windows.pkcs11[gen_cert_path]"
  __util:require:var_set "$gen_id" "windows.pkcs11[gen_id]"

  local pkcs11_url="pkcs11:model=$gen_model;manufacturer=$gen_manufacturer;serial=$gen_serial;id=$gen_id;type=private"

  DIR_TMP="$(mktemp -d)"
  trap 'rm -rf "$DIR_TMP"; unset DIR_TMP' SIGINT ERR

  local file_name=
  for file_name in $(echo "$file_names" | tr "," " "); do
    if [ ! -f "$DIR_TASK/build/out/$dirname_out/mingw/$1/$file_name" ]; then
      echo "$file_name not found for mingw/$1. Skipping..."
      continue
    fi

    echo "Creating detached signature for build/out/$dirname_out/mingw/$1/$file_name"
    if $DRY_RUN; then continue; fi

    ${OSSLSIGNCODE} sign \
      -pkcs11engine "$gen_pkcs11engine_path" \
      -pkcs11module "$gen_pkcs11module_path" \
      -key "$pkcs11_url" \
      -certs "$gen_cert_path" \
      -ts "$gen_ts" \
      -h "sha256" \
      -pass "$hsm_pin" \
      -in "$DIR_TASK/build/out/$dirname_out/mingw/$1/$file_name" \
      -out "$DIR_TMP/$file_name"

    mkdir -p "$DIR_TASK/codesign/$dirname_out/mingw/$1"
    rm -rf "$DIR_TASK/codesign/$dirname_out/mingw/$1/$file_name.signature"

    ../tooling diff-cli create \
      --diff-ext-name ".signature" \
      "$DIR_TASK/build/out/$dirname_out/mingw/$1/$file_name" \
      "$DIR_TMP/$file_name" \
      "$DIR_TASK/codesign/$dirname_out/mingw/$1"

    echo ""
  done

  rm -rf "$DIR_TMP"
  unset DIR_TMP
  trap - SIGINT ERR
}

# Ensure source.util.sh has been sourced, and that
# DIR_TASK is set (i.e. this was sourced from task.sh)
__util:require:file_exists "$DIR_TASK/task.sh"

# TODO: Replace with sign:macos
#function sign:apple:macos { ## 2 ARGS - [1]: smartcard-slot (e.g. 9c)  [2]: /path/to/app/store/connect/api_key.json
#  if [ $# -ne 2 ]; then
#    __util:error "Usage: $0 $FUNCNAME <smartcard-slot (e.g. 9c)> /path/to/app/store/connect/api_key.json"
#  fi
#
#  local os_name="macos"
#  local file_name="tor"
#
#  local dirname_out="tor"
#  __sign:generate:apple "aarch64" "$1" "$2"
#  __sign:generate:apple "x86_64" "$1" "$2"
#
#  local os_subtype="-lts"
#  __sign:generate:apple "aarch64" "$1" "$2"
#  __sign:generate:apple "x86_64" "$1" "$2"
#  unset os_subtype
#
#  dirname_out="tor-gpl"
#  __sign:generate:apple "aarch64" "$1" "$2"
#  __sign:generate:apple "x86_64" "$1" "$2"
#
#  local os_subtype="-lts"
#  __sign:generate:apple "aarch64" "$1" "$2"
#  __sign:generate:apple "x86_64" "$1" "$2"
#  unset os_subtype
#}

#function __sign:generate:apple {
#  __util:require:var_set "$dirname_out" "dirname_out"
#  __util:require:var_set "$file_name" "file_name"
#  __util:require:var_set "$os_name" "os_name"
#  __util:require:cmd "$RCODESIGN" "rcodesign"
#  __util:require:var_set "$1" "arch"
#  __util:require:var_set "$2" "smartcard-slot"
#  # $3 App Store Connect api-key (macos only)
#
#  if [ ! -f "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" ]; then
#    echo "
#    $file_name not found for $dirname_out/$os_name$os_subtype:$1. Skipping...
#    "
#    return 0
#  fi
#
#  # Relative location within template bundle
#  local dir_libs=
#  local executable=
#
#  case "$os_name" in
#    "macos")
#      __util:require:file_exists "$3" "App Store Connect API key"
#      executable="Contents/MacOS/KmpTorResource"
#      dir_libs="Contents/MacOS/NativeLibs"
#      ;;
#    "ios")
#      executable="KmpTorResource"
#      dir_libs="Executables"
#      ;;
#    *)
#      __util:error "Unknown os_name[$os_name]"
#      ;;
#  esac
#
#  echo "
#    Creating detached signature for build/out/$dirname_out/$os_name$os_subtype/$1/$file_name
#  "
#
#  DIR_TMP="$(mktemp -d)"
#  trap 'rm -rf "$DIR_TMP"' SIGINT ERR
#
#  cp -R "$DIR_TASK/codesign/template/$os_name/KmpTorResource.app" "$DIR_TMP"
#  rm -rf "$DIR_TMP/KmpTorResource.app/$dir_libs/.gitkeep"
#  cp "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" "$DIR_TMP/KmpTorResource.app/$executable"
#  cp -a "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" "$DIR_TMP/KmpTorResource.app/$dir_libs/$file_name"
#
#  ${RCODESIGN} sign \
#    --code-signature-flags runtime \
#    --code-signature-flags "$dir_libs/$file_name:runtime" \
#    --smartcard-slot "$2" \
#    "$DIR_TMP/KmpTorResource.app"
#
#  if [ "$os_name" = "macos" ]; then
#    # Developer ID certificate (macOS only)
#    echo ""
#    sleep 1
#
#    # maximum wait time is set to 45m
#    ${RCODESIGN} notary-submit \
#      --api-key-path "$3" \
#      --max-wait-seconds "2700" \
#      --staple \
#      "$DIR_TMP/KmpTorResource.app"
#  fi
#
#  mkdir -p "$DIR_TASK/codesign/$dirname_out/$os_name$os_subtype/$1"
#  rm -rf "$DIR_TASK/codesign/$dirname_out/$os_name$os_subtype/$1/$file_name.signature"
#
#  echo ""
#
#  ../tooling diff-cli create \
#    --diff-ext-name ".signature" \
#    "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" \
#    "$DIR_TMP/KmpTorResource.app/$dir_libs/$file_name" \
#    "$DIR_TASK/codesign/$dirname_out/$os_name$os_subtype/$1"
#
#  echo ""
#
#  rm -rf "$DIR_TMP"
#  unset DIR_TMP
#  trap - SIGINT ERR
#}
