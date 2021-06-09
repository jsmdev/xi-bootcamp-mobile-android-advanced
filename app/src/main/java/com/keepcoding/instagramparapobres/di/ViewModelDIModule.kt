package com.keepcoding.instagramparapobres.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keepcoding.instagramparapobres.GalleryViewModel
import com.keepcoding.instagramparapobres.detail.DetailViewModel
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.singleton
import org.kodein.type.erased

object ViewModelDIModule: DIBaseModule("ViewModelDIModule") {

    override val builder: DI.Builder.() -> Unit ={
        bind<ViewModelProvider.Factory>() with singleton {
            DIViewModelFactory(di)
        }

        bind<GalleryViewModel>() with singleton {
            GalleryViewModel(
                instance(),
                instance()
            )
        }
        bind<DetailViewModel>() with singleton { DetailViewModel() }
    }

    class DIViewModelFactory(private val di: DI): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
           return di.direct.Instance(erased(modelClass))
        }
    }
}