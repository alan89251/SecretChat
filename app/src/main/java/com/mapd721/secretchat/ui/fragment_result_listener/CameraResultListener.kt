package com.mapd721.secretchat.ui.fragment_result_listener

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener

class CameraResultListener(
    private val onResult: (String, String) -> Unit // arg1: path of the saved file, arg2: type of the file
): FragmentResultListener {
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        onResult(
            result.getString(ARG_FILE_PATH) ?: "",
            result.getString(ARG_TYPE) ?: ""
        )
    }

    companion object {
        const val RESULT_LISTENER_KEY = "cameraResult"
        const val ARG_FILE_PATH = "filePath"
        const val ARG_TYPE = "type"
        const val TYPE_IMAGE = "image"
        const val TYPE_VIDEO = "video"
    }
}