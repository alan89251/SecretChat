package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.firestore.chat.ChatFirestore
import com.mapd721.secretchat.data_source.repository.FileRepository
import com.mapd721.secretchat.encryption.MessageCipherDecrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MessageReceiverImp(
    private val cipher: MessageCipherDecrypt,
    private val remoteChat: ChatFirestore,
    private val localChat: Chat,
    private val fileRepository: FileRepository,
    private val mediaFolderPath: String
): MessageReceiver {
    private var onMessageListener: ((Message) -> Unit)? = null
    private var isReceivedInitMessageBatch = false

    override fun listenMessage() {
        remoteChat.listenFriendMessage { messages ->
            // only return new messages
            // therefore skip init message batch
            if (!isReceivedInitMessageBatch) {
                isReceivedInitMessageBatch = true
                return@listenFriendMessage
            }

            messages.forEach {
                CoroutineScope(Dispatchers.IO).launch {
                    val message = it.toMessage()
                    message.type = Message.TYPE_RECEIVE
                    when (message.mime) {
                        Message.Mime.TEXT -> parseTextMessage(message)
                        Message.Mime.IMAGE -> parseImageMessage(message)
                        Message.Mime.VIDEO -> parseVideoMessage(message)
                        Message.Mime.LOCATION -> parseLocationMessage(message)
                    }
                    localChat.addMessage(message)

                    withContext(Dispatchers.Main) {
                        onMessageListener?.invoke(message)
                    }
                }
            }
        }
    }

    private fun parseTextMessage(message: Message) {
        message.text = cipher.decrypt(message.text)
    }

    private fun parseImageMessage(message: Message) {
        message.uploadedFilePath = cipher.decrypt(message.uploadedFilePath)
        message.oriFileName = cipher.decrypt(message.oriFileName)
        downloadFileFromCloud(message.uploadedFilePath, message.oriFileName)
    }

    private fun parseVideoMessage(message: Message) {
        message.uploadedFilePath = cipher.decrypt(message.uploadedFilePath)
        message.oriFileName = cipher.decrypt(message.oriFileName)
        downloadFileFromCloud(message.uploadedFilePath, message.oriFileName)
    }

    private fun parseLocationMessage(message: Message) {
        message.text = cipher.decrypt(message.text)
    }

    override fun setOnMessageListener(onMessage: (Message) -> Unit) {
        onMessageListener = onMessage
    }

    private fun downloadFileFromCloud(uploadedFilePath: String, oriFileName: String) {
        val bytes = fileRepository.getSync(uploadedFilePath)
        val decryptedBytes = cipher.decrypt(bytes)
        val file = File(mediaFolderPath, oriFileName)
        file.createNewFile()
        file.outputStream().use {
            it.write(decryptedBytes)
        }
    }
}