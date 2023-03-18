package com.mapd721.secretchat.data_model.chat

abstract class Chat {
    abstract var id: String
    abstract var messages: MutableList<Message>

    /**
     * @return message id
     */
    abstract fun addMessage(message: Message): String
}