package com.mapd721.secretchat.logic

import android.content.Context
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.data_source.repository.ContactRepositoryFactory

class MessageSenderFactory {
    fun getInstance(
        context: Context,
        senderId: String,
        contact: Contact
    ): MessageSender {
        return MessageSenderImp(
            senderId,
            contact.id,
            MessageCipherEncryptFactory.getCipherFromKey(contact.key),
            ChatFactory.getRemoteChat(senderId, contact.id),
            ChatFactory.getLocalChat(context, senderId, contact.id)
        )
    }
}