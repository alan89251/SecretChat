package com.mapd721.secretchat.data_source.room.message

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: MessageModel)

    @Query("SELECT * FROM ${MessageFields.TABLE} WHERE " +
            "${MessageFields.FIELD_SENDER_ID} = :senderId AND " +
            "${MessageFields.FIELD_RECEIVER_ID} = :receiverId AND " +
            "${MessageFields.FIELD_TYPE} = :type")
    fun getBySenderIdAndReceiverId(senderId: String, receiverId: String, type: Int): List<MessageModel>

    @Query("SELECT * FROM ${MessageFields.TABLE} WHERE " +
            "${MessageFields.FIELD_SENDER_ID} = :senderId AND " +
            "${MessageFields.FIELD_RECEIVER_ID} = :receiverId AND " +
            "${MessageFields.FIELD_TYPE} = :type " +
            "ORDER BY ${MessageFields.FIELD_SENT_DATE_TIME} DESC " +
            "LIMIT 1")
    fun getLatestMessageBySenderIdAndReceiverId(senderId: String, receiverId: String, type: Int): List<MessageModel>
}