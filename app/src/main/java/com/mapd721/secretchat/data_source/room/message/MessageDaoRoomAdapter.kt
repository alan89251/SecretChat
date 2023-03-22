package com.mapd721.secretchat.data_source.room.message

import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.chat.MessageDao

class MessageDaoRoomAdapter(
    private val dao: MessageDaoRoom
): MessageDao {
    override fun insert(message: Message): String {
        dao.insert(
            MessageModel(message)
        )
        return message.id
    }

    fun getBySenderIdAndReceiverId(senderId: String, receiverId: String, type: Int): List<Message> {
        return dao.getBySenderIdAndReceiverId(
            senderId,
            receiverId,
            type
        ).map {
            it.toMessage()
        }
    }

    fun getLatestMessageBySenderIdAndReceiverId(senderId: String, receiverId: String, type: Int): Message? {
        val results = dao.getLatestMessageBySenderIdAndReceiverId(
            senderId,
            receiverId,
            type
        ).map {
            it.toMessage()
        }
        return if (results.isNotEmpty()) results[0] else null
    }
}