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

readonly FILE_BUILD_LOCK="$DIR_TASK/build/.lock"

readonly GIT="$(which git)"

function __util:error {
  echo 1>&2 "
    ERROR: $1
  "

  exit 3
}

function __util:git:apply_patches {
  __util:require:not_empty "$1" "project name must not be empty"
  local dir_current=
  dir_current="$(pwd)"
  cd "$DIR_TASK/$1"

  local patch_file=
  for patch_file in "$DIR_TASK/patches/$1/"*.patch; do
    if [ "$patch_file" = "$DIR_TASK/patches/$1/*.patch" ]; then
      # no patch files
      continue
    fi

    echo "    Applying git patch to $1 >> $patch_file"
    ${GIT} apply "$patch_file"
    sleep 0.25
  done

  cd "$dir_current"
}

function __util:git:clean {
  __util:require:not_empty "$1" "project name must not be empty"
  local dir_current=
  dir_current="$(pwd)"
  cd "$DIR_TASK/$1"

  ${GIT} clean -X --force --quiet
  cd "$dir_current"
}

function __util:git:stash {
  __util:require:not_empty "$1" "project name must not be empty"
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

function __util:require:no_build_lock {
  if [ ! -f "$FILE_BUILD_LOCK" ]; then return 0; fi

  __util:error "A build is in progress

    If this is not the case, delete the following file and re-run the task
    $FILE_BUILD_LOCK"
}

function __util:require:cmd {
  __util:require:file_exists "$1" "$2" "is required to run this script"
}

function __util:require:file_exists {
  if [ -f "$1" ]; then return 0; fi

  local _message="file does not exist"

  if [ -n "$3" ]; then
    _message="$3"
  fi
  __util:error "$2 $_message"
}

function __util:require:var_set {
  __util:require:not_empty "$1" "$2 must be set"
}

function __util:require:not_empty {
  if [ -n "$1" ]; then return 0; fi
  __util:error "$2"
}

# Ensure DIR_TASK is set (i.e. this was sourced from task.sh)
__util:require:file_exists "$DIR_TASK/task.sh"
