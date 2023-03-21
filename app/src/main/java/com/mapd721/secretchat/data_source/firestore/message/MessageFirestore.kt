package com.mapd721.secretchat.data_source.firestore.message

import com.mapd721.secretchat.data_model.chat.Message
import java.util.*

class MessageFirestore {
    var text: String = ""
    var senderId: String = ""
    var receiverId: String = ""
    var sentDateTime: Long = Date().time

    constructor(message: Message) {
        text = message.text
        senderId = message.senderId
        receiverId = message.receiverId
        sentDateTime = message.sentDateTime.time
    }

    fun toMessage(): Message {
        val message = Message()
        message.text = text
        message.senderId = senderId
        message.receiverId = receiverId
        message.sentDateTime = Date(sentDateTime)
        return message
    }
}