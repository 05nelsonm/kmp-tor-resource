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

# TODO: Move to arg parser
readonly DRY_RUN=$(if [ "$2" = "--dry-run" ]; then echo "true"; else echo "false"; fi)
readonly REBUILD=$(if [ "$2" = "--rebuild" ]; then echo "true"; else echo "false"; fi)

readonly DIR_TASK=$( cd "$( dirname "$0" )" >/dev/null && pwd )
readonly FILE_BUILD_LOCK="$DIR_TASK/build/.lock"

# NOTE: If changing, also change versions in docker/Dockerfile.*
# See https://github.com/05nelsonm/build-env
readonly TAG_DOCKER_BUILD_ENV="0.1.3"

# Programs
readonly DOCKER=$(which docker)
readonly GIT=$(which git)
readonly OSSLSIGNCODE=$(which osslsigncode)
readonly RCODESIGN=$(which rcodesign)

# Docker
readonly U_ID=$(id -u)
readonly G_ID=$(id -g)

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
  __exec:docker:run
}

function build:android:armv7 { ## Builds Android armeabi-v7a
  local os_name="android"
  local os_arch="armv7"
  local openssl_target="android-arm"
  local ndk_abi="armeabi-v7a"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

function build:android:x86 { ## Builds Android x86
  local os_name="android"
  local os_arch="x86"
  local openssl_target="android-x86"
  local ndk_abi="x86"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

function build:android:x86_64 { ## Builds Android x86_64
  local os_name="android"
  local os_arch="x86_64"
  local openssl_target="android-x86_64"
  local ndk_abi="x86_64"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

function build:ios-simulator:aarch64 { ## Builds iOS Simulator arm64
  local os_name="ios"
  local os_subtype="-simulator"
  local os_arch="aarch64"
  local openssl_target="iossimulator-xcrun"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

function build:ios-simulator:x86_64 { ## Builds iOS Simulator x86_64
  local os_name="ios"
  local os_subtype="-simulator"
  local os_arch="x86_64"
  local openssl_target="iossimulator-xcrun"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

function build:ios:aarch64 { ## Builds iOS arm64
  local os_name="ios"
  local os_arch="aarch64"
  local openssl_target="ios64-xcrun"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

#function build:freebsd:aarch64 { ## Builds FreeBSD aarch64
#  local os_name="freebsd"
#  local os_arch="aarch64"
#  local openssl_target="BSD-aarch64"
#  __build:configure:target:init
#  # TODO __exec:docker:run
#}

#function build:freebsd:x86 { ## Builds FreeBSD x86
#  local os_name="freebsd"
#  local os_arch="x86"
#  local openssl_target="BSD-x86"
#  __build:configure:target:init
#  # TODO __exec:docker:run
#}

#function build:freebsd:x86_64 { ## Builds FreeBSD x86_64
#  local os_name="freebsd"
#  local os_arch="x86_64"
#  local openssl_target="BSD-x86_64"
#  __build:configure:target:init
#  # TODO __exec:docker:run
#}

function build:linux-libc:aarch64 { ## Builds Linux Libc aarch64
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="aarch64"
  local openssl_target="linux-aarch64"
  __build:configure:target:init
  __conf:CFLAGS '-march=armv8-a'
  __exec:docker:run
}

function build:linux-libc:armv7 { ## Builds Linux Libc armv7
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="armv7"
  local openssl_target="linux-armv4"
  __build:configure:target:init
  __conf:CFLAGS '-march=armv7-a'
  __conf:CFLAGS '-mfloat-abi=hard'
  __conf:CFLAGS '-mfpu=vfp'
  __exec:docker:run
}

function build:linux-libc:ppc64 { ## Builds Linux Libc powerpc64le
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="ppc64"
  local openssl_target="linux-ppc64le"
  __build:configure:target:init
  __exec:docker:run
}

function build:linux-libc:x86 { ## Builds Linux Libc x86
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="x86"
  local openssl_target="linux-x86"
  __build:configure:target:init
  __exec:docker:run
}

function build:linux-libc:x86_64 { ## Builds Linux Libc x86_64
  local os_name="linux"
  local os_subtype="-libc"
  local os_arch="x86_64"
  local openssl_target="linux-x86_64"
  __build:configure:target:init
  __exec:docker:run
}

#function build:linux-musl:aarch64 { ## Builds Linux Musl aarch64
#  local os_name="linux"
#  local os_subtype="-musl"
#  local os_arch="aarch64"
#  local openssl_target="linux-aarch64"
#  __build:configure:target:init
#  # TODO __exec:docker:run
#}

#function build:linux-musl:x86 { ## Builds Linux Musl x86
#  local os_name="linux"
#  local os_subtype="-musl"
#  local os_arch="x86"
#  local openssl_target="linux-x86"
#  __build:configure:target:init
#  __conf:CFLAGS '-m32'
#  __conf:LDFLAGS '-m32'
#  # TODO __exec:docker:run
#}

#function build:linux-musl:x86_64 { ## Builds Linux Musl x86_64
#  local os_name="linux"
#  local os_subtype="-musl"
#  local os_arch="x86_64"
#  local openssl_target="linux-x86_64"
#  __build:configure:target:init
#  # TODO __exec:docker:run
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
  __exec:docker:run
}

function build:macos:x86_64 { ## Builds macOS     (SDK 14.0 - Native) x86_64
  local os_name="macos"
  local os_arch="x86_64"
  local openssl_target="darwin64-x86_64-cc"
  local cc_clang="yes"
  __build:configure:target:init
  __exec:docker:run
}

function build:mingw:x86 { ## Builds Windows x86
  local os_name="mingw"
  local os_arch="x86"
  local openssl_target="mingw"
  __build:configure:target:init
  __conf:LDFLAGS '-Wl,--no-seh'
  __exec:docker:run
}

function build:mingw:x86_64 { ## Builds Windows x86_64
  local os_name="mingw"
  local os_arch="x86_64"
  local openssl_target="mingw64"
  __build:configure:target:init
  __exec:docker:run
}

function clean { ## Deletes the build dir
  rm -rf "$DIR_TASK/build"
}

function help { ## THIS MENU
  # shellcheck disable=SC2154
  echo "
    $0
    Copyright (C) 2023 Matthew Nelson

    Tasks for building, codesigning, and packaging tor binaries

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

function package { ## Packages build dir output
  DIR_STAGING="$(mktemp -d)"
  trap 'rm -rf "$DIR_STAGING"' SIGINT ERR

  local dirname_out=
  local dirname_final="resource-shared-geoip"
  local libname=
  __package:geoip "geoip"
  __package:geoip "geoip6"

  dirname_out="tor"
  dirname_final="resource-shared-$dirname_out"
  libname="tor"
  __package:libs:shared

  dirname_out="tor-gpl"
  dirname_final="resource-shared-$dirname_out"
  __package:libs:shared

  dirname_out="tor"
  dirname_final="resource-exec-$dirname_out"
  __package:libs:native:exec

  dirname_out="tor-gpl"
  dirname_final="resource-exec-$dirname_out"
  __package:libs:native:exec

  rm -rf "$DIR_STAGING"
  trap - SIGINT ERR
}

function sign:apple:macos { ## 2 ARGS - [1]: smartcard-slot (e.g. 9c)  [2]: /path/to/app/store/connect/api_key.json
  # shellcheck disable=SC2128
  if [ $# -ne 2 ]; then
    __error "Usage: $0 $FUNCNAME <smartcard-slot (e.g. 9c)> /path/to/app/store/connect/api_key.json"
  fi

  local os_name="macos"
  local file_name="tor"

  local dirname_out="tor"
  __signature:generate:apple "aarch64" "$1" "$2"
  __signature:generate:apple "x86_64" "$1" "$2"

  local os_subtype="-lts"
  __signature:generate:apple "aarch64" "$1" "$2"
  __signature:generate:apple "x86_64" "$1" "$2"
  unset os_subtype

  dirname_out="tor-gpl"
  __signature:generate:apple "aarch64" "$1" "$2"
  __signature:generate:apple "x86_64" "$1" "$2"

  local os_subtype="-lts"
  __signature:generate:apple "aarch64" "$1" "$2"
  __signature:generate:apple "x86_64" "$1" "$2"
  unset os_subtype
}

function sign:mingw { ## Codesign mingw binaries (see codesign/windows.pkcs11.sample)
  . "$DIR_TASK/codesign/windows.pkcs11"

  local file_name="tor.exe"
  local dirname_out="tor"

  __signature:generate:mingw "x86"
  __signature:generate:mingw "x86_64"

  dirname_out="tor-gpl"
  __signature:generate:mingw "x86"
  __signature:generate:mingw "x86_64"
}

function validate { ## Checks the build/package directory output against expected sha256 hashes
  local kmp_targets="JVM,LINUX_ARM64,LINUX_X64,MACOS_ARM64,MACOS_X64,MINGW_X64,IOS_ARM64,IOS_SIMULATOR_ARM64,IOS_X64,TVOS_ARM64,TVOS_SIMULATOR_ARM64,TVOS_X64,WATCHOS_ARM32,WATCHOS_ARM64,WATCHOS_DEVICE_ARM64,WATCHOS_SIMULATOR_ARM64,WATCHOS_X64"

  if [ -n "$include_android" ]; then
    kmp_targets="ANDROID,$kmp_targets"
  fi

  cd "$DIR_TASK/.."
  ./gradlew clean -PKMP_TARGETS="$kmp_targets"
  ./gradlew prepareKotlinBuildScriptModel -PKMP_TARGETS="$kmp_targets"

  __validate:report "resource-shared-geoip"
  __validate:report "resource-shared-tor"
  __validate:report "resource-shared-tor-gpl"
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

function __validate:report {
  __require:var_set "$1" "module name"

  local dir_module_reports=
  local errs=
  local file_err=

  dir_module_reports="$DIR_TASK/../library/$1/build/reports/resource-validation/$1"
  errs=$(ls "$dir_module_reports")

  for file_err in $errs; do
    echo "$1/$file_err:"
    cat "$dir_module_reports/$file_err"
  done
  echo ""
}

function __build:cleanup {
  rm -rf "$FILE_BUILD_LOCK"
  __build:git:stash "libevent"
  __build:git:stash "openssl"
  __build:git:stash "tor"
  __build:git:stash "xz"
  __build:git:stash "zlib"
}

# shellcheck disable=SC2016
# shellcheck disable=SC1004
function __build:configure:target:init {
  __require:var_set "$os_name" "os_name"
  __require:var_set "$os_arch" "os_arch"
  __require:var_set "$openssl_target" "openssl_target"
  __require:var_set "$U_ID" "U_ID"
  __require:var_set "$G_ID" "G_ID"

  case "$os_name" in
    "android")
      __require:var_set "$ndk_abi" "ndk_abi"

      DIR_OUT_SUFFIX="$os_name/$ndk_abi"
      ;;
    *)
      DIR_OUT_SUFFIX="$os_name$os_subtype/$os_arch"
      ;;
  esac
  DIR_BUILD="build/stage/$DIR_OUT_SUFFIX"

  unset CONF_CFLAGS
  unset CONF_LDFLAGS
  unset CONF_SCRIPT
  unset CONF_LIBEVENT
  unset CONF_OPENSSL
  unset CONF_TOR
  unset CONF_TOR_GPL
  unset CONF_XZ
  unset CONF_ZLIB

  CONF_SCRIPT='#!/bin/sh
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

export LANG=C.UTF-8
export LC_ALL=C
export SOURCE_DATE_EPOCH="1234567890"
export TZ=UTC
set -e

# DO NOT MODIFY. THIS IS AN AUTOMATICALLY GENERATED FILE.
'

  __conf:SCRIPT "readonly TASK_TARGET=\"$os_name$os_subtype:$os_arch\""
  __conf:SCRIPT '
readonly DIR_SCRIPT="$( cd "$( dirname "$0" )" >/dev/null && pwd )"
# Docker container WORKDIR
readonly DIR_EXTERNAL="/work"

if [ ! -f "$DIR_EXTERNAL/task.sh" ]; then
  echo 1>&2 "
    $DIR_EXTERNAL/task.sh not found.
    Are you not using task.sh outside the Docker environment?
  "
  exit 3
fi

if [ -z "$CROSS_TRIPLE" ]; then
  echo 1>&2 "
    CROSS_TRIPLE environment variable must be set.
    Are you not using task.sh?
  "
  exit 3
fi
'

  # Debugging (uncomment for verbose compiler output)
#   __conf:SCRIPT 'export CC="${CC} -v"'
#   __conf:SCRIPT 'export LD="${LD} -v"'

  __conf:SCRIPT 'readonly DIR_TMP="$(mktemp -d)"'
  __conf:SCRIPT "trap 'rm -rf \$DIR_TMP' EXIT"
  __conf:SCRIPT ''
  __conf:SCRIPT 'readonly NUM_JOBS="$(nproc)"'
  __conf:SCRIPT ''
  __conf:SCRIPT 'export LD_LIBRARY_PATH="$DIR_SCRIPT/libevent/lib:$DIR_SCRIPT/openssl/lib:$DIR_SCRIPT/xz/lib:$DIR_SCRIPT/zlib/lib:$LD_LIBRARY_PATH"'
  __conf:SCRIPT 'export LIBS="-L$DIR_SCRIPT/libevent/lib -L$DIR_SCRIPT/openssl/lib -L$DIR_SCRIPT/xz/lib -L$DIR_SCRIPT/zlib/lib"'
  __conf:SCRIPT 'export PKG_CONFIG_PATH="$DIR_SCRIPT/libevent/lib/pkgconfig:$DIR_SCRIPT/openssl/lib/pkgconfig:$DIR_SCRIPT/xz/lib/pkgconfig:$DIR_SCRIPT/zlib/lib/pkgconfig"'

  case "$os_name" in
    "macos")
      __conf:SCRIPT 'export OSXCROSS_PKG_CONFIG_PATH="$PKG_CONFIG_PATH"'
      ;;
    "mingw")
      __conf:SCRIPT 'export CHOST="$CROSS_TRIPLE"'
      ;;
  esac

  # CFLAGS
  __conf:CFLAGS '-I$DIR_SCRIPT/libevent/include'
  __conf:CFLAGS '-I$DIR_SCRIPT/openssl/include'
  __conf:CFLAGS '-I$DIR_SCRIPT/xz/include'
  __conf:CFLAGS '-I$DIR_SCRIPT/zlib/include'
  __conf:CFLAGS '-O3'
  __conf:CFLAGS '-frandom-seed=0'
  __conf:CFLAGS '-fstack-protector-strong'

  if [ -z "$cc_clang" ]; then
    # gcc only
    __conf:CFLAGS '-fno-guess-branch-probability'
  fi

  case "$os_arch" in
    "x86")
      __conf:CFLAGS '-m32'
      ;;
    "x86_64")
      __conf:CFLAGS '-m64'
      ;;
  esac

  case "$os_name" in
    "mingw")
      # In order to utilize the -fstack-protector-strong flag,
      # we also must compile with -static to ensure libssp-0.dll
      # will not be included in the final product.
      #
      # $ objdump -p build/jvm-out/mingw/<arch>/tor.exe | grep "DLL Name"
      __conf:CFLAGS '-static'
      __conf:CFLAGS '-fno-strict-overflow'
      ;;
    *)
      __conf:CFLAGS '-fPIC'
      ;;
  esac

  # LDFLAGS
  __conf:LDFLAGS '-L$DIR_SCRIPT/libevent/lib'
  __conf:LDFLAGS '-L$DIR_SCRIPT/openssl/lib'
  __conf:LDFLAGS '-L$DIR_SCRIPT/xz/lib'
  __conf:LDFLAGS '-L$DIR_SCRIPT/zlib/lib'

  case "$os_name" in
    "mingw")
      __conf:LDFLAGS '-Wl,--no-insert-timestamp'
      __conf:LDFLAGS '-static-libgcc'
      ;;
  esac

  # ZLIB
  CONF_ZLIB='./configure --static \
    --prefix="$DIR_SCRIPT/zlib"'

  # LZMA
  CONF_XZ='./configure --disable-doc \
    --disable-lzma-links \
    --disable-lzmadec \
    --disable-lzmainfo \
    --disable-scripts \
    --disable-shared \
    --disable-xz \
    --disable-xzdec \
    --enable-small \
    --enable-static \
    --with-pic \
    --host="$CROSS_TRIPLE" \
    --prefix="$DIR_SCRIPT/xz"'

  # OPENSSL
  CONF_OPENSSL='./Configure no-shared \
    no-asm \
    no-comp \
    no-dtls \
    no-err \
    no-psk \
    no-srp \
    no-weak-ssl-ciphers \
    no-camellia \
    no-idea \
    no-md2 \
    no-md4 \
    no-rc2 \
    no-rc4 \
    no-rc5 \
    no-rmd160 \
    no-whirlpool \
    no-ui-console \
    enable-pic'

  if [ "${os_arch: -2}" = "64" ]; then
    __conf:OPENSSL 'enable-ec_nistp_64_gcc_128'
  fi

  case "$os_name" in
    "android")
      __conf:OPENSSL '-D__ANDROID_API__=21'
      ;;
    "mingw")
      # Even though -static is declared in CFLAGS, it is declared here
      # because openssl's Configure file is jank.
      __conf:OPENSSL '-static'
      ;;
  esac

  __conf:OPENSSL '--libdir=lib'
  __conf:OPENSSL '--with-zlib-lib="$DIR_SCRIPT/zlib/lib"'
  __conf:OPENSSL '--with-zlib-include="$DIR_SCRIPT/zlib/include"'
  __conf:OPENSSL '--prefix="$DIR_SCRIPT/openssl"'
  __conf:OPENSSL "$openssl_target"

  # LIBEVENT
  CONF_LIBEVENT='./configure --disable-debug-mode \
    --disable-doxygen-html \
    --disable-libevent-regress \
    --disable-openssl \
    --disable-samples \
    --disable-shared \
    --enable-static'

  case "$os_name" in
    "ios"|"macos"|"tvos"|"watchos")
      __conf:LIBEVENT '--disable-clock-gettime'
      ;;
  esac

  # --enable-gcc-hardening adds -fPIE to CFLAGS which does not bode well when
  # creating a PIC static library (to use in a shared lib). So, flags that it
  # **would** add can be expressed here. Flag -fstack-protector-all (already
  # passing -fstack-protector-strong) and -fPIE are not included.
  __conf:LIBEVENT 'CFLAGS="$CFLAGS -D_FORTIFY_SOURCE=2 -fwrapv -Wstack-protector --param ssp-buffer-size=1"'

  __conf:LIBEVENT '--host="$CROSS_TRIPLE"'
  __conf:LIBEVENT '--prefix="$DIR_SCRIPT/libevent"'

  # TOR
  CONF_TOR='./configure --disable-asciidoc \
    --disable-html-manual \
    --disable-manpage \
    --disable-system-torrc \
    --disable-systemd \
    --disable-tool-name-check \
    --enable-pic \
    --enable-zstd=no \
    --enable-static-libevent \
    --with-libevent-dir="$DIR_SCRIPT/libevent" \
    --enable-lzma \
    --enable-static-openssl \
    --with-openssl-dir="$DIR_SCRIPT/openssl" \
    --enable-static-zlib \
    --with-zlib-dir="$DIR_SCRIPT/zlib" \
    --host="$CROSS_TRIPLE"'

  case "$os_name" in
    "android")
      __conf:TOR '--enable-android'
      ;;
    "ios"|"macos"|"tvos"|"watchos")
      __conf:TOR 'ac_cv_func__NSGetEnviron="no"'

      # external calls that are not liked by darwin targets
      __conf:TOR 'ac_cv_func_clock_gettime="no"'
      __conf:TOR 'ac_cv_func_getentropy="no"'
      ;;
    "mingw")
      __conf:TOR '--enable-static-tor'
      ;;
  esac

  CONF_TOR_GPL="$CONF_TOR"

  __conf:TOR '--prefix="$DIR_SCRIPT/tor"'
  __conf:TOR:GPL '--enable-gpl'
  __conf:TOR:GPL '--prefix="$DIR_SCRIPT/tor-gpl"'
}

