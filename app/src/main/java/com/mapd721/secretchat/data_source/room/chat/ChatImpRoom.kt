package com.mapd721.secretchat.data_source.room.chat

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.room.message.MessageDaoRoomAdapter

class ChatImpRoom: Chat {
    override var id: String
    override var messages: MutableList<Message>
    private val messageDao: MessageDaoRoomAdapter

    constructor(id: String, messages: List<Message>, messageDao: MessageDaoRoomAdapter) {
        this.id = id
        this.messages = ArrayList(messages)
        this.messageDao = messageDao
    }

    override fun addMessage(message: Message): String {
        TODO("Not yet implemented")
        return message.id
    }
}