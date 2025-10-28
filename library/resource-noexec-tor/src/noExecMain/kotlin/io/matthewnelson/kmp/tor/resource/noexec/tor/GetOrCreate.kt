/*
 * Copyright (c) 2025 Matthew Nelson
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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.file.File
import io.matthewnelson.kmp.file.IOException
import io.matthewnelson.kmp.file.absoluteFile2
import io.matthewnelson.kmp.tor.common.api.ResourceLoader
import io.matthewnelson.kmp.tor.common.api.TorApi

// noExecMain
/**
 * See [ResourceLoaderTorNoExec]
 * */
public actual abstract class GetOrCreate internal actual constructor() {

    /**
     * Creates a new instance of [ResourceLoaderTorNoExec] with provided [resourceDir]. If
     * an instance of [ResourceLoader.Tor] already exists, that will be returned instead.
     *
     * @param [resourceDir] The directory to extract resources to.
     *
     * @throws [IOException] If [absoluteFile2] has to reference the filesystem to construct
     *   an absolute path and fails due to a filesystem security exception.
     * */
    public abstract fun getOrCreate(
        resourceDir: File
    ): ResourceLoader.Tor

    /**
     * DEPRECATED
     *
     * Creates a new instance of [ResourceLoaderTorNoExec] with provided [resourceDir]. If
     * an instance of [ResourceLoader.Tor] already exists, that will be returned instead.
     *
     * @param [resourceDir] The directory to extract resources to.
     * @param [registerShutdownHook] If `true`, a shutdown hook will be registered for Jvm/Android
     *   which will call [TorApi.terminateAndAwaitResult].
     *
     * @throws [IOException] If [absoluteFile2] has to reference the filesystem to construct
     *   an absolute path and fails due to a filesystem security exception.
     * @suppress
     * */
    @Deprecated(
        message = "ShutdownHook registration causes abnormal application exit behavior for Java/Android",
        level = DeprecationLevel.WARNING,
    )
    public abstract fun getOrCreate(
        resourceDir: File,
        registerShutdownHook: Boolean,
    ): ResourceLoader.Tor
}
