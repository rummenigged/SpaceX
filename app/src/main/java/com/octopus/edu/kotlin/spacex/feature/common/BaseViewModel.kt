package com.octopus.edu.kotlin.spacex.feature.common

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface ViewState
interface ViewEvent
interface ViewEffect

abstract class BaseViewModel<Event: ViewEvent, UiState: ViewState, Effect: ViewEffect>(
    private val savedState: SavedStateHandle
): ViewModel() {

    private val _viewStateFlow by lazy { MutableStateFlow(getInitialValue()) }
    val viewStateFlow
        get() = _viewStateFlow.asStateFlow()

    private val _effect = MutableStateFlow<Effect?>(null)
    val effect = _effect.asStateFlow()

    protected fun setState(reducer: UiState.() -> UiState){
       _viewStateFlow.update(reducer)
    }

    protected fun setEffect(effect: Effect){
        _effect.value = effect
    }

    fun markEffectAsConsumed(){
        _effect.value = null
    }

    fun <T> getSavedStateValue(key: String): T? = savedState[key]

    fun <T> saveStateValue(key: String, value: T) = savedState.set(key, value)

    abstract fun getInitialValue(): UiState

    abstract fun processEvent(event: Event)
}