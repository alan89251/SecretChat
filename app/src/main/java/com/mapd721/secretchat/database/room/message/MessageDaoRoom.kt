package com.mapd721.secretchat.database.room.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: MessageModel)

    @Query("SELECT * FROM ${MessageFields.TABLE} WHERE ${MessageFields.FIELD_SENDER_ID} = :senderId AND ${MessageFields.FIELD_RECEIVER_ID} = :receiverId")
    fun getBySenderIdAndReceiverId(senderId: String, receiverId: String): List<MessageModel>
}