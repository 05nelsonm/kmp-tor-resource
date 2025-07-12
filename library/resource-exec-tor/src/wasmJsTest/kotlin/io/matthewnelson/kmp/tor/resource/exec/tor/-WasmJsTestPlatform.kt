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
@file:Suppress("PropertyName")

package io.matthewnelson.kmp.tor.resource.exec.tor

import io.matthewnelson.kmp.file.AccessDeniedException
import io.matthewnelson.kmp.file.DelicateFileApi
import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.SysFsInfo
import io.matthewnelson.kmp.file.jsExternTryCatch
import io.matthewnelson.kmp.file.toIOException
import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.core.OSHost
import io.matthewnelson.kmp.tor.common.core.OSInfo

actual fun File.isExecutable(): Boolean {
    val fs = node_fs

    return try {
        @OptIn(DelicateFileApi::class)
        jsExternTryCatch { fs.accessSync(toString(), fs.constants.X_OK) }
        true
    } catch (t: Throwable) {
        val e = t.toIOException(this)
        if (e is AccessDeniedException) return false
        throw e
    }
}

@OptIn(InternalKmpTorApi::class)
actual val IS_WINDOWS: Boolean by lazy {
    OSInfo.INSTANCE.osHost is OSHost.Windows
}

private val node_fs by lazy {
    if (SysFsInfo.name != "FsJsNode") throw UnsupportedOperationException("FileSystem is not FsJsNode...")
    nodeModuleFs()
}

private fun nodeModuleFs(): ModuleFs = js("eval('require')('fs')")

private external interface ModuleFs {
    fun accessSync(path: String, mode: Int)

    val constants: ConstantsFs
}

private external interface ConstantsFs {
    val X_OK: Int
}
