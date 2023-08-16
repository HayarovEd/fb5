package com.edurda77.fb5.domain

interface Service {
    fun checkedInternetConnection(): Boolean
    fun vpnActive(): Boolean
    fun batteryLevel(): Int
    fun checkIsEmu(): Boolean
}