package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.encryption.MessageCipherEncrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MessageSenderImp (
    private val senderId: String,
    private val receiverId: String,
    private val cipher: MessageCipherEncrypt,
    private val remoteChat: Chat,
    private var localChat: Chat
): MessageSender {
    override fun send(text: String) {
        sendToFirebase(text)
        saveToDB(text)
    }

    private fun sendToFirebase(text: String) {
        val message = Message()
        message.text = cipher.encrypt(text)
        message.senderId = senderId
        message.receiverId = receiverId
        message.type = Message.TYPE_SNED
        message.sentDateTime = Date()
        remoteChat.addMessage(message)
    }

    private fun saveToDB(text: String) {
        val message = Message()
        message.text = text
        message.senderId = senderId
        message.receiverId = receiverId
        message.type = Message.TYPE_SNED
        message.sentDateTime = Date()
        localChat.addMessage(message)
    }
}