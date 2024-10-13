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

# Sourced by external/task.sh for all 'build:*' tasks

# Dependency
. "$DIR_TASK/source.task/source.docker.sh"

function __build:SCRIPT {
  CONF_SCRIPT+="
$1"
}

function __build:CFLAGS {
  if [ -z "$CONF_CFLAGS" ]; then
    CONF_CFLAGS="$1"
  else
    CONF_CFLAGS+=" $1"
  fi
}

function __build:LDFLAGS {
  if [ -z "$CONF_LDFLAGS" ]; then
    CONF_LDFLAGS="$1"
  else
    CONF_LDFLAGS+=" $1"
  fi
}

function __build:LIBEVENT {
  if [ -z "$1" ]; then return 0; fi
  CONF_LIBEVENT+=" \\
    $1"
}

function __build:OPENSSL {
  if [ -z "$1" ]; then return 0; fi
  CONF_OPENSSL+=" \\
    $1"
}

function __build:TOR {
  if [ -z "$1" ]; then return 0; fi
  CONF_TOR+=" \\
    $1"
}

function __build:TOR:GPL {
  if [ -z "$1" ]; then return 0; fi
  CONF_TOR_GPL+=" \\
    $1"
}

function __build:XZ {
  if [ -z "$1" ]; then return 0; fi
  CONF_XZ+=" \\
    $1"
}

function __build:ZLIB   {
  if [ -z "$1" ]; then return 0; fi
  CONF_ZLIB+=" \\
    $1"
}

function __build:configure:target:init {
  __util:require:var_set "$os_name" "os_name"
  __util:require:var_set "$os_arch" "os_arch"
  __util:require:var_set "$openssl_target" "openssl_target"

  case "$os_name" in
    "android")
      __util:require:var_set "$ndk_abi" "ndk_abi"

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
export ZERO_AR_DATE=1
export TZ=UTC
set -e

# DO NOT MODIFY. THIS IS AN AUTOMATICALLY GENERATED FILE.
'

  __build:SCRIPT "readonly TASK_TARGET=\"$os_name$os_subtype:$os_arch\""
  __build:SCRIPT '
readonly DIR_SCRIPT="$( cd "$( dirname "$0" )" >/dev/null && pwd )"
# Docker container WORKDIR
readonly DIR_EXTERNAL="/work"

