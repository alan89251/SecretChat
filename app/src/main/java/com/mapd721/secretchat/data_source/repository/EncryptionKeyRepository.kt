package com.mapd721.secretchat.data_source.repository

import com.mapd721.secretchat.data_model.encryption_key.EncryptionKey
import com.mapd721.secretchat.data_model.encryption_key.EncryptionKeyDao

class EncryptionKeyRepository(
    private val dao: EncryptionKeyDao
) {
    fun insert(encryptionKey: EncryptionKey) {
        dao.insert(encryptionKey)
    }

    fun getAll(): List<EncryptionKey> {
        return dao.getAll()
    }

    fun getById(id: String): EncryptionKey? {
        return dao.getById(id)
    }
}