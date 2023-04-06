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
import com.mapd721.secretchat.databinding.FragmentVideoRecordBinding
import com.mapd721.secretchat.ui.fragment_result_listener.VideoRecordingResultListener
import com.mapd721.secretchat.ui.view_model.VideoRecordViewModel

class VideoRecordFragment : Fragment() {
    private lateinit var vm: VideoRecordViewModel
    private lateinit var binding: FragmentVideoRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = VideoRecordViewModel()
        vm.pathOfFolderOfRecordedVideo = requireActivity().filesDir.path + "/"
        vm.doStartRecording = ::doStartRecording
        vm.didStopRecording = ::didStopRecording
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoRecordBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        vm.setPreview(binding.cameraDisplayArea.surfaceProvider)
        vm.cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        vm.cameraProviderFuture
            .addListener(
                ::bindCameraUseCase,
                ContextCompat.getMainExecutor(requireContext())
            )

        return binding.root
    }

    private fun bindCameraUseCase() {
        vm.cameraProvider = vm.cameraProviderFuture.get()
        vm.cameraProvider.unbindAll()
        vm.cameraProvider.bindToLifecycle(
            this,
            CameraSelector.DEFAULT_BACK_CAMERA,
            vm.preview,
            vm.videoCapture
        )
    }

    @SuppressLint("MissingPermission")
    private fun doStartRecording(fileOutputOptions: FileOutputOptions) {
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

    private fun didStopRecording(filePath: String) {
        setFragmentResult(
            VideoRecordingResultListener.RESULT_LISTENER_KEY,
            bundleOf(VideoRecordingResultListener.ARG_FILE_PATH to filePath)
        )
        findNavController().popBackStack()
    }
}