package com.mapd721.secretchat.logic

import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CloudImageDownloader(private val context: Context) {
    fun loadInto(imagePath: String, imageView: ImageView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val imageUrl = getDownloadUrlByPath(imagePath)

                withContext(Dispatchers.Main) {
                    GlideApp.with(context)
                        .load(imageUrl)
                        .into(imageView)
                }
            }
            catch (e: Exception) {
                Log.e("Load cloud image", "Failed: $e")
            }
        }
    }

    fun getDownloadUrlByPath(imagePath: String): String {
        val task = FirebaseStorage.getInstance()
            .reference
            .child(imagePath)
            .downloadUrl
        val result = Tasks.await(task)
        return result.toString()
    }
}