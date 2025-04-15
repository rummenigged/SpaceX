package com.octopus.edu.kotlin.spacex.feature.launches.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.octopus.edu.kotlin.spacex.core.data.LaunchRepository
import com.octopus.edu.kotlin.spacex.core.model.ResponseOperation
import com.octopus.edu.kotlin.spacex.feature.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchDetailsViewModel @Inject constructor(
    private val repository: LaunchRepository,
    savedState: SavedStateHandle
): BaseViewModel<UiEvent, UiState, UiEffect>(savedState) {

    init {
        getLaunchDetails()
    }

    override fun getInitialValue(): UiState  = UiState()

    override fun processEvent(event: UiEvent)  = when (event){
        UiEvent.MarkEffectAsConsumed -> { markEffectAsConsumed() }
    }

    private fun getLaunchDetails() = viewModelScope.launch{
        getSavedStateValue<Int>("flightNumber")?.let { flightNumber ->
            when (val value = repository.getLaunchDetails(flightNumber)){
                is ResponseOperation.Error -> {}
                is ResponseOperation.Success -> {
                    setState {
                        copy(details = value.data, isLoading = false)
                    }
                }
            }
        }
    }
}