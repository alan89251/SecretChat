package com.mapd721.secretchat.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mapd721.secretchat.R
import com.mapd721.secretchat.databinding.FragmentViewImageBinding
import com.mapd721.secretchat.ui.view_model.ViewImageViewModel

class ViewImageFragment: Fragment() {
    private lateinit var vm: ViewImageViewModel
    private lateinit var binding: FragmentViewImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewImageViewModel()
        arguments?.let {
            vm.filePath = it.getString(ARG_FILE_NAME)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewImageBinding.inflate(inflater, container, false)

        val image = Drawable.createFromPath(vm.filePath)
        binding.image.setImageDrawable(image)

        return binding.root
    }

    companion object {
        private const val ARG_FILE_NAME = "fileName"

        @JvmStatic
        fun newInstance(fileName: String) =
            ViewImageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_FILE_NAME, fileName)
                }
            }
    }
}