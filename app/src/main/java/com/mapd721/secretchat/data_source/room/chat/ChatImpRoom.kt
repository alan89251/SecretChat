package com.mapd721.secretchat.data_source.room.chat

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.room.message.MessageDaoRoomAdapter

class ChatImpRoom: Chat {
    override var id: String
    private val messageDao: MessageDaoRoomAdapter

    constructor(id: String, messageDao: MessageDaoRoomAdapter) {
        this.id = id
        this.messageDao = messageDao
    }

    override fun addMessage(message: Message): String {
        return messageDao.insert(message)
    }
}