if [ ! -f "$DIR_EXTERNAL/task.sh" ]; then
  echo 1>&2 "
    $DIR_EXTERNAL/task.sh not found.
    Are you using build.sh outside the Docker environment?
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
#   __build:SCRIPT 'export CC="${CC} -v"'
#   __build:SCRIPT 'export LD="${LD} -v"'

  __build:SCRIPT 'readonly DIR_TMP="$(mktemp -d)"'
  __build:SCRIPT "trap 'rm -rf \$DIR_TMP' EXIT"
  __build:SCRIPT ''
  __build:SCRIPT 'readonly NUM_JOBS="$(nproc)"'
  __build:SCRIPT ''
  __build:SCRIPT 'export LD_LIBRARY_PATH="$DIR_SCRIPT/libevent/lib:$DIR_SCRIPT/openssl/lib:$DIR_SCRIPT/xz/lib:$DIR_SCRIPT/zlib/lib:$LD_LIBRARY_PATH"'
  __build:SCRIPT 'export LIBS="-L$DIR_SCRIPT/libevent/lib -L$DIR_SCRIPT/openssl/lib -L$DIR_SCRIPT/xz/lib -L$DIR_SCRIPT/zlib/lib"'
  __build:SCRIPT 'export PKG_CONFIG_PATH="$DIR_SCRIPT/libevent/lib/pkgconfig:$DIR_SCRIPT/openssl/lib/pkgconfig:$DIR_SCRIPT/xz/lib/pkgconfig:$DIR_SCRIPT/zlib/lib/pkgconfig"'

  case "$os_name" in
    "macos")
      __build:SCRIPT 'export OSXCROSS_PKG_CONFIG_PATH="$PKG_CONFIG_PATH"'
      ;;
    "mingw")
      __build:SCRIPT 'export CHOST="$CROSS_TRIPLE"'
      ;;
  esac

  # CFLAGS
  __build:CFLAGS '-I$DIR_SCRIPT/libevent/include'
  __build:CFLAGS '-I$DIR_SCRIPT/openssl/include'
  __build:CFLAGS '-I$DIR_SCRIPT/xz/include'
  __build:CFLAGS '-I$DIR_SCRIPT/zlib/include'
  __build:CFLAGS '-O3'
  __build:CFLAGS '-frandom-seed=0'
  __build:CFLAGS '-fstack-protector-strong'

  if [ -z "$cc_clang" ]; then
    # gcc only
    __build:CFLAGS '-fno-guess-branch-probability'
  fi

  case "$os_arch" in
    "x86")
      __build:CFLAGS '-m32'
      ;;
    "x86_64")
      __build:CFLAGS '-m64'
      ;;
  esac

  case "$os_name" in
    "mingw")
      # In order to utilize the -fstack-protector-strong flag,
      # we also must compile with -static to ensure libssp-0.dll
      # will not be included in the final product.
      #
      # $ objdump -p build/jvm-out/mingw/<arch>/tor.exe | grep "DLL Name"
      __build:CFLAGS '-static'
      __build:CFLAGS '-fno-strict-overflow'
      ;;
    *)
      __build:CFLAGS '-fPIC'
      ;;
  esac

  # LDFLAGS
  __build:LDFLAGS '-L$DIR_SCRIPT/libevent/lib'
  __build:LDFLAGS '-L$DIR_SCRIPT/openssl/lib'
  __build:LDFLAGS '-L$DIR_SCRIPT/xz/lib'
  __build:LDFLAGS '-L$DIR_SCRIPT/zlib/lib'

  case "$os_name" in
    "mingw")
      __build:LDFLAGS '-Wl,--no-insert-timestamp'
      __build:LDFLAGS '-static-libgcc'
      ;;
    "ios"|"macos"|"tvos"|"watchos")
      __build:LDFLAGS '-Wl,-no_adhoc_codesign'
      __build:LDFLAGS '-Wl,-no_uuid'
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
    __build:OPENSSL 'enable-ec_nistp_64_gcc_128'
  fi

  case "$os_name" in
    "android")
      __build:OPENSSL '-D__ANDROID_API__=21'
      ;;
    "mingw")
      # Even though -static is declared in CFLAGS, it is declared here
      # because openssl's Configure file is jank.
      __build:OPENSSL '-static'
      ;;
  esac

  __build:OPENSSL '--libdir=lib'
  __build:OPENSSL '--with-zlib-lib="$DIR_SCRIPT/zlib/lib"'
  __build:OPENSSL '--with-zlib-include="$DIR_SCRIPT/zlib/include"'
  __build:OPENSSL '--prefix="$DIR_SCRIPT/openssl"'
  __build:OPENSSL "$openssl_target"

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
      __build:LIBEVENT '--disable-clock-gettime'
      ;;
  esac

  # --enable-gcc-hardening adds -fPIE to CFLAGS which does not bode well when
  # creating a PIC static library (to use in a shared lib). So, flags that it
  # **would** add can be expressed here. Flag -fstack-protector-all (already
  # passing -fstack-protector-strong) and -fPIE are not included.
  __build:LIBEVENT 'CFLAGS="$CFLAGS -D_FORTIFY_SOURCE=2 -fwrapv -Wstack-protector --param ssp-buffer-size=1"'

  __build:LIBEVENT '--host="$CROSS_TRIPLE"'
  __build:LIBEVENT '--prefix="$DIR_SCRIPT/libevent"'

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
      __build:TOR '--enable-android'
      ;;
    "ios"|"macos"|"tvos"|"watchos")
      __build:TOR 'ac_cv_func__NSGetEnviron="no"'

      # external calls that are not liked by darwin targets
      __build:TOR 'ac_cv_func_clock_gettime="no"'
      __build:TOR 'ac_cv_func_getentropy="no"'
      ;;
    "mingw")
      __build:TOR '--enable-static-tor'
      ;;
  esac

  CONF_TOR_GPL="$CONF_TOR"

  __build:TOR '--prefix="$DIR_SCRIPT/tor"'
  __build:TOR:GPL '--enable-gpl'
  __build:TOR:GPL '--prefix="$DIR_SCRIPT/tor-gpl"'
}

