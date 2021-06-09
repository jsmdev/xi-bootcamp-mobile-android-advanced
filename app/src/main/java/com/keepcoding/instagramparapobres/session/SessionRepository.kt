package com.keepcoding.instagramparapobres.session

interface SessionRepository {

    fun getSession(): Session?

    fun saveSession(session: Session)

}