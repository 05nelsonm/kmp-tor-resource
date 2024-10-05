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
package resource.validation.extensions

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import resource.validation.extensions.internal.ValidationHash
import javax.inject.Inject

open class NoExecTorResourceValidationExtension private constructor(
    project: Project,
    isGpl: Boolean,
): AbstractResourceValidationExtension(
    project = project,
    moduleName = "resource-noexec-tor" + if (isGpl) "-gpl" else "",
    packageName = "io.matthewnelson.kmp.tor.resource.noexec.tor",
) {

    @Inject
    internal constructor(project: Project): this(project, isGpl = false)

    private val headerTorApi: String = "c346e767d3e6dbad44d1802579e7e4a8cf1b1ff8595152ebd4679b05d2de6df3"

    protected open val iosArm64LibTor: String = "59fb38fa1c6de14b96b0e7c3872d4cbba0c4311bc87772043e5a282917447f4f"
    protected open val iosArm64Orconfig: String = "33018b29c3757493b37356f7a6e9bdd28553cd969c471afc9ed460aa97f47219"

    protected open val iosSimulatorArm64LibTor: String = "78ad7682256f82aafecf9cbd42392768aad5cc52b0ce0074599509a5074d5650"
    protected open val iosSimulatorArm64Orconfig: String = "32a216e02257ef4fc609f8907c1ebea7e9e25f45994700683a88b9bdfd29b58c"

    protected open val iosX64LibTor: String = "7ba0abb60d45bf9a97e9b2f59bf51cfa295d05d1d08e01311b1b793f8377683c"
    protected open val iosX64Orconfig: String = "1a341137c719e33563dff813855cdbee70dd0631eec8ee938a00e68c45740675"

    private val iosArm64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libcrypto.a" to "a694ccf8b77265761bffdd6f67af89d01ba9b3c2554ef6b03d736710090b5d08",
            "libevent.a" to "f71ce25368d9b5be79a51f8f4308abcf1cf1bcd1d9d62afa8f7df04f46d894ca",
            "liblzma.a" to "661f84f07aacfa45c1311ff4a3deebc8348fa01d1311aadb0d9a6ddfb96f61fb",
            "libssl.a" to "f6f5095d9c0e06c18fb003ae555d48345f4e4c0c7832dfb4ff2f6168edfd5f9d",
            "libtor.a" to iosArm64LibTor,
            "libz.a" to "983d04947fb966eedf3e3ee519ef516a8ad0635f59fee7aea320d56f34a6f4f7",
        )
    }
    private val iosArm64Headers: List<Pair<String, String>> by lazy {
        listOf(
            "orconfig.h" to iosArm64Orconfig,
            "tor_api.h" to headerTorApi,
        )
    }
    private val iosSimulatorArm64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libcrypto.a" to "64e44ac7566ecdd01094fadc7452c6f17c018451beb70e34c3637ce5b75241d5",
            "libevent.a" to "3ba9dcce7192c48cc3f6773344eaaa30aa28106ad28be81a353f62aa8358b393",
            "liblzma.a" to "a71cc01fe1fa138de0996a5c0362d14974518b675d09e701736d679d1cc43d1c",
            "libssl.a" to "87c13859039cf8606ac073971baa346709b1bd28bbf369619dd2d32ea55ca519",
            "libtor.a" to iosSimulatorArm64LibTor,
            "libz.a" to "4204937ae806276d3647ce510a64aa7737d5ead58b198bdf54f52d137f014f01",
        )
    }
    private val iosSimulatorArm64Headers: List<Pair<String, String>> by lazy {
        listOf(
            "orconfig.h" to iosSimulatorArm64Orconfig,
            "tor_api.h" to headerTorApi,
        )
    }
    private val iosX64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libcrypto.a" to "6306b3586d87e3382e0760fa2493b4d1cb41739c3d99d83a9e6cc2e1017fca91",
            "libevent.a" to "e5b69a15a9d2c1ca0d048b81eda3bf03a506a8c3c85844e865554e8f97b61f67",
            "liblzma.a" to "814630d0933c8b852d24da9bf866d5ab8622a8f71f1875a1edfb2062eb8f55c3",
            "libssl.a" to "a7d27001f86df3b035f46502386575514b9c923ce4512bfb3d39d91d52fa1287",
            "libtor.a" to iosX64LibTor,
            "libz.a" to "71906c6823bd67d1196d50684bd0b9873674277651b868a8660581af6cc06298",
        )
    }
    private val iosX64Headers: List<Pair<String, String>> by lazy {
        listOf(
            "orconfig.h" to iosX64Orconfig,
            "tor_api.h" to headerTorApi,
        )
    }

    abstract class GPL @Inject internal constructor(
        project: Project,
    ): NoExecTorResourceValidationExtension(project, isGpl = true) {

        override val iosArm64LibTor: String = "f8f29eac977b1fb6e56b7b9e19a3dfb508e9c305c7f5c9e3151be7911782543f"
        override val iosArm64Orconfig: String = "bca48d3339b9740583d1a7b8d650ca715d96e6f267d13a9293c3f63233289391"

        override val iosSimulatorArm64LibTor: String = "30fe4104aa84d40a44f42b269395c073ff5af77086ab0c948ea7aa179f0ae14a"
        override val iosSimulatorArm64Orconfig: String = "4e478c6090d7ba94d0616ab8700cc3c973b6dee62e590126161ed056ef90e03b"

        override val iosX64LibTor: String = "ad72a01f295ab4bc1f26b419a718d28f946602a5cadff5ed4046553e4da6936e"
        override val iosX64Orconfig: String = "45dda0dfd5a455321192f73245aff2c0d113b4785ab2a43c284cd99483953c8d"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureNativeInterop(kmp: KotlinMultiplatformExtension) { configureLibNativeInteropProtected(kmp) }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        ValidationHash.LibNativeInterop(
            defFileName = "tor",
            targetName = "iosArm64",
            staticLibs = iosArm64Libs,
            headers = iosArm64Headers,
        ),
        ValidationHash.LibNativeInterop(
            defFileName = "tor",
            targetName = "iosSimulatorArm64",
            staticLibs = iosSimulatorArm64Libs,
            headers = iosSimulatorArm64Headers,
        ),
        ValidationHash.LibNativeInterop(
            defFileName = "tor",
            targetName = "iosX64",
            staticLibs = iosX64Libs,
            headers = iosX64Headers,
        ),
    ) }

    internal companion object {
        internal const val NAME = "noExecTorResourceValidation"
    }
}
