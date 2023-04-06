package com.mapd721.secretchat.ui.view_model

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.common.util.concurrent.ListenableFuture
import java.text.SimpleDateFormat
import java.util.*

class VideoRecordViewModel: ViewModel() {
    val isRecording = MutableLiveData<Boolean>(false)
    val btnRecordVisibility = MediatorLiveData<Int>()
    val btnStopVisibility = MediatorLiveData<Int>()
    var pathOfFolderOfRecordedVideo = ""
    val fileExtension = "mp4"
    lateinit var doStartRecording: (MediaStoreOutputOptions) -> Unit
    lateinit var didStopRecording: (String) -> Unit // arg1: path of the recorded video
    private var filePath = ""
    lateinit var preview: Preview
    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    lateinit var cameraProvider: ProcessCameraProvider
    val recorder: Recorder
    val videoCapture: VideoCapture<Recorder>
    lateinit var recording: Recording
    lateinit var mediaStoreOutputOptionsBuilder: MediaStoreOutputOptions.Builder

    init {
        btnRecordVisibility.addSource(isRecording) {
            btnRecordVisibility.value = if (it) View.INVISIBLE else View.VISIBLE
        }
        btnStopVisibility.addSource(isRecording) {
            btnStopVisibility.value = if (it) View.VISIBLE else View.INVISIBLE
        }
        recorder = Recorder.Builder()
            .setQualitySelector(
                QualitySelector.from(Quality.HIGHEST)
            )
            .build()
        videoCapture = VideoCapture.withOutput(recorder)
    }

    fun setPreview(previewSurfaceProvider: SurfaceProvider) {
        preview = Preview.Builder()
            .build()
        preview.setSurfaceProvider(previewSurfaceProvider)
    }

    fun startRecordingVideo(view: View) {
        val fileName = "IMG_${SimpleDateFormat("yyyy_MM_dd_ss_SSS").format(Date())}.$fileExtension"
        /*filePath = "$pathOfFolderOfRecordedVideo$fileName"
        val file = File(filePath)
        val fileOutputOptions = FileOutputOptions.Builder(file)
            .build()*/
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }
        val mediaStoreOutputOptions = mediaStoreOutputOptionsBuilder.setContentValues(contentValues)
            .build()
        isRecording.value = true
        doStartRecording(mediaStoreOutputOptions)
    }

    fun stopRecordingVideo(view: View) {
        isRecording.value = false
        recording.stop()
    }

    fun onReceiveVideoRecordEvent(videoRecordEvent: VideoRecordEvent) {
        when (videoRecordEvent) {
            is Finalize -> test(videoRecordEvent)
        }
    }

    fun test(videoRecordEvent: VideoRecordEvent.Finalize) {
        Log.i("video stat", "${videoRecordEvent.recordingStats.recordedDurationNanos}")
        didStopRecording(filePath)
    }
}