package com.byandev.storyapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.byandev.storyapp.databinding.ItemImageBinding
import com.byandev.storyapp.di.GlideApp
import java.io.File


class ImageGalleryAdapter : RecyclerView.Adapter<ImageGalleryAdapter.Holder>() {

    private var list: MutableList<String> = mutableListOf()

    inner class Holder(val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        list[position].let { data ->
            GlideApp.with(holder.itemView.context)
                .load(File(data))
                .apply(RequestOptions().override(250, 250))
                .centerCrop()
                .into(holder.binding.image)

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(data) }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    @SuppressLint("NotifyDataSetChanged")
    fun changeList(input: MutableList<String>) {
        list = input
        notifyDataSetChanged()
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}