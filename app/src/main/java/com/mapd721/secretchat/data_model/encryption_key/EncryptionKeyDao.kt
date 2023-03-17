package com.mapd721.secretchat.data_model.encryption_key

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EncryptionKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(encryptionKey: EncryptionKeyModel)

    @Query("SELECT * FROM ${EncryptionKeySchema.TABLE} WHERE ${EncryptionKeySchema.FIELD_ID} = :id")
    fun getById(id: Int): List<EncryptionKeyModel>
}