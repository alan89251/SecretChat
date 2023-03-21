package com.mapd721.secretchat.logic

import android.content.Context
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory

class MessageIOFactory(
    private val context: Context,
    private val senderId: String,
    private val contact: Contact,
    private val localChat: Chat
) {
    private val remoteChat: Chat

    init {
        remoteChat = ChatFactory.getRemoteChat(senderId, contact.id)
    }

    fun getMessageSender(): MessageSender {
        return MessageSenderImp(
            senderId,
            contact.id,
            MessageCipherEncryptFactory.getCipherFromKey(contact.key),
            remoteChat,
            localChat
        )
    }
}