function __build:configure:target:finalize {
  __util:require:var_set "$os_name" "os_name"
  __util:require:var_set "$DIR_BUILD" "DIR_BUILD"

  __build:SCRIPT "export CFLAGS=\"$CONF_CFLAGS\""
  __build:SCRIPT "export LDFLAGS=\"$CONF_LDFLAGS\""
  __build:SCRIPT ''

  __build:SCRIPT 'compile_prepare() {'
  __build:SCRIPT '  echo "    * Compiling $1$3'
  __build:SCRIPT "        LOGS >> $DIR_BUILD/\$2/logs\""
  __build:SCRIPT ''
  __build:SCRIPT '  rm -rf "$DIR_SCRIPT/$2"'
  __build:SCRIPT '  mkdir -p "$DIR_SCRIPT/$2/include"'
  __build:SCRIPT '  mkdir -p "$DIR_SCRIPT/$2/lib"'
  __build:SCRIPT '  mkdir -p "$DIR_SCRIPT/$2/logs"'
  __build:SCRIPT ''
  __build:SCRIPT '  cp -R "$DIR_EXTERNAL/$1" "$DIR_TMP/$2"'
  __build:SCRIPT '  cd "$DIR_TMP/$2"'
  __build:SCRIPT '}'
  __build:SCRIPT ''

  # ZLIB
  __build:SCRIPT 'compile_zlib() {'
  __build:SCRIPT '  compile_prepare "zlib" "zlib"'
  __build:SCRIPT ''
  __build:SCRIPT "  $CONF_ZLIB > \"\$DIR_SCRIPT/zlib/logs/configure.log\" 2> \"\$DIR_SCRIPT/zlib/logs/configure.err\""
  __build:SCRIPT ''
  __build:SCRIPT '  cat configure.log >> "$DIR_SCRIPT/zlib/logs/configure.log"'
  __build:SCRIPT '  make clean > /dev/null'
  __build:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/zlib/logs/make.log" 2> "$DIR_SCRIPT/zlib/logs/make.err"'
  __build:SCRIPT '  make install >> "$DIR_SCRIPT/zlib/logs/make.log" 2>> "$DIR_SCRIPT/zlib/logs/make.err"'
  __build:SCRIPT '}'
  __build:SCRIPT ''

  # XZ
  __build:SCRIPT 'compile_xz() {'
  __build:SCRIPT '  compile_prepare "xz" "xz"'
  __build:SCRIPT ''
  __build:SCRIPT '  ./autogen.sh --no-po4a > "$DIR_SCRIPT/xz/logs/autogen.log" 2> "$DIR_SCRIPT/xz/logs/autogen.err"'
  __build:SCRIPT "  $CONF_XZ > \"\$DIR_SCRIPT/xz/logs/configure.log\" 2> \"\$DIR_SCRIPT/xz/logs/configure.err\""
  __build:SCRIPT ''
  __build:SCRIPT '  make clean > /dev/null'
  __build:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/xz/logs/make.log" 2> "$DIR_SCRIPT/xz/logs/make.err"'
  __build:SCRIPT '  make install >> "$DIR_SCRIPT/xz/logs/make.log" 2>> "$DIR_SCRIPT/xz/logs/make.err"'
  __build:SCRIPT '}'
  __build:SCRIPT ''

  # OPENSSL
  __build:SCRIPT 'compile_openssl() {'
  __build:SCRIPT '  compile_prepare "openssl" "openssl"'
  __build:SCRIPT ''

  if [ "$os_name" = "mingw" ]; then
    # PATCH
    __build:SCRIPT '  # https://github.com/openssl/openssl/issues/14574'
    __build:SCRIPT '  # https://github.com/netdata/netdata/pull/15842'
    __build:SCRIPT "  sed -i \"s/disable('static', 'pic', 'threads');/disable('static', 'pic');/\" \"Configure\""
    __build:SCRIPT ''
  fi
  __build:SCRIPT "  $CONF_OPENSSL > \"\$DIR_SCRIPT/openssl/logs/configure.log\" 2> \"\$DIR_SCRIPT/openssl/logs/configure.err\""
  __build:SCRIPT ''
  __build:SCRIPT '  perl configdata.pm --dump >> "$DIR_SCRIPT/openssl/logs/configure.log"'
  __build:SCRIPT '  make clean > /dev/null'
  __build:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/openssl/logs/make.log" 2> "$DIR_SCRIPT/openssl/logs/make.err"'
  __build:SCRIPT '  make install_sw >> "$DIR_SCRIPT/openssl/logs/make.log" 2>> "$DIR_SCRIPT/openssl/logs/make.err"'
  __build:SCRIPT '}'
  __build:SCRIPT ''

  # LIBEVENT
  __build:SCRIPT 'compile_libevent() {'
  __build:SCRIPT '  compile_prepare "libevent" "libevent"'
  __build:SCRIPT ''
  __build:SCRIPT '  ./autogen.sh > "$DIR_SCRIPT/libevent/logs/autogen.log" 2> "$DIR_SCRIPT/libevent/logs/autogen.err"'
  __build:SCRIPT "  $CONF_LIBEVENT > \"\$DIR_SCRIPT/libevent/logs/configure.log\" 2> \"\$DIR_SCRIPT/libevent/logs/configure.err\""
  __build:SCRIPT ''
  __build:SCRIPT '  make clean > /dev/null'
  __build:SCRIPT '  make -j"$NUM_JOBS" > "$DIR_SCRIPT/libevent/logs/make.log" 2> "$DIR_SCRIPT/libevent/logs/make.err"'
  __build:SCRIPT '  make install >> "$DIR_SCRIPT/libevent/logs/make.log" 2>> "$DIR_SCRIPT/libevent/logs/make.err"'
  __build:SCRIPT '}'

  # TOR & TOR GPL
  __build:SCRIPT '
# Includes are not enough when using --enable-lzma flag.
# Must specify it here so configure picks it up.
export LZMA_CFLAGS="-I$DIR_SCRIPT/xz/include"
export LZMA_LIBS="$DIR_SCRIPT/xz/lib/liblzma.a"'

  local conf_out=
  local tor_target=
  for tor_target in $(echo "tor,tor-gpl" | tr "," " "); do
    __build:SCRIPT ''

    if [ "$tor_target" = "tor" ]; then
      __build:SCRIPT 'compile_tor() {'
      __build:SCRIPT '  compile_prepare "tor" "tor"'
    else
      __build:SCRIPT 'compile_tor_gpl() {'
      __build:SCRIPT '  compile_prepare "tor" "tor-gpl" " (with flag --enable-gpl)"'
    fi
    __build:SCRIPT ''

    case "$os_name" in
      "ios"|"macos"|"tvos"|"watchos")
        # PATCH
        __build:SCRIPT '  # https://trac.macports.org/ticket/65838#no1'
        __build:SCRIPT "  sed -i 's+\"\${AR:-ar}\" x \"\$abs\"+\"\${AR:-ar}\" x \"\$abs\"; rm -f \"__.SYMDEF SORTED\"+' \"scripts/build/combine_libs\""
        __build:SCRIPT ''
        ;;
    esac

    if [ "$os_arch" = "aarch64" ]; then
      case "$os_name" in
        "ios"|"tvos"|"watchos")
          # PATCH
          __build:SCRIPT '  # https://gitlab.torproject.org/tpo/core/tor/-/issues/40903'
          __build:SCRIPT '  sed -i "s+__builtin___clear_cache((void\*)code, (void\*)pos);+return true;+" "src/ext/equix/hashx/src/compiler_a64.c"'
          __build:SCRIPT ''
          ;;
      esac
    fi

    __build:SCRIPT "  ./autogen.sh > \"\$DIR_SCRIPT/$tor_target/logs/autogen.log\" 2> \"\$DIR_SCRIPT/$tor_target/logs/autogen.err\""

    if [ "$tor_target" = "tor" ]; then
      conf_out="$CONF_TOR"
    else
      conf_out="$CONF_TOR_GPL"
    fi

    __build:SCRIPT "  $conf_out > \"\$DIR_SCRIPT/$tor_target/logs/configure.log\" 2> \"\$DIR_SCRIPT/$tor_target/logs/configure.err\""
    __build:SCRIPT ''
    __build:SCRIPT '  make clean > /dev/null 2>&1'
    __build:SCRIPT "  make -j\"\$NUM_JOBS\" > \"\$DIR_SCRIPT/$tor_target/logs/make.log\" 2> \"\$DIR_SCRIPT/$tor_target/logs/make.err\""
    __build:SCRIPT "  make install >> \"\$DIR_SCRIPT/$tor_target/logs/make.log\" 2>> \"\$DIR_SCRIPT/$tor_target/logs/make.err\""
    __build:SCRIPT ''
    __build:SCRIPT "  cp -a \"src/feature/api/tor_api.h\" \"\$DIR_SCRIPT/$tor_target/include\""
    __build:SCRIPT "  cp -a \"orconfig.h\" \"\$DIR_SCRIPT/$tor_target/include\""
    __build:SCRIPT "  cp -a \"libtor.a\" \"\$DIR_SCRIPT/$tor_target/lib\""
    __build:SCRIPT ''
    __build:SCRIPT "  sed -i \"s+BUILDDIR \\\"\$(pwd)\\\"+BUILDDIR \\\"\$DIR_EXTERNAL/tor\\\"+\" \"\$DIR_SCRIPT/$tor_target/include/orconfig.h\""
    __build:SCRIPT "  sed -i \"s+SRCDIR \\\"\$(pwd)\\\"+SRCDIR \\\"\$DIR_EXTERNAL/tor\\\"+\" \"\$DIR_SCRIPT/$tor_target/include/orconfig.h\""

    __build:SCRIPT '}'
  done
  unset conf_out
  unset tor_target

  __build:SCRIPT '
