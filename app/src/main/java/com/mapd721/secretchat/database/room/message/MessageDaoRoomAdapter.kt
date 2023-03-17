package com.mapd721.secretchat.database.room.message

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

    fun getBySenderIdAndReceiverId(senderId: String, receiverId: String): List<Message> {
        return dao.getBySenderIdAndReceiverId(
            senderId,
            receiverId
        ).map {
            it.toMessage()
        }
    }
}