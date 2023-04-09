package com.mapd721.secretchat.ui.view_model

import android.util.Log
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.video.VideoRecordEvent.Finalize
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

class CameraViewModel: ViewModel() {
    // Record video
    val isRecording = MutableLiveData<Boolean>(false)
    val btnRecordVisibility = MediatorLiveData<Int>()
    val btnStopVisibility = MediatorLiveData<Int>()
    val btnCaptureVisibility = MediatorLiveData<Int>()
    var pathOfFolderOfSavedFile = ""
    val videoFileExtension = "mp4"
    lateinit var doStartRecording: (FileOutputOptions) -> Unit
    lateinit var didStopRecording: (String) -> Unit // arg1: path of the recorded video
    private var filePath = ""
    lateinit var preview: Preview
    lateinit var cameraProvider: ProcessCameraProvider
    val recorder: Recorder
    val videoCapture: VideoCapture<Recorder>
    lateinit var recording: Recording

    // Capture image
    lateinit var cameraExecutor: Executor
    val imageCapture: ImageCapture
    val imageFileExtension = "jpg"
    lateinit var doCapturingImage: () -> Unit
    lateinit var didCapturedImage: (String) -> Unit // arg1: path of the saved image

    init {
        btnRecordVisibility.addSource(isRecording) {
            btnRecordVisibility.value = if (it) View.INVISIBLE else View.VISIBLE
        }
        btnStopVisibility.addSource(isRecording) {
            btnStopVisibility.value = if (it) View.VISIBLE else View.INVISIBLE
        }
        btnCaptureVisibility.addSource(isRecording) {
            btnCaptureVisibility.value = if (it) View.INVISIBLE else View.VISIBLE
        }
        recorder = Recorder.Builder()
            .setQualitySelector(
                QualitySelector.from(Quality.HIGHEST)
            )
            .build()
        videoCapture = VideoCapture.withOutput(recorder)
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .build()
    }

    fun setPreview(previewSurfaceProvider: SurfaceProvider) {
        preview = Preview.Builder()
            .build()
        preview.setSurfaceProvider(previewSurfaceProvider)
    }

    fun startRecordingVideo(view: View) {
        val fileName = "IMG_${SimpleDateFormat("yyyy_MM_dd_ss_SSS").format(Date())}.$videoFileExtension"
        filePath = "$pathOfFolderOfSavedFile$fileName"
        val file = File(filePath)
        val fileOutputOptions = FileOutputOptions.Builder(file)
            .build()
        isRecording.value = true
        doStartRecording(fileOutputOptions)
    }

    fun stopRecordingVideo(view: View) {
        isRecording.value = false
        recording.stop()
    }

    fun onReceiveVideoRecordEvent(videoRecordEvent: VideoRecordEvent) {
        when (videoRecordEvent) {
            is Finalize -> didStopRecording(filePath)
        }
    }

    fun onBtnCaptureClick(view: View) {
        doCapturingImage()
    }

    fun captureImage() {
        val fileName = "IMG_${SimpleDateFormat("yyyy_MM_dd_ss_SSS").format(Date())}.$imageFileExtension"
        filePath = "$pathOfFolderOfSavedFile$fileName"
        val file = File(filePath)
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(file)
            .build()

        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            onImageSaved
        )
    }

    private val onImageSaved = object: ImageCapture.OnImageSavedCallback {
        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            didCapturedImage(filePath)
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("Image Capture", "Error: $exception")
        }
    }
}