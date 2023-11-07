/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.build.api.variant.impl

import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider

/**
 * Abstraction of a source file within the Variant object model.
 */
interface FileEntry {
    /**
     *  source file name, human-readable but not guaranteed to be unique.
     */
    val name: String

    /**
     * true if it contains generated sources, false it is editable by the user.
     */
    val isGenerated: Boolean

    /**
     * true if the user added this source folder (generated or not), false if it is a folder
     * that was automatically created by AGP.
     */
    val isUserAdded: Boolean

    /**
     * true if the folder should be added to the IDE model, false otherwise.
     */
    val shouldBeAddedToIdeModel: Boolean

    /**
     * Return the source folder as a [Provider] of [Directory], with appropriate
     * [org.gradle.api.Task] dependency if there is one. Can be used as a task input directly.
     */
    fun asFile(
            projectDir: Provider<Directory>
    ): Provider<RegularFile>
}
