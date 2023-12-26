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

    override val hashAndroidAarch64: String = "d0240feea2b4cf97684e115ead4e05a13548bd5ee25cff75e7cd47fb743e0883"
    override val hashAndroidArmv7: String = "ee3af3336d0dce5d8b7f9f40ad3ec824699f3bc101ca82abd591351d589f6e85"
    override val hashAndroidX86: String = "e901e6558a331bb57088a3212b7f0596c5f5f93d10b31ea239fe26f5eb96da75"
    override val hashAndroidX86_64: String = "db66ec03f4829437723c6aaa8ad5880717c8d601a9c36b5423b9ed138931b255"

    override val hashJvmLinuxAndroidAarch64: String = "5fc412d5151d267de744fbe6595d149af837bc368a603cd64510e23cf7fe76f2"
    override val hashJvmLinuxAndroidArmv7: String = "0ef42bf73bb5a1d28e2fb9faef31032edf7d382b246d8eea92e59d8cb5c862b7"
    override val hashJvmLinuxAndroidX86: String = "ed6e59d7879cd602aaeebc9407bbdc92134de241d14627f4fe851ed5fc66ed57"
    override val hashJvmLinuxAndroidX86_64: String = "d0fadd15b40e294a3cd25cafd9cffd997fd2f63f1563e53363d19ca13222924a"

    override val hashJvmLinuxLibcAarch64: String = "d3ed2d62992650c7c9aa71a4ad68cb16741f7019e63ef8c90298fd2180717beb"
    override val hashJvmLinuxLibcArmv7: String = "f33b71e8460d64a7a2d861260af354d3a9ac43c8ebfc37f5c242808949ed19a6"
    override val hashJvmLinuxLibcPpc64: String = "a40db819557339116438d9fd29b1bc8309482516bd78de3173d18bf2d8deace0"
    override val hashJvmLinuxLibcX86: String = "048f2bda2fe6000a7b5e5eb3ff110966305474e28911a67070d0c33e06bcd874"
    override val hashJvmLinuxLibcX86_64: String = "3abc2786481d8fb266c1a8f0949e0405255eed17c43bbeb3c3726c3ca44bf61c"

    override val hashJvmMacosAarch64: String = "7ae99cab7a67bb403bf246e41c119fb441eef67616af8892af636eb5f680512b"
    override val hashJvmMacosX86_64: String = "fdb3b8d27ee70c8b11efe7ab46770d1b91da1dc4d4d1357e76c0c3550727b8b3"

    override val hashJvmMingwX86: String = "74f20267ff849eea2fc6fc8560f18bcd9c3d457931d783e99b00bb8a2bb110e4"
    override val hashJvmMingwX86_64: String = "3f4c7584e03b652b7322de396a46c5071809f78cbe1f280110d57c3d3800d97a"

    override val hashNativeIosArm64: String = "03ba3afd7ed9efde471de31aa29b26afabdf7bfeab96a3d58ea30f700f291e4e"
    override val hashNativeIosSimulatorArm64: String = "8dc60fbcafad65c935643d51189cfb5736de5cc8d908baba0397155fd0e880c4"
    override val hashNativeIosX64: String = "ae3b3bb2a11f4e431c7d6623e1d8b52b0f42c14b65a1215652f8b04740d90f04"
    override val hashNativeLinuxArm64: String = "d3ed2d62992650c7c9aa71a4ad68cb16741f7019e63ef8c90298fd2180717beb"
    override val hashNativeLinuxX64: String = "3abc2786481d8fb266c1a8f0949e0405255eed17c43bbeb3c3726c3ca44bf61c"
    override val hashNativeMacosArm64: String = "f41d0c0b92f1c65300273d7c220b979f7aceedd5351d8b177f3e8b1b9afd9434"
    override val hashNativeMacosX64: String = "40c18eabb64654fd21ee3326ca5a7c067a3c54914f67da8249bb261b98335a7f"
    override val hashNativeMingwX64: String = "3f4c7584e03b652b7322de396a46c5071809f78cbe1f280110d57c3d3800d97a"

    internal companion object {
        internal const val NAME = "torGPLResourceValidation"
    }
}
