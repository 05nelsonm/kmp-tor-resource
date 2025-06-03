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
 * Returns NULL on successful startup. Otherwise, an error message.
 *
 * After successful startup, `kmp_tor_stop_stage1_interrupt_and_await_result` can be called
 * to interrupt tor's main loop, followed by `kmp_tor_stop_stage2_post_thread_exit_cleanup`
 * after the thread calling this function has exited.
 **/
const char *kmp_tor_run_blocking(const char *lib_tor, int argc, char *argv[]);

/**
 * Returns current state.
 *  - (0) KMP_TOR_STATE_OFF:       Nothing happening. Free to call `kmp_tor_run_blocking`.
 *  - (1) KMP_TOR_STATE_STARTING:  `kmp_tor_run_blocking` is mid-execution. State will
 *                                 either move into KMP_TOR_STATE_OFF and an error message
 *                                 will be returned, or it will move to KMP_TOR_STATE_STARTED
 *                                 whereby no errors occurred and `tor_run_main` has been called.
 *  - (2) KMP_TOR_STATE_STARTED:   `tor_run_main` is executing
 *  - (3) KMP_TOR_STATE_STOPPED:   `tor_run_main` returned. `kmp_tor_stop_stage1_interrupt_and_await_result`
 *                                 is ready to be called to retrieve the result, followed by a final call
 *                                 to `kmp_tor_stop_stage2_post_thread_exit_cleanup` after the thread which
 *                                 `kmp-tor_run_blocking` was called on has exited.
 **/
int kmp_tor_state();

/**
 * A "Stage 1" of a shutdown sequence for tor. If tor's main loop is running, it will interrupt it
 * and wait for it's result. This MUST be called before `kmp_tor_stop_stage2_post_thread_exit_cleanup`
 * can be called.
 *
 * Returns the result of `tor_run_main` on success (0-255), or -1 on failure. Failure can be a result
 * of `kmp_tor_state` was KMP_TOR_STATE_OFF, or this function was called from multiple threads and
 * was "beat out" by another thread.
 **/
int kmp_tor_stop_stage1_interrupt_and_await_result();

/**
 * A "Stage 2" of a shutdown sequence for tor. After `kmp_tor_stop_stage1_interrupt_and_await_result`
 * has returned successfully and then the thread for which `kmp_tor_run_blocking` was called from
 * has exited, this should be called in order to finalize cleanup and set `kmp_tor_state` to
 * KMP_TOR_STATE_OFF.
 *
 * The reason for this function is that tor and some of its dependencies use pthread_key for cleaning
 * up thread local resources via its deconstructor. Only after that has occurred can the library be
 * safely unloaded via dlclose/FreeLibrary.
 *
 * Returns 0 on success, or -1 on failure. Failure can be a result of `kmp_tor_stop_stage1_interrupt_and_await_result`
 * not being called yet.
 * */
int kmp_tor_stop_stage2_post_thread_exit_cleanup();

#endif /* !defined(KMP_TOR_H) */
