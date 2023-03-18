package com.mapd721.secretchat.data_source.repository

import android.content.Context
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_source.ChatImpHybrid
import com.mapd721.secretchat.data_source.firestore.chat.ChatDaoFirestore
import com.mapd721.secretchat.data_source.room.SecretChatDatabase

class ChatFactory(
    private val context: Context
) {
    fun getInstance(
        senderId: String,
        receiverId: String
    ): Chat {
        return ChatImpHybrid(
            senderId,
            ArrayList(),
            SecretChatDatabase
                .getDatabaseClient(context)
                .chatDao(senderId)
                .getByReceiverId(receiverId),
            ChatDaoFirestore(senderId)
                .getByReceiverId(receiverId)
            )
    }
}