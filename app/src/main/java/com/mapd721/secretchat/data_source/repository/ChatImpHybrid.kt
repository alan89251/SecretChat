package com.mapd721.secretchat.data_source.repository

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message

class ChatImpHybrid: Chat {
    override var id: String
    var messages: MutableList<Message>
    private var localChatStorage: Chat
    private var remoteChatStorage: Chat

    constructor(
        id: String,
        localChatStorage: Chat,
        remoteChatStorage: Chat
    ) {
        this.id = id
        this.localChatStorage = localChatStorage
        this.remoteChatStorage = remoteChatStorage
        messages = ArrayList()
    }

    override fun addMessage(message: Message): String {
        message.id = remoteChatStorage.addMessage(message)
        localChatStorage.addMessage(message)
        messages.add(message)
        return message.id
    }
}