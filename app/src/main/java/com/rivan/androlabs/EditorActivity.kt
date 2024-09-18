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

package com.rivan.androlabs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.rivan.androlabs.core.designsystem.theme.AndrolabsTheme
import com.rivan.androlabs.core.model.data.ContentType
import com.rivan.androlabs.navigation.EditorActivityNavHost

class EditorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndrolabsTheme {
                EditorActivityNavHost(
                    contentType = ContentType.SINGLE_PANE,
                    displayFeatures = calculateDisplayFeatures(activity = this),
                    navController = rememberNavController(),
                )
            }
        }
    }
}
