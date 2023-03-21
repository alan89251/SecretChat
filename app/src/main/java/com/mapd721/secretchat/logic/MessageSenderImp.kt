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
    override fun send(text: String): Message {
        var message = Message()
        message.text = cipher.encrypt(text)
        message.senderId = senderId
        message.receiverId = receiverId
        message.type = Message.TYPE_SNED
        message.sentDateTime = Date()

        message = sendToFirebase(message)

        message.text = text
        saveToDB(message)

        return message
    }

    private fun sendToFirebase(message: Message): Message {
        message.id = remoteChat.addMessage(message)
        return message
    }

    private fun saveToDB(message: Message) {
        localChat.addMessage(message)
    }
}