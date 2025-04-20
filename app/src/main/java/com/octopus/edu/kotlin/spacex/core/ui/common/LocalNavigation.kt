package com.octopus.edu.kotlin.spacex.core.ui.common

import androidx.compose.runtime.staticCompositionLocalOf
import com.octopus.edu.kotlin.spacex.core.ui.SpaceXNavigation

val LocalNavigation =
    staticCompositionLocalOf<SpaceXNavigation> {
        error("CompositionLocal LocalNavigation not present")
    }
