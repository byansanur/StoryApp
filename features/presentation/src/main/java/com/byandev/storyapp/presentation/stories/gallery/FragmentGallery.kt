package com.byandev.storyapp.presentation.stories.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.presentation.PICK_IMAGE
import com.byandev.storyapp.presentation.adapter.ImageGalleryAdapter
import com.byandev.storyapp.presentation.databinding.FragmentGalleryBinding
import java.io.File

class FragmentGallery : BaseFragment<FragmentGalleryBinding>() {


    private lateinit var adapter: ImageGalleryAdapter
    private val viewModel: GalleryViewModels by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentGalleryBinding {
        return FragmentGalleryBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        adapter = ImageGalleryAdapter()
        subscribeToObservers()
        binding.apply {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
            recyclerView.adapter = adapter

            adapter.setOnItemClickListener {
                File(it).let { data ->
                    val navController = findNavController()
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        PICK_IMAGE,
                        data
                    )
                    navController.navigateUp()
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

}