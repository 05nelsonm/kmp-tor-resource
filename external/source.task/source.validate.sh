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

# Sourced by external/task.sh for all 'validate:*' tasks

function __validate:report {
  __util:require:var_set "$1" "module name"

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

# Ensure source.util.sh has been sourced, and that
# DIR_TASK is set (i.e. this was sourced from task.sh)
__util:require:file_exists "$DIR_TASK/task.sh"