readonly REBUILD="$(if [ "$1" = "--rebuild" ]; then echo "true"; else echo "false"; fi)"

needs_execution() {
  if $REBUILD; then return 0; fi

  _root="$1"; shift
  _project="$1"; shift
  _filepath=

  for _filepath in "$@"; do
    if [ ! -f "$_root/$_project/$_filepath" ]; then
      unset _root
      unset _project
      unset _filepath
      return 0
    fi
  done

  echo "    - Found $_project >> $*"

  unset _root
  unset _project
  unset _filepath
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

if needs_execution "$DIR_SCRIPT" "zlib" "lib/libz.a"; then
  compile_zlib
fi
if needs_execution "$DIR_SCRIPT" "xz" "lib/liblzma.a"; then
  compile_xz
fi
if needs_execution "$DIR_SCRIPT" "openssl" "lib/libcrypto.a" "lib/libssl.a"; then
  compile_openssl
fi
if needs_execution "$DIR_SCRIPT" "libevent" "lib/libevent.a"; then
  compile_libevent
fi
if needs_execution "$DIR_SCRIPT" "tor" "lib/libtor.a" "include/orconfig.h" "include/tor_api.h"; then
  compile_tor
fi
if needs_execution "$DIR_SCRIPT" "tor-gpl" "lib/libtor.a" "include/orconfig.h" "include/tor_api.h"; then
  compile_tor_gpl
fi
'

  __build:configure:target:finalize:output:shared
  __build:configure:target:finalize:output:static

  mkdir -p "$DIR_BUILD"
  echo "$CONF_SCRIPT" > "$DIR_BUILD/build.sh"
  chmod +x "$DIR_BUILD/build.sh"
}

