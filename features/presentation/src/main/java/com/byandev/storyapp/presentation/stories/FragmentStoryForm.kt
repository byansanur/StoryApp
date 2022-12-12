package com.byandev.storyapp.presentation.stories

import android.app.Dialog
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.byandev.storyapp.common.LocationUtils
import com.byandev.storyapp.common.UtilsConnect
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.presentation.*
import com.byandev.storyapp.presentation.databinding.FragmentStoryFormBinding
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import java.io.File


class FragmentStoryForm : BaseFragment<FragmentStoryFormBinding>() {


    private lateinit var filePath: File
    private var lat: String? = null
    private var lon: String? = null
    private lateinit var description: String

    private var isCheckLocation = false

    private val viewModel: SharedViewModel by viewModels()
    private val presentationViewModel: PresentationViewModel by viewModels()

    private lateinit var dialog: Dialog

    private lateinit var locationUtils : LocationUtils
    private lateinit var utilsConnect : UtilsConnect

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStoryFormBinding {
        return FragmentStoryFormBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        locationUtils = LocationUtils(requireContext())
        utilsConnect = UtilsConnect(requireContext())
        dialog = Dialog(requireContext())
        handleBackStack()
        listener()
    }

    private fun listener() {
        binding.apply {

            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

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
//                findNavController().navigate(FragmentStoryFormDirections.actionFragmentStoryFormToFragmentAddStories())
            }

            btnPostStory.setOnClickListener {
                lifecycleScope.launch {
                    dialogLoading(dialog)
                    delay(2000)
                    if (utilsConnect.isConnectedToInternet()) postingStory()
                    else {
                        Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
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

        lifecycleScope.launch {
            dialogLoading(dialog)
            delay(200)
            when {
                utilsConnect.isConnectedToInternet() -> {
                    presentationViewModel.postStories(
                        convertRequestBody(description),
                        convertRequestBody(filePath),
                        latPart,
                        lonPart
                    ).map { result ->
                        when (result.data?.error) {
                            false -> {
                                Toast.makeText(requireContext(), getString(R.string.success_add_story), Toast
                                    .LENGTH_SHORT)
                                    .show()
                                findNavController().navigateUp()
                            }
                            else -> {
                                Toast.makeText(requireContext(), "Error:${result.message}", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        dialog.dismiss()
                    }
                }
                else -> {
                    Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
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
                    Glide.with(requireContext())
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

}