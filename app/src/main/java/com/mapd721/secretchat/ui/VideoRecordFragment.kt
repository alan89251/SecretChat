package com.mapd721.secretchat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
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
        vm.didStopRecording = ::didStopRecording
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoRecordBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        val surface = SurfaceView(requireActivity().applicationContext)
        val surfaceHolder = surface.holder
        binding.cameraDisplayArea.addView(surface)
        vm.setCameraSurfaceHolder(surfaceHolder)

        return binding.root
    }

    private fun didStopRecording(filePath: String) {
        setFragmentResult(
            VideoRecordingResultListener.RESULT_LISTENER_KEY,
            bundleOf(VideoRecordingResultListener.ARG_FILE_PATH to filePath)
        )
        findNavController().popBackStack()
    }
}