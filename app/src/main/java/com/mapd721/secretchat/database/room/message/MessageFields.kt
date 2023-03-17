package com.mapd721.secretchat.database.room.message

class MessageFields {
    companion object {
        const val TABLE = "message"
        const val FIELD_ID = "id"
        const val FIELD_TEXT = "text"
        const val FIELD_SENDER_ID = "sender_id"
        const val FIELD_RECEIVER_ID = "receiver_id"
        const val FIELD_SENT_DATE_TIME = "sent_date_time"
    }
}