package com.edurda77.fb5.presentation

data class MainState(
    val answer:String = "",
    val status: ApplicationStatus = ApplicationStatus.Succsess("https://ya.ru/")
)