# shellcheck disable=SC2016
# shellcheck disable=SC1004
function __build:configure:target:build_script {
  __require:var_set "$os_name" "os_name"
  __require:var_set "$DIR_BUILD" "DIR_BUILD"
#  __require:var_set "$DIR_OUT_TOR" "DIR_OUT_TOR"
#  __require:var_set "$DIR_OUT_TOR_GPL" "DIR_OUT_TOR_GPL"

  __conf:SCRIPT "export CFLAGS=\"$CONF_CFLAGS\""
  __conf:SCRIPT "export LDFLAGS=\"$CONF_LDFLAGS\""
  __conf:SCRIPT ''

  __conf:SCRIPT 'compile_prepare() {'
  __conf:SCRIPT '  echo "    * Compiling $1$3'
  __conf:SCRIPT "        LOGS >> $DIR_BUILD/\$2/logs\""
  __conf:SCRIPT ''
  __conf:SCRIPT '  rm -rf "$DIR_SCRIPT/$2"'
  __conf:SCRIPT '  mkdir -p "$DIR_SCRIPT/$2/include"'
  __conf:SCRIPT '  mkdir -p "$DIR_SCRIPT/$2/lib"'
  __conf:SCRIPT '  mkdir -p "$DIR_SCRIPT/$2/logs"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  cp -R "$DIR_EXTERNAL/$1" "$DIR_TMP/$2"'
  __conf:SCRIPT '  cd "$DIR_TMP/$2"'
  __conf:SCRIPT '}'
  __conf:SCRIPT ''

  # ZLIB
  __conf:SCRIPT 'compile_zlib() {'
  __conf:SCRIPT '  compile_prepare "zlib" "zlib"'
  __conf:SCRIPT ''
  __conf:SCRIPT "  $CONF_ZLIB > \"\$DIR_SCRIPT/zlib/logs/configure.log\" 2> \"\$DIR_SCRIPT/zlib/logs/configure.err\""
  __conf:SCRIPT ''
  __conf:SCRIPT '  cat configure.log >> "$DIR_SCRIPT/zlib/logs/configure.log"'
  __conf:SCRIPT '  make clean > /dev/null'
  __conf:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/zlib/logs/make.log" 2> "$DIR_SCRIPT/zlib/logs/make.err"'
  __conf:SCRIPT '  make install >> "$DIR_SCRIPT/zlib/logs/make.log" 2>> "$DIR_SCRIPT/zlib/logs/make.err"'
  __conf:SCRIPT '}'
  __conf:SCRIPT ''

  # XZ
  __conf:SCRIPT 'compile_xz() {'
  __conf:SCRIPT '  compile_prepare "xz" "xz"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  ./autogen.sh --no-po4a > "$DIR_SCRIPT/xz/logs/autogen.log" 2> "$DIR_SCRIPT/xz/logs/autogen.err"'
  __conf:SCRIPT "  $CONF_XZ > \"\$DIR_SCRIPT/xz/logs/configure.log\" 2> \"\$DIR_SCRIPT/xz/logs/configure.err\""
  __conf:SCRIPT ''
  __conf:SCRIPT '  make clean > /dev/null'
  __conf:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/xz/logs/make.log" 2> "$DIR_SCRIPT/xz/logs/make.err"'
  __conf:SCRIPT '  make install >> "$DIR_SCRIPT/xz/logs/make.log" 2>> "$DIR_SCRIPT/xz/logs/make.err"'
  __conf:SCRIPT '}'
  __conf:SCRIPT ''

  # OPENSSL
  __conf:SCRIPT 'compile_openssl() {'
  __conf:SCRIPT '  compile_prepare "openssl" "openssl"'
  __conf:SCRIPT ''

  if [ "$os_name" = "mingw" ]; then
    # PATCH
    __conf:SCRIPT '  # https://github.com/openssl/openssl/issues/14574'
    __conf:SCRIPT '  # https://github.com/netdata/netdata/pull/15842'
    __conf:SCRIPT "  sed -i \"s/disable('static', 'pic', 'threads');/disable('static', 'pic');/\" \"Configure\""
    __conf:SCRIPT ''
  fi
  __conf:SCRIPT "  $CONF_OPENSSL > \"\$DIR_SCRIPT/openssl/logs/configure.log\" 2> \"\$DIR_SCRIPT/openssl/logs/configure.err\""
  __conf:SCRIPT ''
  __conf:SCRIPT '  perl configdata.pm --dump >> "$DIR_SCRIPT/openssl/logs/configure.log"'
  __conf:SCRIPT '  make clean > /dev/null'
  __conf:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/openssl/logs/make.log" 2> "$DIR_SCRIPT/openssl/logs/make.err"'
  __conf:SCRIPT '  make install_sw >> "$DIR_SCRIPT/openssl/logs/make.log" 2>> "$DIR_SCRIPT/openssl/logs/make.err"'
  __conf:SCRIPT '}'
  __conf:SCRIPT ''

  # LIBEVENT
  __conf:SCRIPT 'compile_libevent() {'
  __conf:SCRIPT '  compile_prepare "libevent" "libevent"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  ./autogen.sh > "$DIR_SCRIPT/libevent/logs/autogen.log" 2> "$DIR_SCRIPT/libevent/logs/autogen.err"'
  __conf:SCRIPT "  $CONF_LIBEVENT > \"\$DIR_SCRIPT/libevent/logs/configure.log\" 2> \"\$DIR_SCRIPT/libevent/logs/configure.err\""
  __conf:SCRIPT ''
  __conf:SCRIPT '  make clean > /dev/null'
  __conf:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/libevent/logs/make.log" 2> "$DIR_SCRIPT/libevent/logs/make.err"'
  __conf:SCRIPT '  make install >> "$DIR_SCRIPT/libevent/logs/make.log" 2>> "$DIR_SCRIPT/libevent/logs/make.err"'
  __conf:SCRIPT '}'

  # TOR & TOR GPL
  __conf:SCRIPT '
# Includes are not enough when using --enable-lzma flag.
# Must specify it here so configure picks it up.
export LZMA_CFLAGS="-I$DIR_SCRIPT/xz/include"
export LZMA_LIBS="$DIR_SCRIPT/xz/lib/liblzma.a"'

  local conf_out=
  local tor_target=
  for tor_target in $(echo "tor,tor-gpl" | tr "," " "); do
    __conf:SCRIPT ''

    if [ "$tor_target" = "tor" ]; then
      __conf:SCRIPT 'compile_tor() {'
      __conf:SCRIPT '  compile_prepare "tor" "tor"'
    else
      __conf:SCRIPT 'compile_tor_gpl() {'
      __conf:SCRIPT '  compile_prepare "tor" "tor-gpl" " (with flag --enable-gpl)"'
    fi
    __conf:SCRIPT ''

    case "$os_name" in
      "macos"|"ios"|"tvos"|"watchos")
        # PATCH
        __conf:SCRIPT '  # https://trac.macports.org/ticket/65838#no1'
        __conf:SCRIPT "  sed -i 's+\"\${AR:-ar}\" x \"\$abs\"+\"\${AR:-ar}\" x \"\$abs\"; rm -f \"__.SYMDEF SORTED\"+' \"scripts/build/combine_libs\""
        __conf:SCRIPT ''
        ;;
    esac

    if [ "$os_arch" = "aarch64" ]; then
      case "$os_name" in
        "ios"|"tvosos"|"watchos")
          # PATCH
          __conf:SCRIPT '  # https://gitlab.torproject.org/tpo/core/tor/-/issues/40903'
          __conf:SCRIPT '  sed -i "s+__builtin___clear_cache((void\*)code, (void\*)pos);+return true;+" "src/ext/equix/hashx/src/compiler_a64.c"'
          __conf:SCRIPT ''
          ;;
      esac
    fi

    __conf:SCRIPT "  ./autogen.sh > \"\$DIR_SCRIPT/$tor_target/logs/autogen.log\" 2> \"\$DIR_SCRIPT/$tor_target/logs/autogen.err\""

    if [ "$tor_target" = "tor" ]; then
      conf_out="$CONF_TOR"
    else
      conf_out="$CONF_TOR_GPL"
    fi

    __conf:SCRIPT "  $conf_out > \"\$DIR_SCRIPT/$tor_target/logs/configure.log\" 2> \"\$DIR_SCRIPT/$tor_target/logs/configure.err\""
    __conf:SCRIPT ''
    __conf:SCRIPT '  make clean > /dev/null 2>&1'
    __conf:SCRIPT "  make -j\"\$NUM_JOBS\" > \"\$DIR_SCRIPT/$tor_target/logs/make.log\" 2> \"\$DIR_SCRIPT/$tor_target/logs/make.err\""
    __conf:SCRIPT "  make install >> \"\$DIR_SCRIPT/$tor_target/logs/make.log\" 2>> \"\$DIR_SCRIPT/$tor_target/logs/make.err\""
    __conf:SCRIPT ''
    __conf:SCRIPT "  cp -a \"src/feature/api/tor_api.h\" \"\$DIR_SCRIPT/$tor_target/include\""
    __conf:SCRIPT "  cp -a \"orconfig.h\" \"\$DIR_SCRIPT/$tor_target/include\""
    __conf:SCRIPT "  cp -a \"libtor.a\" \"\$DIR_SCRIPT/$tor_target/lib\""
    __conf:SCRIPT ''
    __conf:SCRIPT "  sed -i \"s+BUILDDIR \\\"\$(pwd)\\\"+BUILDDIR \\\"\$DIR_EXTERNAL/tor\\\"+\" \"\$DIR_SCRIPT/$tor_target/include/orconfig.h\""
    __conf:SCRIPT "  sed -i \"s+SRCDIR \\\"\$(pwd)\\\"+SRCDIR \\\"\$DIR_EXTERNAL/tor\\\"+\" \"\$DIR_SCRIPT/$tor_target/include/orconfig.h\""

    __conf:SCRIPT '}'
  done
  unset conf_out
  unset tor_target

  __conf:SCRIPT '
