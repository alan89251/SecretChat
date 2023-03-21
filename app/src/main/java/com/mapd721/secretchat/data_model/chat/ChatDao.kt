package com.mapd721.secretchat.data_model.chat

interface ChatDao {
    fun getBySenderId(senderId: String): Chat
}