package com.octopus.edu.kotlin.feature.launches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Error
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Success
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import com.octopus.edu.kotlin.core.ui.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel
    @Inject
    constructor(
        private val launchRepository: LaunchRepository,
        savedState: SavedStateHandle,
    ) : BaseViewModel<LaunchesUiContract.UiEvent, LaunchesUiContract.UiState, LaunchesUiContract.UiEffect>(
            savedState,
        ) {
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
                        setState {
                            copy(
                                launches = result.data,
                                isLoading = false,
                            )
                        }
                    }

                    is Error -> {
                        setEffect(LaunchesUiContract.UiEffect.ShowError(result.message.toString()))
                    }
                }
            }

        override fun getInitialValue(): LaunchesUiContract.UiState = LaunchesUiContract.UiState()

        override fun processEvent(event: LaunchesUiContract.UiEvent) =
            when (event) {
                is LaunchesUiContract.UiEvent.OnLaunchClicked -> {
                    onLaunchClicked(event.flightNumber)
                }
                LaunchesUiContract.UiEvent.MarkEffectAsConsumed -> markEffectAsConsumed()
            }

        private fun onLaunchClicked(flightNumber: Int) {
            setEffect(LaunchesUiContract.UiEffect.NavigateToLaunchDetails(flightNumber))
        }
    }
