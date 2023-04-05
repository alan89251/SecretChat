package com.mapd721.secretchat.data_source.room.message

class MessageFields {
    companion object {
        const val TABLE = "message"
        const val FIELD_ID = "id"
        const val FIELD_TEXT = "text"
        const val FIELD_SENDER_ID = "sender_id"
        const val FIELD_RECEIVER_ID = "receiver_id"
        const val FIELD_TYPE = "type"
        const val FIELD_SENT_DATE_TIME = "sent_date_time"
        const val FIELD_MIME = "mime"
        const val FIELD_UPLOADED_FILE_PATH = "uploaded_file_path"
        const val FIELD_ORI_FILE_NAME = "ori_file_name"
    }
}