package com.keepcoding.instagramparapobres.room

import androidx.room.TypeConverter
import com.keepcoding.instagramparapobres.room.RoomImage.ImageType

class AppConverters {

    @TypeConverter
    fun toImageType(value: String) = ImageType.valueOf(value)

    @TypeConverter
    fun fromImageType(imageType: ImageType) =imageType.name
}