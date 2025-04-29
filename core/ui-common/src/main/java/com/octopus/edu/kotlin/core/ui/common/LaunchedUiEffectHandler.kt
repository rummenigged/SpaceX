package com.octopus.edu.kotlin.core.ui.common

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@androidx.compose.runtime.Composable
fun <EFFECT : ViewEffect> LaunchedUiEffectHandler(
    effectFlow: StateFlow<EFFECT?>,
    onEffect: suspend (EFFECT) -> Unit,
    onEffectConsumed: () -> Unit,
) {
    val effect by effectFlow.collectAsStateWithLifecycle()
    androidx.compose.runtime.LaunchedEffect(effect) {
        effect?.let {
            onEffect(it)
            onEffectConsumed()
        }
    }
}
