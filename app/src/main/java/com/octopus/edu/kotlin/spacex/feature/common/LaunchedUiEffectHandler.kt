package com.octopus.edu.kotlin.spacex.feature.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <EFFECT : ViewEffect> LaunchedUiEffectHandler(
    effectFlow: StateFlow<EFFECT?>,
    onEffect: suspend (EFFECT) -> Unit,
    onEffectConsumed: () -> Unit,
) {
    val effect by effectFlow.collectAsStateWithLifecycle()
    LaunchedEffect(effect) {
        effect?.let {
            onEffect(it)
            onEffectConsumed()
        }
    }
}
