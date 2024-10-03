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

    protected open val iosArm64LibTor: String = "6be2db547d2b0a67688c96a73b844a7da16b942495e134df5a5832581a29759b"
    protected open val iosArm64Orconfig: String = "33018b29c3757493b37356f7a6e9bdd28553cd969c471afc9ed460aa97f47219"

    protected open val iosSimulatorArm64LibTor: String = "dbbbcb4fed33055d0257a392ec6ddd02d28667b11564f19e846cd0894f877a86"
    protected open val iosSimulatorArm64Orconfig: String = "32a216e02257ef4fc609f8907c1ebea7e9e25f45994700683a88b9bdfd29b58c"

    protected open val iosX64LibTor: String = "2c3788f1fbd2bfad9e05b50d234bb957003b23f3d911614373bc068c6dac15be"
    protected open val iosX64Orconfig: String = "1a341137c719e33563dff813855cdbee70dd0631eec8ee938a00e68c45740675"

    private val iosArm64Libs: List<Pair<String, String>> by lazy {
        listOf(
            "libcrypto.a" to "cce408daad29c2b2b7a62ceae8a1891055a18eb9a9922c45272b758870b4f0ed",
            "libevent.a" to "abf790cff6cd36bcee9b88ebbf71f871d4209cb548a6a7b8df916d4c47f4db57",
            "liblzma.a" to "700321f0f91e3823c630e451e5dc563ccca87e8bc98d5226a5f93f73a25fb0dd",
            "libssl.a" to "32d851475f10fe52d2f6c17c375720243c07ac304724c92411bcff5514c5fc5a",
            "libtor.a" to iosArm64LibTor,
            "libz.a" to "39af3cf3435091264fc8a82f55ec5c3b643975232fa9fd7d14cfd57eb8ec5928",
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
            "libcrypto.a" to "94597b8d04ade66ac9c30f07c19e8441a964906c087263821d21d80148a14922",
            "libevent.a" to "3e00ce05ac70692839964d75702459274b3223d8c70d75da3ed1a08209db53bf",
            "liblzma.a" to "a21aa4d2eeab89c7e16d6392cc32f2ab7467c08edbb24ec80e8f17adc82374cc",
            "libssl.a" to "ea0c31aec75b76428d45012649d9d46a4347987ccea2a5059e129cf3ef07038f",
            "libtor.a" to iosSimulatorArm64LibTor,
            "libz.a" to "b9d35700894c130476ebc20270d490094b5189c200da7e34fba0c785b481e2bc",
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
            "libcrypto.a" to "99479318462fde7ba1784289a1dd6d031bb368c4b0ffb9703133782e24122e93",
            "libevent.a" to "6eeca863932d9497a61571a83ff65b0bcf2ce4bd1f1a16f0ccd476bc3a43cfc9",
            "liblzma.a" to "1b13fb0a06e6375deefafa983b216f4cd7399818bd276ce7d33d5b158e232060",
            "libssl.a" to "b36bfc66ab070728e9fbea1896c3a2498877ae93a393fe114304486a92b243c9",
            "libtor.a" to iosX64LibTor,
            "libz.a" to "6e5fd0f45a478032c321aaab8f9579c613e40fe7455c5228a8587256a17b9f4c",
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

        override val iosArm64LibTor: String = "f6b290b12359e29dfbbb812420c0c444a78861830f94a0238eb4408bd6888372"
        override val iosArm64Orconfig: String = "bca48d3339b9740583d1a7b8d650ca715d96e6f267d13a9293c3f63233289391"

        override val iosSimulatorArm64LibTor: String = "5fa5a97dd1a5a2673e73b3184c1fee71bd60e9d2f617632f8cf7842016ad5292"
        override val iosSimulatorArm64Orconfig: String = "4e478c6090d7ba94d0616ab8700cc3c973b6dee62e590126161ed056ef90e03b"

        override val iosX64LibTor: String = "be3904391e00217671410286e02476d2e96c1da224d72c8db3f5bdd548a41d05"
        override val iosX64Orconfig: String = "45dda0dfd5a455321192f73245aff2c0d113b4785ab2a43c284cd99483953c8d"

        internal companion object {
            internal const val NAME = "noExecTorGPLResourceValidation"
        }
    }

    fun configureNativeInterop() { configureLibNativeInteropProtected() }

    final override val hashes: Set<ValidationHash> by lazy { setOf(
        ValidationHash.LibNativeInterop(
            targetName = "iosArm64",
            staticLibs = iosArm64Libs,
            headers = iosArm64Headers,
        ),
        ValidationHash.LibNativeInterop(
            targetName = "iosSimulatorArm64",
            staticLibs = iosSimulatorArm64Libs,
            headers = iosSimulatorArm64Headers,
        ),
        ValidationHash.LibNativeInterop(
            targetName = "iosX64",
            staticLibs = iosX64Libs,
            headers = iosX64Headers,
        ),
    ) }

    internal companion object {
        internal const val NAME = "noExecTorResourceValidation"
    }
}
