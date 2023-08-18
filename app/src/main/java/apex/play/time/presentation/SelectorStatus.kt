package apex.play.time.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import apex.play.time.presentation.ApplicationStatus.Error
import apex.play.time.presentation.ApplicationStatus.Loading
import apex.play.time.presentation.ApplicationStatus.Mock
import apex.play.time.presentation.ApplicationStatus.Succsess

@Composable
fun SelectorStatus (
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    when (val result = state.value.status) {
        is Error -> {
            ErrorScreen(error = result.error)
        }
        Loading -> {
            LoadingScreen()
        }
        Mock -> {
            MockScreen(
                content = state.value.answer,
                onClick = { viewModel.setAnswer() }
            )
        }
        is Succsess -> {
            WebScreen(
                url = result.url
            )
        }
    }
}