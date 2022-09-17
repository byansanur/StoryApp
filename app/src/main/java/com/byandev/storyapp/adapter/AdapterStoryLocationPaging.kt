package com.byandev.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.byandev.storyapp.data.model.Story
import com.byandev.storyapp.databinding.ItemStoryNearbyBinding
import com.byandev.storyapp.databinding.ItemStoryOtherBinding
import com.byandev.storyapp.di.SharedPrefManager
import javax.inject.Inject

class AdapterStoryLocationPaging @Inject constructor(
    private val listener: StoryLocationClickListener,
    private val sharedPrefManager: SharedPrefManager
) : PagingDataAdapter<Story, AdapterStoryLocationPaging.Holder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class Holder(private val binding: ItemStoryNearbyBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageStory.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = getItem(pos)
                    if (item != null) listener.storyLocationClicked(item)
                }
            }
        }
        fun bind(story: Story) {
            binding.apply {

                val glideUrl = GlideUrl(
                    story.photoUrl,
                    LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer ${sharedPrefManager.token}")
                        .build()
                )
                Glide.with(root.context)
                    .load(glideUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageStory)
                tvName.text = story.name
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val curItem = getItem(position)
        if (curItem != null) holder.bind(curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemStoryNearbyBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
    }

    interface StoryLocationClickListener {
        fun storyLocationClicked(story: Story)
    }
}