package com.mapd721.secretchat.ui.view_model

import android.media.MediaRecorder
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date

class VideoRecordViewModel: ViewModel() {
    val isRecording = MutableLiveData<Boolean>(false)
    val btnRecordVisibility = MediatorLiveData<Int>()
    val btnStopVisibility = MediatorLiveData<Int>()
    private val videoRecorder = MediaRecorder()
    var pathOfFolderOfRecordedVideo = ""
    val fileExtension = "mp4"
    lateinit var didStopRecording: (String) -> Unit // arg1: path of the recorded video
    private var filePath = ""

    init {
        btnRecordVisibility.addSource(isRecording) {
            btnRecordVisibility.value = if (it) View.INVISIBLE else View.VISIBLE
        }
        btnStopVisibility.addSource(isRecording) {
            btnStopVisibility.value = if (it) View.VISIBLE else View.INVISIBLE
        }
        configVideoRecorder()
    }

    private fun configVideoRecorder() {
        videoRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        videoRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        videoRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        videoRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        videoRecorder.setAudioChannels(2)
        videoRecorder.setAudioSamplingRate(48000)
        videoRecorder.setAudioEncodingBitRate(128000)
        videoRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        videoRecorder.setVideoFrameRate(60)
        videoRecorder.setVideoEncodingBitRate(8000000)
        videoRecorder.setVideoSize(1920, 1080)
    }

    fun setCameraSurfaceHolder(surfaceHolder: SurfaceHolder) {
        videoRecorder.setPreviewDisplay(surfaceHolder.surface)
    }

    fun startRecordingVideo(view: View) {
        val fileName = "IMG_${SimpleDateFormat("yyyy_MM_dd_ss_SSS").format(Date())}.$fileExtension"
        filePath = "$pathOfFolderOfRecordedVideo$fileName"
        videoRecorder.setOutputFile(filePath)
        try {
            videoRecorder.prepare()
            videoRecorder.start()
            isRecording.value = true
        } catch (e: Exception) {
            Log.e("Video Recording: ", "Error: $e")
            videoRecorder.release()
        }
    }

    fun stopRecordingVideo(view: View) {
        videoRecorder.stop()
        videoRecorder.release()
        didStopRecording(filePath)
    }
}