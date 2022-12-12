package com.byandev.storyapp.presentation.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.presentation.R
import com.byandev.storyapp.presentation.databinding.FragmentAddStoriesBinding
import com.byandev.storyapp.presentation.stories.camera.FragmentCamera
import com.byandev.storyapp.presentation.stories.gallery.FragmentGallery
import com.google.android.material.tabs.TabLayoutMediator

class FragmentAddStories : BaseFragment<FragmentAddStoriesBinding>() {

    private val cameraFragment : Fragment = FragmentCamera()
    private val galleryFragment : Fragment = FragmentGallery()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAddStoriesBinding {
        return FragmentAddStoriesBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        setupViewPager()
    }

    private fun setupViewPager() {
        binding.apply {
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            viewPager.offscreenPageLimit = 2
            viewPager.isUserInputEnabled = false
            viewPager.adapter = AddStoryFragmentStateAdapter(this@FragmentAddStories)
            TabLayoutMediator(tabLayoutPick, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.camera)
                    1 -> tab.text = getString(R.string.gallery)
                }
            }.attach()
            viewPager.setCurrentItem(0,true)
        }
    }


    inner class AddStoryFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            galleryFragment.arguments = arguments
            cameraFragment.arguments = arguments

            return when (position) {
                0 -> cameraFragment
                1 -> galleryFragment
                else -> cameraFragment
            }
        }

    }

}