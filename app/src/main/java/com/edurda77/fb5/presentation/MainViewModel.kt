package com.edurda77.fb5.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.fb5.data.unswers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun setUnswer () {

        viewModelScope.launch {
            _state.value.copy(
                answer = unswers.random()
            )
                .updateStateUI()
        }
    }

    private fun MainState.updateStateUI() {
        _state.update {
            this
        }
    }

}