readonly REBUILD="$(if [ "$1" = "--rebuild" ]; then echo "true"; else echo "false"; fi)"

needs_execution() {
  if $REBUILD; then return 0; fi

  _root="$1"; shift
  _path="$1"; shift
  _name=

  for _name in "$@"; do
    if [ ! -f "$_root/$_path/$_name" ]; then
      unset _root
      unset _path
      unset _name
      return 0
    fi
  done

  echo "    - Found $_path >> $*"

  unset _root
  unset _path
  unset _name
  return 1
}

# Ensure include/lib directories are present. Clang complains.
_project=
for _project in $(echo "zlib,xz,openssl,libevent" | tr "," " "); do
  mkdir -p "$DIR_SCRIPT/$_project/include"
  mkdir -p "$DIR_SCRIPT/$_project/lib"
done
unset _project

echo "
    ### TASK - $TASK_TARGET ###
"

if needs_execution "$DIR_SCRIPT" "zlib/lib" "libz.a"; then
  compile_zlib
fi
if needs_execution "$DIR_SCRIPT" "xz/lib" "liblzma.a"; then
  compile_xz
fi
if needs_execution "$DIR_SCRIPT" "openssl/lib" "libcrypto.a" "libssl.a"; then
  compile_openssl
fi
if needs_execution "$DIR_SCRIPT" "libevent/lib" "libevent.a"; then
  compile_libevent
