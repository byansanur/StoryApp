package com.byandev.storyapp.presentation.stories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.FragmentAddStoriesBinding
import com.byandev.storyapp.presentation.stories.camera.FragmentCamera
import com.byandev.storyapp.presentation.stories.gallery.FragmentGallery
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAddStories : Fragment() {

    private var _binding: FragmentAddStoriesBinding? = null
    private val binding get() = _binding!!

    private val cameraFragment : Fragment = FragmentCamera()
    private val galleryFragment : Fragment = FragmentGallery()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoriesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}