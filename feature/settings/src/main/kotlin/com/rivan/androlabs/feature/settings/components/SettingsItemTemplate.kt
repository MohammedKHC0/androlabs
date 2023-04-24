/*
 * Copyright 2023 Rivan Parmar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rivan.androlabs.feature.settings.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsItemTemplate(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: @Composable () -> Unit = {},
    startWidget: (@Composable () -> Unit)? = null,
    endWidget: (@Composable () -> Unit)? = null,
    enabled: Boolean = true
) {
    if (!enabled) {
        modifier.alpha(0.38f)
    }

    ListItem(
        headlineText = title,
        supportingText = description,
        modifier = modifier,
        leadingContent = startWidget,
        trailingContent = endWidget,
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}