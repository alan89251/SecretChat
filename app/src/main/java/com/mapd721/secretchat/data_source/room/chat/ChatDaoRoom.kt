package com.mapd721.secretchat.data_source.room.chat

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.ChatDao
import com.mapd721.secretchat.data_source.room.message.MessageDaoRoomAdapter

class ChatDaoRoom(
    private val receiverId: String,
    private val messageDao: MessageDaoRoomAdapter
): ChatDao {
    override fun getBySenderId(senderId: String): Chat {
        /*val messages = messageDao.getBySenderIdAndReceiverId(
            senderId,
            receiverId
        )*/
        return ChatImpRoom(
            senderId,
            receiverId,
            messageDao
        )
    }
}