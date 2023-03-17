package com.mapd721.secretchat.data_model.chat

abstract class Chat {
    abstract var id: String
    abstract var messages: MutableList<Message>
    abstract fun addMessage(message: Message)
}