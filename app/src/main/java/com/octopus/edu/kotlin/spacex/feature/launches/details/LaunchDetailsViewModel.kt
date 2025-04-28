package com.octopus.edu.kotlin.spacex.feature.launches.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Error
import com.octopus.edu.kotlin.core.domain.common.ResponseOperation.Success
import com.octopus.edu.kotlin.core.domain.repository.LaunchRepository
import com.octopus.edu.kotlin.spacex.feature.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchDetailsViewModel
    @Inject
    constructor(
        private val repository: LaunchRepository,
        savedState: SavedStateHandle,
    ) : BaseViewModel<UiEvent, UiState, UiEffect>(savedState) {
        init {
            getLaunchDetails()
        }

        override fun getInitialValue(): UiState = UiState()

        override fun processEvent(event: UiEvent) =
            when (event) {
                UiEvent.MarkEffectAsConsumed -> {
                    markEffectAsConsumed()
                }
            }

        private fun getLaunchDetails() =
            viewModelScope.launch {
                getSavedStateValue<Int>("flightNumber")?.let { flightNumber ->
                    when (
                        val value =
                            repository.getLaunchDetails(
                                flightNumber,
                            )
                    ) {
                        is Error -> {}
                        is Success -> {
                            setState {
                                copy(details = value.data, isLoading = false)
                            }
                        }
                    }
                }
            }
    }
