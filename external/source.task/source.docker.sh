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

# Sourced by external/source.task/source.build.sh & external/source.task/source.package.sh

# NOTE: If changing, also change versions in docker/Dockerfile.*
# See https://github.com/05nelsonm/build-env
readonly TAG_DOCKER_BUILD_ENV="0.1.3"

readonly DOCKER="$(which docker)"
readonly U_ID="$(id -u)"
readonly G_ID="$(id -g)"

function __docker:build {
  __util:require:cmd "$DOCKER" "docker"
  __util:require:file_exists "$1" "[1] docker"
  __util:require:var_set "$2" "[2] build-env image (without tag)"
  __util:require:var_set "$3" "[3] docker directory"

  local cmd_build="$DOCKER build -f $1 -t $2:$TAG_DOCKER_BUILD_ENV $3"

  if $DRY_RUN; then
    echo "
    $cmd_build"
    return 0
  else
    echo ""
  fi

  ${cmd_build}
}

function __docker:run {
  local silent=false
  if [ "$1" = "--silent" ]; then
    silent=true; shift
  fi
  local dir_mount="$1"; shift
  local image="$1"; shift

  __util:require:cmd "$DOCKER" "docker"
  __util:require:var_set "$dir_mount" "[1] dir_mount"
  __util:require:var_set "$image" "[2] build-env image (without tag)"
  __util:require:var_set "$1" "at least 1 argument"
  __util:require:var_set "$U_ID" "U_ID"
  __util:require:var_set "$G_ID" "G_ID"

  local cmd_run="$DOCKER run --rm -u $U_ID:$G_ID -v $dir_mount:/work $image:$TAG_DOCKER_BUILD_ENV"

  if ! $silent; then
    echo "
    $cmd_run $*"
  fi

  if $DRY_RUN; then return 0; fi
  ${cmd_run} "$@"
}

# Ensure source.util.sh has been sourced, and that
# DIR_TASK is set (i.e. this was sourced from task.sh)
__util:require:file_exists "$DIR_TASK/task.sh"