fi
if needs_execution "$DIR_SCRIPT" "tor/lib" "libtor.a"; then
  compile_tor
fi
if needs_execution "$DIR_SCRIPT" "tor-gpl/lib" "libtor.a"; then
  compile_tor_gpl
fi
'

  __build:configure:target:build_script:output

  mkdir -p "$DIR_BUILD"
  echo "$CONF_SCRIPT" > "$DIR_BUILD/build.sh"
  chmod +x "$DIR_BUILD/build.sh"
}

# shellcheck disable=SC2016
# shellcheck disable=SC1004
function __build:configure:target:build_script:output {
  __require:var_set "$os_arch" "os_arch"
  __require:var_set "$os_name" "os_name"
  __require:var_set "$DIR_OUT_SUFFIX" "DIR_OUT_SUFFIX"

  # TODO: JNI
  local shared_name="libtor.so"
  local shared_cflags="-shared"
  local shared_ldadd="-ldl -lm -pthread"

  local exec_name="tor"
  local exec_ldflags='-Wl,-rpath,"\$ORIGIN"' # must use comma (Apple clang cries)

  local strip_flags="-D"

  local is_apple=false
  local is_framework=false

  case "$os_name" in
    "android")
      exec_name="libtorexec.so"
      shared_cflags="$shared_cflags -I\$CROSS_ROOT/sysroot/usr/include -Wl,-soname,$shared_name"
      ;;
    "linux")
      # Defaults
      ;;
    "macos"|"ios"|"tvos"|"watchos")

      if [ "$os_subtype" != "-lts" ]; then
        # Not macOS Jvm/Node.js
        is_framework=true
        exec_ldflags=""
        shared_name="Tor"
      else
        # Jvm/Node.js
        shared_name="libtor.dylib"
      fi

      is_apple=true
      strip_flags="${strip_flags}u"

      if $is_framework; then
        shared_cflags="-dynamiclib -Wl,-install_name,\"@rpath/\$_libname.framework/\$_libname\""
        shared_ldadd='-rpath "@executable_path/Frameworks" -rpath "@loader_path/Frameworks"'
      else
        shared_cflags="-dynamiclib"
        shared_ldadd=""
      fi
      ;;
    "mingw")
      shared_name="tor.dll"
      exec_name="tor.exe"
      shared_ldadd="-lws2_32 -lcrypt32 -lshlwapi -liphlpapi"

      # So if tor.exe is clicked on, it opens in console.
      # This is the same behavior as the tor.exe output by
      # tor-browser-build.
      exec_ldflags="$exec_ldflags -Wl,--subsystem,console"
      ;;
    *)
      __error "Unknown os_name[$os_name]"
      ;;
  esac

  __require:var_set "$shared_name" "shared_name"
  __require:var_set "$shared_cflags" "shared_cflags"
  __require:var_set "$exec_name" "exec_name"
  __require:var_set "$strip_flags" "strip_flags"

  __conf:SCRIPT 'libname() {'
  if $is_framework; then
    __conf:SCRIPT '  if [ "$1" = "tor-gpl" ]; then'
    __conf:SCRIPT "    echo \"${shared_name}GPL\""
    __conf:SCRIPT '  else'
    __conf:SCRIPT "    echo \"$shared_name\""
    __conf:SCRIPT '  fi'
  else
    __conf:SCRIPT "  echo \"$shared_name\""
  fi
  __conf:SCRIPT '}'
  __conf:SCRIPT ''
  __conf:SCRIPT 'compile_shared() {'
  __conf:SCRIPT '  echo "    * Compiling shared-$1 (shared library + linked executable)"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  rm -rf "$DIR_SCRIPT/shared-$1"'
  __conf:SCRIPT "  rm -rf \"\$DIR_EXTERNAL/build/out/\$1/$DIR_OUT_SUFFIX\""
  __conf:SCRIPT '  mkdir "$DIR_TMP/shared-$1"'
  __conf:SCRIPT '  mkdir -p "$DIR_SCRIPT/shared-$1/bin"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  cp -a "$DIR_EXTERNAL/tor/src/app/main/tor_main.c" "$DIR_TMP/shared-$1"'
  __conf:SCRIPT '  cp -a "$DIR_SCRIPT/$1/include/orconfig.h" "$DIR_TMP/shared-$1"'
  __conf:SCRIPT '  cd "$DIR_TMP/shared-$1"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  _libname="$(libname "$1")"'
  __conf:SCRIPT ''
  __conf:SCRIPT "  \$CC \$CFLAGS $shared_cflags \\"
  __conf:SCRIPT '    -o "$_libname" \'

  if $is_apple; then
    __conf:SCRIPT '    $LDFLAGS -Wl,-force_load \'
  else
    __conf:SCRIPT '    $LDFLAGS -Wl,--whole-archive \'
  fi

  # Apple targets require absolute paths when using -force_load
  __conf:SCRIPT '    "$DIR_SCRIPT/$1/lib/libtor.a" \'
  __conf:SCRIPT '    "$DIR_SCRIPT/zlib/lib/libz.a" \'
  __conf:SCRIPT '    "$DIR_SCRIPT/xz/lib/liblzma.a" \'
  __conf:SCRIPT '    "$DIR_SCRIPT/openssl/lib/libssl.a" \'
  __conf:SCRIPT '    "$DIR_SCRIPT/openssl/lib/libcrypto.a" \'
  __conf:SCRIPT '    "$DIR_SCRIPT/libevent/lib/libevent.a" \'

  if $is_apple; then
    __conf:SCRIPT "    \$LDFLAGS $shared_ldadd"
  else
    __conf:SCRIPT "    -Wl,--no-whole-archive \$LDFLAGS $shared_ldadd"
  fi

  __conf:SCRIPT ''
  __conf:SCRIPT '  $CC $CFLAGS tor_main.c \'
  __conf:SCRIPT "    -o $exec_name $exec_ldflags \\"
  __conf:SCRIPT '    "$_libname"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  cp -a "$_libname" "$DIR_SCRIPT/shared-$1/bin"'
  __conf:SCRIPT "  cp -a $exec_name \"\$DIR_SCRIPT/shared-\$1/bin\""
  __conf:SCRIPT '  unset _libname'
  __conf:SCRIPT '}'

  __conf:SCRIPT "
