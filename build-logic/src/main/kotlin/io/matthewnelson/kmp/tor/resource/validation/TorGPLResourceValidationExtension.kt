/*
 * Copyright (c) 2023 Matthew Nelson
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
package io.matthewnelson.kmp.tor.resource.validation

import org.gradle.api.Project
import javax.inject.Inject

/**
 * GPL build of `tor` is structurally identical to the non-GPL build. The only
 * difference is the hash values and the [moduleName] for which the resource
 * src directory is derived from the calling project.
 * */
abstract class TorGPLResourceValidationExtension @Inject internal constructor(
    project: Project
): TorResourceValidationExtension(project, moduleName = "resource-tor-gpl") {

    override val hashAndroidAarch64: String = "c8d6e2aa51b90a3406364254e2af2fa507fc23f96a9afb9bc230a1fecc9b968f"
    override val hashAndroidArmv7: String = "c8c1e0b9a436fa293733a83e060015e6e5f16290d471fbe714b874e6e18e173a"
    override val hashAndroidX86: String = "c3a0da8a446c1c05b8baaeabef719408d2d226cb55c86f71ee4cfe005dc41b2a"
    override val hashAndroidX86_64: String = "8d4fdcd96849c929ae302bce9dc01e68c74212c677af5bd11de73ce65b8ced21"

    override val hashJvmLinuxAndroidAarch64: String = "b23c860fe832b6055eb24fb523e3568dee2305948034d0b957348882f8c47fb2"
    override val hashJvmLinuxAndroidArmv7: String = "c12d75d5ccab45ef2e44dd73c5bcd835d97e5e965eea4cfa97359ab21cc52921"
    override val hashJvmLinuxAndroidX86: String = "dfe0a718f14c8dbe320231238654518dd600828963ba40956f4f5b902ebee3cf"
    override val hashJvmLinuxAndroidX86_64: String = "b4da1567a272ef6f4674e22a6168bdee3d0482cebb1a13db7c10ff9d9c888434"

    override val hashJvmLinuxLibcAarch64: String = "c8492f1e2bdbbe25d8b9787fd571d17d557ff5c157dca7db5dc3b2bb181185ec"
    override val hashJvmLinuxLibcArmv7: String = "4c4d9fc5c55e5189773f690e5fa6bea23806370c9e90eadf5a7e2befb2f07908"
    override val hashJvmLinuxLibcPpc64: String = "4af1becaf64483ca55ab8605fbee93a7c54869e4af5726cef0f238786fd969f1"
    override val hashJvmLinuxLibcX86: String = "3caa33ac51195495163e0ffcc714185b638e4532ce4d807ff2525dcd6a5d4acb"
    override val hashJvmLinuxLibcX86_64: String = "ffdb6866ba86f7011f77459f4772ef6c3f857bd9362acec345a306141f9a4fe3"

    override val hashJvmMacosAarch64: String = "e284d16d584661599b8bd15a3b8700c989c184a418ff2370d6c01652723d61c3"
    override val hashJvmMacosX86_64: String = "d63565a6e1129e7bdaff6fcde295df7ab528c77b3699ca13655bdd74b52921a7"

    override val hashJvmMingwX86: String = "402e1717f736ff4e7c4e9387a1d91fc98515129ec9c2360591d6c816beb547b7"
    override val hashJvmMingwX86_64: String = "7a75807309c18dddd081384f86dd4b70e777f6008387d21aa04e019b8afdf4c5"

    override val hashNativeIosArm64: String = "cdf12383215622a187099dab9632b1b0bcd30e28663ac76fea86f7b6f9543eb7"
    override val hashNativeIosSimulatorArm64: String = "b578c457178855a4d3e1a3b3ddc3e9337a146fcbd90144c796df617c76c7ab7b"
    override val hashNativeIosX64: String = "6eda719e8ad858772ae623672a45cbc447175cbbaf8a96e8f82357339dee74df"
    override val hashNativeLinuxArm64: String = "c8492f1e2bdbbe25d8b9787fd571d17d557ff5c157dca7db5dc3b2bb181185ec"
    override val hashNativeLinuxX64: String = "ffdb6866ba86f7011f77459f4772ef6c3f857bd9362acec345a306141f9a4fe3"
    override val hashNativeMacosArm64: String = "b578c457178855a4d3e1a3b3ddc3e9337a146fcbd90144c796df617c76c7ab7b"
    override val hashNativeMacosX64: String = "6eda719e8ad858772ae623672a45cbc447175cbbaf8a96e8f82357339dee74df"
    override val hashNativeMingwX64: String = "7a75807309c18dddd081384f86dd4b70e777f6008387d21aa04e019b8afdf4c5"

    internal companion object {
        internal const val NAME = "torGPLResourceValidation"
    }
}
