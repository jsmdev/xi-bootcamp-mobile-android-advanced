package com.keepcoding.instagramparapobres.di

import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.keepcoding.instagramparapobres.room.AppDatabase
import com.keepcoding.instagramparapobres.room.ImageDAO
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object RoomDIModule : DIBaseModule("RoomDIModule") {
    override val builder: DI.Builder.() -> Unit = {
        bind<AppDatabase>() with singleton {
            Room
                .databaseBuilder(instance(), AppDatabase::class.java, "keepcoding")
                .build()
        }
        bind<ImageDAO>() with singleton {
            instance<AppDatabase>().imageDao()
        }
    }
}