if needs_execution \"\$DIR_SCRIPT\" \"shared-tor/bin\" \"$exec_name\" \"\$(libname \"tor\")\"; then
  compile_shared \"tor\"
fi
if needs_execution \"\$DIR_SCRIPT\" \"shared-tor-gpl/bin\" \"$exec_name\" \"\$(libname \"tor-gpl\")\"; then
  compile_shared \"tor-gpl\"
fi
"

  __conf:SCRIPT 'install_shared() {'
  __conf:SCRIPT '  echo "    * Installing compilations from shared-$1/bin"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  cd "$DIR_EXTERNAL/build"'
  __conf:SCRIPT "  _bin=\"stage/$DIR_OUT_SUFFIX/shared-\$1/bin\""
  __conf:SCRIPT "  _out=\"out/\$1/$DIR_OUT_SUFFIX\""
  __conf:SCRIPT '  _libname="$(libname "$1")"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  rm -rf "$_out"'
  __conf:SCRIPT '  mkdir -p "$_out"'
  __conf:SCRIPT ''
  __conf:SCRIPT '  cp -a "$_bin/$_libname" "$_out"'
  __conf:SCRIPT "  cp -a \"\$_bin/$exec_name\" \"\$_out\""
  __conf:SCRIPT "  cp -aR \"stage/$DIR_OUT_SUFFIX/\$1/include\" \"\$_out\""
  __conf:SCRIPT ''
  __conf:SCRIPT "  \$STRIP $strip_flags \"\$_out/\$_libname\" 2>/dev/null"
  __conf:SCRIPT "  \$STRIP $strip_flags \"\$_out/$exec_name\" 2>/dev/null"
  __conf:SCRIPT ''

  if [ "$os_name" = "android" ]; then
    __conf:SCRIPT "  _out_linux=\"out/\$1/linux-android/$os_arch\""
    __conf:SCRIPT '  rm -rf "$_out_linux"'
    __conf:SCRIPT '  mkdir -p "$_out_linux"'
    __conf:SCRIPT '  cp -a "$_out/$_libname" "$_out_linux"'
    __conf:SCRIPT "  cp -a \"\$_out/$exec_name\" \"\$_out_linux/tor\""
    __conf:SCRIPT '  cp -aR "$_out/include" "$_out_linux"'
    __conf:SCRIPT '  unset _out_linux'
    __conf:SCRIPT '  sleep 0.5'
    __conf:SCRIPT ''
  fi

  __conf:SCRIPT "  echo \"        STRIP[\$STRIP $strip_flags]\""
  __conf:SCRIPT "  echo \"        BIN: \$(du -sh \"\$_bin/\$_libname\" | cut -d 's' -f 1) \$(cd \"\$_bin\" && sha256sum \"\$_libname\")\""
  __conf:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/\$_libname\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"\$_libname\")\""
  __conf:SCRIPT "  echo \"        BIN: \$(du -sh \"\$_bin/$exec_name\" | cut -d 's' -f 1) \$(cd \"\$_bin\" && sha256sum \"$exec_name\")\""
  __conf:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/$exec_name\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"$exec_name\")\""
  __conf:SCRIPT ''
  __conf:SCRIPT '  unset _bin'
  __conf:SCRIPT '  unset _out'
  __conf:SCRIPT '  unset _libname'
  __conf:SCRIPT '}'

  __conf:SCRIPT "
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor/$DIR_OUT_SUFFIX\" \"$exec_name\" \"\$(libname \"tor\")\" \"include/orconfig.h\" \"include/tor_api.h\"; then
  install_shared \"tor\"
