package com.mapd721.secretchat.data_source.repository

import android.content.Context
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_source.firestore.chat.ChatDaoFirestore
import com.mapd721.secretchat.data_source.firestore.chat.ChatFirestore
import com.mapd721.secretchat.data_source.room.SecretChatDatabase

class ChatFactory(
    private val context: Context
) {
    fun getChatFirestore(senderId: String, receiverId: String): ChatFirestore {
        return ChatDaoFirestore(receiverId)
            .getBySenderIdAsChatFirestore(senderId)
    }

    fun getLocalChat(senderId: String, receiverId: String): Chat {
        return SecretChatDatabase
            .getDatabaseClient(context)
            .chatDao(receiverId)
            .getBySenderId(senderId)
    }
}