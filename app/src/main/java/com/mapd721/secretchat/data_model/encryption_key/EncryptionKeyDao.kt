package com.mapd721.secretchat.data_model.encryption_key

interface EncryptionKeyDao {
    fun insert(encryptionKey: EncryptionKey)
    fun getAll(): List<EncryptionKey>
    fun getById(id: String): EncryptionKey?
}