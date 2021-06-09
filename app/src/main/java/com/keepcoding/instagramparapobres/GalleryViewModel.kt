package com.keepcoding.instagramparapobres

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepcoding.instagramparapobres.gallery.GalleryRepository
import com.keepcoding.instagramparapobres.gallery.Image
import com.keepcoding.instagramparapobres.session.Session
import com.keepcoding.instagramparapobres.session.SessionRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class GalleryViewModel(
    private val galleryRepository: GalleryRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val stateFlow: MutableStateFlow<GalleryState> = MutableStateFlow(GalleryState.empty())
    val state: StateFlow<GalleryState>
        get() = stateFlow

    private val sessionFlow: MutableStateFlow<SessionState> = MutableStateFlow(SessionState.empty())
    val session: StateFlow<SessionState>
        get() = sessionFlow

    private var requestJob: Job? = null
    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Timber.e(throwable)
        stateFlow.value = GalleryState(emptyList(), true)
    }

    init {
        sessionRepository.getSession().let { session ->
            sessionFlow.value = SessionState(session != null, session?.accountName)
        }
    }

    fun getHotImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(handler) {
            val gallery = galleryRepository.getHotGallery()
            stateFlow.value = GalleryState(gallery.images)
        }
    }

    fun getTopImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(handler) {
            val gallery = galleryRepository.getTopGallery()
            stateFlow.value = GalleryState(gallery.images)
        }
    }

    fun getMyImages() {
        requestJob?.cancel()
        requestJob = viewModelScope.launch(handler) {
            val gallery = galleryRepository.getMyGallery()
            stateFlow.value = GalleryState(gallery.images)
        }
    }

    fun processIntentData(intent: Intent) {
        val url = intent.data.toString()
        "imgram://oauth2.+".toRegex().matches(url).alsoIfTrue {
            val accestoken = "access_token=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "expires_in=(\\w+)".toRegex().find(url)!!.groupValues[1].toLong() + System.currentTimeMillis()
            "token_type=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "refresh_token=(\\w+)".toRegex().find(url)!!.groupValues[1]
            val accountName = "account_username=(\\w+)".toRegex().find(url)!!.groupValues[1]
            "account_id=(\\w+)".toRegex().find(url)!!.groupValues[1]

            Session(accestoken, accountName)
                .also { session ->
                    sessionRepository.saveSession(session)
                }.also { session ->
                    sessionFlow.value = SessionState(true, session.accountName)
                }
        }
    }

    data class GalleryState(val images: List<Image>, val hasError: Boolean = false) {
        companion object {
            fun empty() = GalleryState(emptyList(), false)
        }
    }

    data class SessionState(val hasSession: Boolean, val accountName: String?) {
        companion object {
            fun empty() = SessionState(false, null)
        }
    }
}