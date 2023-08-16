package com.edurda77.fb5.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SelectorStatus (
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    when (val result = state.value.status) {
        is ApplicationStatus.Error -> {
            ErrorScreen(error = result.error)
        }
        ApplicationStatus.Loading -> {
            LoadingScreen()
        }
        ApplicationStatus.Mock -> {
            MockScreen(
                content = state.value.answer,
                onClick = { viewModel.setAnswer() }
            )
        }
        is ApplicationStatus.Succsess -> {
            WebScreen()
        }
    }
}