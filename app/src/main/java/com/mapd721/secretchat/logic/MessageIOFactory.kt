package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import java.security.PrivateKey

class MessageIOFactory(
    private val selfId: String,
    private val contact: Contact,
    private val localChat: Chat,
    private val selfPrivateKey: PrivateKey
) {
    fun getMessageSender(): MessageSender {
        return MessageSenderImp(
            selfId,
            contact.id,
            MessageCipherFactory.getCipherEncryptFromKey(contact.key),
            ChatFactory.getChatFirestore(selfId, contact.id),
            localChat
        )
    }

    fun getMessageReceiver(): MessageReceiver {
        return MessageReceiverImp(
            MessageCipherFactory.getCipherDecryptFromKey(selfPrivateKey),
            ChatFactory.getChatFirestore(contact.id, selfId),
            localChat
        )
    }
}