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
import io.matthewnelson.kmp.configuration.extension.container.target.KmpConfigurationContainerDsl
import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

fun KmpConfigurationContainerDsl.sourceSetConnect(
    newName: String,
    existingNames: List<String>,
    dependencyName: String = "common",
    sourceSetTest: Action<KotlinSourceSet>? = null,
    sourceSetMain: Action<KotlinSourceSet>? = null,
) {
    kotlin {
        with(sourceSets) {
            val sourceSets = existingNames.map { name ->
                Pair(
                    findByName(name + "Main"),
                    findByName(name + "Test"),
                )
            }

            if (sourceSets.find { it.first != null } == null) return@kotlin

            val newMain = maybeCreate(newName + "Main")
            val newTest = maybeCreate(newName + "Test")
            newMain.dependsOn(getByName(dependencyName + "Main"))
            newTest.dependsOn(getByName(dependencyName + "Test"))

            sourceSets.forEach { (main, test) ->
                main?.dependsOn(newMain)
                test?.dependsOn(newTest)
            }

            sourceSetTest?.execute(newTest)
            sourceSetMain?.execute(newMain)
        }
    }
}
