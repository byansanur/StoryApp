package com.byandev.storyapp.presentation.details

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.data.model.dto.Stories
import com.byandev.storyapp.presentation.R
import com.byandev.storyapp.presentation.covertTimeToText
import com.byandev.storyapp.presentation.databinding.FragmentDetailStoryBinding
import com.byandev.storyapp.presentation.glideUrls
import com.byandev.storyapp.presentation.textAsBitmap
import org.koin.android.ext.android.inject

class FragmentDetailStory : BaseFragment<FragmentDetailStoryBinding>() {


    private val args: FragmentDetailStoryArgs by navArgs()
    private lateinit var story: Stories

    private val sharedPrefManager by inject<SharedPrefManager>()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailStoryBinding {
        return FragmentDetailStoryBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        story = args.detailStories
        listener()
    }

    private fun listener() {
        binding.apply {
            val glideUrl = glideUrls(url = story.photoUrl, token = sharedPrefManager.token)
            Glide.with(requireContext())
                .load(glideUrl)
                .fitCenter()
                .error(R.drawable.ic_dicoding)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Toast.makeText(requireContext(),getString(R.string.failed_to_load_photo), Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(photoView)
            imgUser.setImageIcon(
                textAsBitmap(
                    text = story.name[0].toString().uppercase(),
                    textSize = 40F,
                    textColor = Color.WHITE
                )
            )
            tvUserName.text = story.name
            tvDesc.text = story.description
            tvUpdated.text = covertTimeToText(story.createdAt, requireContext())

        }
    }

}