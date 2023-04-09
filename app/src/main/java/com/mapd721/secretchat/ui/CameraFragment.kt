package com.mapd721.secretchat.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.mapd721.secretchat.databinding.FragmentCameraBinding
import com.mapd721.secretchat.ui.fragment_result_listener.CameraResultListener
import com.mapd721.secretchat.ui.view_model.CameraViewModel

class CameraFragment : Fragment() {
    private lateinit var vm: CameraViewModel
    private lateinit var binding: FragmentCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = CameraViewModel()
        vm.pathOfFolderOfSavedFile = requireActivity().filesDir.path + "/"
        vm.cameraExecutor = ContextCompat.getMainExecutor(requireActivity())
        vm.doStartRecording = ::doStartRecording
        vm.didStopRecording = {
            returnResult(it, CameraResultListener.TYPE_VIDEO)
        }
        vm.doCapturingImage = ::doCapturingImage
        vm.didCapturedImage = {
            returnResult(it, CameraResultListener.TYPE_IMAGE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        vm.setPreview(binding.cameraDisplayArea.surfaceProvider)

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun doStartRecording(fileOutputOptions: FileOutputOptions) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture
            .addListener(
                {
                    onGetCameraProvider(cameraProviderFuture.get(), fileOutputOptions)
                },
                ContextCompat.getMainExecutor(requireContext())
            )
    }

    @SuppressLint("MissingPermission")
    private fun onGetCameraProvider(
        cameraProvider: ProcessCameraProvider,
        fileOutputOptions: FileOutputOptions
    ) {
        bindVideoUseCase(cameraProvider)
        vm.recording = vm.videoCapture.output
            .prepareRecording(
                requireContext(),
                fileOutputOptions
            )
            .withAudioEnabled()
            .start(
                ContextCompat.getMainExecutor(requireContext()),
                vm::onReceiveVideoRecordEvent
            )
    }

    private fun bindVideoUseCase(cameraProvider: ProcessCameraProvider) {
        vm.cameraProvider = cameraProvider
        vm.cameraProvider.unbindAll()
        vm.cameraProvider.bindToLifecycle(
            this,
            CameraSelector.DEFAULT_BACK_CAMERA,
            vm.preview,
            vm.videoCapture
        )
    }

    private fun returnResult(filePath: String, fileType: String) {
        setFragmentResult(
            CameraResultListener.RESULT_LISTENER_KEY,
            bundleOf(
                CameraResultListener.ARG_FILE_PATH to filePath,
                CameraResultListener.ARG_TYPE to fileType
            )
        )
        findNavController().popBackStack()
    }

    private fun doCapturingImage() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(
            {
                onGotCameraProvider(cameraProviderFuture.get())
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun onGotCameraProvider(cameraProvider: ProcessCameraProvider) {
        bindImageUseCase(cameraProvider)
        vm.captureImage()
    }

    private fun bindImageUseCase(cameraProvider: ProcessCameraProvider) {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            this,
            CameraSelector.DEFAULT_BACK_CAMERA,
            vm.preview,
            vm.imageCapture
        )
    }
}