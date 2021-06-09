package com.keepcoding.instagramparapobres.gallery


import com.keepcoding.instagramparapobres.network.ImgurApi
import com.keepcoding.instagramparapobres.network.NetworkGallery
import com.keepcoding.instagramparapobres.room.ImageDAO
import com.keepcoding.instagramparapobres.room.RoomImage
import com.keepcoding.instagramparapobres.room.RoomImage.ImageType
import com.keepcoding.instagramparapobres.room.RoomImage.ImageType.HOT
import com.keepcoding.instagramparapobres.room.RoomImage.ImageType.TOP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class GalleryRepositoryImpl(
    private val imgurApi: ImgurApi,
    private val imageDAO: ImageDAO
) : GalleryRepository {

    override suspend fun getHotGallery() =
        withContext(Dispatchers.IO) {
            try {
                imgurApi.getHotGallery().toDomain().also { gallery ->
                    imageDAO.insertImages(gallery.toRoom(HOT))
                }

            } catch (e: Exception) {
                Timber.e(e)
                imageDAO.getImages(HOT).toDomain()
            }
        }

    override suspend fun getTopGallery() =
        withContext(Dispatchers.IO) {
            try {
                imgurApi.getTopGallery().toDomain().also { gallery ->
                    imageDAO.insertImages(gallery.toRoom(ImageType.TOP))
                }

            } catch (e: Exception) {
                Timber.e(e)
                imageDAO.getImages(TOP).toDomain()
            }
        }

    override suspend fun getMyGallery() =
        withContext(Dispatchers.IO) { imgurApi.getMyGallery().toDomain() }

    private fun NetworkGallery.toDomain(): Gallery {
        val images = data.filter { image ->
            val imageLink = image.images?.first()?.link ?: image.link
            imageLink.contains(".jpg") || imageLink.contains(".png")
        }.mapNotNull { image ->
//            val imageLink = image.images?.first()?.link ?: image.link
            val imageLinks = mutableListOf<String>()
            image.images?.forEach {
                imageLinks.add(it.link)
            }
            Image(
                id = image.id,
                title = image.title,
                urls = imageLinks,
                likes = image.favorite_count ?: 0,
                datetime = image.datetime,
                author = image.account_url
            )
        }

        return Gallery(images)
    }

    private fun List<RoomImage>.toDomain(): Gallery {
        return Gallery(map { roomImage ->
            Image(
                id = roomImage.id,
                title = roomImage.title,
                urls = listOf(roomImage.url),
                likes = roomImage.likes,
                datetime = roomImage.datetime,
                author = roomImage.author
            )
        })
    }

    private fun Gallery.toRoom(imageType: ImageType): List<RoomImage> =
        images.map { image ->
            RoomImage(
                id = image.id,
                title = image.title,
                url = image.urls[0],
                likes = image.likes,
                datetime = image.datetime,
                author = image.author,
                imageType = imageType
            )
        }
}