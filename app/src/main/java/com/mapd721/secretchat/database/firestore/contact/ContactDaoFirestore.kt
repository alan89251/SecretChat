package com.mapd721.secretchat.database.firestore.contact

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.mapd721.secretchat.data_model.contact.Contact
import com.mapd721.secretchat.data_model.contact.ContactDao

class ContactDaoFirestore: ContactDao {
    companion object {
        const val COLLECTION_NAME = "contacts"
    }

    private val db = FirebaseFirestore.getInstance()

    override fun insert(contact: Contact) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Contact> {
        val task = db.collection(COLLECTION_NAME)
            .get()
        val docs = Tasks.await(task)
        val results = ArrayList<Contact>()
        for (doc in docs) {
            results.add(
                doc.toObject<ContactModel>()
                    .toContact()
            )
        }
        return results
    }

    override fun getById(id: Int): Contact? {
        val task = db.collection(COLLECTION_NAME)
            .document(id.toString())
            .get()
        val doc = Tasks.await(task)
        return if (doc.exists()) {
            doc.toObject<ContactModel>()!!
                .toContact()
        } else {
            null
        }
    }
}