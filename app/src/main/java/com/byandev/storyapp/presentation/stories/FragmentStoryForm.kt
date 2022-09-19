package com.byandev.storyapp.presentation.stories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.databinding.FragmentStoryFormBinding
import com.byandev.storyapp.di.GlideApp
import com.byandev.storyapp.di.LocationUtils
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.utils.PICK_IMAGE
import dagger.hilt.android.AndroidEntryPoint
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

        handleBackStack()
        listener()

    }

    private fun listener() {
        binding.apply {


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
        )?.observe(viewLifecycleOwner) {
            Log.e(TAG, "handleBackStack: $it")
            filePath = it
            GlideApp.with(requireContext())
                .load(File(it.path))
                .centerCrop()
                .into(binding.imgPreview)
        }
    }
    companion object {
        private const val REQUEST_CODE_IMAGE = 1001
        private const val REQUEST_CODE_LOCATION = 1002
        private const val TAG = "FragmentStoryForm"
    }


//    private fun dialogSelectCameraOrGallery() {
//        val dialogBuilder = AlertDialog.Builder(context, R.style.MyDialogTheme)
//        val inflater = requireActivity().layoutInflater
//        val dialogView = inflater.inflate(R.layout.item_dialog_confirm, null)
//        dialogBuilder.setView(dialogView)
//
//
//        val tvMessage = dialogView.findViewById<TextView>(R.id.tvInfoDialog)
//        val btnNegative = dialogView.findViewById<Button>(R.id.btnNegative)
//        val btnPositive = dialogView.findViewById<Button>(R.id.btnPositive)
//
//        tvMessage.text = getString(R.string.choose_from)
//        btnPositive.text = getString(R.string.camera)
//        btnNegative.text = getString(R.string.gallery)
//
//        val alertDialog = dialogBuilder.create()
//        btnNegative.setOnClickListener {
//            val nav = FragmentStoryFormDirections.actionStoryFormActivityToFragmentGallery()
//            findNavController().navigate(nav)
//            alertDialog.dismiss()
//        }
//
//        btnPositive.setOnClickListener {
//            val nav = FragmentStoryFormDirections.actionStoryFormActivityToFragmentCamera()
//            findNavController().navigate(nav)
//            alertDialog.dismiss()
//        }
//
//        alertDialog.show()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}