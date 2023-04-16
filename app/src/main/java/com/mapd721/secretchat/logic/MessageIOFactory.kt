package com.mapd721.secretchat.logic

import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_source.repository.ChatFactory
import com.mapd721.secretchat.data_source.repository.FileRepositoryFactory
import java.security.PrivateKey

class MessageIOFactory(
    private val selfId: String,
    private val selfPrivateKey: PrivateKey,
    private val chatFactory: ChatFactory,
    private val mode: Mode,
    private val cloudStorageRootFolderName: String
) {
    fun getMessageSender(contact: Contact): MessageSender {
        return MessageSenderNetwork(
            selfId,
            contact.id,
            MessageCipherFactory.getCipherEncryptFromKey(contact.key),
            chatFactory.getChatFirestore(selfId, contact.id),
            chatFactory.getLocalChat(selfId, contact.id),
            FileRepositoryFactory.getFireStore(cloudStorageRootFolderName)
        )
    }

    fun getMessageReceiver(contact: Contact, mediaFolderPath: String): MessageReceiver {
        return MessageReceiverImp(
            MessageCipherFactory.getCipherDecryptFromKey(selfPrivateKey),
            chatFactory.getChatFirestore(contact.id, selfId),
            chatFactory.getLocalChat(selfId, contact.id),
            FileRepositoryFactory.getFireStore(cloudStorageRootFolderName),
            mediaFolderPath
        )
    }

    enum class Mode {
        UI,
        SERVICE
    }
}