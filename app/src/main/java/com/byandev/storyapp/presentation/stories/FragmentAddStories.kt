package com.byandev.storyapp.presentation.stories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.byandev.storyapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAddStories : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_stories, container, false)
    }

}