fi
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor-gpl/$DIR_OUT_SUFFIX\" \"$exec_name\" \"\$(libname \"tor-gpl\")\" \"include/orconfig.h\" \"include/tor_api.h\"; then
  install_shared \"tor-gpl\"
fi"

  if [ "$os_name" = "android" ]; then
    __conf:SCRIPT "
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor/linux-$os_name/$os_arch\" \"tor\" \"\$(libname \"tor\")\" \"include/orconfig.h\" \"include/tor_api.h\"; then
  install_shared \"tor\"
fi
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor-gpl/linux-$os_name/$os_arch\" \"tor\" \"\$(libname \"tor-gpl\")\" \"include/orconfig.h\" \"include/tor_api.h\"; then
  install_shared \"tor-gpl\"
fi"
  fi
}

function __build:git:apply_patches {
  __require:not_empty "$1" "project name must not be empty"
  local dir_current=
  dir_current="$(pwd)"
  cd "$DIR_TASK/$1"

  local patch_file=
  for patch_file in "$DIR_TASK/patches/$1/"*.patch; do
    if [ "$patch_file" = "$DIR_TASK/patches/$1/*.patch" ]; then
      # no patch files
      continue
    fi

    echo "Applying git patch to $1 >> $patch_file"
    ${GIT} apply "$patch_file"
    sleep 0.25
  done

  cd "$dir_current"
}

