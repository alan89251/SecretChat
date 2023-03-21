package com.mapd721.secretchat.data_source.room.message

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.mapd721.secretchat.data_model.chat.Message
import java.util.*

@Entity(
    tableName = MessageFields.TABLE,
    primaryKeys = [
        MessageFields.FIELD_ID,
        MessageFields.FIELD_SENDER_ID,
        MessageFields.FIELD_RECEIVER_ID
    ]
)
data class MessageModel(
    @ColumnInfo(name = MessageFields.FIELD_ID)
    var id: String,

    @ColumnInfo(name = MessageFields.FIELD_TEXT)
    var text: String,

    @ColumnInfo(name = MessageFields.FIELD_SENDER_ID)
    var senderId: String,

    @ColumnInfo(name = MessageFields.FIELD_RECEIVER_ID)
    var receiverId: String,

    @ColumnInfo(name = MessageFields.FIELD_TYPE)
    var type: Int,

    @ColumnInfo(name = MessageFields.FIELD_SENT_DATE_TIME)
    var sentDateTime: Long
) {
    constructor(message: Message): this(
        message.id,
        message.text,
        message.senderId,
        message.receiverId,
        message.type,
        message.sentDateTime.time
    )

    @Ignore
    fun toMessage(): Message {
        val message = Message()
        message.id = id
        message.text = text
        message.senderId = senderId
        message.receiverId = receiverId
        message.type = type
        message.sentDateTime = Date(sentDateTime)
        return message
    }
}