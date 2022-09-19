package com.byandev.storyapp.presentation

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.R
import com.byandev.storyapp.adapter.SlidePermissionAdapter
import com.byandev.storyapp.data.model.IntroItem
import com.byandev.storyapp.databinding.FragmentIntroPermissionBinding
import com.byandev.storyapp.di.SharedPrefManager
import com.google.android.material.tabs.TabLayoutMediator
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class FragmentIntroPermission : Fragment() {

    private var _binding: FragmentIntroPermissionBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroPermissionBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { requireActivity().finishAffinity() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        setupViewPager()
    }

    private fun setupViewPager() {
        binding.apply {
            viewPager.offscreenPageLimit = 1
            viewPager.isUserInputEnabled = false
            val list: MutableList<IntroItem> = mutableListOf()
            list.add(
                IntroItem(
                    "Camera Access",
                    R.drawable.ic_camera_permission,
                    "App requires camera access"
                )
            )
            list.add(
                IntroItem(
                    "File Access",
                    R.drawable.ic_file_permission,
                    "App requires file access"
                )
            )
            list.add(
                IntroItem(
                    "Location Access",
                    R.drawable.ic_location_permission,
                    "App requires location access"
                )
            )
            viewPager.adapter = SlidePermissionAdapter(list)
            TabLayoutMediator(tabLayout, viewPager) { tab, _ ->
                tab.view.isClickable = false
                tab.view.isEnabled = false
            }.attach()
            btnPermission.setOnClickListener {
                when (viewPager.currentItem) {
                    0 -> {
                        //ask permission camera
                        checkCameraPermission()
                    }
                    1 -> {
                        //ask permission file
                        checkFilePermission()
                    }
                    2 -> {
                        //ask permission location
                        checkLocationPermission()
                    }
                }
            }
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_CAMERA)
    private fun checkCameraPermission() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)) {
            //next page
            binding.viewPager.setCurrentItem(1, true)
            Timber.wtf("CAMERA ALLOWED")
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Allow the app to access the camera",
                REQUEST_CODE_CAMERA,
                Manifest.permission.CAMERA
            )
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_FILE)
    private fun checkFilePermission() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //next page
            binding.viewPager.setCurrentItem(2, true)
            Timber.wtf("FILE ALLOWED")
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Allow the app to access the media file",
                REQUEST_CODE_FILE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun checkLocationPermission() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest
                .permission.ACCESS_COARSE_LOCATION)) {
            //end page
            sharedPrefManager.isIntroSp = false
            findNavController().navigate(FragmentIntroPermissionDirections.actionFragmentIntroPermissionToFragmentHome())
        } else {
            EasyPermissions.requestPermissions(this, "Allow the app to access the location",
                REQUEST_CODE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 1001
        private const val REQUEST_CODE_FILE = 1002
        private const val REQUEST_CODE_LOCATION = 1003
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


}