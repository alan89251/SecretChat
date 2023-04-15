package com.mapd721.secretchat.ui.view_holder

import android.media.ThumbnailUtils
import android.provider.MediaStore
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.MessageDialogBodyImageBinding
import java.io.File

class VideoDialogBodyViewHolder(
    private val binding: MessageDialogBodyImageBinding,
    private val mediaFolderPath: String
    ): DialogBodyViewHolder {
    override fun setMessage(message: Message) {
        val file = File(mediaFolderPath + message.oriFileName)
        if (!file.exists()) {
            return
        }
        val thumbnail = ThumbnailUtils.createVideoThumbnail(
            mediaFolderPath + message.oriFileName,
            MediaStore.Video.Thumbnails.MINI_KIND
        )
        binding.mainContent.setImageBitmap(thumbnail)
    }
}