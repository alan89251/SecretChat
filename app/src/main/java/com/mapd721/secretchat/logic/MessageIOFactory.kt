package com.mapd721.secretchat.logic

import android.content.Context
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.firestore.chat.ChatFirestore
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.encryption.EncryptionKeyPairManager

class MessageIOFactory(
    private val context: Context,
    private val senderId: String,
    private val contact: Contact,
    private val localChat: Chat,
    private val keyPairManager: EncryptionKeyPairManager
) {
    private val remoteChat: ChatFirestore

    init {
        remoteChat = ChatFactory.getChatFirestore(senderId, contact.id)
    }

    fun getMessageSender(): MessageSender {
        return MessageSenderImp(
            senderId,
            contact.id,
            MessageCipherFactory.getCipherEncryptFromKey(contact.key),
            remoteChat,
            localChat
        )
    }

    fun getMessageReceiver(): MessageReceiver {
        return MessageReceiverImp(
            senderId,
            MessageCipherFactory.getCipherDecryptFromKey(
                keyPairManager.getKey(senderId)!!
            ),
            remoteChat,
            localChat
        )
    }
}