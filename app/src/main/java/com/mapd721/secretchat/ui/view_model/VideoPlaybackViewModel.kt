package com.mapd721.secretchat.ui.view_model

import android.widget.MediaController
import androidx.lifecycle.ViewModel

class VideoPlaybackViewModel: ViewModel() {
    var filePath = ""
    lateinit var mediaController: MediaController
}