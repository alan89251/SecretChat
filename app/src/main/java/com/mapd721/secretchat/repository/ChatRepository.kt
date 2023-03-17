package com.mapd721.secretchat.repository

import com.mapd721.secretchat.data_model.chat.ChatDao

class ChatRepository(
    private val localDao: ChatDao,
    private val remoteDao: ChatDao
) {
}