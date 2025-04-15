package com.octopus.edu.kotlin.spacex.feature.launches.details

import com.octopus.edu.kotlin.spacex.core.model.LaunchDetails
import com.octopus.edu.kotlin.spacex.feature.common.ViewEffect
import com.octopus.edu.kotlin.spacex.feature.common.ViewEvent
import com.octopus.edu.kotlin.spacex.feature.common.ViewState

data class UiState(
    val details: LaunchDetails? = null,
    val isLoading: Boolean = true
): ViewState

sealed class UiEvent: ViewEvent{
    object MarkEffectAsConsumed: UiEvent()
}

sealed class UiEffect: ViewEffect{
    data class ShowError(val message: String): UiEffect()
}