package com.keepcoding.instagramparapobres.di

import android.util.Log
import com.keepcoding.instagramparapobres.network.AuthInterceptor
import com.keepcoding.instagramparapobres.network.ImgurApi
import com.keepcoding.instagramparapobres.network.LoggerInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object NetworkDIModule: DIBaseModule("NetworkDIModule") {

    override val builder: DI.Builder.() -> Unit = {
        bind<OkHttpClient>() with singleton {
            Log.e("DEBUG", "OkHttpClient")
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient().newBuilder()
                .addInterceptor(AuthInterceptor(instance()))
                .addInterceptor(logging)
                .build()
        }

        bind<Moshi>() with singleton {
            Log.e("DEBUG", "Moshi")
            Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        }

        bind<Retrofit>() with singleton {
            Log.e("DEBUG", "Retrofit")
            Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(instance())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<ImgurApi>() with singleton {
            instance<Retrofit>().create(ImgurApi::class.java)
        }
    }
}