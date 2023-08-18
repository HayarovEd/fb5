package apex.play.time.presentation

import apex.play.time.presentation.ApplicationStatus.Loading

data class MainState(
    val answer:String = "",
    val status: ApplicationStatus = Loading
)
