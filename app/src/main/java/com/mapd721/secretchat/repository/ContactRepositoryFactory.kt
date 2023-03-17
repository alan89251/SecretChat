package com.mapd721.secretchat.repository

import android.content.Context
import com.mapd721.secretchat.database.firestore.contact.ContactDaoFirestore
import com.mapd721.secretchat.database.room.SecretChatDatabase
import com.mapd721.secretchat.database.room.contact.ContactDaoRoomAdapter

class ContactRepositoryFactory (
    private val context: Context
) {
    fun getLocalRepository(): ContactRepository {
        val localDb = SecretChatDatabase.getDatabaseClient(context)
        return ContactRepository(
            ContactDaoRoomAdapter(
                localDb.contactModelDao()
            )
        )
    }

    fun getRemoteRepository(): ContactRepository {
        return ContactRepository(
            ContactDaoFirestore()
        )
    }
}