package com.octopus.edu.kotlin.spacex.feature.launches.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.octopus.edu.kotlin.spacex.core.data.LaunchRepository
import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation
import com.octopus.edu.kotlin.spacex.feature.common.BaseViewModel
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesUiContract.UiEvent
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesUiContract.UiState
import com.octopus.edu.kotlin.spacex.feature.launches.list.LaunchesUiContract.UiEffect
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val launchRepository: LaunchRepository,
    savedState: SavedStateHandle
): BaseViewModel<UiEvent, UiState, UiEffect>(savedState) {

    init {
        getAllLaunches()
    }

    private fun getAllLaunches() = viewModelScope.launch{
        when(val result =  launchRepository.getAllLaunches()){
            is ResponseOperation.Success -> {
                setState { copy(launches = result.data, isLoading = false) }
            }

            is ResponseOperation.Error -> {
                setEffect(UiEffect.ShowError(result.message.toString()))
            }
        }
    }

    override fun getInitialValue(): UiState = UiState()

    override fun processEvent(event: UiEvent)  = when (event) {
        is UiEvent.OnLaunchClicked -> { onLaunchClicked(event.flightNumber) }
        UiEvent.MarkEffectAsConsumed -> markEffectAsConsumed()
    }

    private fun onLaunchClicked(flightNumber: Int) {
        setEffect(UiEffect.NavigateToLaunchDetails(flightNumber))
    }
}