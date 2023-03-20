package com.mapd721.secretchat.data_source.repository

import android.content.Context
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_source.firestore.chat.ChatDaoFirestore
import com.mapd721.secretchat.data_source.firestore.chat.ChatImpFirebase
import com.mapd721.secretchat.data_source.room.SecretChatDatabase

class ChatFactory {
    companion object {
        fun getRemoteChat(senderId: String, receiverId: String): Chat {
            return ChatDaoFirestore(senderId)
                .getByReceiverId(receiverId)
        }

        fun getLocalChat(context: Context, senderId: String, receiverId: String): Chat {
            return SecretChatDatabase
                .getDatabaseClient(context)
                .chatDao(senderId)
                .getByReceiverId(receiverId)
        }
    }
}