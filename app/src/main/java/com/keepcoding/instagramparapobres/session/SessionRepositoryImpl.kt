package com.keepcoding.instagramparapobres.session

class SessionRepositoryImpl(
    private val memoryDataSource: SessionMemoryDataSource,
    private val localDataSource: SessionLocalDataSource
) : SessionRepository {

    override fun getSession(): Session? {
        return memoryDataSource.session
            ?: localDataSource.retrieveSession()
                .also { memoryDataSource.session = it }
    }

    override fun saveSession(session: Session) {
        memoryDataSource.session = session
        localDataSource.saveSession(session)
    }

}