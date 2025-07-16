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

#ifndef KMP_TOR_H
#define KMP_TOR_H

#define KMP_TOR_STATE_OFF       0
#define KMP_TOR_STATE_STARTING  1
#define KMP_TOR_STATE_STARTED   2
#define KMP_TOR_STATE_STOPPED   3

/**
 *
 **/
typedef struct kmp_tor_context_t kmp_tor_context_t;

/**
 * Returns a new kmp_tor_context_t on success, or NULL on failure.
 **/
kmp_tor_context_t *kmp_tor_init();

/**
 * Deinitializes kmp_tor and free's the kmp_tor_context_t struct.
 *
 * **NOTE:** kmp_tor_terminate_and_await_result is always called prior
 * to freeing the kmp_tor_context_t.
 *
 * Returns -1 if ctx is NULL, otherwise 0.
 **/
int kmp_tor_deinit(kmp_tor_context_t *ctx);

/**
 * Returns NULL on successful startup. Otherwise, an error message.
 *
 * After successful startup, `kmp_tor_terminate_and_await_result` can be called
 * to interrupt tor's main loop.
 *
 * `kmp_tor_terminate_and_await_result` MUST be called when done to release resources.
 **/
const char *kmp_tor_run_main(kmp_tor_context_t *ctx, const char *lib_tor, int argc, char *argv[]);

/**
 * Returns current state.
 *  - (0) KMP_TOR_STATE_OFF:       Nothing happening. Free to call `kmp_tor_run_main`
 *  - (1) KMP_TOR_STATE_STARTING:  `kmp_tor_run_main` was called and awaiting return
 *  - (2) KMP_TOR_STATE_STARTED:   tor's `tor_run_main` is running in its thread
 *  - (3) KMP_TOR_STATE_STOPPED:   tor's `tor_run_main` returned and the thread is ready
 *                                 for cleanup via `kmp_tor_terminate_and_await_result`.
 *
 * If ctx is NULL, -1 is returned.
 **/
int kmp_tor_state(kmp_tor_context_t *ctx);

/**
 * This MUST be called to release resources before calling `kmp_tor_run_main` again. It
 * should be called as soon as possible (i.e. when `kmp_tor_state` is `KMP_TOR_STATE_STOPPED`).
 *
 * Returns -1 if state is `KMP_TOR_STATE_OFF` or ctx is NULL. Otherwise, will return whatever
 * `tor_run_main` completed with.
 **/
int kmp_tor_terminate_and_await_result(kmp_tor_context_t *ctx);

#endif /* !defined(KMP_TOR_H) */
