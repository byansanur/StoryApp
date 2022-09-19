package com.byandev.storyapp.presentation.stories

import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.FragmentStoryFormBinding
import com.byandev.storyapp.di.GlideApp
import com.byandev.storyapp.di.LocationUtils
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.utils.PICK_IMAGE
import com.byandev.storyapp.utils.Resources
import com.byandev.storyapp.utils.convertRequestBody
import com.byandev.storyapp.utils.dialogLoading
import dagger.hilt.android.AndroidEntryPoint
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class FragmentStoryForm : Fragment() {

    private var _binding: FragmentStoryFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var filePath: File
    private var lat: String? = null
    private var lon: String? = null
    private lateinit var description: String

    private var isCheckLocation = false

    private val viewModel: SharedViewModel by viewModels()

    private lateinit var dialog: Dialog

    @Inject
    lateinit var locationUtils: LocationUtils


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = Dialog(requireContext())
        handleBackStack()
        listener()

    }

    private fun listener() {
        binding.apply {

            edtCaption.doOnTextChanged { text, start, before, count ->
                description = text.toString()
            }

            switchLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    lat = getLatitude()
                    lon = getLongitude()
                    Log.e(TAG, "listener: true $lat - $lon")
                } else {
                    lat = null
                    lon = null
                    Log.e(TAG, "listener: false $lat - $lon")
                }
            }

            imgPreview.setOnClickListener {
                findNavController().navigate(FragmentStoryFormDirections.actionFragmentStoryFormToFragmentAddStories())
            }

            btnPostStory.setOnClickListener {
                lifecycleScope.launch {
                    dialogLoading(dialog)
                    delay(2000)
                    postingStory()
                }
            }
        }

    }

    private fun postingStory() {

        var latPart: RequestBody? = null
        var lonPart: RequestBody? = null
        if (lat?.isNotEmpty() == true && lon?.isNotEmpty() == true) {
            latPart = convertRequestBody(lat!!)
            lonPart = convertRequestBody(lon!!)
        }

        viewModel.postStories(
            description = convertRequestBody(description),
            photo = convertRequestBody(filePath),
            lat = latPart,
            lon = lonPart
        ).observe(viewLifecycleOwner) {
            when(it) {
                is Resources.Loading -> {}
                is Resources.Success -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), getString(R.string.success_add_story), Toast
                        .LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                }
                is Resources.Error -> {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getLatitude(): String {
        var lati = ""
        if (locationUtils.canGetLocation) {
            lati = locationUtils.latitude.toString()
        }
        return lati
    }

    private fun getLongitude(): String {
        var longi = ""
        if (locationUtils.canGetLocation) {
            longi = locationUtils.longitude.toString()
        }
        return longi
    }

    private fun handleBackStack() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<File>(
            PICK_IMAGE
        )?.observe(viewLifecycleOwner) { files ->
            files.let {
                lifecycleScope.launch {
                    dialogLoading(dialog)
                    delay(1000)
                    val compressedImageFile = Compressor.compress(requireContext(), files) {
                        resolution(1280, 720)
                        quality(80)
                        format(Bitmap.CompressFormat.JPEG)
                        size(1_097_152) // 2 MB
                    }
                    filePath = compressedImageFile
                    dialog.dismiss()
                    GlideApp.with(requireContext())
                        .load(File(filePath.path))
                        .centerCrop()
                        .into(binding.imgPreview)
                }
            }
        }
    }
    companion object {
        private const val TAG = "FragmentStoryForm"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}