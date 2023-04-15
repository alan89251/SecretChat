package com.mapd721.secretchat.ui.view_holder

import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.MessageDialogBodyVideoBinding
import java.io.File

class VideoDialogBodyViewHolder(
    private val binding: MessageDialogBodyVideoBinding,
    private val mediaFolderPath: String
    ): DialogBodyViewHolder {
    override fun setMessage(message: Message) {
        val file = File(mediaFolderPath + message.oriFileName)
        if (!file.exists()) {
            return
        }
        binding.mainContent.setVideoPath(mediaFolderPath + message.oriFileName)
    }
}