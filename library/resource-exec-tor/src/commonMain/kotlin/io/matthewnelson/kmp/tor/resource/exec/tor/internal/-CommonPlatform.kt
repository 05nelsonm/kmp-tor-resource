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
@file:Suppress("KotlinRedundantDiagnosticSuppress")

package io.matthewnelson.kmp.tor.resource.exec.tor.internal

import io.matthewnelson.kmp.file.File

internal const val ALIAS_TOR: String = "tor"
internal const val ALIAS_LIB_TOR: String = "libtor"

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal expect inline fun Map<String, File>.findLibTorExec(): Map<String, File>
