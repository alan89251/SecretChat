package com.mapd721.secretchat.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import com.mapd721.secretchat.R
import com.mapd721.secretchat.databinding.FragmentVideoPlaybackBinding
import com.mapd721.secretchat.ui.view_model.VideoPlaybackViewModel

class VideoPlaybackFragment : Fragment() {
    private lateinit var vm: VideoPlaybackViewModel
    private lateinit var binding: FragmentVideoPlaybackBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = VideoPlaybackViewModel()
        vm.mediaController = MediaController(requireContext())
        arguments?.let {
            val fileName = it.getString(ARG_FILE_NAME)!!
            vm.filePath = requireActivity().filesDir.path + "/" +
                    resources.getString(R.string.media_storage_root) + "/" +
                    fileName

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVideoPlaybackBinding.inflate(inflater, container, false)
        binding.vm = vm
        binding.lifecycleOwner = this

        binding.videoPlayer.setMediaController(vm.mediaController)
        binding.videoPlayer.setVideoPath(vm.filePath)

        return binding.root
    }

    companion object {
        private const val ARG_FILE_NAME = "fileName"

        @JvmStatic
        fun newInstance(fileName: String) =
            VideoPlaybackFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILE_NAME, fileName)
                }
            }
    }
}