package com.mapd721.secretchat.data_source.firestore.message

import com.google.firebase.firestore.Exclude
import com.mapd721.secretchat.data_model.chat.Message
import java.util.*

class MessageFirestore(
    @get: Exclude
    var id: String = "",
    var text: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var sentDateTime: Long = Date().time,
    var mime: Int = 0,
    var uploadedFilePath: String = "",
    var oriFileName: String = ""
) {
    constructor(message: Message) : this() {
        id = message.id
        text = message.text
        senderId = message.senderId
        receiverId = message.receiverId
        sentDateTime = message.sentDateTime.time
        mime = mapMime(message.mime)
        uploadedFilePath = message.uploadedFilePath
        oriFileName = message.oriFileName
    }

    fun toMessage(): Message {
        val message = Message()
        message.id = id
        message.text = text
        message.senderId = senderId
        message.receiverId = receiverId
        message.sentDateTime = Date(sentDateTime)
        message.mime = mapMime(mime)
        message.uploadedFilePath = uploadedFilePath
        message.oriFileName = oriFileName
        return message
    }

    companion object {
        private fun mapMime(mime: Message.Mime): Int {
            return when (mime) {
                Message.Mime.TEXT -> 0
                Message.Mime.IMAGE -> 1
                Message.Mime.VIDEO -> 2
                Message.Mime.LOCATION -> 3
            }
        }

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