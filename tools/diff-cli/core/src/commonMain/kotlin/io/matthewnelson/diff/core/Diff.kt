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

import io.matthewnelson.diff.core.Header.Companion.readDiffHeader
import io.matthewnelson.diff.core.internal.apply.Apply
import io.matthewnelson.diff.core.internal.create.Create
import io.matthewnelson.diff.core.internal.InternalDiffApi
import io.matthewnelson.diff.core.internal.system
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import okio.*
import okio.Path.Companion.toPath
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * Diff
 *
 * Create and Apply file diff's
 *
 * @see [apply]
 * @see [create]
 * @see [readHeader]
 * */
public class Diff private constructor() {

    /**
     * Diff file schema versions.
     * */
    @Suppress("EnumEntryName")
    public enum class Schema(@JvmField public val code: Int) {
        v1(code = 1);

        public companion object {
            @JvmStatic
            public fun latest(): Schema = v1
        }
    }

    public companion object {

        /**
         * Apply a diff to it's associate file.
         * */
        @JvmStatic
        @JvmOverloads
        @Throws(IllegalStateException::class, Exception::class)
        public fun apply(
            diffFilePath: String,
            applyToFilePath: String,
            options: Options.Apply = Options.Apply(),
        ) {
            apply(diffFilePath.toPath(), applyToFilePath.toPath(), options)
        }

        /**
         * Apply a diff to it's associate file.
         * */
        @JvmStatic
        @JvmOverloads
        @Throws(IllegalStateException::class, IOException::class)
        public fun apply(
            diffFile: Path,
            applyTo: Path,
            options: Options.Apply = Options.Apply(),
        ) {
            @OptIn(InternalDiffApi::class)
            apply(FileSystem.system(), diffFile, applyTo, options)
        }

        /**
         * Apply a diff to it's associate file.
         * */
        @JvmStatic
        @JvmOverloads
        @InternalDiffApi
        @Throws(IllegalStateException::class, IOException::class)
        public fun apply(
            fs: FileSystem,
            diffFile: Path,
            applyTo: Path,
            options: Options.Apply = Options.Apply(),
        ) {
            Apply.diff(fs, diffFile, applyTo, options)
        }

        /**
         * Create a diff from 2 files and write it to the associated directory
         * */
        @JvmStatic
        @JvmOverloads
        @Throws(
            NoDiffException::class,
            IllegalArgumentException::class,
            IllegalStateException::class,
            Exception::class,
        )
        public fun create(
            file1Path: String,
            file2Path: String,
            diffDirPath: String,
            options: Options.Create = Options.Create(),
        ): String {
            return create(file1Path.toPath(), file2Path.toPath(), diffDirPath.toPath(), options).toString()
        }

        /**
         * Create a diff from 2 files and write it to the associated directory
         * */
        @JvmStatic
        @JvmOverloads
        @Throws(
            NoDiffException::class,
            IllegalArgumentException::class,
            IllegalStateException::class,
            IOException::class,
        )
        public fun create(
            file1: Path,
            file2: Path,
            diffDir: Path,
            options: Options.Create = Options.Create()
        ): Path {
            @OptIn(InternalDiffApi::class)
            return create(FileSystem.system(), file1, file2, diffDir, options)
        }

        /**
         * Create a diff from 2 files and write it to the associated directory
         * */
        @JvmStatic
        @JvmOverloads
        @InternalDiffApi
        @Throws(
            NoDiffException::class,
            IllegalArgumentException::class,
            IllegalStateException::class,
            IOException::class,
        )
        public fun create(
            fs: FileSystem,
            file1: Path,
            file2: Path,
            diffDir: Path,
            options: Options.Create = Options.Create()
        ): Path {
            return Create.diff(fs, file1, file2, diffDir, options)
        }

        /**
         * Reads only the diff's header and returns it.
         * */
        @JvmStatic
        @Throws(IllegalStateException::class, Exception::class)
        public fun readHeader(diffFilePath: String): Header = readHeader(diffFilePath.toPath())

        /**
         * Reads only the diff's header and returns it.
         * */
        @JvmStatic
        @OptIn(InternalDiffApi::class)
        @Throws(IllegalStateException::class, IOException::class)
        public fun readHeader(diffFile: Path): Header = readHeader(FileSystem.system(), diffFile)

        /**
         * Reads only the diff's header and returns it.
         * */
        @JvmStatic
        @InternalDiffApi
        @Throws(IllegalStateException::class, IOException::class)
        public fun readHeader(fs: FileSystem, diffFile: Path): Header = fs.read(diffFile) { readDiffHeader() }
    }
}
