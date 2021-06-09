package com.keepcoding.instagramparapobres.session

import android.content.SharedPreferences

class SessionLocalDataSource(private val sharedPreferences: SharedPreferences) {

    fun saveSession(session: Session) {
        sharedPreferences.edit().putString("ACCESS_TOKEN", session.accessToken).apply()
        sharedPreferences.edit().putString("ACCOUNT_NAME", session.accountName).apply()
    }

    fun retrieveSession(): Session? {
        val accessToken = sharedPreferences.getString("ACCESS_TOKEN", null) ?: return null
        val accountName = sharedPreferences.getString("ACCOUNT_NAME", null) ?: return null

        return Session(accessToken, accountName)
    }

}