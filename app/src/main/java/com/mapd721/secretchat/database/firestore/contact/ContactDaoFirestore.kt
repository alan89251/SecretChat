package com.mapd721.secretchat.database.firestore.contact

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKey
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKeyDao

class ContactDaoFirestore: EncryptionKeyDao {
    companion object {
        const val COLLECTION_NAME = "contacts"
    }

    private val db = FirebaseFirestore.getInstance()

    override fun insert(encryptionKey: EncryptionKey) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<EncryptionKey> {
        val task = db.collection(COLLECTION_NAME)
            .get()
        val docs = Tasks.await(task)
        val results = ArrayList<EncryptionKey>()
        for (doc in docs) {
            results.add(
                doc.toObject<EncryptionKey>()
            )
        }
        return results
    }

    override fun getById(id: Int): EncryptionKey? {
        val task = db.collection(COLLECTION_NAME)
            .document(id.toString())
            .get()
        val doc = Tasks.await(task)
        return if (doc.exists()) {
            doc.toObject<EncryptionKey>()!!
        } else {
            null
        }
    }
}