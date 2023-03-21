package com.mapd721.secretchat.data_source.firestore.chat

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.firestore.message.MessageDaoFirestore
import com.mapd721.secretchat.data_source.firestore.message.MessageFirestore

class ChatFirestore: Chat {
    override var senderId: String
    override var receiverId: String
    private val messageDao: MessageDaoFirestore

    constructor(senderId: String, receiverId: String, messageDao: MessageDaoFirestore) {
        this.senderId = senderId
        this.receiverId = receiverId
        this.messageDao = messageDao
    }

    override fun addMessage(message: Message): String {
        return messageDao.insert(message)
    }

    override fun getAllSentMessages(): List<Message> {
        TODO("Not yet implemented")
        return ArrayList()
    }

    override fun getAllReceivedMessages(): List<Message> {
        TODO("Not yet implemented")
    }

    fun listenFriendMessage(onMessages: (List<MessageFirestore>) -> Unit) {
        messageDao.listenFriendMessage(onMessages)
    }
}