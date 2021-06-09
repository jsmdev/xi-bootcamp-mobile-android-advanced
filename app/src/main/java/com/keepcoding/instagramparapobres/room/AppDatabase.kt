package com.keepcoding.instagramparapobres.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RoomImage::class], version = 2)
@TypeConverters(value = [AppConverters::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDAO
}