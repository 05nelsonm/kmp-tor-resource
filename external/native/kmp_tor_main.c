/*
 * Copyright (c) 2024 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

/**
 * An alternative implementation of tor's src/app/main/tor_main.c,
 * specific to how kmp-tor-resource compiles tor.
 */

int tor_main(int argc, char *argv[]);

/**
 * kmp-tor-resource compiles openssl statically with flags `no-atexit`
 * and `no-dso` such that a final call to OPENSSL_cleanup is necessary
 * after tor_main returns.
 */
void OPENSSL_cleanup(void);

int
main(int argc, char *argv[])
{
  int r;
  r = tor_main(argc, argv);
  OPENSSL_cleanup();
  if (r < 0 || r > 255) {
    return 1;
  } else {
    return r;
  }
}
