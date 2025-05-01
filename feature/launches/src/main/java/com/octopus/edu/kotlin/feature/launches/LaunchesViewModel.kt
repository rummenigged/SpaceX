package com.octopus.edu.kotlin.feature.launches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Error
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Success
import com.octopus.edu.kotlin.core.domain.models.launch.Launch
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import com.octopus.edu.kotlin.core.ui.common.BaseViewModel
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab.Past
import com.octopus.edu.kotlin.feature.launches.LaunchesUiContract.Tab.Upcoming
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val launchRepository: LaunchRepository,
    savedState: SavedStateHandle
) : BaseViewModel<
    LaunchesUiContract.UiEvent,
    LaunchesUiContract.UiState,
    LaunchesUiContract.UiEffect
    >(savedState) {

    init {
        onTabSelected(getInitialValue().tabSelected)
    }

    private fun onTabSelected(tab: Tab) = viewModelScope.launch {
        setState {
            copy(
                isLoading = true,
                tabSelected = tab
            )
        }
        when (tab) {
            is Past -> {
                updateLaunchList(launchRepository.getPastLaunches())
            }
            is Upcoming -> {
                updateLaunchList(launchRepository.getUpcomingLaunches())
            }
        }
    }

    private fun updateLaunchList(result: ResponseOperation<List<Launch>>) {
        when (result) {
            is Success -> {
                setState {
                    copy(
                        launches = result.data,
                        isLoading = false
                    )
                }
            }

            is Error -> {
                setEffect(LaunchesUiContract.UiEffect.ShowError(result.message.toString()))
            }
        }
    }

    override fun getInitialValue(): LaunchesUiContract.UiState = LaunchesUiContract.UiState(
        isLoading = false
    )

    override fun processEvent(event: LaunchesUiContract.UiEvent) {
        when (event) {
            is LaunchesUiContract.UiEvent.OnLaunchClicked -> {
                onLaunchClicked(event.flightNumber)
            }

            LaunchesUiContract.UiEvent.MarkEffectAsConsumed -> markEffectAsConsumed()

            is LaunchesUiContract.UiEvent.OnTabSelected -> {
                onTabSelected(event.tab)
            }
        }
    }

    private fun onLaunchClicked(flightNumber: Int) {
        setEffect(LaunchesUiContract.UiEffect.NavigateToLaunchDetails(flightNumber))
    }
}
