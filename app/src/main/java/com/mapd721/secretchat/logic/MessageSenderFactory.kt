package com.mapd721.secretchat.logic

import android.content.Context
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.data_source.repository.ContactRepositoryFactory

class MessageSenderFactory {
    fun getInstance(
        context: Context,
        senderId: String,
        receiverId: String
    ): MessageSender {
        return MessageSenderImp(
            senderId,
            receiverId,
            MessageCipherEncryptFactory(
                ContactRepositoryFactory(context)
                    .getLocalRepository()
            )
                .getCipher(receiverId),
            ChatFactory(context)
                .getInstance(
                    senderId,
                    receiverId
                )
        )
    }
}