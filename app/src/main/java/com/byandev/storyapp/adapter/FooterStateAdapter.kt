package com.byandev.storyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.byandev.storyapp.databinding.ItemFooterStateBinding
import com.byandev.storyapp.utils.handlingError

class FooterStateAdapter (private val retry: () -> Unit) : LoadStateAdapter<FooterStateAdapter.Holder>() {

    class Holder private constructor(
        val binding: ItemFooterStateBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        companion object {
            fun create(parent: ViewGroup, retry: () -> Unit): Holder {
                return Holder(
                    ItemFooterStateBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    retry
                )
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.binding.apply {
            if (loadState is LoadState.Error) {
                tvErrorMessage.text = handlingError(loadState.error)
            } else if (loadState is LoadState.NotLoading) {
                tvErrorMessage.text = "End of list"
                btnRetry.isVisible = false
                tvErrorMessage.isVisible = true
            }

            progressBar.isVisible = loadState is LoadState.Loading
            btnRetry.isVisible = loadState !is LoadState.NotLoading
            tvErrorMessage.isVisible = loadState !is LoadState.Error
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        return Holder.create(parent, retry)

    }
}