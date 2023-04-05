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
    var sentDateTime: Long,

    @ColumnInfo(name = MessageFields.FIELD_MIME)
    var mime: Int,

    @ColumnInfo(name = MessageFields.FIELD_UPLOADED_FILE_PATH)
    var uploadedFilePath: String,

    @ColumnInfo(name = MessageFields.FIELD_ORI_FILE_NAME)
    var oriFileName: String
) {
    constructor(message: Message): this(
        message.id,
        message.text,
        message.senderId,
        message.receiverId,
        message.type,
        message.sentDateTime.time,
        mapMime(message.mime),
        message.uploadedFilePath,
        message.oriFileName
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
        message.mime = mapMime(mime)
        message.uploadedFilePath = uploadedFilePath
        message.oriFileName = oriFileName
        return message
    }

    companion object {
        @Ignore
        private fun mapMime(mime: Message.Mime): Int {
            return when (mime) {
                Message.Mime.TEXT -> 0
                Message.Mime.IMAGE -> 1
                Message.Mime.VIDEO -> 2
                Message.Mime.LOCATION -> 3
            }
        }

        @Ignore
        private fun mapMime(mime: Int): Message.Mime {
            return when (mime) {
                0 -> Message.Mime.TEXT
                1 -> Message.Mime.IMAGE
                2 -> Message.Mime.VIDEO
                3 -> Message.Mime.LOCATION
                else -> Message.Mime.TEXT
            }
        }
    }
}