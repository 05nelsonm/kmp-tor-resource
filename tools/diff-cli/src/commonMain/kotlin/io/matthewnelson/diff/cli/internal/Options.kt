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
package io.matthewnelson.diff.cli.internal

import io.matthewnelson.diff.core.Diff
import io.matthewnelson.diff.core.Options
import kotlinx.cli.*

internal interface OptDiffFileExtName {
    val diffFileExtNameOpt: String

    companion object {
        internal const val NAME_OPT = "diff-ext-name"

        internal fun ArgParser.diffFileExtNameOption(
            description: String
        ): SingleOption<String, DefaultRequiredType.Default> {
            return option(
                type = ArgType.String,
                fullName = NAME_OPT,
                description = description
            ).default(Options.Create.DEFAULT_EXT_NAME)
        }
    }
}

internal interface OptStaticTime {
    val staticTimeOpt: Boolean

    companion object {
        internal const val NAME_OPT = "static-time"

        internal fun ArgParser.staticTimeOption(): SingleOption<Boolean, DefaultRequiredType.Default> {
            return option(
                type = ArgType.Boolean,
                fullName = NAME_OPT,
                description = "Uses a static time value of ${Options.Create.STATIC_TIME} instead of the current time value"
            ).default(false)
        }
    }
}
