package com.octopus.edu.kotlin.spacex.feature.common

import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.octopus.edu.kotlin.spacex.core.ui.SpaceXNavigation
import com.octopus.edu.kotlin.spacex.core.ui.common.LocalNavigation
import com.octopus.edu.kotlin.spacex.ui.theme.SpaceXTheme

@Composable
fun Fragment.SpaceXThemeWithCompositionsLocals(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
)  {
    CompositionLocalProvider(
        LocalNavigation provides requireActivity() as SpaceXNavigation,
    ) {
        SpaceXTheme(darkTheme = isSystemInDarkTheme) { content() }
    }
}

fun Fragment.setComposableContent(content: @Composable (View) -> Unit) =
    ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            SpaceXThemeWithCompositionsLocals {
                content(this)
            }
        }
    }
