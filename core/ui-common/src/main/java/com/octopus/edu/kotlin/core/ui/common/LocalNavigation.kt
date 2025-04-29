package com.octopus.edu.kotlin.core.ui.common

import androidx.compose.runtime.staticCompositionLocalOf

val LocalNavigation =
    staticCompositionLocalOf<SpaceXNavigation> {
        error("CompositionLocal LocalNavigation not present")
    }
