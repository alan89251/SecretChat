package com.mapd721.secretchat.data_model.chat

interface ChatDao {
    fun getByReceiverId(receiverId: String): Chat
}