package com.keepcoding.instagramparapobres

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.keepcoding.instagramparapobres.databinding.MainActivityBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.direct
import org.kodein.di.instance

class MainActivity : AppCompatActivity(), DIAware {

    override val di: DI by di()
    private val viewModel: GalleryViewModel by lazy {
        ViewModelProvider(this, direct.instance()).get(GalleryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.processIntentData(intent)

        val binding = MainActivityBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        val adapter = GalleryRecyclerAdapter()
        binding.galleryRecyclerView.adapter = adapter

        viewModel.getHotImages()

        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.state
                    .collect { state ->
                        adapter.imageList = state.images
                        if (state.hasError)
                            Snackbar.make(binding.root, "Ha ocurrido un error", 5_000).show()
                    }
            }

            launch {
                viewModel.session.collect { sessionState ->
                    binding.bottomBar.menu.findItem(R.id.menu_login).apply {
                        title = when (sessionState.hasSession) {
                            true -> sessionState.accountName
                            false -> "Login"
                        }
                        isEnabled = !sessionState.hasSession
                    }
                    binding.bottomBar.menu.findItem(R.id.menu_me).isVisible = sessionState.hasSession
                }
            }
        }

        binding.bottomBar.setOnNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_hot -> viewModel.getHotImages()
                R.id.menu_top -> viewModel.getTopImages()
                R.id.menu_me -> viewModel.getMyImages()
                R.id.menu_login -> oauth2Flow()
            }
            true
        }

    }

    private fun oauth2Flow() {
        val url = "https://api.imgur.com/oauth2/authorize?client_id=3795c60af5383c1&response_type=token"
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }.also {
            startActivity(it)
        }
    }
}