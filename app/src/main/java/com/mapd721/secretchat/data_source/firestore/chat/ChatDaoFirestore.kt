package com.mapd721.secretchat.data_source.firestore.chat

import com.google.android.gms.tasks.Tasks
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

    override fun getByReceiverId(receiverId: String): Chat {
        val chatCollectionReference = chatsDocument.collection(receiverId)
        /*val task = chatCollectionReference.get()
        val docs = Tasks.await(task)
        val messages = ArrayList<Message>()
        for (doc in docs) {
            messages.add(
                parseMessage(doc)
            )
        }*/
        return ChatImpFirebase(
            id,
            receiverId,
            MessageDaoFirestore(chatCollectionReference)
        )
    }

    private fun parseMessage(doc: QueryDocumentSnapshot): Message {
        val message = doc.toObject<Message>()
        message.id = doc.id
        return message
    }
}