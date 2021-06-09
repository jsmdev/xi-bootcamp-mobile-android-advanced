package com.keepcoding.instagramparapobres.di

import com.keepcoding.instagramparapobres.session.SessionLocalDataSource
import com.keepcoding.instagramparapobres.session.SessionMemoryDataSource
import com.keepcoding.instagramparapobres.session.SessionRepository
import com.keepcoding.instagramparapobres.session.SessionRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object SessionDIModule : DIBaseModule("SessionDIModule") {
    override val builder: DI.Builder.() -> Unit = {

        bind<SessionLocalDataSource>() with singleton {
            SessionLocalDataSource(instance())
        }

        bind<SessionMemoryDataSource>() with singleton {
            SessionMemoryDataSource()
        }

        bind<SessionRepository>() with singleton {
                SessionRepositoryImpl(instance(), instance())
        }
    }
}