function __build:configure:target:finalize:output:shared {
  __util:require:var_set "$os_arch" "os_arch"
  __util:require:var_set "$os_name" "os_name"
  __util:require:var_set "$DIR_OUT_SUFFIX" "DIR_OUT_SUFFIX"

  local exec_name="tor"
  # NOTE: Android API 23 and below will still need LD_LIBRARY_PATH set
  local exec_ldflags="-Wl,-rpath,'\$ORIGIN'"

  local jni_java_version="java8"

  local shared_name="libtor.so"
  local shared_cflags="-shared"
  local shared_ldadd="-ldl -lm -pthread"

  local strip_flags="-D"

  case "$os_name" in
    "android")
      exec_name="libtorexec.so"
      jni_java_version="java6"
      shared_cflags+=" -I\$CROSS_ROOT/sysroot/usr/include -Wl,-soname,$shared_name"
      ;;
    "linux")
      # Defaults
      ;;
    "macos")
      exec_ldflags=""
      shared_name="libtor.dylib"
      shared_cflags="-dynamiclib -install_name @executable_path/$shared_name"
      shared_ldadd=""
      strip_flags+="u"
      if [ "$os_subtype" != "-lts" ]; then
        unset jni_java_version
      fi
      ;;
    "mingw")
      exec_name="tor.exe"
      # So if tor.exe is clicked on, it opens in console.
      # This is the same behavior as the tor.exe output by
      # tor-browser-build.
      exec_ldflags+=" -Wl,--subsystem,console"
      shared_name="tor.dll"
      shared_ldadd="-lws2_32 -lcrypt32 -lshlwapi -liphlpapi"
      ;;
    "ios"|"tvos"|"watchos")
      return 0
      ;;
    *)
      __util:error "Unknown os_name[$os_name]"
      ;;
  esac

  __util:require:var_set "$exec_name" "exec_name"
  __util:require:var_set "$shared_name" "shared_name"
  __util:require:var_set "$shared_cflags" "shared_cflags"
  __util:require:var_set "$strip_flags" "strip_flags"

  __build:SCRIPT 'compile_shared() {'
  __build:SCRIPT '  echo "    * Compiling shared-$1 (shared library + linked executable)"'
  __build:SCRIPT ''
  __build:SCRIPT '  rm -rf "$DIR_SCRIPT/shared-$1"'
  __build:SCRIPT "  rm -rf \"\$DIR_EXTERNAL/build/out/\$1/$DIR_OUT_SUFFIX\""
  __build:SCRIPT '  mkdir "$DIR_TMP/shared-$1"'
  __build:SCRIPT '  mkdir -p "$DIR_SCRIPT/shared-$1/bin"'
  __build:SCRIPT '  cd "$DIR_TMP/shared-$1"'
  __build:SCRIPT ''
  __build:SCRIPT '  cp -a "$DIR_EXTERNAL/tor/src/app/main/tor_main.c" "$DIR_TMP/shared-$1"'
  __build:SCRIPT '  cp -a "$DIR_SCRIPT/$1/include/orconfig.h" "$DIR_TMP/shared-$1"'
  __build:SCRIPT ''

  if [ -n "$jni_java_version" ]; then
    __build:SCRIPT '  cp -a "$DIR_EXTERNAL/jni/tor_api-jni.c" "$DIR_TMP/shared-$1"'
    __build:SCRIPT '  cp -a "$DIR_EXTERNAL/jni/tor_api-jni.h" "$DIR_TMP/shared-$1"'
    __build:SCRIPT "  \$CC -I\${JNI_H}/$jni_java_version/include -I\$DIR_SCRIPT/\$1/include \$CFLAGS \\"
    __build:SCRIPT '    -c tor_api-jni.c'
    __build:SCRIPT ''
  fi

  __build:SCRIPT "  \$CC \$CFLAGS $shared_cflags \\"
  __build:SCRIPT "    -o $shared_name \\"

  if [ -n "$jni_java_version" ]; then
    # Cannot include in windows tor.dll because tor_api.h overrides
    # tor_main and linking tor.exe against it will fail. Need to
    # compile and load separately.
    if [ "$os_name" != "mingw" ]; then
      __build:SCRIPT '    tor_api-jni.o \'
    fi
  fi

  if [ "$os_name" = "macos" ]; then
    __build:SCRIPT '    $LDFLAGS -Wl,-force_load \'
  else
    __build:SCRIPT '    $LDFLAGS -Wl,--whole-archive \'
  fi

  # Apple targets require absolute paths when using -force_load
  __build:SCRIPT '    "$DIR_SCRIPT/$1/lib/libtor.a" \'
  __build:SCRIPT '    "$DIR_SCRIPT/zlib/lib/libz.a" \'
  __build:SCRIPT '    "$DIR_SCRIPT/xz/lib/liblzma.a" \'
  __build:SCRIPT '    "$DIR_SCRIPT/openssl/lib/libssl.a" \'
  __build:SCRIPT '    "$DIR_SCRIPT/openssl/lib/libcrypto.a" \'
  __build:SCRIPT '    "$DIR_SCRIPT/libevent/lib/libevent.a" \'

  if [ "$os_name" = "macos" ]; then
    __build:SCRIPT "    \$LDFLAGS $shared_ldadd"
  else
    __build:SCRIPT "    -Wl,--no-whole-archive \$LDFLAGS $shared_ldadd"
  fi

  __build:SCRIPT ''
  __build:SCRIPT '  $CC $CFLAGS tor_main.c \'
  __build:SCRIPT "    -o $exec_name $exec_ldflags \\"
  __build:SCRIPT "    $shared_name"
  __build:SCRIPT ''

  if [ "$os_name" = "mingw" ]; then
    __build:SCRIPT "  \$CC $shared_cflags \$CFLAGS tor_api-jni.o \\"
    __build:SCRIPT '    -o torjni.dll \'
    __build:SCRIPT '    $LDFLAGS \'
    __build:SCRIPT "    $shared_name"
    __build:SCRIPT ''
    __build:SCRIPT '  cp -a torjni.dll "$DIR_SCRIPT/shared-$1/bin"'
  fi

  __build:SCRIPT "  cp -a $shared_name \"\$DIR_SCRIPT/shared-\$1/bin\""
  __build:SCRIPT "  cp -a $exec_name \"\$DIR_SCRIPT/shared-\$1/bin\""
  __build:SCRIPT '}'

  local needs_execution_items="\"bin/$exec_name\""
  needs_execution_items+=" \"bin/$shared_name\""

  if [ "$os_name" = "mingw" ]; then
    needs_execution_items+=' "bin/torjni.dll"'
  fi

  __build:SCRIPT "
