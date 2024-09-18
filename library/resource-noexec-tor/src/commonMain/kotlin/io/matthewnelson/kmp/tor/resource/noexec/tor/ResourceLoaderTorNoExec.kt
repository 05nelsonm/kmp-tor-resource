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
@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package io.matthewnelson.kmp.tor.resource.noexec.tor

import io.matthewnelson.kmp.tor.common.api.InternalKmpTorApi
import io.matthewnelson.kmp.tor.common.api.ResourceLoader

// commonMain
public expect class ResourceLoaderTorNoExec: ResourceLoader.Tor.NoExec {

    @OptIn(InternalKmpTorApi::class)
    @Throws(IllegalStateException::class)
    @Suppress("ConvertSecondaryConstructorToPrimary", "UnnecessaryOptInAnnotation", "unused")
    private constructor()
}
