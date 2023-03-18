package com.mapd721.secretchat.data_source.repository

import com.mapd721.secretchat.data_source.firestore.contact.EncryptionKeyDaoFirestore

class EncryptionKeyRepositoryFactory {
    fun getRemoteRepository(): EncryptionKeyRepository {
        return EncryptionKeyRepository(
            EncryptionKeyDaoFirestore()
        )
    }
}