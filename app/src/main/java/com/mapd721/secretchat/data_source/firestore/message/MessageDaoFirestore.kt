package com.mapd721.secretchat.data_source.firestore.message

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.toObject
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
            .add(MessageFirestore(message))
        val result = Tasks.await(task)
        return result.id
    }
    
    fun listenMessage(onMessages: (List<MessageFirestore>) -> Unit) {
        chatCollectionReference
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("MessageDaoFirestore", "listen:error", e)
                    return@addSnapshotListener
                }

                val messages = ArrayList<MessageFirestore>()
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            val message = dc.document.toObject<MessageFirestore>()
                            message.id = dc.document.id
                            messages.add(message)
                        }
                        else -> {}
                    }
                }
                onMessages(messages)
            }
    }
}