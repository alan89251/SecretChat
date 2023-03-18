package com.mapd721.secretchat.data_source.firestore.contact

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKey
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKeyDao

class EncryptionKeyDaoFirestore: EncryptionKeyDao {
    companion object {
        const val COLLECTION_NAME = "contacts"
    }

    private val db = FirebaseFirestore.getInstance()

    override fun insert(encryptionKey: EncryptionKey) {
        val task = db.collection(COLLECTION_NAME)
            .document(encryptionKey.id)
            .set(encryptionKey)
        val result = Tasks.await(task)
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

    override fun getById(id: String): EncryptionKey? {
        val task = db.collection(COLLECTION_NAME)
            .document(id)
            .get()
        val doc = Tasks.await(task)
        return if (doc.exists()) {
            doc.toObject<EncryptionKey>()!!
        } else {
            null
        }
    }
}