package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Message

interface MessageReceiver {
    fun listenMessage()
    fun setOnMessageListener(onMessage: (Message) -> Unit)
}