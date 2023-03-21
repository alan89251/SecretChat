package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.firestore.chat.ChatFirestore
import com.mapd721.secretchat.encryption.MessageCipherDecrypt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageReceiverImp(
    private val cipher: MessageCipherDecrypt,
    private val remoteChat: ChatFirestore,
    private val localChat: Chat
): MessageReceiver {
    private var onMessageListener: ((Message) -> Unit)? = null
    private var isReceivedInitMessageBatch = false

    override fun listenMessage() {
        remoteChat.listenMessage { messages ->
            // only return new messages
            // therefore skip init message batch
            if (!isReceivedInitMessageBatch) {
                isReceivedInitMessageBatch = true
                return@listenMessage
            }

            messages.forEach {
                val message = it.toMessage()
                message.text = cipher.decrypt(message.text)
                message.type = Message.TYPE_RECEIVE
                CoroutineScope(Dispatchers.IO).launch {
                    localChat.addMessage(message)
                }
                onMessageListener?.invoke(message)
            }
        }
    }

    override fun setOnMessageListener(onMessage: (Message) -> Unit) {
        onMessageListener = onMessage
    }
}