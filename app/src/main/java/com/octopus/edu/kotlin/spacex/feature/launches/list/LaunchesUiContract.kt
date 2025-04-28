package com.octopus.edu.kotlin.spacex.feature.launches.list

import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus.Failure
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus.Success
import com.octopus.edu.kotlin.spacex.feature.common.ViewEffect
import com.octopus.edu.kotlin.spacex.feature.common.ViewEvent
import com.octopus.edu.kotlin.spacex.feature.common.ViewState

object LaunchesUiContract {
    data class UiState(
        val launches: List<Launch> = listOf<Launch>(),
        val isLoading: Boolean = true,
    ) : ViewState

    sealed class UiEffect : ViewEffect {
        data class NavigateToLaunchDetails(
            val flightNumber: Int,
        ) : UiEffect()

        data class ShowError(
            val message: String,
        ) : UiEffect()
    }

    sealed class UiEvent : ViewEvent {
        data object MarkEffectAsConsumed : UiEvent()

        data class OnLaunchClicked(
            val flightNumber: Int,
        ) : UiEvent()
    }

    internal fun LaunchStatus.getStatusValue() =
        when (this) {
            is Success -> "Success"
            is Failure -> "Failure"
        }
}
