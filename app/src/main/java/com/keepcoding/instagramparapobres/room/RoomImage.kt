package com.keepcoding.instagramparapobres.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class RoomImage(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "likes") val likes: Int,
    @ColumnInfo(name = "datetime") val datetime: Long,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "type") val imageType: ImageType
) {
    enum class ImageType {
        HOT, TOP, MY_IMAGES
    }
}