package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.repository.FileRepository
import com.mapd721.secretchat.encryption.MessageCipherEncrypt
import java.util.*

class MessageSenderNetwork (
    private val senderId: String,
    private val receiverId: String,
    private val cipher: MessageCipherEncrypt,
    private val remoteChat: Chat,
    private var localChat: Chat,
    private val remoteFileRepository: FileRepository
): MessageSender {
    override fun send(text: String): Message {
        var message = Message()
        setMessageHeader(message)
        message.mime = Message.Mime.TEXT
        message.text = cipher.encrypt(text)
        message = sendToFirebase(message)

        message.text = text
        saveToDB(message)

        return message
    }

    override fun sendImage(name: String, bytes: ByteArray): Message {
        var message = Message()
        setMessageHeader(message)
        message.mime = Message.Mime.IMAGE
        message.oriFileName = cipher.encrypt(name)
        val pathOfUploadedImage = uploadToFirestore(message.image!!)
        message.uploadedFilePath = cipher.encrypt(pathOfUploadedImage)
        message = sendToFirebase(message)

        message.oriFileName = name
        message.uploadedFilePath = pathOfUploadedImage
        saveToDB(message)

        return message
    }

    private fun sendToFirebase(message: Message): Message {
        message.id = remoteChat.addMessage(message)
        return message
    }

    /**
     * @return path of uploaded file
     */
    private fun uploadToFirestore(bytes: ByteArray): String {
        return remoteFileRepository.saveSync(bytes)
    }

    private fun saveToDB(message: Message) {
        localChat.addMessage(message)
    }

    private fun setMessageHeader(message: Message) {
        message.senderId = senderId
        message.receiverId = receiverId
        message.type = Message.TYPE_SNED
        message.sentDateTime = Date()
    }
}