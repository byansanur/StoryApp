package com.byandev.storyapp.presentation.details

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.byandev.storyapp.R
import com.byandev.storyapp.data.model.Story
import com.byandev.storyapp.databinding.FragmentDetailStoryBinding
import com.byandev.storyapp.di.GlideApp
import com.byandev.storyapp.di.SharedPrefManager
import com.byandev.storyapp.utils.covertTimeToText
import com.byandev.storyapp.utils.glideUrls
import com.byandev.storyapp.utils.textAsBitmap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDetailStory : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

    private val args: FragmentDetailStoryArgs by navArgs()
    private lateinit var story: Story

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        story = args.story
        listener()
    }

    private fun listener() {
        binding.apply {
            val glideUrl = glideUrls(url = story.photoUrl, token = sharedPrefManager.token)
            GlideApp.with(requireContext())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}