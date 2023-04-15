package com.mapd721.secretchat.ui.view_holder

import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.databinding.MessageDialogBodyTextBinding

class TextDialogBodyViewHolder(private val binding: MessageDialogBodyTextBinding): DialogBodyViewHolder {
    override fun setMessage(message: Message) {
        binding.mainContent.text = message.text
    }
}