package com.byandev.storyapp.presentation.stories.camera

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.FragmentCameraBinding
import com.byandev.storyapp.utils.PICK_IMAGE
import com.byandev.storyapp.utils.dialogLoading
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

@AndroidEntryPoint
class FragmentCamera : Fragment() {

    companion object {
        private const val TAG = "FragmentCamera"
    }

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private val executor = Executors.newSingleThreadExecutor()

    private var cameraProvideFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var lensFacing: Int? = CameraSelector.LENS_FACING_BACK
    private var flashing: Int? = ImageCapture.FLASH_MODE_OFF

    private lateinit var loading: Dialog
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lensFacing != null) {
            startCamera(lensFacing!!)
        } else {
            Toast.makeText(requireContext(), getString(R.string.check_permission_again), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
        requireActivity().runOnUiThread {
            listener()
        }
    }

    override fun onResume() {
        super.onResume()
        if (lensFacing != null) {
            startCamera(lensFacing!!)
        } else {
            Toast.makeText(requireContext(), getString(R.string.check_permission_again), Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    private fun listener() {
        loading = Dialog(requireContext())
        binding.apply {
            captureImage.setOnClickListener {
                captureImage.isEnabled = false
                dialogLoading(loading)
                val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale("in", "ID"))
                val file = File(getBatchDirectoryName(), dateFormat.format(Date()) + ".jpg")
                Log.e(TAG, "listener: $file")

                val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                Log.e(TAG, "listener: $outputFileOptions")
                imageCapture?.takePicture(
                    outputFileOptions,
                    executor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            Handler(Looper.getMainLooper()).post {
                                captureImage.isEnabled = true
                                lifecycleScope.launch {
                                    loading.dismiss()
                                    val navController = findNavController()
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        PICK_IMAGE,
                                        file
                                    )
                                    navController.navigateUp()
                                }

                            }

                        }

                        override fun onError(exception: ImageCaptureException) {
                            Handler(Looper.getMainLooper()).post {
                                captureImage.isEnabled = true
                                loading.dismiss()
                                exception.message?.let {
                                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                    Log.e(TAG, "onError: $it", )
                                }
                            }

                        }

                    })
            }
        }
    }

    private fun startCamera(lensFacing: Int) {
        binding.apply {
            previewView.post {
                cameraProvideFuture = ProcessCameraProvider.getInstance(requireContext())
                cameraProvideFuture?.addListener({
                    try {
                        cameraProvideFuture?.get()?.also {
                            it.unbindAll()
                            bindPreview(it, lensFacing)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(requireContext()))
            }
        }
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider, lensFacing: Int) {
        cameraProvider.unbindAll()
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(requireContext()),
            ImageProxy::close
        )
        val builder = ImageCapture.Builder()

        imageCapture = builder.setFlashMode(ImageCapture.FLASH_MODE_OFF).build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        } catch (e: Exception) {
            cameraProvider.unbindAll()
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (ex: Exception) {
                Toast.makeText(requireContext(), getString(R.string.failed_access_camera), Toast
                    .LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }


    private fun getBatchDirectoryName(): String {
        val appFolderPath = requireContext().externalCacheDir.toString() + "/images"
        Log.e(TAG, "getBatchDirectoryName: $appFolderPath")
        val dir = File(appFolderPath)
        Log.e(TAG, "getBatchDirectoryName: $dir")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return appFolderPath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loading.dismiss()
        cameraProvider?.unbindAll()
        _binding = null
    }

}