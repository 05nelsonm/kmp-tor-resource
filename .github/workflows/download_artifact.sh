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
export LC_ALL=C
set -e

# ARGS:
#  - 1: bearer token
#  - 2: name
#  - 3: artifact id

curl -L \
  -H "Accept: application/vnd.github+json" \
  -H "Authorization: Bearer $1" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "https://api.github.com/repos/05nelsonm/kmp-tor-resource/actions/artifacts/$3/zip" \
  --output "$2.zip"

unzip "$2.zip"
tar -xzvf "$2.tar.gz"

rm -f "$2.zip"
rm -f "$2.tar.gz"