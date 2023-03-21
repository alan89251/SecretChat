package com.mapd721.secretchat.data_model.chat

abstract class Chat {
    abstract var senderId: String
    abstract var receiverId: String

    /**
     * @return message id
     */
    abstract fun addMessage(message: Message): String
    abstract fun getAllSentMessages(): List<Message>

    abstract fun getAllReceivedMessages(): List<Message>
}