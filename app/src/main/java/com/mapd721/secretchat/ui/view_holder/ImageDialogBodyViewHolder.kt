package com.mapd721.secretchat.ui.view_holder

import android.graphics.drawable.Drawable
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.MessageDialogBodyImageBinding

class ImageDialogBodyViewHolder(
    private val binding: MessageDialogBodyImageBinding,
    private val mediaFolderPath: String
    ): DialogBodyViewHolder {
    override fun setMessage(message: Message) {
        val image = Drawable.createFromPath(mediaFolderPath + message.oriFileName) ?: return
        binding.mainContent.setImageDrawable(image)
    }
}