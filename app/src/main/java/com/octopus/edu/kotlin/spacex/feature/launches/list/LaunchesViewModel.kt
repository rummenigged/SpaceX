package com.octopus.edu.kotlin.spacex.feature.launches.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.octopus.edu.kotlin.core.data.launches.LaunchRepository
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Error
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Success
import com.octopus.edu.kotlin.spacex.feature.common.BaseViewModel
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesUiContract.UiEffect
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesUiContract.UiEvent
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesUiContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel
    @Inject
    constructor(
        private val launchRepository: LaunchRepository,
        savedState: SavedStateHandle,
    ) : BaseViewModel<UiEvent, UiState, UiEffect>(savedState) {
        init {
            getAllLaunches()
        }

        private fun getAllLaunches() =
            viewModelScope.launch {
                when (
                    val result =
                        launchRepository.getAllLaunches()
                ) {
                    is Success -> {
                        setState { copy(launches = result.data, isLoading = false) }
                    }

                    is Error -> {
                        setEffect(UiEffect.ShowError(result.message.toString()))
                    }
                }
            }

        override fun getInitialValue(): UiState = UiState()

        override fun processEvent(event: UiEvent) =
            when (event) {
                is UiEvent.OnLaunchClicked -> {
                    onLaunchClicked(event.flightNumber)
                }
                UiEvent.MarkEffectAsConsumed -> markEffectAsConsumed()
            }

        private fun onLaunchClicked(flightNumber: Int) {
            setEffect(UiEffect.NavigateToLaunchDetails(flightNumber))
        }
    }
