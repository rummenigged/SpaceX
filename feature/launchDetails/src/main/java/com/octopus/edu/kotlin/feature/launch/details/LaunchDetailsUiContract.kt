package com.octopus.edu.kotlin.feature.launch.details

import com.octopus.edu.kotlin.core.domain.models.launch.LaunchDetails
import com.octopus.edu.kotlin.core.ui.common.ViewEffect
import com.octopus.edu.kotlin.core.ui.common.ViewEvent
import com.octopus.edu.kotlin.core.ui.common.ViewState

data class UiState(
    val details: LaunchDetails? = null,
    val isLoading: Boolean = true,
) : ViewState

sealed class UiEvent : ViewEvent {
    object MarkEffectAsConsumed : UiEvent()
}

sealed class UiEffect : ViewEffect {
    data class ShowError(
        val message: String,
    ) : UiEffect()
}