if needs_execution \"\$DIR_SCRIPT\" \"shared-tor\" $needs_execution_items; then
  compile_shared \"tor\"
fi
if needs_execution \"\$DIR_SCRIPT\" \"shared-tor-gpl\" $needs_execution_items; then
  compile_shared \"tor-gpl\"
fi
"

  __build:SCRIPT 'install_shared() {'
  __build:SCRIPT '  echo "    * Installing compilations from shared-$1/bin"'
  __build:SCRIPT ''
  __build:SCRIPT '  cd "$DIR_EXTERNAL/build"'
  __build:SCRIPT "  _bin=\"stage/$DIR_OUT_SUFFIX/shared-\$1/bin\""
  __build:SCRIPT "  _out=\"out/\$1/$DIR_OUT_SUFFIX\""
  __build:SCRIPT ''
  __build:SCRIPT '  rm -rf "$_out"'
  __build:SCRIPT '  mkdir -p "$_out"'
  __build:SCRIPT ''
  __build:SCRIPT "  cp -a \"\$_bin/$shared_name\" \"\$_out\""
  __build:SCRIPT "  cp -a \"\$_bin/$exec_name\" \"\$_out\""
  __build:SCRIPT "  cp -aR \"stage/$DIR_OUT_SUFFIX/\$1/include\" \"\$_out\""

  if [ "$os_name" = "mingw" ]; then
    __build:SCRIPT '  cp -a "$_bin/torjni.dll" "$_out"'
    __build:SCRIPT ''
    __build:SCRIPT "  \$STRIP $strip_flags \"\$_out/torjni.dll\" 2>/dev/null"
  else
    __build:SCRIPT ''
  fi

  __build:SCRIPT "  \$STRIP $strip_flags \"\$_out/$shared_name\" 2>/dev/null"
  __build:SCRIPT "  \$STRIP $strip_flags \"\$_out/$exec_name\" 2>/dev/null"
  __build:SCRIPT ''

  if [ "$os_name" = "android" ]; then
    __build:SCRIPT "  _out_linux=\"out/\$1/linux-android/$os_arch\""
    __build:SCRIPT '  rm -rf "$_out_linux"'
    __build:SCRIPT '  mkdir -p "$_out_linux"'
    __build:SCRIPT "  cp -a \"\$_out/$shared_name\" \"\$_out_linux\""
    __build:SCRIPT "  cp -a \"\$_out/$exec_name\" \"\$_out_linux/tor\""
    __build:SCRIPT '  cp -aR "$_out/include" "$_out_linux"'
    __build:SCRIPT '  unset _out_linux'
    __build:SCRIPT '  sleep 1'
    __build:SCRIPT ''
  fi

  __build:SCRIPT "  echo \"        STRIP[\$STRIP $strip_flags]\""
  __build:SCRIPT "  echo \"        BIN: \$(du -sh \"\$_bin/$shared_name\" | cut -d 's' -f 1) \$(cd \"\$_bin\" && sha256sum \"$shared_name\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/$shared_name\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"$shared_name\")\""
  __build:SCRIPT "  echo \"        BIN: \$(du -sh \"\$_bin/$exec_name\" | cut -d 's' -f 1) \$(cd \"\$_bin\" && sha256sum \"$exec_name\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/$exec_name\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"$exec_name\")\""

  if [ "$os_name" = "mingw" ]; then
    __build:SCRIPT "  echo \"        BIN: \$(du -sh \"\$_bin/torjni.dll\" | cut -d 's' -f 1) \$(cd \"\$_bin\" && sha256sum \"torjni.dll\")\""
    __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/torjni.dll\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"torjni.dll\")\""
  fi

  __build:SCRIPT ''
  __build:SCRIPT '  unset _bin'
  __build:SCRIPT '  unset _out'
  __build:SCRIPT '}'

  needs_execution_items="\"$shared_name\""

  if [ "$os_name" = "mingw" ]; then
    needs_execution_items+=' "torjni.dll"'
  fi

  needs_execution_items+=' "include/orconfig.h"'
  needs_execution_items+=' "include/tor_api.h"'

  __build:SCRIPT "
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor/$DIR_OUT_SUFFIX\" \"$exec_name\" $needs_execution_items; then
  install_shared \"tor\"
