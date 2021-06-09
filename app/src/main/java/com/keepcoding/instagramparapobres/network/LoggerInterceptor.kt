package com.keepcoding.instagramparapobres.network

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class LoggerInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Timber.d(request.toString())
        val response = chain.proceed(request)
        Timber.d(response.toString())
        return response
    }
}