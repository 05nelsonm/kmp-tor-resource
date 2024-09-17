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

function __sign:generate:detached:macos {
  __util:require:cmd "$RCODESIGN" "rcodesign"
  __util:require:var_set "$hsm_pin" "HSM PIN"
  __util:require:var_set "$1" "arch"
  __util:require:var_set "$smartcard_slot" "smartcard_slot"
  __util:require:file_exists "$path_apikey" "App Store Connect API key"

  # CSV
  local arches_remove=
  case "$1" in
    "aarch64")
      arches_remove="x86_64"
      ;;
    "x86_64")
      arches_remove="aarch64"
      ;;
    *)
      __util:error "Unknown architecture $1"
      ;;
  esac
  __util:require:var_set "$arches_remove" "arches_remove"

  DIR_TMP="$(mktemp -d)"
  trap 'rm -rf "$DIR_TMP"; unset DIR_TMP' SIGINT ERR

  echo "
    Creating detached signatures for macos arch[$1] libs
"

  local dir_bundle="$DIR_TMP/KmpTor.app"
  local dir_bundle_macos="$dir_bundle/Contents/MacOS"

  local targets="tor,tor-gpl"
  local locations="macos,macos-lts"
  local file_names="libtor.dylib,tor"
  local diff_ext=".signature"

  local cmd_sign="$RCODESIGN sign"
  cmd_sign="$cmd_sign --smartcard-slot $smartcard_slot"
  cmd_sign="$cmd_sign --smartcard-pin $hsm_pin"
  cmd_sign="$cmd_sign --code-signature-flags runtime"


  local target=
  local location=
  local file_name=
  local program_file=
  for target in $(echo "$targets" | tr "," " "); do
    mkdir -p "$dir_bundle_macos/$target"

    for location in $(echo "$locations" | tr "," " "); do
      if [ ! -d "$DIR_TASK/build/out/$target/$location/$1" ]; then
        echo "$1 not found for $target/$location Skipping..."
        continue
      fi

      cp -aR "$DIR_TASK/build/out/$target/$location" "$dir_bundle_macos/$target"

      local remove_arch=
      for remove_arch in $(echo "$arches_remove" | tr "," " "); do
        if [ -z "$remove_arch" ]; then
          continue
        fi

        rm -rf "$dir_bundle_macos/$target/$location/$remove_arch"
      done
      unset remove_arch

      for file_name in $(echo "$file_names" | tr "," " "); do
        cmd_sign="$cmd_sign --code-signature-flags Contents/MacOS/$target/$location/$1/$file_name:runtime"
      done
      unset file_name

      program_file="$dir_bundle_macos/$target/$location/$1/tor"

      rm -rf "$dir_bundle_macos/$target/$location/$1/include"
    done
    unset location

  done
  unset target

  if [ ! -f "$program_file" ]; then
    echo "No files to sign. Skipping..."
    return 0
  fi

  cp -a "$program_file" "$dir_bundle_macos/tor.program"

  echo '<?xml version="1.0" encoding="UTF-8"?>
<plist version="1.0">
<dict>
    <key>CFBundleExecutable</key>
    <string>tor.program</string>
    <key>CFBundleIdentifier</key>
    <string>io.matthewnelson.kmp-tor</string>
    <key>LSUIElement</key>
    <true/>
</dict>
</plist>' > "$dir_bundle/Contents/Info.plist"

  sleep 1

  ${cmd_sign} "$dir_bundle"

  sleep 1

  ${RCODESIGN} notary-submit \
    --api-key-path "$path_apikey" \
    --max-wait-seconds "1800" \
    --staple \
    "$dir_bundle"

  for target in $(echo "$targets" | tr "," " "); do
    for location in $(echo "$locations" | tr "," " "); do
      if [ ! -d "$dir_bundle_macos/$target/$location/$1" ]; then
        continue
      fi

      mkdir -p "$DIR_TASK/codesign/$target/$location/$1"

      for file_name in $(echo "$file_names" | tr "," " "); do
        rm -rf "$DIR_TASK/codesign/$target/$location/$1/$file_name$diff_ext"

        ../tooling diff-cli create \
          --diff-ext-name "$diff_ext" \
          "$DIR_TASK/build/out/$target/$location/$1/$file_name" \
          "$dir_bundle_macos/$target/$location/$1/$file_name" \
          "$DIR_TASK/codesign/$target/$location/$1"
      done
    done
  done

  rm -rf "$DIR_TMP"
  unset DIR_TMP
  trap - SIGINT ERR
}

function __sign:generate:detached:mingw {
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