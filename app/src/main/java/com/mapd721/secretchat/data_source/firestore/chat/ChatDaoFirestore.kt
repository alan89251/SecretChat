package com.mapd721.secretchat.data_source.firestore.chat

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.mapd721.secretchat.data_model.chat.Chat
import com.mapd721.secretchat.data_model.chat.ChatDao
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_source.firestore.message.MessageDaoFirestore

class ChatDaoFirestore(
    private val id: String
): ChatDao {
    companion object {
        const val COLLECTION_NAME = "chats"
    }

    private val db = FirebaseFirestore.getInstance()
    private val chatsDocument: DocumentReference

    init {
        chatsDocument = db.collection(COLLECTION_NAME)
            .document(id)
    }

    override fun getBySenderId(senderId: String): Chat {
        val chatCollectionReference = chatsDocument.collection(senderId)
        return ChatFirestore(
            id,
            senderId,
            MessageDaoFirestore(chatCollectionReference)
        )
    }

    fun getBySenderIdAsChatFirestore(senderId: String): ChatFirestore {
        val chatCollectionReference = chatsDocument.collection(senderId)
        return ChatFirestore(
            id,
            senderId,
            MessageDaoFirestore(chatCollectionReference)
        )
    }

    private fun parseMessage(doc: QueryDocumentSnapshot): Message {
        val message = doc.toObject<Message>()
        message.id = doc.id
        return message
    }
}