fi
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor-gpl/$DIR_OUT_SUFFIX\" \"$exec_name\" $needs_execution_items; then
  install_shared \"tor-gpl\"
fi"

  if [ "$os_name" = "android" ]; then
    __build:SCRIPT "
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor/linux-$os_name/$os_arch\" \"tor\" $needs_execution_items; then
  install_shared \"tor\"
fi
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor-gpl/linux-$os_name/$os_arch\" \"tor\" $needs_execution_items; then
  install_shared \"tor-gpl\"
fi"
  fi
}

function __build:configure:target:finalize:output:static {
  __util:require:var_set "$os_arch" "os_arch"
  __util:require:var_set "$os_name" "os_name"
  __util:require:var_set "$DIR_OUT_SUFFIX" "DIR_OUT_SUFFIX"

  case "$os_name" in
    "ios"|"tvos"|"watchos")
      ;;
    "android"|"linux"|"macos"|"mingw")
      return 0
      ;;
    *)
      __util:error "Unknown os_name[$os_name]"
      ;;
  esac

  __build:SCRIPT 'install_static() {'
  __build:SCRIPT '  echo "    * Installing static compilations for $1"'
  __build:SCRIPT ''
  __build:SCRIPT '  cd "$DIR_EXTERNAL/build"'
  __build:SCRIPT "  _out=\"out/\$1/$DIR_OUT_SUFFIX\""
  __build:SCRIPT ''
  __build:SCRIPT '  rm -rf "$_out"'
  __build:SCRIPT '  mkdir -p "$_out"'
  __build:SCRIPT ''
  __build:SCRIPT "  cp -a \"stage/$DIR_OUT_SUFFIX/zlib/lib/libz.a\" \"\$_out\""
  __build:SCRIPT "  cp -a \"stage/$DIR_OUT_SUFFIX/xz/lib/liblzma.a\" \"\$_out\""
  __build:SCRIPT "  cp -a \"stage/$DIR_OUT_SUFFIX/openssl/lib/libcrypto.a\" \"\$_out\""
  __build:SCRIPT "  cp -a \"stage/$DIR_OUT_SUFFIX/openssl/lib/libssl.a\" \"\$_out\""
  __build:SCRIPT "  cp -a \"stage/$DIR_OUT_SUFFIX/libevent/lib/libevent.a\" \"\$_out\""
  __build:SCRIPT "  cp -a \"stage/$DIR_OUT_SUFFIX/\$1/lib/libtor.a\" \"\$_out\""
  __build:SCRIPT "  cp -aR \"stage/$DIR_OUT_SUFFIX/\$1/include\" \"\$_out\""
  __build:SCRIPT ''
  __build:SCRIPT '  llvm-objcopy --enable-deterministic-archives "$_out/libz.a"'
  __build:SCRIPT '  llvm-objcopy --enable-deterministic-archives "$_out/liblzma.a"'
  __build:SCRIPT '  llvm-objcopy --enable-deterministic-archives "$_out/libcrypto.a"'
  __build:SCRIPT '  llvm-objcopy --enable-deterministic-archives "$_out/libssl.a"'
  __build:SCRIPT '  llvm-objcopy --enable-deterministic-archives "$_out/libevent.a"'
  __build:SCRIPT '  llvm-objcopy --enable-deterministic-archives "$_out/libtor.a"'
  __build:SCRIPT ''
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/libz.a\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"libz.a\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/liblzma.a\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"liblzma.a\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/libcrypto.a\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"libcrypto.a\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/libssl.a\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"libssl.a\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/libevent.a\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"libevent.a\")\""
  __build:SCRIPT "  echo \"        OUT: \$(du -sh \"\$_out/libtor.a\" | cut -d 'o' -f 1) \$(cd \"\$_out\" && sha256sum \"libtor.a\")\""
  __build:SCRIPT ''
  __build:SCRIPT '  unset _out'
  __build:SCRIPT '}'

  __build:SCRIPT "
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor/$DIR_OUT_SUFFIX\" \"libz.a\" \"liblzma.a\" \"libcrypto.a\" \"libssl.a\" \"libevent.a\" \"libtor.a\" \"include/orconfig.h\" \"include/tor_api.h\"; then
  install_static \"tor\"
