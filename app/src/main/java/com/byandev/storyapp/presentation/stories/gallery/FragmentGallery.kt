package com.byandev.storyapp.presentation.stories.gallery

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.byandev.storyapp.R
import com.byandev.storyapp.adapter.ImageGalleryAdapter
import com.byandev.storyapp.databinding.FragmentCameraBinding
import com.byandev.storyapp.databinding.FragmentGalleryBinding
import com.byandev.storyapp.presentation.stories.ActivityStoryForms
import com.byandev.storyapp.utils.PICK_IMAGE
import com.byandev.storyapp.utils.getEncoded64ImageStringFromBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class FragmentGallery : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ImageGalleryAdapter
    private val viewModel: GalleryViewModels by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ImageGalleryAdapter(requireContext())
        subscribeToObservers()
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.adapter = adapter

            adapter.setOnItemClickListener {
                File(it).let { data ->
                    val intent = Intent(requireContext(), ActivityStoryForms::class.java)
                    intent.putExtra("file", data.toURI())
                    requireActivity().startActivity(intent)
                }
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner) {
            val listTemp: MutableList<String> = mutableListOf()
            it?.let {
                listTemp.add("File")
                listTemp.addAll(it)
                adapter.changeList(listTemp)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}