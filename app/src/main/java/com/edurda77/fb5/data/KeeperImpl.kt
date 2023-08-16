package com.edurda77.fb5.data

import android.app.Application
import android.content.Context
import com.edurda77.fb5.domain.Keeper
import javax.inject.Inject

class KeeperImpl @Inject constructor(
    application: Application
): Keeper {
    private val sharedPref =
        application.getSharedPreferences(SAVED_SETTINGS, Context.MODE_PRIVATE)

    override fun getSharedUrl(): String? = sharedPref.getString(URL_SETTINGS, "")

    override fun setSharedUrl(url:String) {
        sharedPref.edit().putString(URL_SETTINGS, url).apply()
    }
}