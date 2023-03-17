package com.mapd721.secretchat.database.firestore.message

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.mapd721.secretchat.data_model.chat.Message
import com.mapd721.secretchat.data_model.chat.MessageDao

class MessageDaoFirestore(
    private val chatCollectionReference: CollectionReference
): MessageDao {
    /**
     * @return id
     */
    override fun insert(message: Message): String {
        val task = chatCollectionReference
            .add(message)
        val result = Tasks.await(task)
        return result.id
    }
}