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

    override fun sendFile(name: String, bytes: ByteArray, mime: Message.Mime): Message {
        var message = Message()
        setMessageHeader(message)
        message.mime = mime
        message.oriFileName = cipher.encrypt(name)
        val encryptedBytes = cipher.encrypt(bytes)
        val pathOfUploadedFile = uploadToFirestore(encryptedBytes)
        message.uploadedFilePath = cipher.encrypt(pathOfUploadedFile)
        message = sendToFirebase(message)

        message.oriFileName = name
        message.uploadedFilePath = pathOfUploadedFile
        saveToDB(message)

        return message
    }

    override fun sendLocation(latitude: Double, longitude: Double): Message {
        val text = String.format("%.3f,%.3f", latitude, longitude)
        var message = Message()
        setMessageHeader(message)
        message.mime = Message.Mime.LOCATION
        message.text = cipher.encrypt(text)
        message = sendToFirebase(message)

        message.text = text
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