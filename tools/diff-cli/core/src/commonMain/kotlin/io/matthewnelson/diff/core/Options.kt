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
package io.matthewnelson.diff.core

import io.matthewnelson.diff.core.Diff.Companion.create
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName

public sealed class Options {

    public class Apply: Options {

        @JvmField
        public val dryRun: Boolean

        public constructor(): this(Builder())

        public constructor(builder: Builder): super() {
            dryRun = builder.dryRun
        }

        public constructor(configure: Builder.() -> Unit): super() {
            val builder = Builder()
            configure(builder)
            dryRun = builder.dryRun
        }

        /**
         * Configure [Options.Apply]
         * */
        public class Builder {

            /**
             * Will apply the diff to its associated file, but skip
             * the final step of atomically moving the .bak file
             * into its place.
             * */
            @JvmField
            public var dryRun: Boolean = false
            public fun dryRun(value: Boolean): Builder {
                dryRun = value
                return this
            }
        }

        override fun equals(other: Any?): Boolean {
            return  other is Apply
                    && other.dryRun == dryRun
        }

        override fun hashCode(): Int {
            var result = 17
            result = result * 31 + dryRun.hashCode()
            return result
        }

        override fun toString(): String {
            return """
                Options.Apply [
                    dryRun: $dryRun
                ]
            """.trimIndent()
        }
    }

    /**
     * [Options] for use when creating a new [Diff] via [create].
     *
     * @see [Create.Builder]
     * */
    public class Create: Options {

        @JvmField
        public val diffFileExtensionName: String
        @JvmField
        public val useStaticTime: Boolean
        @JvmField
        public val schema: Diff.Schema

        public constructor(): this(Builder())

        public constructor(builder: Builder): super() {
            diffFileExtensionName = builder.diffFileExtensionName
            useStaticTime = builder.useStaticTime
            schema = builder.schema
        }

        public constructor(configure: Builder.() -> Unit): super() {
            val builder = Builder()
            configure(builder)
            diffFileExtensionName = builder.diffFileExtensionName
            useStaticTime = builder.useStaticTime
            schema = builder.schema
        }

        /**
         * Configure [Options.Create]
         * */
        public class Builder  {
            @get:JvmName("diffFileExtensionName")
            public var diffFileExtensionName: String = DEFAULT_EXT_NAME
                private set

            /**
             * Use a different file extension name for the diff file
             *
             * Default is .diff
             *
             * @throws [IllegalArgumentException] if [value]:
             *  - Contains whitespace
             *  - Contains new lines
             *  - Does not start with '.'
             *  - Is less than 2 chars
             * */
            @Throws(IllegalArgumentException::class)
            public fun diffFileExtensionName(value: String): Builder {
                with(value) {
                    require(!contains(' ')) { "diff file extension name cannot contain white space" }
                    require(lines().size == 1) { "diff file extension name cannot contain line breaks" }
                    require(startsWith('.')) { "diff file extension name must start with a '.'" }
                    require(length > 1) { "diff file extension name length must be greater than 1" }
                }

                diffFileExtensionName = value
                return this
            }

            /**
             * Will use a static time value for [Header.createdAt],
             * instead of now().
             *
             * @see [time]
             * */
            @JvmField
            public var useStaticTime: Boolean = false

            public fun useStaticTime(value: Boolean): Builder {
                useStaticTime = value
                return this
            }

            @JvmField
            public var schema: Diff.Schema = Diff.Schema.latest()
            public fun schema(value: Diff.Schema): Builder {
                schema = value
                return this
            }
        }

        internal fun time(): Instant {
            return if (useStaticTime) {
                STATIC_TIME.toInstant()
            } else {
                Clock.System.now()
            }
        }

        override fun equals(other: Any?): Boolean {
            return  other is Create
                    && other.diffFileExtensionName == diffFileExtensionName
                    && other.useStaticTime == useStaticTime
                    && other.schema == schema
        }

        override fun hashCode(): Int {
            var result = 17
            result = result * 31 + diffFileExtensionName.hashCode()
            result = result * 31 + useStaticTime.hashCode()
            result = result * 31 + schema.hashCode()
            return result
        }

        override fun toString(): String {
            return """
                Options.Create [
                    diffFileExtensionName: $diffFileExtensionName
                    useStaticTime: $useStaticTime
                    schema: $schema
                ]
            """.trimIndent()
        }

        public companion object {
            public const val DEFAULT_EXT_NAME: String = ".diff"
            public const val STATIC_TIME: String = "1971-08-21T00:01:00Z"
        }
    }
}
