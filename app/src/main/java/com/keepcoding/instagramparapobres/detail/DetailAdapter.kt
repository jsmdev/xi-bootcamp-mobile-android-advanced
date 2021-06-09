package com.keepcoding.instagramparapobres.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keepcoding.instagramparapobres.databinding.DetailItemBinding

class DetailAdapter : RecyclerView.Adapter<DetailViewHolder>() {

    var urls: List<String> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailViewHolder =
        DetailItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
            .run { DetailViewHolder(this) }

    override fun onBindViewHolder(
        holder: DetailViewHolder,
        position: Int
    ) {
        holder.bind(urls[position])
    }

    override fun getItemCount(): Int = urls.size
}

data class DetailViewHolder(val binding: DetailItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(url: String) {
        with(binding) {
            Glide.with(root)
                .load(url)
                .into(imageView)
        }
    }
}
