package com.keepcoding.instagramparapobres.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.keepcoding.instagramparapobres.room.RoomImage.ImageType

@Dao
interface ImageDAO {

    @Query("SELECT * FROM images WHERE type = :imageType")
    suspend fun getImages(imageType: ImageType): List<RoomImage>



    @Insert(onConflict = REPLACE)
    suspend fun insertImages(imagesList: List<RoomImage>)
}