fi
if needs_execution \"\$DIR_EXTERNAL/build\" \"out/tor-gpl/$DIR_OUT_SUFFIX\" \"libz.a\" \"liblzma.a\" \"libcrypto.a\" \"libssl.a\" \"libevent.a\" \"libtor.a\" \"include/orconfig.h\" \"include/tor_api.h\"; then
  install_static \"tor-gpl\"
fi"
}

function __build:docker:execute {
  __build:configure:target:finalize

  if $DRY_RUN; then
    echo "
    Build Script >> $DIR_BUILD/build.sh"
  fi

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
      __docker:build "$DIR_TASK/docker/Dockerfile.$os_name.base" \
        "05nelsonm/build-env.$os_name.base" \
        "$DIR_TASK/docker"

      __docker:build "$DIR_TASK/docker/Dockerfile.$os_name$os_subtype.$docker_arch" \
        "05nelsonm/build-env.$os_name$os_subtype.$docker_arch" \
        "$DIR_TASK/docker"
      ;;
  esac

  local rebuild=""
  if $REBUILD; then
    rebuild="--rebuild"
  fi

  __docker:run "$DIR_TASK" \
    "05nelsonm/build-env.$os_name$os_subtype.$docker_arch" \
    "./$DIR_BUILD/build.sh" "$rebuild"

  trap - SIGINT
}

function __build:cleanup {
  rm -rf "$FILE_BUILD_LOCK"
  __util:git:stash "libevent"
  __util:git:stash "openssl"
  __util:git:stash "tor"
  __util:git:stash "xz"
  __util:git:stash "zlib"
}
