package com.mapd721.secretchat.repository

import com.mapd721.secretchat.database.firestore.contact.EncryptionKeyDaoFirestore

class EncryptionKeyRepositoryFactory {
    fun getRemoteRepository(): EncryptionKeyRepository {
        return EncryptionKeyRepository(
            EncryptionKeyDaoFirestore()
        )
    }
}