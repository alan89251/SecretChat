package com.mapd721.secretchat.ui.fragment_result_listener

import android.os.Bundle
import androidx.fragment.app.FragmentResultListener

class VideoRecordingResultListener(
    private val onResult: (String) -> Unit // arg1: path of the recorded video
): FragmentResultListener {
    override fun onFragmentResult(requestKey: String, result: Bundle) {
        onResult(result.getString(ARG_FILE_PATH) ?: "")
    }

    companion object {
        const val RESULT_LISTENER_KEY = "videoRecordingResult"
        const val ARG_FILE_PATH = "filePath"
    }
}