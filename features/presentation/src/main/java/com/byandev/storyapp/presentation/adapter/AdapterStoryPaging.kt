package com.byandev.storyapp.presentation.adapter

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.data.model.dto.Stories
import com.byandev.storyapp.presentation.covertTimeToText
import com.byandev.storyapp.presentation.databinding.ItemStoryOtherBinding
import com.byandev.storyapp.presentation.glideUrls
import com.byandev.storyapp.presentation.textAsBitmap

class AdapterStoryPaging(
    private val listener: StoryClickListener,
    private val sharedPrefManager: SharedPrefManager
) : PagingDataAdapter<Stories, AdapterStoryPaging.Holder>(DIFF_UTIL) {

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Stories>() {
            override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class Holder(private val binding: ItemStoryOtherBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imageStory.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = getItem(pos)
                    if (item != null) listener.storyClicked(item)
                }
            }
        }

        fun bind(story: Stories) {
            binding.apply {

                val glideUrl = glideUrls(url = story.photoUrl, token = sharedPrefManager.token)
                Glide.with(root.context)
                    .load(glideUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageStory)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvName.text = Html.fromHtml("<b>${story.name}</b>   <font color='#666766'>${story.description}</font>", Html.FROM_HTML_MODE_LEGACY)
                } else tvName.text = "${story.name}  ${story.description}"
                tvCreatedAt.text = covertTimeToText(story.createdAt, tvCreatedAt.context)
                imgUser.setImageIcon(textAsBitmap(
                    text = story.name[0].toString().uppercase(),
                    textSize = 40F,
                    textColor = Color.WHITE
                ))
                tvUserName.text = story.name.lowercase()

            }
        }
    }


    override fun onBindViewHolder(holder: Holder, position: Int) {
        val curItem = getItem(position)
        if (curItem != null) holder.bind(curItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            ItemStoryOtherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
    }

    interface StoryClickListener {
        fun storyClicked(story: Stories)
    }
}