function __build:git:clean {
  __require:not_empty "$1" "project name must not be empty"
  local dir_current=
  dir_current="$(pwd)"
  cd "$DIR_TASK/$1"

  ${GIT} clean -X --force --quiet
  cd "$dir_current"
}

function __build:git:stash {
  __require:not_empty "$1" "project name must not be empty"
  local dir_current=
  dir_current="$(pwd)"
  cd "$DIR_TASK/$1"

  ${GIT} add --all

  if [ "$(${GIT} stash)" = "No local changes to save" ]; then
    cd "$dir_current"
    return 0
  fi

  ${GIT} stash drop
  cd "$dir_current"
}

function __conf:SCRIPT {
  CONF_SCRIPT+="
$1"
}

function __conf:CFLAGS {
  if [ -z "$CONF_CFLAGS" ]; then
    CONF_CFLAGS="$1"
  else
    CONF_CFLAGS+=" $1"
  fi
}

function __conf:LDFLAGS {
  if [ -z "$CONF_LDFLAGS" ]; then
    CONF_LDFLAGS="$1"
  else
    CONF_LDFLAGS+=" $1"
  fi
}

function __conf:LIBEVENT {
  if [ -z "$1" ]; then return 0; fi
  CONF_LIBEVENT+=" \\
    $1"
}

function __conf:OPENSSL {
  if [ -z "$1" ]; then return 0; fi
  CONF_OPENSSL+=" \\
    $1"
}

function __conf:TOR {
  if [ -z "$1" ]; then return 0; fi
  CONF_TOR+=" \\
    $1"
}

function __conf:TOR:GPL {
  if [ -z "$1" ]; then return 0; fi
  CONF_TOR_GPL+=" \\
    $1"
}

function __conf:XZ {
  if [ -z "$1" ]; then return 0; fi
  CONF_XZ+=" \\
    $1"
}

function __conf:ZLIB   {
  if [ -z "$1" ]; then return 0; fi
  CONF_ZLIB+=" \\
    $1"
}

function __exec:docker:run {
  __build:configure:target:build_script

  if $DRY_RUN; then
    echo "Build Script >> $DIR_BUILD/build.sh"
    return 0
  fi

  __require:cmd "$DOCKER" "docker"

  trap 'echo "
    SIGINT intercepted... exiting...
"; exit 1' SIGINT

  # map os_arch to what docker container expects
  local docker_arch="$os_arch"
  case "$os_arch" in
    "armv7") docker_arch="armv7a"  ;;
    "ppc64") docker_arch="ppc64le" ;;
  esac

  case "$os_name" in
    "ios"|"tvos"|"watchos")
      # Currently have to build containers locally until they
      # are moved to the build-env project
      ${DOCKER} build \
        -f "$DIR_TASK/docker/Dockerfile.$os_name.base" \
        -t "05nelsonm/build-env.$os_name.base:$TAG_DOCKER_BUILD_ENV" \
        "$DIR_TASK/docker"

      ${DOCKER} build \
        -f "$DIR_TASK/docker/Dockerfile.$os_name$os_subtype.$docker_arch" \
        -t "05nelsonm/build-env.$os_name$os_subtype.$docker_arch:$TAG_DOCKER_BUILD_ENV" \
        "$DIR_TASK/docker"
      ;;
  esac

  local rebuild=""
  if $REBUILD; then
    rebuild="--rebuild"
  fi

  ${DOCKER} run \
    --rm \
    -u "$U_ID:$G_ID" \
    -v "$DIR_TASK:/work" \
    "05nelsonm/build-env.$os_name$os_subtype.$docker_arch:$TAG_DOCKER_BUILD_ENV" \
    "./$DIR_BUILD/build.sh" "$rebuild"

  trap - SIGINT
}

function __package:libs:shared {
  __require:var_set "$libname" "libname"
  __require:var_set "$dirname_final" "dirname_final"
  __require:var_set "$dirname_out" "dirname_out"

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
  __require:var_set "$libname" "libname"
  __require:var_set "$dirname_final" "dirname_final"
  __require:var_set "$dirname_out" "dirname_out"

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
  __require:var_set "$1" "Packaging target dir (relative to dir external/build/)"
  __require:var_set "$2" "Binary module src path (e.g. external/package/resource-shared-tor/src)"
  __require:var_set "$3" "File name"
  __require:var_set "$dirname_final" "dirname_final"

  __require:var_set "$permissions" "permissions"
  __require:var_set "$DIR_STAGING" "DIR_STAGING"

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

  __require:cmd "$DOCKER" "docker"

  cp -a "$DIR_TASK/$1/$3" "$DIR_STAGING"

  if [ -n "$detached_sig" ]; then
    ../tooling diff-cli apply \
      "$DIR_TASK/codesign/$dirname_out/$detached_sig/$3.signature" \
      "$DIR_STAGING/$3"
  fi

  local file_ext=""
  if [ -n "$gzip" ]; then
    # Must utilize docker for reproducable results
    ${DOCKER} run \
      --rm \
      -u "$U_ID:$G_ID" \
      -v "$DIR_STAGING:/work" \
      "05nelsonm/build-env.linux-libc.base:$TAG_DOCKER_BUILD_ENV" \
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

    # shellcheck disable=SC2115
    rm -rf "$DIR_STAGING/$3$file_ext"
  fi

}

