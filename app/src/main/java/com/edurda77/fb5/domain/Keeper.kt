package com.edurda77.fb5.domain

interface Keeper {
    fun getSharedUrl(): String?
    fun setSharedUrl(url:String)
}