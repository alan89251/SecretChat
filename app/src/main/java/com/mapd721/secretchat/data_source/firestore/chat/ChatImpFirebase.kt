package com.mapd721.secretchat.data_source.firestore.chat

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.firestore.message.MessageDaoFirestore

class ChatImpFirebase: Chat {
    override var id: String
    private val messageDao: MessageDaoFirestore

    constructor(id: String, messageDao: MessageDaoFirestore) {
        this.id = id
        this.messageDao = messageDao
    }

    override fun addMessage(message: Message): String {
        return messageDao.insert(message)
    }
}