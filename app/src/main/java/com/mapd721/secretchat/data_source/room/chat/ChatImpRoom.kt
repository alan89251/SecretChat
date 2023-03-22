package com.mapd721.secretchat.data_source.room.chat

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.room.message.MessageDaoRoomAdapter

class ChatImpRoom: Chat {
    override var senderId: String
    override var receiverId: String
    private val messageDao: MessageDaoRoomAdapter

    constructor(senderId: String, receiverId: String, messageDao: MessageDaoRoomAdapter) {
        this.senderId = senderId
        this.receiverId = receiverId
        this.messageDao = messageDao
    }

    override fun addMessage(message: Message): String {
        return messageDao.insert(message)
    }

    override fun getAllSentMessages(): List<Message> {
        return messageDao.getBySenderIdAndReceiverId(senderId, receiverId, Message.TYPE_SNED)
    }

    override fun getAllReceivedMessages(): List<Message> {
        return messageDao.getBySenderIdAndReceiverId(receiverId, senderId, Message.TYPE_RECEIVE)
    }

    override fun getLatestMessage(): Message? {
        val latestSentMsg = messageDao.getLatestMessageBySenderIdAndReceiverId(senderId, receiverId, Message.TYPE_SNED)
        val latestReceivedMsg = messageDao.getLatestMessageBySenderIdAndReceiverId(receiverId, senderId, Message.TYPE_RECEIVE)
        if (latestSentMsg == null && latestReceivedMsg == null) {
            return null
        }
        if (latestSentMsg == null) {
            return latestReceivedMsg
        }
        if (latestReceivedMsg == null) {
            return latestSentMsg
        }
        if (latestSentMsg.sentDateTime.after(latestReceivedMsg.sentDateTime)) {
            return latestSentMsg
        }
        else {
            return latestReceivedMsg
        }
    }
}