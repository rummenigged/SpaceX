package com.octopus.edu.kotlin.core.ui.common

import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
import androidx.fragment.app.Fragment
import com.octopus.edu.kotlin.core.design.designSystem.theme.SpaceXTheme

@Composable
fun Fragment.SpaceXThemeWithCompositionsLocals(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNavigation provides requireActivity() as SpaceXNavigation,
    ) {
        SpaceXTheme(
            darkTheme = isSystemInDarkTheme,
            dynamicColor = false,
        ) { content() }
    }
}

fun Fragment.setComposableContent(content: @Composable (View) -> Unit) =
    ComposeView(requireContext()).apply {
        setViewCompositionStrategy(DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            SpaceXThemeWithCompositionsLocals {
                content(this)
            }
        }
    }
