package com.mapd721.secretchat.repository

import android.content.Context
import com.mapd721.secretchat.database.firestore.chat.ChatDaoFirestore
import com.mapd721.secretchat.database.room.SecretChatDatabase
import com.mapd721.secretchat.database.room.chat.ChatDaoRoom
import com.mapd721.secretchat.database.room.message.MessageDaoRoomAdapter

class ChatRepositoryFactory(
    private val context: Context
) {
    fun getInstance(id: String): ChatRepository {
        return ChatRepository(
            SecretChatDatabase
                .getDatabaseClient(context)
                .chatDao(id),
            ChatDaoFirestore(id)
        )
    }
}