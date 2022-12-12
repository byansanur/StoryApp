package com.byandev.storyapp.presentation.stories.camera

import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.presentation.PICK_IMAGE
import com.byandev.storyapp.presentation.R
import com.byandev.storyapp.presentation.databinding.FragmentCameraBinding
import com.byandev.storyapp.presentation.dialogLoading
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class FragmentCamera : BaseFragment<FragmentCameraBinding>() {

    companion object {
        private const val TAG = "FragmentCamera"
    }


    private val executor = Executors.newSingleThreadExecutor()

    private var cameraProvideFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var lensFacing: Int? = CameraSelector.LENS_FACING_BACK

    private lateinit var loading: Dialog

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCameraBinding {
        return FragmentCameraBinding.inflate(inflater, container, false)
    }

    override fun initView() {
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
            flipCamera.setOnClickListener {
                var lens = 0
                if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                    lens = CameraSelector.LENS_FACING_FRONT
                } else if (lensFacing == CameraSelector.LENS_FACING_FRONT){
                    lens = CameraSelector.LENS_FACING_BACK
                }
                lensFacing = lens
                startCamera(lens)

            }

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
                                    Log.e(TAG, "onError: $it")
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

        imageCapture = builder
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .build()

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
    }

}