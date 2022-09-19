package com.byandev.storyapp.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.FragmentDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetailStory : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding!!

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}