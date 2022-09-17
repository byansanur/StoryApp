package com.byandev.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.byandev.storyapp.data.model.IntroItem
import com.byandev.storyapp.databinding.ItemIntroPermissionBinding

class SlidePermissionAdapter(
    private val list: MutableList<IntroItem>
) : RecyclerView.Adapter<SlidePermissionAdapter.IntroViewHolder>() {
    class IntroViewHolder(val binding: ItemIntroPermissionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroViewHolder {
        return IntroViewHolder(
            ItemIntroPermissionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: IntroViewHolder, position: Int) {
        list[position].let { data ->
            holder.binding.apply {
                title.text = data.title
                image.setImageResource(data.image)
                description.text = data.description
            }
        }
    }

    override fun getItemCount(): Int = list.size
}