function __signature:generate:apple {
  __require:var_set "$dirname_out" "dirname_out"
  __require:var_set "$file_name" "file_name"
  __require:var_set "$os_name" "os_name"
  __require:cmd "$RCODESIGN" "rcodesign"
  __require:var_set "$1" "arch"
  __require:var_set "$2" "smartcard-slot"
  # $3 App Store Connect api-key (macos only)

  if [ ! -f "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" ]; then
    echo "
    $file_name not found for $dirname_out/$os_name$os_subtype:$1. Skipping...
    "
    return 0
  fi

  # Relative location within template bundle
  local dir_libs=
  local executable=

  case "$os_name" in
    "macos")
      __require:file_exists "$3" "App Store Connect api key file does not exist"
      executable="Contents/MacOS/KmpTorResource"
      dir_libs="Contents/MacOS/NativeLibs"
      ;;
    "ios")
      executable="KmpTorResource"
      dir_libs="Executables"
      ;;
    *)
      __error "Unknown os_name[$os_name]"
      ;;
  esac

  echo "
    Creating detached signature for build/out/$dirname_out/$os_name$os_subtype/$1/$file_name
  "

  DIR_TMP="$(mktemp -d)"
  trap 'rm -rf "$DIR_TMP"' SIGINT ERR

  cp -R "$DIR_TASK/codesign/template/$os_name/KmpTorResource.app" "$DIR_TMP"
  rm -rf "$DIR_TMP/KmpTorResource.app/$dir_libs/.gitkeep"
  cp "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" "$DIR_TMP/KmpTorResource.app/$executable"
  cp -a "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" "$DIR_TMP/KmpTorResource.app/$dir_libs/$file_name"

  ${RCODESIGN} sign \
    --code-signature-flags runtime \
    --code-signature-flags "$dir_libs/$file_name:runtime" \
    --smartcard-slot "$2" \
    "$DIR_TMP/KmpTorResource.app"

  if [ "$os_name" = "macos" ]; then
    # Developer ID certificate (macOS only)
    echo ""
    sleep 1

    # maximum wait time is set to 45m
    ${RCODESIGN} notary-submit \
      --api-key-path "$3" \
      --max-wait-seconds "2700" \
      --staple \
      "$DIR_TMP/KmpTorResource.app"
  fi

  mkdir -p "$DIR_TASK/codesign/$dirname_out/$os_name$os_subtype/$1"
  rm -rf "$DIR_TASK/codesign/$dirname_out/$os_name$os_subtype/$1/$file_name.signature"

  echo ""

  ../tooling diff-cli create \
    --diff-ext-name ".signature" \
    "$DIR_TASK/build/out/$dirname_out/$os_name$os_subtype/$1/$file_name" \
    "$DIR_TMP/KmpTorResource.app/$dir_libs/$file_name" \
    "$DIR_TASK/codesign/$dirname_out/$os_name$os_subtype/$1"

  echo ""

  rm -rf "$DIR_TMP"
  unset DIR_TMP
  trap - SIGINT ERR
}

# shellcheck disable=SC2154
function __signature:generate:mingw {
  __require:var_set "$dirname_out" "dirname_out"
  __require:var_set "$file_name" "file_name"
  __require:cmd "$OSSLSIGNCODE" "osslsigncode"
  __require:var_set "$1" "arch"

  __require:file_exists "$gen_pkcs11engine_path" "windows.pkcs11[gen_pkcs11engine_path] file does not exist"
  __require:file_exists "$gen_pkcs11module_path" "windows.pkcs11[gen_pkcs11module_path] file does not exist"
  __require:var_set "$gen_model" "windows.pkcs11[gen_model] not set"
  __require:var_set "$gen_manufacturer" "windows.pkcs11[gen_manufacturer] not set"
  __require:var_set "$gen_serial" "windows.pkcs11[gen_serial] not set"
  __require:var_set "$gen_ts" "windows.pkcs11[gen_ts] not set"
  __require:file_exists "$gen_cert_path" "windows.pkcs11[gen_cert_path] file does not exist"
  __require:var_set "$gen_id" "windows.pkcs11[gen_id] not set"

  local pkcs11_url="pkcs11:model=$gen_model;manufacturer=$gen_manufacturer;serial=$gen_serial;id=$gen_id;type=private"

  if [ ! -f "$DIR_TASK/build/out/$dirname_out/mingw/$1/$file_name" ]; then
    echo "
    $file_name not found for mingw/$1. Skipping...
    "
    return 0
  fi

  echo "
    Creating detached signature for build/out/$dirname_out/mingw/$1/$file_name
  "

  DIR_TMP="$(mktemp -d)"
  trap 'rm -rf "$DIR_TMP"' SIGINT ERR

  ${OSSLSIGNCODE} sign \
    -pkcs11engine "$gen_pkcs11engine_path" \
    -pkcs11module "$gen_pkcs11module_path" \
    -key "$pkcs11_url" \
    -certs "$gen_cert_path" \
    -ts "$gen_ts" \
    -h "sha256" \
    -in "$DIR_TASK/build/out/$dirname_out/mingw/$1/$file_name" \
    -out "$DIR_TMP/$file_name"

  mkdir -p "$DIR_TASK/codesign/$dirname_out/mingw/$1"
  rm -rf "$DIR_TASK/codesign/$dirname_out/mingw/$1/$file_name.signature"

  echo ""

  ../tooling diff-cli create \
    --diff-ext-name ".signature" \
    "$DIR_TASK/build/out/$dirname_out/mingw/$1/$file_name" \
    "$DIR_TMP/$file_name" \
    "$DIR_TASK/codesign/$dirname_out/mingw/$1"

  echo ""

  rm -rf "$DIR_TMP"
  unset DIR_TMP
  trap - SIGINT ERR
}

function __require:cmd {
  __require:file_exists "$1" "$2 is required to run this script"
}

function __require:file_exists {
  if [ -f "$1" ]; then return 0; fi
  __error "$2"
}

function __require:var_set {
  __require:not_empty "$1" "$2 must be set"
}

function __require:not_empty {
  if [ -n "$1" ]; then return 0; fi
  __error "$2"
}

function __require:no_build_lock {
  if [ ! -f "$FILE_BUILD_LOCK" ]; then return 0; fi

  __error "A build is in progress

    If this is not the case, delete the following file and re-run the task
    $FILE_BUILD_LOCK"
}

function __error {
  echo 1>&2 "
    ERROR: $1
  "

  exit 3
}

# Run
if [ -z "$1" ] || [ "$1" = "help" ] || echo "$1" | grep -q "^__"; then
  help
elif ! grep -qE "^function $1 {" "$0"; then
  help
  __error "Unknown task '$1'"
else
  __require:no_build_lock

  # Ensure always starting in the external directory
  cd "$DIR_TASK"
  mkdir -p "build"

  if echo "$1" | grep -q "^build"; then
    __require:cmd "$GIT" "git"

    ${GIT} submodule update --init

    __require:no_build_lock
    trap '__build:cleanup' EXIT
    echo "$1" > "$FILE_BUILD_LOCK"

    __build:git:clean "libevent"
    __build:git:apply_patches "libevent"
    __build:git:clean "openssl"
    __build:git:apply_patches "openssl"
    __build:git:clean "tor"
    __build:git:apply_patches "tor"
    __build:git:clean "xz"
    __build:git:apply_patches "xz"
    __build:git:clean "zlib"
    __build:git:apply_patches "zlib"
  elif echo "$1" | grep -q "^package"; then
    __require:cmd "$GIT" "git"

    ${GIT} submodule update --init "$DIR_TASK/tor"
    __build:git:clean "tor"

    __require:no_build_lock
    trap 'rm -rf "$FILE_BUILD_LOCK"' EXIT
    echo "$1" > "$FILE_BUILD_LOCK"
  elif echo "$1" | grep -q "^sign"; then
    trap 'rm -rf "$FILE_BUILD_LOCK"' EXIT
    echo "$1" > "$FILE_BUILD_LOCK"
  fi

  TIMEFORMAT="
    Task '$1' completed in %3lR
  "
  time "$@"
fi
