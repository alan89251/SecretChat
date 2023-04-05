package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.firestore.chat.ChatFirestore
import com.mapd721.secretchat.encryption.MessageCipherDecrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageReceiverImp(
    private val cipher: MessageCipherDecrypt,
    private val remoteChat: ChatFirestore,
    private val localChat: Chat
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
                val message = it.toMessage()
                message.type = Message.TYPE_RECEIVE
                when (message.mime) {
                    Message.Mime.TEXT -> parseTextMessage(message)
                    Message.Mime.IMAGE -> parseImageMessage(message)
                    Message.Mime.VIDEO -> parseVideoMessage(message)
                    else -> {}
                }
                CoroutineScope(Dispatchers.IO).launch {
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
    }

    private fun parseVideoMessage(message: Message) {
        message.uploadedFilePath = cipher.decrypt(message.uploadedFilePath)
        message.oriFileName = cipher.decrypt(message.oriFileName)
    }

    override fun setOnMessageListener(onMessage: (Message) -> Unit) {
        onMessageListener = onMessage
    }
}