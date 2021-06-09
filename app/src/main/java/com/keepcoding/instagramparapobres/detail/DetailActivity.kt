package com.keepcoding.instagramparapobres.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.keepcoding.instagramparapobres.databinding.DetailActivityBinding
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.di
import org.kodein.di.direct
import org.kodein.di.instance
import java.util.ArrayList

class DetailActivity : AppCompatActivity(),
    DIAware {

    companion object {
        private const val EXTRA_URLS = "EXTRA_URLS"

        fun start(
            context: Context,
            urls: List<String>
        ) {
            val intent = Intent(
                context,
                DetailActivity::class.java
            )
            intent.putStringArrayListExtra(
                EXTRA_URLS,
                urls as ArrayList<String>
            )
            context.startActivity(intent)
        }
    }

    override val di: DI by di()
    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(
            this,
            direct.instance()
        ).get(DetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DetailActivityBinding.inflate(layoutInflater)
            .also {
                setContentView(it.root)
            }

        val adapter = DetailAdapter()
        binding.detailRecyclerView.adapter = adapter
        val urls = intent.getStringArrayListExtra(EXTRA_URLS)
        urls?.let {
            adapter.urls = urls.toList()
            viewModel.adapterHasBeenLoaded()
        }
    }
}