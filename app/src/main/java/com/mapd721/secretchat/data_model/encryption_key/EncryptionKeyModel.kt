package com.mapd721.secretchat.data_model.encryption_key

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EncryptionKeySchema.TABLE)
data class EncryptionKeyModel(
    @ColumnInfo(name = EncryptionKeySchema.FIELD_KEY_NAME)
    var keyName: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = EncryptionKeySchema.FIELD_ID)
    var id: Int? = null
}