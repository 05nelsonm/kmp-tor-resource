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

package io.matthewnelson.kmp.tor.resource.noexec.tor.internal

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.Resource
import io.matthewnelson.kmp.tor.resource.lib.tor.configureNoExecTorResource
import io.matthewnelson.kmp.tor.resource.noexec.tor.AbstractKmpTorApi.Companion.ALIAS_LIBTORJNI
import io.matthewnelson.kmp.tor.resource.noexec.tor.ResourceLoaderTorNoExec
import kotlin.Throws

@Suppress("NOTHING_TO_INLINE")
@OptIn(InternalKmpTorApi::class)
internal actual inline fun Resource.Config.Builder.configureLibTorResources() {
    configureNoExecTorResource(
        aliasLibTor = ALIAS_LIBTOR,
        aliasLibTorJni = ALIAS_LIBTORJNI,
        loaderClass = ResourceLoaderTorNoExec::class.java,
    )
}

@Suppress("NOTHING_TO_INLINE")
@Throws(IllegalStateException::class)
internal actual inline fun Map<String, File>.findLibs(): Map<String, File> = this
