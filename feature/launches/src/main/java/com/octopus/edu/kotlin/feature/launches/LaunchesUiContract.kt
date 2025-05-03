package com.octopus.edu.kotlin.feature.launches

import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus.Failure
import com.octopus.edu.kotlin.core.domain.models.launch.LaunchStatus.Success
import com.octopus.edu.kotlin.core.ui.common.ViewEffect
import com.octopus.edu.kotlin.core.ui.common.ViewEvent
import com.octopus.edu.kotlin.core.ui.common.ViewState
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab.Past
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab.Upcoming

object LaunchesUiContract {
    data class UiState(
        val isLoading: Boolean = true,
        val launches: List<Launch> = listOf<Launch>(),
        val tabSelected: Tab = Past(),
    ) : ViewState {
        val tabs: List<Tab> = listOf<Tab>(Past(), Upcoming())
        val tabTitles: List<String>
            get() = tabs.map { tab -> tab.title }

        fun getTab(position: Int): Tab = tabs[position]
    }

    sealed class Tab(
        open val title: String
    ) {
        data class Past(
            override val title: String = "Past"
        ) : Tab(title)

        data class Upcoming(
            override val title: String = "Upcoming"
        ) : Tab(title)
    }

    sealed class UiEffect : ViewEffect {
        data class NavigateToLaunchDetails(
            val flightNumber: Int
        ) : UiEffect()

        data class ShowError(
            val message: String
        ) : UiEffect()
    }

    sealed class UiEvent : ViewEvent {
        data object MarkEffectAsConsumed : UiEvent()

        data class OnLaunchClicked(
            val flightNumber: Int
        ) : UiEvent()

        data class OnTabSelected(
            val tab: Tab
        ) : UiEvent()

        data object ReloadLaunches : UiEvent()
    }

    internal fun LaunchStatus.getStatusValue() =
        when (this) {
            is Success -> "Success"
            is Failure -> "Failure"
        }
}
