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
plugins {
    id("configuration")
    id("resource-validation")
}

kmpConfiguration {
    configure {
        androidLibrary(namespace = "io.matthewnelson.kmp.tor.resource.android.unit.test") {
            target { publishLibraryVariants("release") }

            android {
                sourceSets.getByName("main").resources {
                    // Only want to include binary resources from jvmMain
                    // and not geoip files which are positioned at
                    // jvmAndroidMain/resources.
                    //
                    // Doing so would cause a conflict for anyone depending
                    // on both :library:binary and :library:binary-android-unit-test
                    srcDir(torResourceValidation.jvmTorLibResourcesSrcDir)
                }
            }
        }

        common { pluginIds("publication") }
    }
}
