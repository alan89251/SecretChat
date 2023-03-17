package com.mapd721.secretchat.data_model.chat

interface MessageDao {
    /**
     * @return id
     */
    fun insert(message: Message): String
}